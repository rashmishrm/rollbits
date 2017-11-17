/**
 * 
 */
package com.sjsu.rollbits.raft;

/**
 * @author nishantrathi
 *
 */
public class RaftContext {
	
	private static RaftContext raftContext;
	private RaftState raftState;
	public static Long HEARTBEAT_TIMER = 1 * 60 * 1000L;
	public static Long RAFT_TIMER = 2 * 60 * 1000L;
	private Long LAST_RECIEVED = -1L;
	private String leaderNodeId;
	private static RaftEngine raftEngine;
	private static Thread raftEngineThread;
	private static RaftHeartBeatEngine raftHeartBeatEngine;
	private static Thread raftHeartBeatEngineThread;
	private Long leaderElectionTime;
	
	static {
		if (raftContext == null) {
			synchronized (RaftContext.class) {
				if (raftContext == null) {
					raftContext = new RaftContext();
					raftContext.setRaftState(new RaftFollowerState());
					raftEngine = new RaftEngine();
					raftEngineThread = new Thread(raftEngine);
					raftEngineThread.start();
					System.out.println("Raft Engine started..");
					raftHeartBeatEngine = new RaftHeartBeatEngine();
					raftHeartBeatEngineThread = new Thread(raftHeartBeatEngine);
					raftHeartBeatEngineThread.start();
					System.out.println("Raft Heartbeat Engine started..");
				}
				
			}
		}
	}
	
	/**
	 * Making this as singleton
	 */
	private RaftContext() {
		System.out.println("Creating Raft Context...");
	}
	
	public static RaftContext getInstance() {
		return raftContext;
	}

	/**
	 * @return the raftState
	 */
	public RaftState getRaftState() {
		return raftState;
	}

	/**
	 * @param raftState the raftState to set
	 */
	public void setRaftState(RaftState raftState) {
		this.raftState = raftState;
	}

	public Long getLAST_RECIEVED() {
		return LAST_RECIEVED;
	}

	public void setLAST_RECIEVED(Long lAST_RECIEVED) {
		synchronized (LAST_RECIEVED) {
			LAST_RECIEVED = lAST_RECIEVED;
		}
	}

	public void setLeaderNodeId(String leaderNodeId) {
		this.leaderNodeId = leaderNodeId;
		
	}

	public String getLeaderNodeId() {
		return this.leaderNodeId;
		
	}

	/**
	 * @return the leaderElectionTime
	 */
	public Long getLeaderElectionTime() {
		return this.leaderElectionTime;
	}

	/**
	 * @param leaderElectionTime the leaderElectionTime to set
	 */
	public void setLeaderElectionTime(Long leaderElectionTime) {
		this.leaderElectionTime = leaderElectionTime;
	}

}
