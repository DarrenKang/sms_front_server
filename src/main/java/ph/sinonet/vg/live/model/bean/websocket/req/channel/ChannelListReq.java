package ph.sinonet.vg.live.model.bean.websocket.req.channel;

import com.google.gson.Gson;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.AbstractReqMsg;

/**
 * Created by jay on 11/17/16.
 */
public class ChannelListReq extends AbstractReqMsg {

    public static final String NAME = "getChannelList";

    private String queryId;

    public String getQueryId() {
        return queryId;
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
