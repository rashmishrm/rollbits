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
public class RaftEngine implements Runnable {

	private Long INITIAL_TIMEOUT = 180 * 1000L;

	private Long TIMEOUT = 90 * 1000L;

	private Long LAST_RECIEVED = -1L;

	private RaftState raftState = RaftState.Follower;

	/**
	 * 
	 */
	public RaftEngine() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			Thread.sleep(INITIAL_TIMEOUT);
			while (true) {
				if (RaftState.Leader.equals(raftState)) {
					// Broadcast heartbeat as Leader to everyone

				} else if (RaftState.Follower.equals(raftState)) {
					if (LAST_RECIEVED == -1 || System.currentTimeMillis() - LAST_RECIEVED > TIMEOUT) {
						// Broadcast himself as a candidate
						this.raftState = raftState.Candidate;
						Route.Builder routeBuilder = Route.newBuilder();
						routeBuilder.setPath(Path.RAFT_MSG);
						RaftMessage.Builder raftMessageBuilder = RaftMessage.newBuilder();
						raftMessageBuilder.setType(RaftMsgType.RequestVote);
						raftMessageBuilder.setSenderNodeid(RaftHelper.getMyNodeId());
						RaftMessage raftMessage = raftMessageBuilder.build();
						routeBuilder.setRaftMessage(raftMessage);
						RaftHelper.broadcast(routeBuilder.build());
					}

				} else if (RaftState.Candidate.equals(raftState)) {
					// Count vote until receives Leader Elected message from any
					// other node

				}
				Thread.sleep(TIMEOUT);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the lAST_RECIEVED
	 */
	public Long getLAST_RECIEVED() {
		return LAST_RECIEVED;
	}

	/**
	 * @param lAST_RECIEVED
	 *            the lAST_RECIEVED to set
	 */
	public void setLAST_RECIEVED(Long lAST_RECIEVED) {
		LAST_RECIEVED = lAST_RECIEVED;
	}

	/**
	 * @return the raftState
	 */
	public RaftState getRaftState() {
		return raftState;
	}

	/**
	 * @param raftState
	 *            the raftState to set
	 */
	public void setRaftState(RaftState raftState) {
		this.raftState = raftState;
	}

	/**
	 * @return the iNITIAL_TIMEOUT
	 */
	public Long getINITIAL_TIMEOUT() {
		return INITIAL_TIMEOUT;
	}

	/**
	 * @return the tIMEOUT
	 */
	public Long getTIMEOUT() {
		return TIMEOUT;
	}

}
