package ph.sinonet.vg.live.service.interfaces;

import java.util.List;

import ph.sinonet.vg.live.model.sms.ServerInFo;

public interface SmsAsiarouteService {

	String addSmsAsiarouteConfig(String name, String serverIp, String username, String password, Integer channel,
			String platformIds, String remark);

	List<ServerInFo> querySmsServerConfigByProject(String name, String projectname, Integer channel);

	String deleteSmsServerConfig(Integer id);

	String updateSmsServerConfig(Integer id, String name, String serverIp, String username, String password,
			Integer channel, String platformIds, String remark);

}
