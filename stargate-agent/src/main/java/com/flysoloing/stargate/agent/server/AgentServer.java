package com.flysoloing.stargate.agent.server;

import com.flysoloing.stargate.agent.config.AgentServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * @author laitao
 * @since 2017-04-11 15:17:46
 */
public class AgentServer {

    private static final Logger logger = LoggerFactory.getLogger(AgentServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private AgentServerConfiguration serverConfiguration;

    public AgentServer() {
        this.bossGroup = new NioEventLoopGroup(4);
        this.workerGroup = new NioEventLoopGroup(4);
        this.serverConfiguration = new AgentServerConfiguration();
    }

    public boolean startup() {
        //根据证书配置信息来配置SSL证书上下文
        final SslContext sslContext;
        if (serverConfiguration != null) {
            //如果支持SSL，且为自定义类型证书
            //TODO  自定义类型证书，后续可以替换为正式证书，是可配置的
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (CertificateException | SSLException e) {
                e.printStackTrace();
                return false;
            }
        }

        //配置ServerChannel类型
        final Class<? extends ServerChannel> serverChannelClazz;
        if (serverConfiguration != null) {
            //如果OS不支持epoll线程模型，则默认为nio线程模型
            serverChannelClazz = NioServerSocketChannel.class;
        } else {
            serverChannelClazz = EpollServerSocketChannel.class;
        }

        //配置Server启动器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(serverChannelClazz)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new AgentServerChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 4096)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        //绑定端口
        Channel channel;
        try {
            channel = serverBootstrap.bind(80).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean shutdown() {
        return false;
    }
}
