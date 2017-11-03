package com.sjsu.rollbits.sharding.hashing;

import java.util.List;

public interface ShardingInterface {

	public List<RNode> getNode(Message message);

}
