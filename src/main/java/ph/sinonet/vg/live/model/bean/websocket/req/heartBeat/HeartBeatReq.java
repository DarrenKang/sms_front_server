package ph.sinonet.vg.live.model.bean.websocket.req.heartBeat;

import com.google.gson.Gson;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.AbstractReqMsg;
public class HeartBeatReq extends AbstractReqMsg {
	 public static final String NAME = "heartBeat";
	private  String currentTime ;
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	 
	
	public static void main(String[] args) {
		String s = "{\n" +
				"\t\"service\": \"heartBeat\",\n" +
				"\t\"functionName\": \"heartBeat\",\n" +
				"\t\"data\": {\n" +
				"\t\t\"currentTime\": 1531466023631\n" +
				"\t}\n" +
				"}";
		Gson gson = new Gson();
		RequestMessage a = gson.fromJson(s, RequestMessage.class);
		System.out.println(a);
	}
	 
}
