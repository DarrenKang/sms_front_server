package ph.sinonet.vg.live.service;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.model.bean.websocket.resp.AbstractRespMsg;

/**
 * Created by kierpagdato on 7/21/16.
 */
public class BaseServiceImpl {

    public ResponseMessage createResponse(RequestMessage req, AbstractRespMsg resp){
        return new ResponseMessage(req.getService(), req.getFunctionName(), resp);
    }
}
