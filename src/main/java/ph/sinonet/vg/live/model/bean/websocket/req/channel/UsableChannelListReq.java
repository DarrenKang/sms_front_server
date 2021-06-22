package ph.sinonet.vg.live.model.bean.websocket.req.channel;

import com.google.gson.Gson;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.AbstractReqMsg;

public class UsableChannelListReq extends AbstractReqMsg{
	 public static final String NAME = "getUsableChannelList";

	    private String platformId;

	    

	    public String getPlatformId() {
			return platformId;
		}



		public void setPlatformId(String platformId) {
			this.platformId = platformId;
		}



		public static void main(String[] args) {
			String s = "{\n" +
					"\t\"service\": \"channel\",\n" +
					"\t\"functionName\": \"getChannelList\",\n" +
					"\t\"data\": {\n" +
					"\t\t\"queryId\": 111\n" +
					"\t}\n" +
					"}";
			Gson gson = new Gson();
			RequestMessage a = gson.fromJson(s, RequestMessage.class);
			System.out.println(a);
		}
}
