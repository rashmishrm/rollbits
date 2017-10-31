/**
 * 
 */
package com.sjsu.rollbits.raft;

import routing.Pipe.RaftMessage;
import routing.Pipe.RaftMessage.RaftMsgType;
import routing.Pipe.RaftNode.RaftState;
import routing.Pipe.Route;
import routing.Pipe.Route.Path;

/**
 * @author nishantrathi
 *
 */
public class RaftHeartBeatEngine implements Runnable {

	
	/**
	 * 
	 */
	public RaftHeartBeatEngine() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(RaftContext.HEARTBEAT_TIMER);
				RaftContext raftContext = RaftContext.getInstance();

				if (RaftState.Leader.equals(raftContext.getRaftState().getRaftState())) {
					Route.Builder routeBuilder = Route.newBuilder();
					routeBuilder.setPath(Path.RAFT_MSG);
					RaftMessage.Builder raftMessageBuilder = RaftMessage.newBuilder();
					raftMessageBuilder.setType(RaftMsgType.LeaderHeartBeat);
					raftMessageBuilder.setSenderNodeid(RaftHelper.getMyNodeId());
					RaftMessage raftMessage = raftMessageBuilder.build();
					routeBuilder.setRaftMessage(raftMessage);
					RaftHelper.broadcast(routeBuilder.build());
					raftContext.setLAST_RECIEVED(System.currentTimeMillis());
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


}
