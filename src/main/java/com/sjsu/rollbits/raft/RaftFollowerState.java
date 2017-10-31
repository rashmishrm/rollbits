/**
 * 
 */
package com.sjsu.rollbits.raft;

import routing.Pipe.RaftMessage;
import routing.Pipe.RaftMessage.RaftMsgType;
import routing.Pipe.Route;
import routing.Pipe.Route.Path;

/**
 * @author nishantrathi
 *
 */
public class RaftFollowerState implements RaftState {
	
	private Boolean hasVoted = false;

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
		hasVoted = false;
		if (raftContext.getLAST_RECIEVED() == -1
				|| System.currentTimeMillis() - raftContext.getLAST_RECIEVED() > RaftContext.RAFT_TIMER) {
			RaftState raftState = new RaftCandidateState();
			raftContext.setRaftState(raftState);
			Route.Builder routeBuilder = Route.newBuilder();
			routeBuilder.setPath(Path.RAFT_MSG);
			RaftMessage.Builder raftMessageBuilder = RaftMessage.newBuilder();
			raftMessageBuilder.setType(RaftMsgType.RequestVote);
			raftMessageBuilder.setSenderNodeid(RaftHelper.getMyNodeId());
			RaftMessage raftMessage = raftMessageBuilder.build();
			routeBuilder.setRaftMessage(raftMessage);
			RaftHelper.broadcast(routeBuilder.build());
		}
	}

	@Override
	public routing.Pipe.RaftNode.RaftState getRaftState() {
		return routing.Pipe.RaftNode.RaftState.Follower;
	}

	@Override
	public void handleVoteRequest(String senderNodeId) {
		synchronized (hasVoted) {
			if (!hasVoted) {
				Route.Builder routeBuilder = Route.newBuilder();
				routeBuilder.setPath(Path.RAFT_MSG);
				RaftMessage.Builder raftMessageBuilder = RaftMessage.newBuilder();
				raftMessageBuilder.setType(RaftMsgType.VoteResponse);
				raftMessageBuilder.setSenderNodeid(RaftHelper.getMyNodeId());
				RaftMessage raftMessage = raftMessageBuilder.build();
				routeBuilder.setRaftMessage(raftMessage);
				RaftHelper.sendMessageToNode(senderNodeId, routeBuilder.build());
				hasVoted = true;
			}
		}
	}

	@Override
	public void handleVoteResponse(String senderNodeId) {
		// As this scenario is not possible ideally, hence Do Nothing.
		
	}

	@Override
	public void handleLeaderElectionResult(String senderNodeId) {
		RaftContext raftContext = RaftContext.getInstance();
		raftContext.setLAST_RECIEVED(System.currentTimeMillis());
		raftContext.setLeaderNodeId(senderNodeId);
		
	}

	@Override
	public void handleLeaderHeartBeat(String senderNodeId) {
		RaftContext raftContext = RaftContext.getInstance();
		raftContext.setLAST_RECIEVED(System.currentTimeMillis());
		
	}

}
