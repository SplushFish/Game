package net.dragonclaw.server;

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
        } else if (request[0].equals("LOGOUT")) {
            handleLogout(ctx, request);
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

        SQLResult result = db.getFromTable("users", UserProfile.class, new SQLGetter("accountName", username), new SQLGetter("password", pass));
        if (result.isSuccesSingleton()) {
            UserProfile prof = (UserProfile) result.asDataType();
            prof.lastLogin = ZonedDateTime.now(ZoneOffset.UTC).toString();
            db.updateTable("users", prof);
            send(ctx, "RESPONSE#LOGIN#SUCCES#" + prof.id + "|" + prof.accountName + "|" + prof.uuid + "|" + prof.emailAdress + "|" + prof.creationDate + "|"
                    + prof.lastLogin + "|" + prof.newAccount);
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
        String email = request[3];
        if (!pass.matches("^[a-f0-9]{32}$")) {
            send(ctx, "RESPONSE#REGISTER#DENIED#invalid password! Contact server owners!");
            return;
        }

        if (!username.matches("^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) {
            send(ctx, "RESPONSE#REGISTER#DENIED#invalid username!");
            return;
        }

        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            send(ctx, "RESPONSE#REGISTER#DENIED#invalid email!");
            return;
        }

        UserProfile prof = new UserProfile(username, pass, email);
        SQLResult result = db.insertIntoTable("users", prof);
        if (result.isSucces()) {
            send(ctx, "RESPONSE#REGISTER#SUCCES#" + prof.id + "|" + prof.accountName + "|" + prof.uuid + "|" + prof.emailAdress + "|" + prof.creationDate + "|"
                    + prof.lastLogin + "|" + prof.newAccount);
        } else {
            if (result.isError() && result.getResultMessage().equals("19")) {
                send(ctx, "RESPONSE#REGISTER#DENIED#user already exists!");
            } else {
                send(ctx, "RESPONSE#REGISTER#DENIED#invalid request! contact server owners!");
                System.err.println("Register error: " + result.getResultMessage());
            }
        }
    }

    private void handleLogout(ChannelHandlerContext ctx, String[] request) {
        if (request.length != 2) {
            send(ctx, "RESPONSE#INVALID");
            return;
        }
        String username = request[1];
        if (!username.matches("^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) {
            send(ctx, "RESPONSE#LOGOUT#DENIED#wrong username!");
            return;
        }

        SQLResult result = db.getFromTable("users", UserProfile.class, new SQLGetter("accountName", username));
        if (result.isSuccesSingleton()) {
            UserProfile prof = (UserProfile) result.asDataType();
            prof.lastLogout = ZonedDateTime.now(ZoneOffset.UTC).toString();
            db.updateTable("users", prof);
            send(ctx, "RESPONSE#LOGOUT#SUCCES");
        } else {
            if (result.isError() && result.getResultMessage().equals("19")) {
                send(ctx, "RESPONSE#LOGOUT#DENIED#cannot logout wrong username!");
            } else {
                send(ctx, "RESPONSE#LOGOUT#DENIED#invalid request! contact server owners!");
                System.err.println("Logout error: " + result.getResultMessage());
            }
        }
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
