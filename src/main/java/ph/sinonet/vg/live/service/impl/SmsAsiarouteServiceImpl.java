package ph.sinonet.vg.live.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.sinonet.vg.live.dao.SmsDao;
import ph.sinonet.vg.live.model.sms.ServerInFo;
import ph.sinonet.vg.live.service.BaseServiceImpl;
import ph.sinonet.vg.live.service.interfaces.SmsAsiarouteService;
@Service("smsAsiarouteService")
public class SmsAsiarouteServiceImpl extends BaseServiceImpl  implements SmsAsiarouteService {
	@Autowired
	private SmsDao smsDao;

	@Override
	public String addSmsAsiarouteConfig(String name, String serverIp, String username, String password, Integer channel,
			String platformIds, String remark) {
		// TODO Auto-generated method stub
		String msg = smsDao.addSmsAsiarouteConfig(name, serverIp, username, password,channel,platformIds,remark);
		return msg;
	}

	@Override
	public List<ServerInFo> querySmsServerConfigByProject(String name, String projectname, Integer channel) {
		// TODO Auto-generated method stub
		return smsDao.querySmsServerConfigByProject(name, channel,projectname);
	}

	@Override
	public String deleteSmsServerConfig(Integer id) {
		// TODO Auto-generated method stub
		 smsDao.deleteSmsServerConfig(id);
		 return "删除成功";
	}

	@Override
	public String updateSmsServerConfig(Integer id, String name, String serverIp, String username, String password,
			Integer channel, String platformIds, String remark) {
		// TODO Auto-generated method stub
		String msg=smsDao.updateSmsServerConfig(id,name,serverIp,username,password,channel,platformIds,remark);
		return msg;
	}

}
