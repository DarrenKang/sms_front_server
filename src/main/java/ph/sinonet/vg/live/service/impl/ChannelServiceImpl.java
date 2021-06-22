package ph.sinonet.vg.live.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.sinonet.vg.live.dao.SmsDao;
import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.channel.ChannelListReq;
import ph.sinonet.vg.live.model.bean.websocket.resp.channel.ChannelListResp;
import ph.sinonet.vg.live.model.enums.ErrorCode;
import ph.sinonet.vg.live.service.BaseServiceImpl;
import ph.sinonet.vg.live.service.interfaces.ChannelService;

import java.util.Collections;
import java.util.List;

/**
 * Created by jay on 11/17/16.
 */
@Service
public class ChannelServiceImpl extends BaseServiceImpl implements ChannelService {

    private static final Logger log = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Autowired
    private SmsDao smsDao;

    @Override
    public ResponseMessage processReq(RequestMessage req) {
        switch (req.getFunctionName()) {
            case ChannelListReq.NAME:
                return getChannelList(req);
            default :
                return null;
        }
    }

    private ResponseMessage getChannelList(RequestMessage req) {
        log.info("ChannelServiceImpl.getChannelList");
        ChannelListReq reqMsg = (ChannelListReq) req.getData();
        List<Integer> channels = smsDao.getChanneList();
        Collections.sort(channels);
        ChannelListResp respData = new ChannelListResp();
        respData.setQueryId(reqMsg.getQueryId());
        respData.setChannels(channels);

        respData.setStatusAndMsg(ErrorCode.SUCCESS);

        return createResponse(req, respData);

    }
}
