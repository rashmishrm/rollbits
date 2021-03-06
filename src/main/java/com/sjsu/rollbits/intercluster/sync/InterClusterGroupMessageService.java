/**
 * 
 */
package com.sjsu.rollbits.intercluster.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.datasync.server.resources.ProtoUtil;
import com.sjsu.rollbits.datasync.server.resources.RollbitsConstants;
import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.Node;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.yml.Loadyaml;

import io.netty.channel.Channel;
import routing.Pipe.Response;
import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class InterClusterGroupMessageService implements ResultCollectable<Response> {
	protected static Logger logger = Logger.getLogger("InterClusterGroupMessageService");

	Random rand = new Random();
	int noOfResultExpected = 0;
	private long routeId;
	private Channel replyChannel;
	private String userName;
	private boolean finalResult = false;
	private String sender;
	private String reciever;
	private String message;

	private String internalPrimaryNode;

	private boolean localClusterChecked;

	private boolean sentToAllClusters;

	private List<RNode> groupShards;

	private boolean isInterCLuster;

	private boolean isResultPublished = false;

	class SendGroupMessageTask implements CommListener {
		protected  Logger logger = Logger.getLogger("SendGroupMessageTask");

		MessageClient mc;
		String username;
		ResultCollectable<Response> resultCollectable;
		private String requestType;
		private String sender;
		private String reciever;
		private String message;

		/**
		 * @param username
		 */
		public SendGroupMessageTask(String host, int port, ResultCollectable<Response> resultCollectable,
				String requestType, String sender, String reciever, String message) {
			this.mc = new MessageClient(host, port);
			mc.addListener(this);
			this.resultCollectable = resultCollectable;
			this.requestType = requestType;
			this.sender = sender;
			this.reciever = reciever;
			this.message = message;
		}

		// @Override
		public void doTask() {
			mc.sendMessage(sender, reciever, message, requestType, true, RollbitsConstants.GROUP);
		}

		@Override
		public String getListenerID() {
			return "SendGroupMessageTask";
		}

		@Override
		public void onMessage(Route msg) {
			logger.info("SendGroupMessageTask");
			if (msg.hasResponse())
				resultCollectable.collectResult(msg.getResponse());
			else
				noOfResultExpected--;
		}

	}

	public InterClusterGroupMessageService(long routeId, Channel replyChannel, String sender, String reciever,
			String message, List<RNode> groupShards, boolean isInterCluster) {
		noOfResultExpected = ClusterDirectory.getGroupMap().size();
		this.routeId = routeId;
		this.replyChannel = replyChannel;
		this.sender = sender;
		this.reciever = reciever;
		this.message = message;
		this.groupShards = groupShards;
		this.isInterCLuster = isInterCluster;
	}

	public void sendGroupMessage() {
		Thread t = new Thread(new Timer(this));
		t.start();
		if (!Loadyaml.getProperty(RollbitsConstants.NODE_NAME).equals(groupShards.get(0).getNodeId())) {
			SendGroupMessageTask task = new SendGroupMessageTask(
					ClusterDirectory.getNodeMap().get(groupShards.get(0).getNodeId()).getNodeIp(),
					ClusterDirectory.getNodeMap().get(groupShards.get(0).getNodeId()).getPort(), this,
					RollbitsConstants.INTERNAL, sender, reciever, message);
			task.doTask();
			this.localClusterChecked = false;
			this.sentToAllClusters = false;
		} else {
			sendMessageToAllClusters();
			this.localClusterChecked = true;
			this.sentToAllClusters = true;
		}

	}

	public void addUserToGroupOnReplicas() {

		for (RNode node : groupShards) {
			if (node.getType().equals(RNode.Type.REPLICA)) {
				SendGroupMessageTask task = new SendGroupMessageTask(
						ClusterDirectory.getNodeMap().get(node.getNodeId()).getNodeIp(),
						ClusterDirectory.getNodeMap().get(node.getNodeId()).getPort(), this, RollbitsConstants.INTERNAL,
						sender, reciever, message);
				task.doTask();
			}
		}
	}

	public void sendMessageToAllClusters() {
		if (isInterCLuster) {
			Map<String, Map<String, Node>> groupMap = ClusterDirectory.getGroupMap();
			for (Map.Entry<String, Map<String, Node>> entry : groupMap.entrySet()) {
				if (Loadyaml.getProperty(RollbitsConstants.CLUSTER_NAME).equals(entry.getKey())) {
					continue;
				}
				Map<String, Node> nodeMap = entry.getValue();
				Node nodeOfEachGroup = null;
				List<Node> nodeList = new ArrayList<>(nodeMap.values());
				nodeOfEachGroup = nodeList.get(rand.nextInt(nodeList.size()));
				SendGroupMessageTask task = new SendGroupMessageTask(nodeOfEachGroup.getNodeIp(),
						nodeOfEachGroup.getPort(), this, RollbitsConstants.INTER_CLUSTER, sender, reciever, message);
				task.doTask();
			}
		}

	}

	@Override
	public synchronized void collectResult(Response t) {

		if (t != null && t.getSuccess() && !localClusterChecked) {
			localClusterChecked = true;
			finalResult = true;
			addUserToGroupOnReplicas();
			publishResult();

		} else if (t != null && !t.getSuccess() && !sentToAllClusters) {

			sendMessageToAllClusters();
			sentToAllClusters = true;

		} else {

			if (t != null && t.getSuccess()) {
				finalResult = true;
				noOfResultExpected = 0;

				publishResult();
			} else {
				noOfResultExpected--;
				if (noOfResultExpected == 0) {
					publishResult();
				}
			}

		}

	}

	@Override
	public void publishResult() {
		if (!isResultPublished) {
			Route.Builder rb = ProtoUtil.createResponseRoute(routeId, finalResult, finalResult ? "" : "ERR_GRP_201",
					finalResult ? "" : "Message could not be sent!");
			replyChannel.writeAndFlush(rb.build());
			isResultPublished = true;
		}
	}

	@Override
	public void timeout() {
		logger.info("TIMEOUT ICGMS:"+finalResult);
		noOfResultExpected = 0;
		finalResult = true;
		publishResult();
	}

}
