package net.dragonclaw.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AuthClientHandler extends SimpleChannelInboundHandler<String> {

    private final AuthClient client;

    public AuthClientHandler(AuthClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (msg.equals("welcome")) {
            System.out.println("Connected to server!");
            return;
        } else {
            String[] parts = msg.split("#");
            if (parts.length < 1) {
                System.err.println("invalid message! length smaller then 1");
                return;
            } else {
                if (parts[0].equals("RESPONSE")) {
                    if (parts.length < 3) {
                        if (parts.length == 2) {
                            if (parts[1].equals("INVALID")) {
                                System.err.println("invalid message!");
                                return;
                            }
                        }
                        System.err.println("invalid message! length smaller then 3");
                        return;
                    } else {
                        if (parts[1].equals("LOGIN")) {
                            if (parts[2].equals("SUCCES")) {
                                System.out.println("succesfully logged in!"); // TODO
                                return;
                            } else if (parts[2].equals("DENIED")) {
                                if (parts.length < 4) {
                                    System.err.println("invalid message! length smaller then 4");
                                    return;
                                } else {
                                    System.out.println("could not log in!: " + parts[3]); // TODO
                                    return;
                                }
                            }
                        } else if (parts[1].equals("REGISTER")) {
                            if (parts[2].equals("SUCCES")) {
                                System.out.println("succesfully registered!"); // TODO
                                return;
                            } else if (parts[2].equals("DENIED")) {
                                if (parts.length < 4) {
                                    System.err.println("invalid message! length smaller then 4");
                                    return;
                                } else {
                                    System.out.println("could not register!: " + parts[3]); // TODO
                                    return;
                                }
                            }
                        } else if (parts[1].equals("LOGOUT")) {
                            if (parts[2].equals("SUCCES")) {
                                System.out.println("succesfully loggged out!"); // TODO
                                return;
                            } else if (parts[2].equals("DENIED")) {
                                if (parts.length < 4) {
                                    System.err.println("invalid message! length smaller then 4");
                                    return;
                                } else {
                                    System.out.println("could not logout!: " + parts[3]); // TODO
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.err.println("Somebody tried to send a wrong message!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            client.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ctx.close();
    }
}
