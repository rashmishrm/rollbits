package com.sjsu.rollbits.datasync.server.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import routing.Pipe;
import routing.Pipe.Message;
import routing.Pipe.Message.ActionType;
import routing.Pipe.MessagesRequest.Type;
import routing.Pipe.MessagesResponse;
import routing.Pipe.Response;
import routing.Pipe.Route;
import routing.Pipe.Route.Path;

public class ProtoUtil {

	public static Route.Builder createResponseRoute(long id, boolean success, String errorCode, String message) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Path.RESPONSE);

		Response.Builder p = Response.newBuilder();
		p.setErrorCode(errorCode == null ? "" : errorCode);
		p.setMessage(message);
		p.setSuccess(success);

		rb.setResponse(p);

		return rb;

	}

	public static Response.Builder createResponseBuilder(boolean success, String errorCode, String message) {

		Response.Builder p = Response.newBuilder();
		p.setErrorCode(errorCode);
		p.setMessage(message);
		p.setSuccess(success);

		return p;

	}

	public static Route.Builder createMessageResponseRoute(long id,
			List<com.sjsu.rollbits.dao.interfaces.model.Message> messages, String uname, boolean user) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.MESSAGES_RESPONSE);

		Pipe.MessagesResponse.Builder messageBuilder = Pipe.MessagesResponse.newBuilder();

		List<Pipe.Message> list = new ArrayList<>();

		for (com.sjsu.rollbits.dao.interfaces.model.Message mesg : messages) {
			if (mesg != null) {
				Pipe.Message.Builder m = Pipe.Message.newBuilder();
				m.setSenderId(mesg.getFromuserid() == null ? "" : mesg.getFromuserid());
				m.setReceiverId(mesg.getTouserid() == null ? mesg.getTogroupid()==null?"":mesg.getTogroupid() : mesg.getTouserid());

				m.setAction(ActionType.POST);
				m.setTimestamp(mesg.getTimestamp().toString());
				m.setType(mesg.getTogroupid() == null ? Message.Type.GROUP : Message.Type.SINGLE);
				m.setPayload(mesg.getMessage());

				list.add(m.build());
			}
		}

		messageBuilder.addAllMessages(list);

		messageBuilder.setId(uname);
		messageBuilder.setType(user ? MessagesResponse.Type.USER : MessagesResponse.Type.GROUP);

		rb.setMessagesResponse(messageBuilder);

		Response.Builder resp = createResponseBuilder(true, "", RollbitsConstants.SUCCESS);

		rb.setResponse(resp);

		return rb;

	}

	public static Route.Builder createMessageResponseRoute2(long id, List<Message> messages, String uname,
			boolean user) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.MESSAGES_RESPONSE);

		Pipe.MessagesResponse.Builder messageBuilder = Pipe.MessagesResponse.newBuilder();

		List<Pipe.Message> list = new ArrayList<>();

		messageBuilder.addAllMessages(messages);

		messageBuilder.setId(uname);
		messageBuilder.setType(user ? MessagesResponse.Type.USER : MessagesResponse.Type.GROUP);

		rb.setMessagesResponse(messageBuilder);

		Response.Builder resp = createResponseBuilder(true, "", RollbitsConstants.SUCCESS);

		rb.setResponse(resp);

		return rb;

	}

	public static Route.Builder createMessageRequest(long id, String uname, boolean user, String type) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.MESSAGES_REQUEST);
		Pipe.MessagesRequest.Builder ub = Pipe.MessagesRequest.newBuilder();
		ub.setId(uname);
		ub.setType(Type.GROUP);

		if (user) {
			ub.setType(Type.USER);
		}

		Pipe.Header.Builder header = Pipe.Header.newBuilder();

		if (type.equals(Pipe.Header.Type.INTERNAL.toString())) {
			header.setType(Pipe.Header.Type.INTERNAL);

		} else if (type.equals(Pipe.Header.Type.INTER_CLUSTER.toString())) {
			header.setType(Pipe.Header.Type.INTER_CLUSTER);

		} else if (type.equals(Pipe.Header.Type.CLIENT.toString())) {
			header.setType(Pipe.Header.Type.CLIENT);

		}
		rb.setHeader(header.build());

		rb.setMessagesRequest(ub);

		return rb;
	}

	public static Route.Builder addMessageRequest(long id, String senderId, String recieverId, String message,
			String type, String messageType) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.MESSAGE);
		Pipe.Message.Builder msg = Pipe.Message.newBuilder();
		msg.setPayload(message);
		msg.setSenderId(senderId);
		msg.setReceiverId(recieverId);

		msg.setAction(routing.Pipe.Message.ActionType.POST);

		Pipe.Header.Builder header = Pipe.Header.newBuilder();

		if (type.equals(RollbitsConstants.INTERNAL)) {
			header.setType(header.getType().INTERNAL);

		} else if (type.equals(RollbitsConstants.CLIENT)) {
			header.setType(header.getType().CLIENT);

		} else {
			header.setType(header.getType().INTER_CLUSTER);

		}
		if (messageType.equals(RollbitsConstants.SINGLE))
			msg.setType(Message.Type.SINGLE);
		else
			msg.setType(Message.Type.GROUP);

		msg.setTimestamp(new Date().toString());
		rb.setMessage(msg);
		rb.setHeader(header);

		return rb;
	}

	public static Route.Builder createAddUserRequest(long id, String uname, String type) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.USER);
		Pipe.User.Builder ub = Pipe.User.newBuilder();
		ub.setEmail(uname);
		ub.setUname(uname);
		ub.setAction(routing.Pipe.User.ActionType.REGISTER);
		rb.setUser(ub);
		Pipe.Header.Builder header = Pipe.Header.newBuilder();

		if (type.equals(Pipe.Header.Type.INTERNAL.toString())) {
			header.setType(Pipe.Header.Type.INTERNAL);

		} else if (type.equals(Pipe.Header.Type.INTER_CLUSTER.toString())) {
			header.setType(Pipe.Header.Type.INTER_CLUSTER);

		} else if (type.equals(Pipe.Header.Type.CLIENT.toString())) {
			header.setType(Pipe.Header.Type.CLIENT);

		}
		rb.setHeader(header);
		return rb;
	}

	public static Route.Builder createAddGroupRequest(long id, String name, String type) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.GROUP);
		Pipe.Group.Builder gb = Pipe.Group.newBuilder();
		gb.setGname(name);
		gb.setGid(id);
		gb.setAction(routing.Pipe.Group.ActionType.CREATE);
		rb.setGroup(gb);

		Pipe.Header.Builder header = Pipe.Header.newBuilder();

		if (type.equals(Pipe.Header.Type.INTERNAL.toString())) {
			header.setType(Pipe.Header.Type.INTERNAL);

		} else if (type.equals(Pipe.Header.Type.INTER_CLUSTER.toString())) {
			header.setType(Pipe.Header.Type.INTER_CLUSTER);

		} else if (type.equals(Pipe.Header.Type.CLIENT.toString())) {
			header.setType(Pipe.Header.Type.CLIENT);

		}
		rb.setHeader(header);
		return rb;
	}

	public static Route.Builder createGetGroupRequest(long requestId, String name, String type) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(requestId);
		rb.setPath(Route.Path.GROUP);
		Pipe.Group.Builder gb = Pipe.Group.newBuilder();
		gb.setGname(name);
		gb.setGid(requestId);
		gb.setAction(routing.Pipe.Group.ActionType.GET);
		rb.setGroup(gb);

		Pipe.Header.Builder header = Pipe.Header.newBuilder();

		if (type.equals(Pipe.Header.Type.INTERNAL.toString())) {
			header.setType(Pipe.Header.Type.INTERNAL);

		} else if (type.equals(Pipe.Header.Type.INTER_CLUSTER.toString())) {
			header.setType(Pipe.Header.Type.INTER_CLUSTER);

		} else if (type.equals(Pipe.Header.Type.CLIENT.toString())) {
			header.setType(Pipe.Header.Type.CLIENT);

		}
		rb.setHeader(header);
		return rb;
	}

	public static Route.Builder createAddUsertoGroupRequest(long id, String gname, String uname, String type) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.GROUP);
		Pipe.Group.Builder gb = Pipe.Group.newBuilder();
		gb.setGname(gname);
		gb.setGid(id);
		gb.setAction(routing.Pipe.Group.ActionType.ADDUSER);
		gb.setUsername(uname);
		// System.out.println(uname);
		rb.setGroup(gb);

		Pipe.Header.Builder header = Pipe.Header.newBuilder();

		if (type.equals(Pipe.Header.Type.INTERNAL.toString())) {
			header.setType(Pipe.Header.Type.INTERNAL);

		} else if (type.equals(Pipe.Header.Type.INTER_CLUSTER.toString())) {
			header.setType(Pipe.Header.Type.INTER_CLUSTER);

		} else if (type.equals(Pipe.Header.Type.CLIENT.toString())) {
			header.setType(Pipe.Header.Type.CLIENT);

		}
		rb.setHeader(header);
		return rb;
	}

}
