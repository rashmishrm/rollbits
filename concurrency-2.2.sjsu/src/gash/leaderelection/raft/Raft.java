package gash.leaderelection.raft;

import gash.messaging.Message;
import gash.messaging.Message.Delivery;
import gash.messaging.Node;
import gash.messaging.transports.Bus;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Raft consensus algorithm is similar to PAXOS and Flood Max though it
 * claims to be easier to understand. The paper "In Search of an Understandable
 * Consensus Algorithm" explains the concept. See
 * https://ramcloud.stanford.edu/raft.pdf
 * 
 * Note the Raft algo is both a leader election and consensus algo. It ties the
 * election process to the state of its distributed log (state) as the state is
 * part of the decision process of which node should be the leader.
 * 
 * 
 * @author gash
 *
 */
public class Raft {
	static AtomicInteger msgID = new AtomicInteger(0);

	private Bus<? extends RaftMessage> transport;

	public Raft() {
		transport = new Bus<RaftMessage>(0);
	}

	public void addNode(RaftNode node) {
		if (node == null)
			return;

		node.setTransport(transport);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		Node<Message> n = (Node) (node);
		transport.addNode(n);

		if (!node.isAlive())
			node.start();
	}

	/** processes heartbeats */
	public interface HeartMonitorListener {
		public void doMonitor();
	}

	public static abstract class LogEntryBase {
		private int term;
	}

	private static class LogEntry extends LogEntryBase {

	}

	/** triggers monitoring of the heartbeat */
	public static class RaftMonitor extends TimerTask {
		private RaftNode<RaftMessage> node;

		public RaftMonitor(RaftNode<RaftMessage> node) {
			if (node == null)
				throw new RuntimeException("Missing node");

			this.node = node;
		}

		@Override
		public void run() {
			node.checkBeats();
		}
	}

	/** our network node */
	public static class RaftNode<M extends RaftMessage> extends Node<M> {
		public enum RState {
			Follower, Candidate, Leader
		}

		private Bus<? extends RaftMessage> transport;

		public RaftNode(int id) {
			super(id);
		}

		protected void checkBeats() {
			System.out.println("--> node " + getNodeId() + " heartbeat");
		}

		private void sendLeaderNotice() {
			RaftMessage msg = new RaftMessage(Raft.msgID.incrementAndGet());
			msg.setOriginator(getNodeId());
			msg.setDeliverAs(Delivery.Broadcast);
			msg.setDestination(-1);
			msg.setAction(RaftMessage.Action.Append);
			send(msg);
		}

		private void sendAppendNotice() {
			RaftMessage msg = new RaftMessage(Raft.msgID.incrementAndGet());
			msg.setOriginator(getNodeId());
			msg.setDeliverAs(Delivery.Broadcast);
			msg.setDestination(-1);
			msg.setAction(RaftMessage.Action.Append);
			send(msg);
		}
		
		/** TODO args should set voting preference */
		private void sendRequestVoteNotice() {
			RaftMessage msg = new RaftMessage(Raft.msgID.incrementAndGet());
			msg.setOriginator(getNodeId());
			msg.setDeliverAs(Delivery.Broadcast);
			msg.setDestination(-1);
			msg.setAction(RaftMessage.Action.Append);
			send(msg);
		}

		private void send(RaftMessage msg) {
			// enqueue the message - if we directly call the nodes method, we
			// end up with a deep call stack and not a message-based model.
			// transport.message(msg);
		}

		/** this is called by the Node's run() - reads from its inbox */
		@Override
		public void process(RaftMessage msg) {
			// TODO process
		}

		public void setTransport(Bus<? extends RaftMessage> t) {
			this.transport = t;
		}
	}
}
