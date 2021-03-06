/**
 * 
 */
package com.sjsu.rollbits.raft;

/**
 * @author nishantrathi
 *
 */
public interface RaftState {
	public void doAction(RaftContext raftContext);
	
	public routing.Pipe.RaftNode.RaftState getRaftState();
	
	public void handleVoteRequest(String senderNodeId);
	
	public void handleVoteResponse(String senderNodeId);
	
	public void handleLeaderElectionResult(String senderNodeId, Long l);
	
	public void handleLeaderHeartBeat(String senderNodeId, Long leaderSelectionTime);
}
