package cn.apisium.netty.engineio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class Main extends ChannelInitializer<Channel> {
    private final EngineIoServer engineIoServer = new EngineIoServer();
    private Main() {
        SocketIoServer socketIoServer = new SocketIoServer(engineIoServer);
        socketIoServer.namespace("/").on("connection", args -> {
            try {
                final SocketIoSocket socket = (SocketIoSocket) args[0];
                socket.send("test2", 666);
                socket.on("test", it -> System.out.println((int) it[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).on("error", System.out::println);
    }

    public static void main(String[] args) {
        NioEventLoopGroup el1 = new NioEventLoopGroup(), el2 = new NioEventLoopGroup();
        try {
            new ServerBootstrap()
                    .group(el1, el2)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new Main())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind(2333).sync().channel()
                    .closeFuture().sync();
        } catch (Exception e) { e.printStackTrace(); }
        finally {
            el1.shutdownGracefully();
            el2.shutdownGracefully();
        }
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpContentCompressor())
                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new IdleStateHandler(20000, 20000, 20000))
                .addLast(new EngineIoHandler(engineIoServer) {
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        cause.printStackTrace();
                    }
                })
                .addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        if (msg instanceof FullHttpRequest) {
                            FullHttpRequest req = (FullHttpRequest) msg;
                            ctx.channel()
                                    .writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.NOT_FOUND))
                                    .addListener(ChannelFutureListener.CLOSE);
                            req.release();
                        } else super.channelRead(ctx, msg);
                    }
                });
    }
}
