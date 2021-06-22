package ph.sinonet.vg.live.service.interfaces;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;

public interface HeartBeatService {
	ResponseMessage processReq(RequestMessage req);
}
