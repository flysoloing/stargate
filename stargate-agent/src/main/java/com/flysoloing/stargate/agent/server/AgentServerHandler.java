package com.flysoloing.stargate.agent.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laitao
 * @date 2017-04-11 15:35:07
 */
public class AgentServerHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgentServerHandler.class);

    private String index;

    public AgentServerHandler(String index) {
        this.index = index;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info(index);
        ctx.fireChannelRead(msg);
    }
}
