package ph.sinonet.vg.live.service.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.sinonet.vg.live.dao.SmsDao;
import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.channel.UsableChannelListReq;
import ph.sinonet.vg.live.model.bean.websocket.resp.channel.UsableChannelListResp;
import ph.sinonet.vg.live.model.enums.ErrorCode;
import ph.sinonet.vg.live.service.BaseServiceImpl;
import ph.sinonet.vg.live.service.interfaces.UsableChannelService;

@Service
public class UsableChannelServiceImpl extends BaseServiceImpl implements UsableChannelService{
	 private static final Logger log = LoggerFactory.getLogger(UsableChannelServiceImpl.class);

	@Autowired
	private SmsDao smsDao;
	@Override
	public ResponseMessage processReq(RequestMessage req) {
		// TODO Auto-generated method stub
		  switch (req.getFunctionName()) {
          case UsableChannelListReq.NAME:
              return getUsableChannelList(req);
          default :
              return null;
      }
	}
	 private ResponseMessage getUsableChannelList(RequestMessage req) {
	        log.info("UsableChannelServiceImpl.getUsableChannelList");
	        UsableChannelListReq reqMsg = (UsableChannelListReq) req.getData();
	        String platformId=reqMsg.getPlatformId();
	        List<Integer> channels = smsDao.getUsableChanneList(platformId);
	        Collections.sort(channels);
	        UsableChannelListResp respData = new UsableChannelListResp();
	        respData.setChannels(channels);

	        respData.setStatusAndMsg(ErrorCode.SUCCESS);

	        return createResponse(req, respData);

	    }

}
