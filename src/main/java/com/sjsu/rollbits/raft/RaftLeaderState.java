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
	public void handleLeaderElectionResult(String senderNodeId, Long leaderElectionTime) {
		RaftContext raftContext = RaftContext.getInstance();
		if(raftContext.getLeaderNodeId() == null || raftContext.getLeaderElectionTime() == null){
			raftContext.setLeaderNodeId(senderNodeId);
			raftContext.setLeaderElectionTime(leaderElectionTime);
			raftContext.setLAST_RECIEVED(System.currentTimeMillis());
			raftContext.setRaftState(new RaftFollowerState());
		}
		if (raftContext.getLeaderElectionTime()!=null && leaderElectionTime < raftContext.getLeaderElectionTime()) {
			raftContext.setLeaderNodeId(senderNodeId);
			raftContext.setLeaderElectionTime(leaderElectionTime);
			raftContext.setLAST_RECIEVED(System.currentTimeMillis());
			raftContext.setRaftState(new RaftFollowerState());
		}
		
	}

	@Override
	public void handleLeaderHeartBeat(String senderNodeId, Long leaderElectionTime) {
		RaftContext raftContext = RaftContext.getInstance();
		if(raftContext.getLeaderNodeId() == null || raftContext.getLeaderElectionTime() == null){
			raftContext.setLeaderNodeId(senderNodeId);
			raftContext.setLeaderElectionTime(leaderElectionTime);
			raftContext.setLAST_RECIEVED(System.currentTimeMillis());
			raftContext.setRaftState(new RaftFollowerState());
		}
		if (raftContext.getLeaderElectionTime()!=null && leaderElectionTime < raftContext.getLeaderElectionTime()) {
			raftContext.setLeaderNodeId(senderNodeId);
			raftContext.setLeaderElectionTime(leaderElectionTime);
			raftContext.setLAST_RECIEVED(System.currentTimeMillis());
			raftContext.setRaftState(new RaftFollowerState());
		}
	}

}
