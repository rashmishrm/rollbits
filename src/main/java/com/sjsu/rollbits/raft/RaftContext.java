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
	public static Long RAFT_TIMER = 5 * 60 * 1000L;
	private Long LAST_RECIEVED = -1L;
	private String leaderNodeId;
	/**
	 * Making this as singleton
	 */
	private RaftContext() {
	}
	
	public static RaftContext getInstance(){
	    if(raftContext == null){
	        synchronized (RaftContext.class) {
	            if(raftContext == null){
	            	raftContext = new RaftContext();
	            	raftContext.setRaftState(new RaftFollowerState());
	            }
	        }
	    }
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

	

}
