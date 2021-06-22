package ph.sinonet.vg.live.service.interfaces;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.model.sms.Platform;

import java.util.List;

/**
 * Created by jay on 11/16/16.
 */
public interface PlatformService {

    ResponseMessage processReq(RequestMessage req);

    List<Platform> getPlatformList(String platformId);
}
