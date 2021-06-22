package ph.sinonet.vg.live.model.bean.websocket;

import ph.sinonet.vg.live.model.bean.websocket.resp.AbstractRespMsg;

/**
 * Created by jay on 7/14/16.
 */
public class ResponseMessage extends AbstractMessage {

	private AbstractRespMsg data;

	public ResponseMessage(String service, String functionName, AbstractRespMsg data) {
		this.service = service;
		this.functionName = functionName;
		this.data = data;
	}

	public ResponseMessage(AbstractRespMsg data) {
		this.data = data;
	}

	public ResponseMessage() {
	}

	public AbstractRespMsg getData() {
		return data;
	}

	public void setData(AbstractRespMsg data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResponseMessage{" + "service='" + service + '\'' + ", functionName='" + functionName + '\'' + ", data="
				+ data + '}';
	}
}
