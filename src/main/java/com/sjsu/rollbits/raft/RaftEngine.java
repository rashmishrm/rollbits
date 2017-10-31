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
		try {
			RaftContext raftContext = RaftContext.getInstance();
			while(true){
				Thread.sleep(RaftContext.RAFT_TIMER);
				RaftState raftState = raftContext.getRaftState();
				raftState.doAction(raftContext);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
