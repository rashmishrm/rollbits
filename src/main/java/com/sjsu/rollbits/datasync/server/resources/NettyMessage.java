package com.sjsu.rollbits.datasync.server.resources;

import routing.Pipe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhrumil on 11/5/17.
 */
public class NettyMessage implements NettyInterface {

    Map<String, String> nodeMap = new HashMap<>();
    @Override
    public void sendMessageToNode(String senderNodeId, Pipe.Route build) {

        for(Map.Entry<String, String> iter : nodeMap.entrySet()) {
            String key = iter.getKey();
            String value = iter.getValue();
                if(value.equals(senderNodeId))
                {
                    Pipe.Route.Path path = build.getPath();
                    if (path.equals("/message")) {
                        new MessageResource().process(build);
                    }

                }


        }


    }

    @Override
    public void broadcast() {


    }
}
