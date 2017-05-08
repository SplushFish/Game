package net.dragonclaw.client;

import javax.swing.JOptionPane;

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
                                JOptionPane.showMessageDialog(null, "Succesfully logged in!", "Login Request",
                                        JOptionPane.INFORMATION_MESSAGE);
                                synchronized (client) {
                                    client.profile = new UserProfileInternal(parts[3].split("\\|"));
                                }
                                return;
                            } else if (parts[2].equals("DENIED")) {
                                if (parts.length < 4) {
                                    System.err.println("invalid message! length smaller then 4");
                                    return;
                                } else {
                                    JOptionPane.showMessageDialog(null, "could not login: " + parts[3], "Login Request",
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                        } else if (parts[1].equals("REGISTER")) {
                            if (parts[2].equals("SUCCES")) {
                                JOptionPane.showMessageDialog(null, "Succesfully registered!", "Register Request",
                                        JOptionPane.INFORMATION_MESSAGE);
                                synchronized (client) {
                                    client.profile = new UserProfileInternal(parts[3].split("\\|"));
                                }
                                return;
                            } else if (parts[2].equals("DENIED")) {
                                if (parts.length < 4) {
                                    System.err.println("invalid message! length smaller then 4");
                                    return;
                                } else {
                                    JOptionPane.showMessageDialog(null, "could not register: " + parts[3],
                                            "Register Request", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                        } else if (parts[1].equals("LOGOUT")) {
                            if (parts[2].equals("SUCCES")) {
                                JOptionPane.showMessageDialog(null, "Succesfully logged out!", "Register Request",
                                        JOptionPane.INFORMATION_MESSAGE);
                                synchronized (client) {
                                    client.logout = true;
                                }
                                return;
                            } else if (parts[2].equals("DENIED")) {
                                if (parts.length < 4) {
                                    System.err.println("invalid message! length smaller then 4");
                                    return;
                                } else {
                                    JOptionPane.showMessageDialog(null, "could not logout: " + parts[3],
                                            "Logout Request", JOptionPane.ERROR_MESSAGE);
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
