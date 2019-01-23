package com.flysoloing.stargate.agent.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author laitao
 * @date 2017-04-11 15:35:32
 */
public class AgentServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        channelPipeline.addLast(new AgentServerHandler("1"));
        channelPipeline.addLast(new AgentServerHandler("2"));
        channelPipeline.addLast(new AgentServerHandler("3"));
        channelPipeline.addLast(new AgentServerHandler("4"));
        channelPipeline.addLast(new AgentServerHandler("5"));
        channelPipeline.addLast(new AgentServerHandler("6"));
    }
}
