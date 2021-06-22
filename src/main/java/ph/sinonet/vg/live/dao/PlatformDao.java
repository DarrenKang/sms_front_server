package ph.sinonet.vg.live.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ph.sinonet.vg.live.model.bean.websocket.req.platform.SyncDataReq;
import ph.sinonet.vg.live.model.enums.PlatformStatus;
import ph.sinonet.vg.live.model.sms.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 11/16/16.
 */
@Repository
public class PlatformDao {

    @Autowired
    private UniversalDao smsBaseDao;

    public Platform getPlatformById(String platformId) {
        return (Platform) smsBaseDao.get(Platform.class, platformId);
    }

    public void saveOrUpdate(Object o) {
        smsBaseDao.getHibernateTemplate().saveOrUpdate(o);
    }

    public void deleteByIds(SyncDataReq syncDataReq) {
        List<Platform> platformList = smsBaseDao.loadAll(Platform.class);
        List<String> syncedIds = new ArrayList<String>();

        for (SyncDataReq.Platform reqPlatform : syncDataReq.getPlatforms()) {
            syncedIds.add(reqPlatform.getPlatformId());
        }

        for (Platform platform : platformList) {
            if (!syncedIds.contains(platform.getPlatformId())) {
                platform.setStatus(PlatformStatus.DISABLE);
                smsBaseDao.update(platform);
            }
        }

    }

    public List<Platform> getPlatformList(String platformId) {
        List<Platform> platformList = null;
        if (platformId == null) {
            platformList = smsBaseDao.loadAll(Platform.class);
            return platformList;

        } else {
            Platform platform = (Platform) smsBaseDao.get(Platform.class, platformId);
            platformList.add(platform);
        }

        return platformList;
    }

}