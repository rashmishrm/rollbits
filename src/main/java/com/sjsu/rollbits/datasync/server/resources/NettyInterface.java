package com.sjsu.rollbits.datasync.server.resources;

import routing.Pipe;

/**
 * Created by dhrumil on 11/5/17.
 */
public interface NettyInterface {

     void sendMessageToNode(String senderNodeId, Pipe.Route build);

     void broadcast();

}
