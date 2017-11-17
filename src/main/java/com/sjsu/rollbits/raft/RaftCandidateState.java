/**
 * 
 */
package com.sjsu.rollbits.raft;

import java.util.ArrayList;
import java.util.List;

import routing.Pipe.RaftMessage;
import routing.Pipe.Route;
import routing.Pipe.RaftMessage.RaftMsgType;
import routing.Pipe.Route.Path;

/**
 * @author nishantrathi
 *
 */
public class RaftCandidateState implements RaftState {
	
	private Integer voteCount = 0;
	private List<String> voterList = new ArrayList<>();
	

	/**
	 * 
	 */
	public RaftCandidateState() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.raft.RaftState#doAction(com.sjsu.rollbits.raft.RaftContext, routing.Pipe.Route)
	 */
	@Override
	public void doAction(RaftContext raftContext) {
		synchronized (voteCount) {
			voteCount = 0;
		}
		synchronized (voterList) {
			voterList.clear();
		}
		Route.Builder routeBuilder = Route.newBuilder();
		routeBuilder.setPath(Path.RAFT_MSG);
		RaftMessage.Builder raftMessageBuilder = RaftMessage.newBuilder();
		raftMessageBuilder.setType(RaftMsgType.RequestVote);
		raftMessageBuilder.setSenderNodeid(RaftHelper.getMyNodeId());
		RaftMessage raftMessage = raftMessageBuilder.build();
		routeBuilder.setRaftMessage(raftMessage);
		RaftHelper.broadcast(routeBuilder);
	}

	@Override
	public routing.Pipe.RaftNode.RaftState getRaftState() {
		return routing.Pipe.RaftNode.RaftState.Candidate;
	}

	@Override
	public void handleVoteRequest(String senderNodeId) {
		//Do nothing as you are yourself a candidate
		
	}

	@Override
	public void handleVoteResponse(String senderNodeId) {
		synchronized (voteCount) {
			voteCount++;
		}
		synchronized (voterList) {
			voterList.add(senderNodeId);
		}
		if (voteCount >= RaftHelper.requiredMajorityCount()) {
			//Declare itself as leader
			RaftContext raftContext = RaftContext.getInstance();
			RaftState raftState = new RaftLeaderState();
			raftContext.setRaftState(raftState);
			raftContext.setLeaderNodeId(RaftHelper.getMyNodeId());
			
			Route.Builder routeBuilder = Route.newBuilder();
			routeBuilder.setPath(Path.RAFT_MSG);
			RaftMessage.Builder raftMessageBuilder = RaftMessage.newBuilder();
			raftMessageBuilder.setType(RaftMsgType.LeaderElectionResult);
			raftMessageBuilder.setSenderNodeid(RaftHelper.getMyNodeId());
			RaftMessage raftMessage = raftMessageBuilder.build();
			routeBuilder.setRaftMessage(raftMessage);
			RaftHelper.broadcast(routeBuilder);
		}

	}

	@Override
	public void handleLeaderElectionResult(String senderNodeId, Long leaderElectionTime) {
		RaftContext raftContext = RaftContext.getInstance();
		RaftState raftState = new RaftFollowerState();
		raftContext.setRaftState(raftState);
		raftContext.setLAST_RECIEVED(System.currentTimeMillis());
		raftContext.setLeaderNodeId(senderNodeId);
		raftContext.setLeaderElectionTime(leaderElectionTime);
	}

	@Override
	public void handleLeaderHeartBeat(String senderNodeId, Long leaderElectionTime) {
		RaftContext raftContext = RaftContext.getInstance();
		RaftState raftState = new RaftFollowerState();
		raftContext.setRaftState(raftState);
		raftContext.setLAST_RECIEVED(System.currentTimeMillis());
		raftContext.setLeaderNodeId(senderNodeId);
		raftContext.setLeaderElectionTime(leaderElectionTime);
		
	}

}
