package com.sjsu.rollbits.datasync.server.resources;

import org.apache.log4j.Logger;

import com.sjsu.rollbits.raft.RaftContext;
import com.sjsu.rollbits.raft.RaftState;

import io.netty.channel.Channel;
import routing.Pipe;
import routing.Pipe.RaftMessage;
import routing.Pipe.RaftMessage.RaftMsgType;
import routing.Pipe.Route;

public class RaftResource implements RouteResource {
	protected static Logger logger = Logger.getLogger("RaftResource");

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.RAFT_MSG;
	}

	@Override
	public Object process(Pipe.Route msg, Channel returnChannel) {

		RaftMessage raftMessage = msg.getRaftMessage();
		RaftMsgType raftMsgType = raftMessage.getType();

		RaftContext raftContext = RaftContext.getInstance();
		RaftState raftState = raftContext.getRaftState();

		String senderNodeId = raftMessage.getSenderNodeid();
		Long leaderSelectionTime = raftMessage.getLeaderSelectionTime();

		if (RaftMsgType.RequestVote.equals(raftMsgType)) {
			// Received voting request from a candidate
			raftState.handleVoteRequest(senderNodeId);

		} else if (RaftMsgType.VoteResponse.equals(raftMsgType)) {
			// Received vote from a follower, you being a candidate
			raftState.handleVoteResponse(senderNodeId);

		} else if (RaftMsgType.LeaderHeartBeat.equals(raftMsgType)) {
			// Received Heart beat from leader
			raftState.handleLeaderHeartBeat(senderNodeId, leaderSelectionTime);

		} else if (RaftMsgType.LeaderElectionResult.equals(raftMsgType)) {
			// Received Confirmation from a newly elected Leader
			raftState.handleLeaderElectionResult(senderNodeId, leaderSelectionTime);

		}
		Route.Builder rb = ProtoUtil.createResponseRoute(msg.getId(), true, null, RollbitsConstants.SUCCESS);

		return rb;

	}

}
