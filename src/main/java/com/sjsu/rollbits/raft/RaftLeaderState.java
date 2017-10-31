/**
 * 
 */
package com.sjsu.rollbits.raft;

/**
 * @author nishantrathi
 *
 */
public class RaftLeaderState implements RaftState {

	/**
	 * 
	 */
	public RaftLeaderState() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.raft.RaftState#doAction(com.sjsu.rollbits.raft.RaftContext, routing.Pipe.Route)
	 */
	@Override
	public void doAction(RaftContext raftContext) {
		//Do Nothing
	}

	@Override
	public routing.Pipe.RaftNode.RaftState getRaftState() {
		return routing.Pipe.RaftNode.RaftState.Leader;
	}

	@Override
	public void handleVoteRequest(String senderNodeId) {
		//Do Nothing
		
	}

	@Override
	public void handleVoteResponse(String senderNodeId) {
		//Do Nothing
		
	}

	@Override
	public void handleLeaderElectionResult(String senderNodeId) {
		//Do Nothing
		
	}

	@Override
	public void handleLeaderHeartBeat(String senderNodeId) {
		//Do Nothing
		
	}

}
