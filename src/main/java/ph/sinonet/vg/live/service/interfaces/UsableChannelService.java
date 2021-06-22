package ph.sinonet.vg.live.service.interfaces;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;

public interface UsableChannelService {
	ResponseMessage processReq(RequestMessage req);
}
