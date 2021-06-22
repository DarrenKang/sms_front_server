package ph.sinonet.vg.live.model.bean.websocket;

import com.google.gson.Gson;
import ph.sinonet.vg.live.model.bean.websocket.req.AbstractReqMsg;
import ph.sinonet.vg.live.validator.annotation.JsonRequired;

public class RequestMessage extends AbstractMessage {

	public AbstractReqMsg data;

	public AbstractReqMsg getData() {
		return data;
	}
	
	public void setData(AbstractReqMsg data) {
		this.data = data;
	}

	@Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            	public String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      toString() {
		return "RequestMessage{" + "service='" + service + '\'' + ", functionName='" + functionName + '\'' + ", data="
				+ data + '}';
	}

	public static void main(String[] args) {
		String s = "{\n" +
				"\t\"service\": \"payment\",\n" +
				"\t\"functionName\": \"requestManualBankCard\",\n" +
				"\t\"data\": {\n" +
				"\t\t\"realName\": \"ggg\",\n" +
				"\t\t\"bankTypeId\": \"1\",\n" +
				"\t\t\"bankCardNo\": \"31313\",\n" +
				"\t\t\"provinceId\": \"111\",\n" +
				"\t\t\"cityId\": \"333\",\n" +
				"\t\t\"districtId\": \"444\",\n" +
				"\t\t\"requestId\": \"123123\",\n" +
				"\t\t\"userName\": \"dev\",\n" +
				"\t\t\"platformId\": \"555\",\n" +
				"\t\t\"amount\": 200.0\n" +
				"\t}\n" +
				"}";
		Gson gson = new Gson();
		RequestMessage a = gson.fromJson(s, RequestMessage.class);
		System.out.println(a);
	}

}
