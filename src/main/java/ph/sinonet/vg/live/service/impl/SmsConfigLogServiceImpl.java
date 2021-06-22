package ph.sinonet.vg.live.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ph.sinonet.vg.live.dao.SmsDao;
import ph.sinonet.vg.live.model.sms.SmsConfigLog;
import ph.sinonet.vg.live.service.interfaces.SmsConfigLogService;
import ph.sinonet.vg.live.utils.Page;
@Service("smsConfigLogService")
public class SmsConfigLogServiceImpl implements SmsConfigLogService {
	@Resource
	private SmsDao smsDao;
	@Override
	public void saveEntity(SmsConfigLog smsConfigLog) {
		// TODO Auto-generated method stub
		smsDao.saveEntity(smsConfigLog);
	}
	@Override
	public Page querySMSLogs(String operator, String platformIds, Date start, Date end, Integer pageIndex, Integer size) {
		// TODO Auto-generated method stub
		return smsDao.querySMSLogsContents(operator,platformIds,start,end,pageIndex,size);
	}

}
