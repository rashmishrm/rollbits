/**
 * Copyright 2016 Gash.
 *
 * This file and intellectual content is protected under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.sjsu.rollbits.datasync.server.resources;



import org.apache.log4j.Logger;

import io.netty.channel.Channel;
import routing.Pipe;

/**
 * responds to request for pinging the service
 * 
 * @author gash
 * 
 */
public class PingResource implements RouteResource {
	protected static Logger logger = Logger.getLogger("PingResource");

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.PING;
	}

	@Override
	public Object process(Pipe.Route body, Channel returnChannel) {

		return null;
	}


}
