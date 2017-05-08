package net.dragonclaw.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.FastThreadLocal;
import net.dragonclaw.sqlite.SQLiteDatabase;

public class AuthServer {

    private static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));
    private final SQLiteDatabase db;

    private static final FastThreadLocal<MessageDigest> tlmd = new FastThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                // All Java implementation must have SHA1 digest algorithm.
                throw new Error(e);
            }
        }
    };

    public AuthServer() throws InterruptedException {
        db = new SQLiteDatabase("users");
        db.createTable("users", UserProfile.class);
        try {
            SslContext sslCtx;
            File cert = new File(".", "certificate.crt");
            File key = new File(".", "private.key");
            if (cert.exists() && key.exists()) {
                sslCtx = SslContextBuilder.forServer(cert, key).build();
            } else {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                saveCert(ssc.cert());
                saveKey(ssc.key());
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            }
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ServerInitializer(sslCtx));

                System.out.println("Started Auth Server");
                b.bind(PORT).sync().channel().closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void saveCert(X509Certificate cert) throws IOException, CertificateEncodingException {
        ByteBuf wrappedBuf = Unpooled.wrappedBuffer(cert.getEncoded());
        ByteBuf encodedBuf;
        final String certText;
        try {
            encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                certText = "-----BEGIN CERTIFICATE-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII)
                        + "\n-----END CERTIFICATE-----\n";
            } finally {
                encodedBuf.release();
            }
        } finally {
            wrappedBuf.release();
        }

        File certFile = new File(".", "certificate.crt");
        OutputStream certOut = new FileOutputStream(certFile);
        try {
            certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
            certOut.close();
            certOut = null;
        } finally {
            if (certOut != null) {
                certOut.close();
            }
        }
    }

    private void saveKey(PrivateKey key) throws IOException {
        ByteBuf wrappedBuf = Unpooled.wrappedBuffer(key.getEncoded());
        ByteBuf encodedBuf;
        final String keyText;
        try {
            encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                keyText = "-----BEGIN PRIVATE KEY-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII)
                        + "\n-----END PRIVATE KEY-----\n";
            } finally {
                encodedBuf.release();
            }
        } finally {
            wrappedBuf.release();
        }

        File keyFile = new File(".", "private.key");
        OutputStream keyOut = new FileOutputStream(keyFile);
        try {
            keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
            keyOut.close();
            keyOut = null;
        } finally {
            if (keyOut != null) {
                keyOut.close();
            }
        }

    }

    private class ServerInitializer extends ChannelInitializer<SocketChannel> {

        private final SslContext sslCtx;

        public ServerInitializer(SslContext sslCtx) {
            this.sslCtx = sslCtx;
        }

        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new StringEncoder());
            pipeline.addLast(new AuthServerHandler(db));
        }

    }
}
