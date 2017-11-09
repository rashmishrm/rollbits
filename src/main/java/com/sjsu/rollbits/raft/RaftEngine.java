/**
 * 
 */
package com.sjsu.rollbits.raft;

/**
 * @author nishantrathi
 *
 */
public class RaftEngine implements Runnable {

	
	/**
	 * 
	 */
	public RaftEngine() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(RaftContext.RAFT_TIMER);
				RaftContext raftContext = RaftContext.getInstance();
				RaftState raftState = raftContext.getRaftState();
				raftState.doAction(raftContext);
				System.out.println("Raft Leader is : "+raftContext.getLeaderNodeId());
				System.out.println("State of this node is : "+raftContext.getRaftState());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


}
