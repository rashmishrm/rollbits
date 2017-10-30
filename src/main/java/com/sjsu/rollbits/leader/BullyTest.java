package com.sjsu.rollbits.leader;



import com.sjsu.rollbits.leader.Bully.BullyNode;
import com.sjsu.rollbits.leader.Bully.BullyWrapperNode;
import com.sjsu.rollbits.message.StatNode;

public class BullyTest {


	public static void testStartup() throws Exception {
		Bully b = new Bully();

		StatNode stat = new StatNode(Integer.MAX_VALUE - 1);
		BullyWrapperNode bw = new BullyWrapperNode(stat);
		b.addNode(bw);
		if (!bw.isAlive())
			bw.start();

		for (int n = 0; n < 10; n++) {
			BullyNode node = new BullyNode(n);
			b.addNode(node);
			if (!node.isAlive())
				node.start();
		}

		// allow a couple of leaders to die so that we can see the leader
		// election process in action
		Thread.sleep(40000);

		stat.report();
	}
	
	public static void main(String[] args) throws Exception {
		testStartup();
	}
}
