/**
 * 
 */
package com.sjsu.rollbits.raft;

/**
 * @author nishantrathi
 *
 */
public class RaftFollowerState implements RaftState {

	/**
	 * 
	 */
	public RaftFollowerState() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.raft.RaftState#doAction(com.sjsu.rollbits.raft.RaftContext, routing.Pipe.Route)
	 */
	@Override
	public void doAction(RaftContext raftContext) {
		raftContext.setRaftState(this);
	}

	@Override
	public routing.Pipe.RaftNode.RaftState getRaftState(RaftContext raftContext) {
		return routing.Pipe.RaftNode.RaftState.Follower;
	}

	@Override
	public void handleVoteRequest(String senderNodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleVoteResponse(String senderNodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLeaderElectionResult(String senderNodeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLeaderHeartBeat(String senderNodeId) {
		// TODO Auto-generated method stub
		
	}

}
