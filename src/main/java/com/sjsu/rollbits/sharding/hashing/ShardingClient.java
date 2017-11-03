package com.sjsu.rollbits.sharding.hashing;

import java.util.ArrayList;
import java.util.List;



public class ShardingClient implements ShardingInterface {

	private ConsistentHash<RNode> hash = null;

	public ShardingClient(int numberOfReplicas, List<RNode> nodes) {
		hash = new ConsistentHash<>(new MurmurHash128(), numberOfReplicas, nodes);
	}

	public ConsistentHash<RNode> getHash() {
		return hash;
	}

	@Override
	public RNode getNode(Message message) {
		RNode node = hash.get(message.getUniqueKey());
		return node;
	}

	
	public static void main(String[] args) {
		List<RNode> nodes= new ArrayList<>();
		nodes.add(new RNode("1"));
		nodes.add(new RNode("2"));
		nodes.add(new RNode("3"));
		nodes.add(new RNode("4"));
		
		ShardingClient s= new ShardingClient(3,nodes );
		System.out.println(s.getNode(new Message("abcdefghi@gmail.com")).getNodeId());
		System.out.println(s.getNode(new Message("342345345@gmail.com")).getNodeId());

		System.out.println(s.getNode(new Message("dvgfx@gmail.com")).getNodeId());

		System.out.println(s.getNode(new Message("abcdefghi@gmail.com")).getNodeId());

		
	}

}
