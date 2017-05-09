package net.dragonclaw.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;
import net.dragonclaw.sqlite.SQLGetter;
import net.dragonclaw.sqlite.SQLResult;
import net.dragonclaw.sqlite.SQLiteDatabase;

public class AuthServerHandler extends SimpleChannelInboundHandler<String> {

    private final SQLiteDatabase db;

    public AuthServerHandler(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(future -> {
            ctx.writeAndFlush("welcome\r\n");
            System.out.println("AuthClient Connected!");
        });
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] request = msg.split("#");
        if (request.length < 1) {
            send(ctx, "RESPONSE#INVALID");
            return;
        }
        if (request[0].equals("LOGIN")) {
            handleLogin(ctx, request);
        } else if (request[0].equals("REGISTER")) {
            handleRegister(ctx, request);
        } else {
            send(ctx, "RESPONSE#INVALID");
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, String[] request) {
        if (request.length != 3) {
            send(ctx, "RESPONSE#INVALID");
            return;
        }
        String username = request[1];
        String pass = request[2];
        if (!pass.matches("^[a-f0-9]{32}$")) {
            send(ctx, "RESPONSE#LOGIN#DENIED#invalid password! Contact server owners!");
            return;
        }

        if (!username.matches("^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) {
            send(ctx, "RESPONSE#LOGIN#DENIED#wrong username or password!");
            return;
        }

        SQLResult result = db.getFromTable("users", UserProfile.class, new SQLGetter("accountName", username),
                new SQLGetter("password", pass));
        if (result.isSuccesSingleton()) {
            UserProfile prof = (UserProfile) result.asDataType();
            prof.lastLogin = ZonedDateTime.now(ZoneOffset.UTC).toString();
            db.updateTable("users", prof);
            send(ctx, "RESPONSE#LOGIN#SUCCES#" + prof.id + "|" + prof.accountName + "|" + prof.uuid + "|"
                    + prof.emailAdress + "|" + prof.creationDate + "|" + prof.lastLogin + "|" + prof.newAccount);
        } else {
            if (result.isError() && result.getResultMessage().equals("19")) {
                send(ctx, "RESPONSE#LOGIN#DENIED#wrong username or password!");
            } else {
                send(ctx, "RESPONSE#LOGIN#DENIED#invalid request! contact server owners!");
                System.err.println("Login error: " + result.getResultMessage());
            }
        }
    }


    private void handleRegister(ChannelHandlerContext ctx, String[] request) {
        if (request.length != 4) {
            send(ctx, "RESPONSE#INVALID");
            return;
        }
        String username = request[1];
        String pass = request[2];
        if (!pass.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$")) {
            send(ctx,
                    "RESPONSE#REGISTER#DENIED#invalid password: password requires atleast 8 characters, a lowercase, an uppercase and a digit!");
            return;
        }
        String encrypted = encryptPassword(pass);
        String email = request[3];
        if (!encrypted.matches("^[a-f0-9]{32}$")) {
            send(ctx, "RESPONSE#REGISTER#DENIED#invalid password! Contact server owners!");
            return;
        }

        if (!username.matches("^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) {
            send(ctx,
                    "RESPONSE#REGISTER#DENIED#invalid username: username requires between 6 and 20 characters and no special characters!");
            return;
        }

        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            send(ctx, "RESPONSE#REGISTER#DENIED#invalid email!");
            return;
        }

        UserProfile prof = new UserProfile(username, encrypted, email);
        SQLResult result = db.insertIntoTable("users", prof);
        if (result.isSucces()) {
            prof = (UserProfile) db.getFromTable("users", UserProfile.class, new SQLGetter("accountName", username))
                    .asDataType();
            send(ctx, "RESPONSE#REGISTER#SUCCES#" + prof.id + "|" + prof.accountName + "|" + prof.uuid + "|"
                    + prof.emailAdress + "|" + prof.creationDate + "|" + prof.lastLogin + "|" + prof.newAccount);
        } else {
            if (result.isError() && result.getResultMessage().equals("19")) {
                send(ctx, "RESPONSE#REGISTER#DENIED#user already exists!");
            } else {
                send(ctx, "RESPONSE#REGISTER#DENIED#invalid request! contact server owners!");
                System.err.println("Register error: " + result.getResultMessage());
            }
        }
    }

    private String encryptPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "ERROR";
        }
        byte[] hash = md.digest(password.getBytes());
        BigInteger bi = new BigInteger(1, hash);
        return String.format("%0" + (hash.length << 1) + "x", bi);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("AuthClient Disconnected!");
        ctx.close();
    }

    private void send(ChannelHandlerContext ctx, String msg) {
        ctx.writeAndFlush(msg + System.lineSeparator());
    }
}
