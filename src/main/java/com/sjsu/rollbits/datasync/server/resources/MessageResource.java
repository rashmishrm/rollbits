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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * processes requests of message passing - demonstration
 * 
 * @author gash
 * 
 */
public class MessageResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("message");

	public String getPath() {
		return "/message";
	}

	public String process(String body) {
		if (body == null || body.trim().length() == 0)
			throw new RuntimeException("Missing/Null data");

		logger.info(body);
		return "good";
	}

}
