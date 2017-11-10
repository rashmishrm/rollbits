package com.sjsu.rollbits.datasync.server.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.dao.interfaces.model.User;
import com.sjsu.rollbits.dao.interfaces.service.Service;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

import routing.Pipe;
import routing.Pipe.Route;

public class UserResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("user");

	private ShardingService shardingService;

	private Service dbService = null;

	public UserResource() {
		shardingService = ShardingService.getInstance();
		dbService = new Service();
	}

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.USER;
	}

	@Override
	public Object process(Pipe.Route msg) {
		boolean success = false;
		Pipe.actionType option = msg.getUser().getAction();

		switch (option) {
		case GET:

			break;

		case PUT:
			Pipe.User user = msg.getUser();

			Pipe.Header header = msg.getHeader();

			if (header.getType() != null && !header.getType().equals("INTERNAL")) {

				System.out.println(user.getUname());

				// Set<RNode> nodes = new HashSet<>(shardingService.getNodes(new
				// Message(user.getUname())));
				List<RNode> nodes = shardingService.getNodes(new Message(user.getUname()));

				// save to database

				for (RNode node : nodes) {
					MessageClient mc = new MessageClient(node.getIpAddress(), node.getPort());
					if (node.getType().equals(RNode.Type.REPLICA)) {
						mc.addUser(user.getUname(), user.getEmail(), true, true);
					} else {
						success = mc.addUser(user.getUname(), user.getEmail(), true, false);

					}

				}

			} else {

				System.out.println("Adding to database!!!!!");
				User dbuser = new User(user.getUname(), user.getEmail());

				dbService.persist(dbuser);
				success = true;

			}

			// below line can be moved to client

			// send request for sync to other intra cluster nodes

			// wait for result
			// send response back

			// Does it have to be blocking?

			break;

		}
		Route.Builder rb = Route.newBuilder();
		rb.setPayload(success ? "sucess" : "Failed");
		return rb;
	}

}
