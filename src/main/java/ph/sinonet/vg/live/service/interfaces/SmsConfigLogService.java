package ph.sinonet.vg.live.service.interfaces;

import java.util.Date;
import java.util.List;

import ph.sinonet.vg.live.model.sms.SmsConfigLog;
import ph.sinonet.vg.live.utils.Page;

public interface SmsConfigLogService {
	public void saveEntity(SmsConfigLog smsConfigLog);
	public Page  querySMSLogs(String name,String projectname,Date start,Date end,Integer pageIndex,Integer size);
}
