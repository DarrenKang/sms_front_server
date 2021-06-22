package ph.sinonet.vg.live.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.service.interfaces.ChannelService;
import ph.sinonet.vg.live.service.interfaces.HeartBeatService;
import ph.sinonet.vg.live.service.interfaces.PlatformService;
import ph.sinonet.vg.live.service.interfaces.SmsService;
import ph.sinonet.vg.live.service.interfaces.UsableChannelService;

/**
 * Created by jay on 11/16/16.
 */
@Component
public class WebSocketServiceProvider {

    @Autowired
    private SmsService smsService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private HeartBeatService heartBeatService;
    @Autowired
    private UsableChannelService usableChannelService;
    public ResponseMessage processReq(RequestMessage req) {
        switch (req.getService()) {
            case "sending" :
                return smsService.processWSReq(req);
            case "sendingGroupMessage":
            	return smsService.processGroupWSReq(req);
            case "platform" :
                return platformService.processReq(req);
            case "channel" :
                return channelService.processReq(req);
            case "heartBeat" :
                return heartBeatService.processReq(req);
            case "getUsableChannel" :
                return usableChannelService.processReq(req);
            default:
                return null;
        }

    }

}
