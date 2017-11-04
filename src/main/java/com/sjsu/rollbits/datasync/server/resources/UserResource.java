package com.sjsu.rollbits.datasync.server.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

import routing.Pipe;

public class UserResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("user");

	private ShardingService shardingService;

	public UserResource() {
		shardingService = ShardingService.getInstance();
	}

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.USER;
	}

	@Override
	public String process(Pipe.Route msg) {

		Pipe.actionType option = msg.getUser().getAction();

		switch (option) {
		case GET:

			break;

		case PUT:
			Pipe.User user = msg.getUser();

			Pipe.Header header = msg.getHeader();

			if (header.getType() != null && !header.getType().equals("REPLICA")) {

				System.out.println(user.getUname());

				List<RNode> nodes = shardingService.getNodes(new Message(user.getUname()));

				// save to database

				for (RNode node : nodes) {

					MessageClient mc = new MessageClient(node.getIpAddress(), node.getPort());
					mc.addUser(user.getUname(), user.getEmail(), true);

				}

			} else {
				
				//save in databse

			}

			// below line can be moved to client

			// send request for sync to other intra cluster nodes

			// wait for result
			// send response back

			// Does it have to be blocking?

			break;

		}

		return "success";
	}

}
