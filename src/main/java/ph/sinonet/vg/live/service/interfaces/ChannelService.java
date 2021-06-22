package ph.sinonet.vg.live.service.interfaces;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;

/**
 * Created by jay on 11/17/16.
 */
public interface ChannelService {

    ResponseMessage processReq(RequestMessage req);
}
