package ph.sinonet.vg.live.dao;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ph.sinonet.vg.live.model.Constants;
import ph.sinonet.vg.live.model.bean.websocket.req.send.SendMsgReq;
import ph.sinonet.vg.live.model.sms.NexmoResponse;
import ph.sinonet.vg.live.model.sms.ServerInFo;
import ph.sinonet.vg.live.model.sms.SmsAccount;
import ph.sinonet.vg.live.model.sms.SmsAds;
import ph.sinonet.vg.live.model.sms.SmsConfig;
import ph.sinonet.vg.live.model.sms.SmsConfigLog;
import ph.sinonet.vg.live.model.sms.SmsLog;
import ph.sinonet.vg.live.utils.DateUtils;
import ph.sinonet.vg.live.utils.Page;
import ph.sinonet.vg.live.utils.PageQuery;
import ph.sinonet.vg.live.utils.StringUtil;
import ph.sinonet.vg.live.model.sms.SmsMessages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Tom on 10/25/16.
 */

@Repository
public class SmsDao {

    @Autowired
    private UniversalDao smsBaseDao;
    @Autowired
    private UniversalDao bisDao;


    public String modifyAccount(String loginname, String password){
    	String msg=null;
        SmsAccount smsAccount = (SmsAccount) smsBaseDao.get(SmsAccount.class, loginname);
        smsAccount.setLoginname(loginname);
        smsAccount.setPassword(password);
        smsBaseDao.update(smsAccount);
        msg = "MODIFYACCOUNT";
        return msg;
    }

    public String addAccount(SmsAccount smsAccount){
    	String msg=null;
    	 smsBaseDao.saveOrUpdate(smsAccount); 
    	 msg="ADDACOUNT";
    	 return msg;
    	 }

    public SmsAccount getSmsAccount(String loginname, String password){

        SmsAccount smsAccount = (SmsAccount) smsBaseDao.get(SmsAccount.class, loginname);

        if(smsAccount!= null){
            if(password.equals(smsAccount.getPassword()))
                return smsAccount;
        }

        return null;
    }

    public void saveSentSMS(SmsMessages smsMessages){
        bisDao.save(smsMessages);
    }
    public void saveWsSentSMS(ph.sinonet.vg.live.model.sms.SmsMessages smsMessages){
        smsBaseDao.save(smsMessages);
    }

    public ArrayList<SmsConfig> getSmsConfig(SendMsgReq req) {
        System.out.println("query data " + req);
        DetachedCriteria dc = DetachedCriteria.forClass(SmsConfig.class);
        dc.add(Restrictions.eq("channel", Integer.parseInt(req.getChannel())));
        dc.add(Restrictions.eq("platformIds", req.getPlatformId()));
        dc.add(Restrictions.eq("flag", 0));
        dc.add(Restrictions.eq("flag1", 0));
        return (ArrayList<SmsConfig>) smsBaseDao.getHibernateTemplate().findByCriteria(dc);
    }
    public List<SmsConfig> getsmsConfig(SendMsgReq req,String priority) {
        System.out.println("query data " + req);
        DetachedCriteria dc = DetachedCriteria.forClass(SmsConfig.class);
        dc.add(Restrictions.eq("channel", Integer.parseInt(req.getChannel())));
        dc.add(Restrictions.like("platformIds", req.getPlatformId(), MatchMode.ANYWHERE));
        dc.add(Restrictions.eq("priority",priority));
        dc.add(Restrictions.eq("flag1", 0));
        return (List<SmsConfig>) smsBaseDao.getHibernateTemplate().findByCriteria(dc);
    }
    
    public List<SmsConfig> getSmsConfig(SendMsgReq req,String priority) {
        System.out.println("query data " + req);
        DetachedCriteria dc = DetachedCriteria.forClass(SmsConfig.class);
        dc.add(Restrictions.eq("channel", Integer.parseInt(req.getChannel())));
        dc.add(Restrictions.like("platformIds", req.getPlatformId(), MatchMode.ANYWHERE));
        dc.add(Restrictions.eq("flag", 0));
        dc.add(Restrictions.eq("priority",priority));
        dc.add(Restrictions.eq("flag1", 0));
        return (List<SmsConfig>) smsBaseDao.getHibernateTemplate().findByCriteria(dc);
    }
    public List<SmsConfig> getAllSmsConfig(SendMsgReq req) {
        System.out.println("query data " + req);
        DetachedCriteria dc = DetachedCriteria.forClass(SmsConfig.class);
        dc.add(Restrictions.eq("channel", Integer.parseInt(req.getChannel())));
        dc.add(Restrictions.like("platformIds", req.getPlatformId(), MatchMode.ANYWHERE));
        dc.add(Restrictions.eq("flag1", 0));
        return (List<SmsConfig>) smsBaseDao.getHibernateTemplate().findByCriteria(dc);
    }
    
    public List<Integer> getChanneList() {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsConfig.class);
        dc.setProjection(Projections.distinct(Projections.property("channel")));
        return smsBaseDao.findByCriteria(dc);
    }

    public void saveSmsLog(String apiResp, String content, String phone, String platform, String smsName, String provider) {
        SmsLog log = new SmsLog();
        log.setPlatform(platform);
        log.setProvider(provider);
        log.setSmsName(smsName);
        log.setPhoneNumber(phone);
        log.setContent(content);
        log.setApiResponse(apiResp);
        smsBaseDao.save(log);
    }

    /**
     * add sms config
     */
    public String addSmsConfig(String type, String user, String server, String remark, Integer port, String[] platformIds, String password, String name, Integer flag, Integer channel,String priority,String totalCounts){
        String msg = null;
        String projects = null;
        if(platformIds != null){
            projects = StringUtil.arrayToString(platformIds);
        }

        SmsConfig smsConfig = (SmsConfig) smsBaseDao.get(SmsConfig.class, name);
        if(smsConfig == null){
            SmsConfig config = new SmsConfig(type, user, server, remark, port, projects, password, name, flag, channel,DateUtils.parseDateForStandard(),priority,totalCounts,"0",0);
            config.setType(type);
            smsBaseDao.save(config);
            msg = "SMSCONFIG_ADDED";
        }else{
            msg = "SMSCONFIG_EXISTS";
        }
        return msg;
    }
    
    /**
     * delete sms config
     */
    public String deleteSmsConfig(String username, String name){
        String msg = null;
        SmsConfig smsConfig = (SmsConfig) smsBaseDao.get(SmsConfig.class, username);
        if(smsConfig != null){
            smsBaseDao.delete(smsConfig);
            msg = "SMSCONFIG_DELETED";
        }
        return msg;
    }
    
    /**
     * update sms config
     */
    public String updateSmsConfig(String type, String user, String server, String remark, Integer port, String[] platformIds, String password, String name, Integer flag, Integer channel,String priority,String totalCounts){
        String msg = null;
        String projects = null;
        if(platformIds != null){
            projects = StringUtil.arrayToString(platformIds);
        }
        SmsConfig smsConfig = (SmsConfig) smsBaseDao.get(SmsConfig.class, name);
        if(smsConfig != null){

            if(type != null)
                smsConfig.setType(type);

            if(server != null)
                smsConfig.setServer(server);

           if(port != null)
               smsConfig.setPort(port);

            if(user != null)
                smsConfig.setUser(user);

            if(channel != null)
                smsConfig.setChannel(channel);

            if(remark != null)
                smsConfig.setRemark(remark);

            if (StringUtil.isNotBlank(password)) {
                smsConfig.setPassword(password);
            }

            if (StringUtil.isNotBlank(projects)) {
                smsConfig.setPlatformIds(projects);
            }
            
            	smsConfig.setPriority(priority);
            if(StringUtil.isNotEmpty(totalCounts)){
            	smsConfig.setTotalCounts(totalCounts);
            }

            smsBaseDao.update(smsConfig);
            msg = "SMSCONFIG_UPDATED";
        }
        return msg;
    }

    /**
     * enable sms config
     */
    public String enableConfig(String name){
        String msg = null;
        SmsConfig smsConfig = (SmsConfig) smsBaseDao.get(SmsConfig.class, name);
        if(smsConfig != null){
            smsConfig.setFlag(Constants.ENABLE);
            smsConfig.setSwitchTime(new Date());
            smsBaseDao.update(smsConfig);
            msg = "SMSCONFIG_ENABLED";
        }
        return msg;
    }

    /**
     * disable sms config
     */
    public String disableConfig(String name){
        String msg = null;
        SmsConfig smsConfig = (SmsConfig) smsBaseDao.get(SmsConfig.class, name);
        if(smsConfig != null){
            smsConfig.setFlag(Constants.DISABLE);
            smsConfig.setSwitchTime(new Date());
            smsBaseDao.update(smsConfig);
            msg = "SMSCONFIG_DISABLED";
        }
        return msg;
    }
    public Page querySmsContents(String product, String code, Date dateStart, Date dateEnd, Integer index, Integer size, String accountId,String phonenumber){

        System.out.println("product = [" + product + "], code = [" + code + "], dateStart = [" + dateStart + "], dateEnd = [" + dateEnd + "], index = [" + index + "], size = [" + size + "], accountId = ["+accountId+"]");
        List<SmsMessages> list = null;
        DetachedCriteria dc = DetachedCriteria.forClass(ph.sinonet.vg.live.model.sms.SmsMessages.class);
        if(StringUtil.isNotEmpty(product))
            dc.add(Restrictions.eq("product",product));
        if(dateStart != null)
            dc.add(Restrictions.gt("createtime",dateStart));
        if(dateEnd != null)
            dc.add(Restrictions.lt("createtime",dateEnd));
        if(StringUtil.isNotEmpty(code))
            dc.add(Restrictions.eq("smsprovider",code));
        if(StringUtil.isNotEmpty(accountId)){
        	dc.add(Restrictions.eq("user_account", accountId));
        }
        if(StringUtil.isNotEmpty(phonenumber))
            dc.add(Restrictions.eq("phonenumber",phonenumber));
        dc.addOrder(Order.desc("createtime"));

        return PageQuery.queryForPagenation(smsBaseDao.getHibernateTemplate(), dc, index, size);
    }

    public List<SmsConfig> querySmsConfigByProject(String name, String type, String platformIds, Integer flag,Integer channel,String sortField,String orderRule){
        DetachedCriteria dc = DetachedCriteria.forClass(SmsConfig.class);
        if(StringUtil.isNotEmpty(name)){
            dc.add(Restrictions.eq("name", name));
        }
        if(StringUtil.isNotEmpty(type)){
            dc.add(Restrictions.eq("type", type));
        }
        if(flag != null){
            dc.add(Restrictions.eq("flag", flag));
        }
        if(StringUtil.isNotEmpty(platformIds)){
            dc.add(Restrictions.eq("platformIds", platformIds));
        }
        if(channel!=null){
        	dc.add(Restrictions.eq("channel",channel));
        }
        if(StringUtils.isNotEmpty(orderRule) && StringUtils.isNotEmpty(sortField)){
        	if("asc".equals(orderRule)){
        		dc.addOrder(Order.asc(sortField));
        	}
        	if("desc".equals(orderRule)){
        		dc.addOrder(Order.desc(sortField));
        	}
        }
        return  (List<SmsConfig>) smsBaseDao.findByCriteria(dc);
    }
	public void updateSmsConfigFlag(String name,Integer flag,Date swithTime,String areadyCounts,Integer flag1) {
		// TODO Auto-generated method stub
		SmsConfig smsConfig = (SmsConfig) smsBaseDao.get(SmsConfig.class, name);
		if(smsConfig!=null){
			if(flag!=null){
				smsConfig.setFlag(flag);
			}
			if(swithTime!=null){
				smsConfig.setSwitchTime(swithTime);
			}
			if(areadyCounts!=null){
				smsConfig.setAreadyCounts(areadyCounts);
			}
			if(flag1!=null){
				smsConfig.setFlag1(flag1);
			}
			 smsBaseDao.update(smsConfig);
		}
	}
	public SmsMessages querySmsContent(String phonenumber,String user_account){
		 System.out.println("phonenumber = [" + phonenumber + "], user_account = [" + user_account + "]");
	        List<SmsMessages> list = null;
	        DetachedCriteria dc = DetachedCriteria.forClass(ph.sinonet.vg.live.model.sms.SmsMessages.class);
	        if(phonenumber != null)
	            dc.add(Restrictions.eq("phonenumber",phonenumber));
	        if(user_account != null)
	            dc.add(Restrictions.eq("user_account",user_account));
	        dc.addOrder(Order.desc("createtime"));
	        
	        List<SmsMessages>  smsMessages=smsBaseDao.findByCriteria(dc);
	        return smsMessages.get(0);
	}
	public String cancelDisableSmsConfig(String name){
		String msg=null;
		SmsConfig smsConfig = (SmsConfig) smsBaseDao.get(SmsConfig.class, name);
		if(smsConfig!=null){
			smsConfig.setAreadyCounts("0");
			smsConfig.setFlag1(0);
			smsBaseDao.update(smsConfig);
			 msg = "SMSCANCELDISABLED";
		}
		return msg;
	}
	public void saveEntity(SmsConfigLog smsConfigLog){
		smsBaseDao.save(smsConfigLog);
	}
	public Page querySMSLogsContents(String operator, String platformIds, Date dateStart, Date dateEnd, Integer index, Integer size){
		System.out.println("platformIds = [" + platformIds + "], operator = [" + operator + "], dateStart = [" + dateStart + "], dateEnd = [" + dateEnd + "], index = [" + index + "], size = [" + size + "]");
		DetachedCriteria dc=DetachedCriteria.forClass(ph.sinonet.vg.live.model.sms.SmsConfigLog.class);
		if(StringUtil.isNotEmpty(operator)){
			dc.add(Restrictions.eq("operator", operator));
		}
		if(StringUtil.isNotEmpty(platformIds)){
			dc.add(Restrictions.eq("platformIds", platformIds));
		}
		if(dateStart!=null){
			dc.add(Restrictions.gt("operTime", dateStart));
		}
		if(dateEnd!=null){
			dc.add(Restrictions.lt("operTime", dateEnd));
		}
		dc.addOrder(Order.desc("operTime"));
		return PageQuery.queryForPagenation(smsBaseDao.getHibernateTemplate(), dc, index, size);
	}
	public String notParticipateAutoSwitch(String name){
		String msg=null;
		SmsConfig smsConfig=(SmsConfig)smsBaseDao.get(SmsConfig.class, name);
		if(smsConfig!=null){
			smsConfig.setFlag1(1);
			smsConfig.setFlag(1);
			smsBaseDao.update(smsConfig);
			msg="NOTPARTICIPATEAUTOSWITCHSUCCESS";
		}
		return msg;
	}

	public String selectchannelonesensitive() {
		// TODO Auto-generated method stub
		String msg=null;
		SmsAds smsAds=(SmsAds)smsBaseDao.get(SmsAds.class, 1);
		if(smsAds!=null){
			msg=smsAds.getAds();
		}
		return msg;
	}

	public String updateAds(String advertise) {
		// TODO Auto-generated method stub
		String msg=null;
		SmsAds smsAds = (SmsAds) smsBaseDao.get(SmsAds.class, 1);
		if(StringUtils.isNotEmpty(advertise)){
			smsAds.setAds(advertise);
		}
		 smsBaseDao.update(smsAds);
         msg = "SMSADS_UPDATED";
		return msg;
	}

	public NexmoResponse findByMessageId(String messageId) {
		// TODO Auto-generated method stub
		NexmoResponse nexmoResponse=(NexmoResponse)smsBaseDao.get(NexmoResponse.class, messageId);
		return nexmoResponse;
	}

	public void saveOrUpdate(NexmoResponse nexmoResponse) {
		// TODO Auto-generated method stub
		smsBaseDao.saveOrUpdate(nexmoResponse);
	}

	public void update(NexmoResponse nexmoResponse1) {
		// TODO Auto-generated method stub
		smsBaseDao.update(nexmoResponse1);
	}

	public void save(NexmoResponse nexmoResponse1) {
		// TODO Auto-generated method stub
		smsBaseDao.save(nexmoResponse1);
	}
	public List<SmsMessages> getSmsMessage(String product,String smsprovider, Date datestart, Date dateEnd) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria=DetachedCriteria.forClass(SmsMessages.class);
		criteria.add(Restrictions.eq("product", product));
		criteria.add(Restrictions.eq("smsprovider", smsprovider));
		criteria.add(Restrictions.gt("createtime", datestart));
		criteria.add(Restrictions.lt("createtime", dateEnd));
		criteria.addOrder(Order.asc("createtime"));
		return smsBaseDao.findByCriteria(criteria);
	}

	public Page checkNexmoMessage(String phoneNum, Date dateStart, Date dateEnd, Integer pageIndex, Integer size,String status1) {
		// TODO Auto-generated method stub
        List<NexmoResponse> list = null;
        DetachedCriteria dc = DetachedCriteria.forClass(ph.sinonet.vg.live.model.sms.NexmoResponse.class);
        if(StringUtil.isNotEmpty(phoneNum))
            dc.add(Restrictions.eq("msisdn",phoneNum));
        if(dateStart != null)
            dc.add(Restrictions.gt("message_timestamp",dateStart));
        if(dateEnd != null)
            dc.add(Restrictions.lt("message_timestamp",dateEnd));
        if(StringUtil.isNotEmpty(status1)){
        	dc.add(Restrictions.eq("status",status1));
        }
        dc.addOrder(Order.desc("message_timestamp"));

        return PageQuery.queryForPagenation(smsBaseDao.getHibernateTemplate(), dc, pageIndex, size);
	}

	public Integer queryNumberOfMessage(String accountId, String type, String projectname,  Date start,
			Date end) {
		// TODO Auto-generated method stub
		//accountId:代理方账号  type：哪个代理  projectname：哪个平台  channel通道几  start：开始时间  end结束时间
		 DetachedCriteria dc =DetachedCriteria.forClass(ph.sinonet.vg.live.model.sms.SmsMessages.class);
		 if(StringUtils.isNotEmpty(accountId)){
			 dc.add(Restrictions.eq("user_account", accountId));
		 }
		 if(StringUtils.isNotEmpty(type)){
			 dc.add(Restrictions.eq("smsprovider", type));
		 }
		 if(StringUtils.isNotEmpty(projectname)){
			 dc.add(Restrictions.eq("product", projectname));
		 }
		 if(start != null)
	            dc.add(Restrictions.gt("createtime",start));
	      if(end != null)
	          dc.add(Restrictions.lt("createtime",end));
	      return smsBaseDao.queryNumberOfMessage(dc);
	}

	public Integer querySumNumberOfMessage(String projectname, Integer channel, Date start, Date end) {
		// TODO Auto-generated method stub
		 DetachedCriteria dc =DetachedCriteria.forClass(ph.sinonet.vg.live.model.sms.SmsMessages.class);
		 if(StringUtils.isNotEmpty(projectname)){
			 dc.add(Restrictions.eq("product", projectname));
		 }
		 if(start != null)
	            dc.add(Restrictions.gt("createtime",start));
	      if(end != null)
	          dc.add(Restrictions.lt("createtime",end));
		 if(channel!=null){
			 if(channel==1){
				 dc.add(Restrictions.in("smsprovider","SPH_API","OPEN_API","BDT360","HCTCOM1","HCTCOM","LX"));
			 }else if(channel==2){
				 dc.add(Restrictions.in("smsprovider","SMS63","ACCESSYOU","SYNHEY","GJDX","BULK","NEXMO"));
			 }else if(channel==3){
				 dc.add(Restrictions.in("smsprovider","SMS63","ACCESSYOU","SYNHEY","GJDX","BULK","NEXMO"));
			 }else if(channel==4){
				 dc.add(Restrictions.in("smsprovider","SMS63","ACCESSYOU","SYNHEY","GJDX","BULK","NEXMO"));
			 }else if(channel==5){
				 dc.add(Restrictions.in("smsprovider","SMS63","ACCESSYOU","SYNHEY","GJDX","BULK","NEXMO"));
			 }else{
				 dc.add(Restrictions.in("smsprovider","SMS63","ACCESSYOU","SYNHEY","GJDX","BULK","NEXMO"));
			 }
		 }
		return smsBaseDao.querySumNumberOfMessage(dc);
	}

	public String addSmsAsiarouteConfig(String name, String serverIp, String username, String password, Integer channel,
			String platformId, String remark) {
		// TODO Auto-generated method stub
				String msg = null;
	        	ServerInFo severconfig = new ServerInFo();
	        	severconfig.setChannel(channel);
	        	severconfig.setPlatformId(platformId);
	        	severconfig.setRemark(remark);
	        	severconfig.setType("ASIAROUTE");
	        	severconfig.setServerIp(serverIp);
	        	severconfig.setUsername(username);
	        	severconfig.setName(name);
	        	severconfig.setPassword(password);
	            smsBaseDao.save(severconfig);
	            msg = "SMSCONFIG_ADDED";
	            return msg;
	}

	public List<ServerInFo> querySmsServerConfigByProject(String name, Integer channel, String projectname) {
		// TODO Auto-generated method stub
		 DetachedCriteria dc = DetachedCriteria.forClass(ServerInFo.class);
	        if(StringUtil.isNotEmpty(name)){
	            dc.add(Restrictions.eq("name", name));
	        }
	        if(channel!=null){
	            dc.add(Restrictions.eq("channel", channel));
	        }
	        
	        if(StringUtil.isNotEmpty(projectname)){
	            dc.add(Restrictions.eq("platformId", projectname));
	        }
	        return  (List<ServerInFo>) smsBaseDao.findByCriteria(dc);
	}

	public void deleteSmsServerConfig(Integer id) {
		// TODO Auto-generated method stub
		smsBaseDao.delete(ServerInFo.class, id);;
	}

	public String updateSmsServerConfig(Integer id, String name, String serverIp, String username, String password,
			Integer channel, String platformIds, String remark) {
		// TODO Auto-generated method stub
		ServerInFo serverInFo=(ServerInFo)smsBaseDao.get(ServerInFo.class, id);
	        if(StringUtil.isNotEmpty(name)){
	        	serverInFo.setName(name);
	        }
	        if(StringUtil.isNotEmpty(serverIp)){
	        	serverInFo.setServerIp(serverIp);
	        }
	        if(StringUtil.isNotEmpty(username)){
	        	serverInFo.setUsername(username);
	        }
	        if(StringUtil.isNotEmpty(password)){
	        	serverInFo.setPassword(password);
	        }
	        if(channel!=null){
	        	serverInFo.setChannel(channel);
	        }
	        if(StringUtil.isNotEmpty(remark)){
	        	serverInFo.setRemark(remark);
	        }
	        if(StringUtil.isNotEmpty(platformIds)){
	        	serverInFo.setPlatformId(platformIds);
	        }
	        smsBaseDao.update(serverInFo);
		return "更新成功";
	}
	public List<Integer> getUsableChanneList(String platformId) {
		// TODO Auto-generated method stub
		  DetachedCriteria dc = DetachedCriteria.forClass(SmsConfig.class);
		  dc.add(Restrictions.eq("platformIds", platformId));
		  dc.add(Restrictions.eq("flag", 0));
		  dc.add(Restrictions.eq("flag1", 0));
	      dc.setProjection(Projections.distinct(Projections.property("channel")));
	      return smsBaseDao.findByCriteria(dc);
	}

	
}
