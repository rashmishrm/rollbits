package com.sjsu.rollbits.sharding.hashing;


import java.util.Properties;

public interface HashAlgo {
	void init(Properties conf);

	Long hash(Object value);
}
