/**
 * 
 */
package com.sjsu.rollbits.intercluster.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class InterClusterGroupUserService implements ResultCollectable<Response> {
	protected static Logger logger = LoggerFactory.getLogger("InterClusterGroupUserService");

	Random rand = new Random();
	int noOfResultExpected = 0;
	private long routeId;
	private Channel replyChannel;
	private String groupName;
	private long gid;
	private boolean finalResult = false;

	private boolean localClusterChecked;

	private boolean sentToAllClusters;

	private List<RNode> groupShards;

	private boolean addedToPrimaryShard;

	private String groupUserName;

	private boolean intercluster;

	class AddUserGroupTask implements CommListener {
		protected Logger gmlogger = LoggerFactory.getLogger("AddUserGroupTask");

		MessageClient mc;
		String username;
		ResultCollectable<Response> resultCollectable;
		private String requestType;
		private String group;
		private long gid;
		private String groupUserName;

		private boolean intercluster;

		/**
		 * @param username
		 */
		public AddUserGroupTask(String host, int port, ResultCollectable<Response> resultCollectable,
				String requestType, String group, long gid, String groupUserName, boolean intercluster) {
			this.mc = new MessageClient(host, port);
			mc.addListener(this);
			this.resultCollectable = resultCollectable;
			this.requestType = requestType;
			this.group = group;
			this.gid = gid;
			this.groupUserName = groupUserName;
			this.intercluster = intercluster;

		}

		// @Override
		public void doTask() {
			mc.addUsertoGroup((int) gid, group, groupUserName, RollbitsConstants.INTER_CLUSTER, true);
		}

		@Override
		public String getListenerID() {
			return "AddUserGroupTask";
		}

		@Override
		public void onMessage(Route msg) {
			logger.info("AddUserGroupTask");

			resultCollectable.collectResult(msg.getResponse());
		}

	}

	
	public InterClusterGroupUserService(long routeId, Channel replyChannel, String group, long gid,
			List<RNode> groupShards, String groupUserName, boolean intercluster) {
		noOfResultExpected = ClusterDirectory.getGroupMap().size();
		this.routeId = routeId;
		this.replyChannel = replyChannel;
		this.groupName = group;
		this.groupShards = groupShards;
		this.groupUserName = groupUserName;
		this.intercluster = intercluster;
	}

	public void addUserToGroup() {

		
		
		// first just add primary shard
		AddUserGroupTask task = new AddUserGroupTask(
				ClusterDirectory.getNodeMap().get(groupShards.get(0).getNodeId()).getNodeIp(),
				ClusterDirectory.getNodeMap().get(groupShards.get(0).getNodeId()).getPort(), this,
				RollbitsConstants.INTERNAL, this.groupName, this.gid, this.groupUserName, false);

		task.doTask();
		this.addedToPrimaryShard = false;
		this.localClusterChecked = false;
		this.sentToAllClusters = false;

	}

	public void addUserToGroupOnReplicas() {

		for (RNode node : groupShards) {
			if (node.getType().equals(RNode.Type.REPLICA)) {
				AddUserGroupTask task = new AddUserGroupTask(
						ClusterDirectory.getNodeMap().get(node.getNodeId()).getNodeIp(),
						ClusterDirectory.getNodeMap().get(node.getNodeId()).getPort(), this, RollbitsConstants.INTERNAL,
						this.groupName, this.gid, this.groupUserName, false);
				task.doTask();
			}
		}
	}

	public void addUserToGroupAllClusters() {
		
		
		
		if (intercluster) {
			Map<String, Map<String, Node>> groupMap = ClusterDirectory.getGroupMap();
			for (Map.Entry<String, Map<String, Node>> entry : groupMap.entrySet()) {
				if (Loadyaml.getProperty(RollbitsConstants.CLUSTER_NAME).equals(entry.getKey())) {
					continue;
				}
				Map<String, Node> nodeMap = entry.getValue();
				Node nodeOfEachGroup = null;
				List<Node> nodeList = new ArrayList<>(nodeMap.values());
				nodeOfEachGroup = nodeList.get(rand.nextInt(nodeList.size()));
				AddUserGroupTask task = new AddUserGroupTask(nodeOfEachGroup.getNodeIp(), nodeOfEachGroup.getPort(),
						this, RollbitsConstants.INTER_CLUSTER, this.groupName, this.gid, this.groupUserName, false);
				task.doTask();
			}
		}
		if(noOfResultExpected==0){
			finalResult = true;
			publishResult();
		}

	}

	@Override
	public synchronized void collectResult(Response t) {

		if (t != null && t.getSuccess() && !localClusterChecked) {
			localClusterChecked = true;
			noOfResultExpected = 0;
			finalResult = true;
			publishResult();

		} else if (t != null && !t.getSuccess() && !sentToAllClusters) {

			addUserToGroupAllClusters();
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
		Route.Builder rb = ProtoUtil.createResponseRoute(routeId, finalResult, finalResult ? "" : "ERR_GRP_202",
				finalResult ? "" : "Group Could not be added!!");
		replyChannel.writeAndFlush(rb.build());
	}

	@Override
	public void timeout() {
		// TODO Auto-generated method stub
		
	}

}
