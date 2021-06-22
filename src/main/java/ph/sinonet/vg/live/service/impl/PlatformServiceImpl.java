package ph.sinonet.vg.live.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.sinonet.vg.live.dao.PlatformDao;
import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.platform.SyncDataReq;
import ph.sinonet.vg.live.model.bean.websocket.resp.platform.SyncDataResp;
import ph.sinonet.vg.live.model.enums.ErrorCode;
import ph.sinonet.vg.live.model.enums.PlatformStatus;
import ph.sinonet.vg.live.model.sms.Platform;
import ph.sinonet.vg.live.service.BaseServiceImpl;
import ph.sinonet.vg.live.service.interfaces.PlatformService;

import java.util.List;

/**
 * Created by jay on 11/16/16.
 */
@Service("platformService")
public class PlatformServiceImpl extends BaseServiceImpl implements PlatformService {

    private static final Logger log = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Autowired
    private PlatformDao platformDao;

    @Override
    public ResponseMessage processReq(RequestMessage req) {
        switch (req.getFunctionName()) {
            case SyncDataReq.NAME:
                return syncData(req);
            default :
                return null;
        }

    }

    @Override
    public List<Platform> getPlatformList(String platformId) {
        return platformDao.getPlatformList(platformId);
    }

    private ResponseMessage syncData(RequestMessage msg) {
        log.info("PlatformServiceImpl.syncData");
        SyncDataReq reqMsg = (SyncDataReq) msg.getData();
        SyncDataResp respData = new SyncDataResp();

        for (SyncDataReq.Platform reqPlatform : reqMsg.getPlatforms()) {

            Platform platform = platformDao.getPlatformById(reqPlatform.getPlatformId());
            if (platform == null) {
                platform = new Platform();
                platform.setPlatformId(reqPlatform.getPlatformId());
            }
            platform.setCode(reqPlatform.getCode());
            platform.setName(reqPlatform.getName());
            platform.setDescription(reqPlatform.getDescription());
            platform.setStatus(PlatformStatus.ENABLE);
            platformDao.saveOrUpdate(platform);

        }

        platformDao.deleteByIds(reqMsg);
        respData.setStatusAndMsg(ErrorCode.SUCCESS);

        return createResponse(msg, respData);
    }
}
