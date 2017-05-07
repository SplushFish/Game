package net.dragonclaw.client;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.FingerprintTrustManagerFactory;

public class AuthClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));
    private SslContext sslCtx;
    private EventLoopGroup group;
    private Bootstrap client;
    private Channel channel;
    private boolean isOpen = false;

    public AuthClient() {
        try {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(new FingerprintTrustManagerFactory("77:d3:de:a2:d6:50:10:3d:ea:35:4d:ce:d1:eb:4f:b9:05:2d:40:2f")).build();
            group = new NioEventLoopGroup();
            client = new Bootstrap();
            client.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(sslCtx));
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }

    public AuthClient open() throws InterruptedException {
        channel = client.connect(HOST, PORT).sync().channel();
        isOpen = true;
        return this;
    }

    public void sendLoginRequest(String username, String password) {
        password = encryptPassword(password);
        if (isOpen) {
            channel.writeAndFlush("LOGIN#" + username + "#" + password + "\r\n");
            return;
        }
        System.out.println("The AuthClient is closed!");
    }

    public void sendRegisterRequest(String username, String password, String email) {
        password = encryptPassword(password);
        if (isOpen) {
            channel.writeAndFlush("REGISTER#" + username + "#" + password + "#" + email + "\r\n");
            return;
        }
        System.out.println("The AuthClient is closed!");
    }

    public void sendLogoutRequest(String username) {
        if (isOpen) {
            channel.writeAndFlush("LOGOUT#" + username + "\r\n");
            return;
        }
        System.out.println("The AuthClient is closed!");
    }

    public void close() throws InterruptedException {
        channel.closeFuture().sync();
        group.shutdownGracefully();
        isOpen = false;
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

    private class ClientInitializer extends ChannelInitializer<SocketChannel> {

        private final SslContext sslCtx;

        public ClientInitializer(SslContext sslCtx) {
            this.sslCtx = sslCtx;
        }

        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new StringEncoder());
            pipeline.addLast(new AuthClientHandler(AuthClient.this));
        }
    }
}
