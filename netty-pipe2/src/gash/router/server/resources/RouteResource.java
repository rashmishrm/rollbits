/**
 * Copyright 2012 Gash.
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
package gash.router.server.resources;

/**
 * template for route handlers
 * 
 * @author gash
 * 
 */
public interface RouteResource {

	/**
	 * the path that the resource processes (same as a routing path/id)
	 * 
	 * @return
	 */
	String getPath();

	/**
	 * processing of a request. Requests are delegated to this method by the
	 * server. Responses are collected as a String and returned to the caller
	 * 
	 * @param body
	 *            the string representation of the request
	 * @return The string representation of the response
	 */
	String process(String body);
}
