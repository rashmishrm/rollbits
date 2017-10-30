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
	private routing.Pipe.RaftNode.RaftState raftStateProtoBuff;

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
	            	raftContext.setRaftStateProtoBuff(routing.Pipe.RaftNode.RaftState.Follower);
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

	/**
	 * @return the raftStateProtoBuff
	 */
	public routing.Pipe.RaftNode.RaftState getRaftStateProtoBuff() {
		return raftStateProtoBuff;
	}

	/**
	 * @param raftStateProtoBuff the raftStateProtoBuff to set
	 */
	public void setRaftStateProtoBuff(routing.Pipe.RaftNode.RaftState raftStateProtoBuff) {
		this.raftStateProtoBuff = raftStateProtoBuff;
	}
	
	

}
