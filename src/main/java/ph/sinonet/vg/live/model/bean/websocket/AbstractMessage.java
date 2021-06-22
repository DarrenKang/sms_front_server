package ph.sinonet.vg.live.model.bean.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.sf.json.JSONObject;
import ph.sinonet.vg.live.model.bean.websocket.req.AbstractReqMsg;
import ph.sinonet.vg.live.model.bean.websocket.req.channel.ChannelListReq;
import ph.sinonet.vg.live.model.bean.websocket.req.channel.UsableChannelListReq;
import ph.sinonet.vg.live.model.bean.websocket.req.heartBeat.HeartBeatReq;
import ph.sinonet.vg.live.model.bean.websocket.req.platform.SyncDataReq;
import ph.sinonet.vg.live.model.bean.websocket.req.send.SendMsgReq;
import ph.sinonet.vg.live.validator.deserializer.AnnotatedDeserializer;

/**
 * Created by jay on 7/14/16.
 */
public abstract class AbstractMessage {

	String service;
	String functionName;

	public String getService() {
		return service;
	}

	public String getFunctionName() {
		return functionName;
	}
	

	public void setService(String service) {
		this.service = service;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String toJson() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}

	public static <R> R convert(String jsonReq, Class clazz) {
		Gson gson = new Gson();
		return (R) gson.fromJson(jsonReq, clazz);
	}

	public static RequestMessage convert(String json) throws JsonParseException {
		System.out.println("JSON DATA " + json);
		Gson gson = new GsonBuilder().registerTypeAdapter(RequestMessage.class, new AnnotatedDeserializer<RequestMessage>()).create();
		RequestMessage req = gson.fromJson(json, RequestMessage.class);
		req.data = gson.fromJson(getData(json), req.getTypeFunction());
		return req;
	}

	private static String getData(String json) {
		JSONObject jsonObj = JSONObject.fromObject(json);
		return jsonObj.getString("data");
	}

	public Class<? extends AbstractReqMsg> getTypeFunction() {
		switch (this.service) {
		case "sending":
			return getSendServiceType(this.functionName);
		case "sendingGroupMessage":	
			return getSendGroupServiceType(this.functionName);
		case "platform":
				return getPlatformServiceType(this.functionName);
		case "channel" :
				return getChannelServiceType(this.functionName);
		case "connection":
			return getConnectionServiceType(this.functionName);
		case "heartBeat":
			return getHeartBeatServiceType(this.functionName);
		case "getUsableChannel":
			return getUsableChannelServiceType(this.functionName);
		default:
			return null;
		}

	}

	private Class getSendGroupServiceType(String functionName) {
		// TODO Auto-generated method stub
		switch (functionName) {
		case SendMsgReq.NAME:
			return SendMsgReq.class;
		default:
			return null;
		}
	}
	private Class getHeartBeatServiceType(String functionName) {
		// TODO Auto-generated method stub
		switch (functionName) {
		case HeartBeatReq.NAME:
			return HeartBeatReq.class;
		default:
			return null;
		}
	}

	private Class getSendServiceType(String functionName) {
		switch (functionName) {
		case SendMsgReq.NAME:
			return SendMsgReq.class;
		default:
			return null;
		}
	}

	private Class getPlatformServiceType(String functionName) {
		switch (functionName) {
			case SyncDataReq.NAME:
				return SyncDataReq.class;
			default:
				return null;
		}
	}

	private Class getChannelServiceType(String functionName) {
		switch (functionName) {
			case ChannelListReq.NAME:
				return ChannelListReq.class;
			default:
				return null;
		}
	}
	private Class getUsableChannelServiceType(String functionName) {
		switch (functionName) {
			case UsableChannelListReq.NAME:
				return UsableChannelListReq.class;
			default:
				return null;
		}
	}
	private Class getConnectionServiceType(String functionName) {
		switch (functionName) {
//		case ConnectionReq.NAME:
//			return ConnectionReq.class;
		default:
			return null;
		}
	}

}
