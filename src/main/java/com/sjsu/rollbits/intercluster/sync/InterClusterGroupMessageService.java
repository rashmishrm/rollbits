/**
 * 
 */
package com.sjsu.rollbits.intercluster.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.datasync.server.resources.ProtoUtil;
import com.sjsu.rollbits.datasync.server.resources.RollbitsConstants;
import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.Node;
import com.sjsu.rollbits.yml.Loadyaml;

import io.netty.channel.Channel;
import routing.Pipe.Message;
import routing.Pipe.Response;
import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class InterClusterGroupMessageService implements ResultCollectable<Response> {
	protected static Logger logger = LoggerFactory.getLogger("InterClusterGroupMessageService");

	Random rand = new Random();
	int noOfResultExpected = 0;
	private long routeId;
	private Channel replyChannel;
	private String userName;
	private boolean finalResult=false;
	private String sender;
	private String reciever;
	private String message;

	class SendGroupMessageTask implements CommListener {
		protected  Logger gmlogger = LoggerFactory.getLogger("SendGroupMessageTask");

		MessageClient mc;
		String username;
		ResultCollectable<Response> resultCollectable;
		private String requestType;

		/**
		 * @param username
		 */
		public SendGroupMessageTask(String host, int port, String username,
				ResultCollectable<Response> resultCollectable, String requestType) {
			this.mc = new MessageClient(host, port);
			mc.addListener(this);
			this.username = username;
			this.resultCollectable = resultCollectable;
			this.requestType = requestType;
		}

		// @Override
		public void doTask() {
			mc.fetchMessages(username, requestType);
		}

		@Override
		public String getListenerID() {
			return "SendGroupMessageTask";
		}

		@Override
		public void onMessage(Route msg) {
			logger.info("SendGroupMessageTask");

			resultCollectable.collectResult(msg.getResponse());
		}

	}

	public InterClusterGroupMessageService(long routeId, Channel replyChannel, String sender, String reciever, String message) {
		noOfResultExpected = ClusterDirectory.getGroupMap().size() - 1;
		this.routeId = routeId;
		this.replyChannel = replyChannel;
		this.sender = sender;
		this.reciever = reciever;
		this.message = message;
		
	}

	public void sendGroupMessage() {
		Map<String, Map<String, Node>> groupMap = ClusterDirectory.getGroupMap();
			for (Map.Entry<String, Map<String, Node>> entry : groupMap.entrySet()) {
				if (Loadyaml.getProperty(RollbitsConstants.CLUSTER_NAME).equals(entry.getKey())) {
					continue;
				}
				Map<String, Node> nodeMap = entry.getValue();
				Node nodeOfEachGroup = null;
				List<Node> nodeList = new ArrayList<>(nodeMap.values());
				nodeOfEachGroup = nodeList.get(rand.nextInt(nodeList.size()));
				SendGroupMessageTask task = new SendGroupMessageTask(nodeOfEachGroup.getNodeIp(), nodeOfEachGroup.getPort(),
						userName, this, RollbitsConstants.INTER_CLUSTER);
				task.doTask();
			}
			if(noOfResultExpected == 0){
				publishResult();
			}
	}

	@Override
	public synchronized void collectResult(Response t) {
		
		if(t!=null && t.getSuccess()){
			finalResult=true;
			noOfResultExpected =0;
			publishResult();
		} else{
			noOfResultExpected--;
		}
		if(noOfResultExpected==0){
			publishResult();
		}
	}

	@Override
	public void publishResult() {
		Route.Builder rb = ProtoUtil.createResponseRoute(routeId, finalResult, finalResult ? "" : "ERR_GRP_201",
				finalResult ? "" : "Message could not be sent!");
		replyChannel.writeAndFlush(rb.build());
	}

}
