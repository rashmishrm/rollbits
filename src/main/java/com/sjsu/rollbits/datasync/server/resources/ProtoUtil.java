package com.sjsu.rollbits.datasync.server.resources;

import java.util.ArrayList;
import java.util.List;

import routing.Pipe;
import routing.Pipe.Message.ActionType;
import routing.Pipe.MessagesRequest.Type;
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
			List<com.sjsu.rollbits.dao.interfaces.model.Message> messages, String uname) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.MESSAGES_RESPONSE);

		Pipe.MessagesResponse.Builder messageBuilder = Pipe.MessagesResponse.newBuilder();

		List<Pipe.Message> list = new ArrayList<>();

		for (com.sjsu.rollbits.dao.interfaces.model.Message mesg : messages) {
			if (mesg != null) {
				Pipe.Message.Builder m = Pipe.Message.newBuilder();
				m.setSenderId(mesg.getFromuserid() == null ? "" : mesg.getMessage());
				m.setReceiverId(mesg.getTouserid() == null ? "" : mesg.getTouserid());
				m.setAction(ActionType.POST);
				m.setPayload(mesg.getMessage());

				list.add(m.build());
			}
		}

		messageBuilder.addAllMessages(list);

		messageBuilder.setId(uname);

		rb.setMessagesResponse(messageBuilder);

		Response.Builder resp = createResponseBuilder(true, "", RollbitsConstants.SUCCESS);

		rb.setResponse(resp);

		return rb;

	}

	public static Route.Builder createMessageRequest(long id, String uname, boolean user) {

		Route.Builder rb = Route.newBuilder();
		rb.setId(id);
		rb.setPath(Route.Path.MESSAGES_REQUEST);
		Pipe.MessagesRequest.Builder ub = Pipe.MessagesRequest.newBuilder();
		ub.setId(uname);
		ub.setType(Type.GROUP);

		if (user) {
			ub.setType(Type.USER);
		}

		rb.setMessagesRequest(ub);

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

}
