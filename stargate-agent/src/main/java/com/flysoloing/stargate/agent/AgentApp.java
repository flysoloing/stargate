package com.flysoloing.stargate.agent;

import com.flysoloing.stargate.agent.server.AgentServer;

/**
 * @author laitao
 * @since 2017-04-11 15:11:16
 */
public class AgentApp {

    public static void main( String[] args ) throws Exception {
        AgentApp agentApp = new AgentApp();
        agentApp.start();
    }

    private void start() {
        //TODO
        final AgentServer agentServer = new AgentServer();
        agentServer.startup();
    }
}
