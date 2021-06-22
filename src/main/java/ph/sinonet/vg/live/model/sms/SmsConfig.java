package ph.sinonet.vg.live.model.sms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jay on 11/10/16.
 */
@Entity
@Table(name = "sms_config")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SmsConfig implements Serializable {


    @Id
    @javax.persistence.Column(name = "name")
    private String name;
    @javax.persistence.Column(name = "type")
    private String type;
    @javax.persistence.Column(name = "channel")
    private Integer channel;
    @javax.persistence.Column(name = "flag")
    private Integer flag;
    @javax.persistence.Column(name = "server")
    private String server;
    @javax.persistence.Column(name = "port")
    private Integer port;
    @javax.persistence.Column(name = "user")
    private String user;
    @javax.persistence.Column(name = "password")
    private String password;
    @javax.persistence.Column(name = "platformIds")
    private String platformIds;
    @javax.persistence.Column(name = "remark")
    private String remark;
    @javax.persistence.Column(name = "id")
    private Integer id;
    @javax.persistence.Column(name = "priority")
    private String priority;
    @javax.persistence.Column(name = "switch_time")
    private Date switchTime;
    @javax.persistence.Column(name = "total_switching_times")
    private String totalCounts;
    @javax.persistence.Column(name = "aready_switching_times")
    private String areadyCounts;
    @javax.persistence.Column(name = "flag1")
    private Integer flag1;
    public SmsConfig() {
    }

    public SmsConfig(String type, String user, String server, String remark, Integer port, String platformIds, String password, String name, Integer flag, Integer channel) {
        this.type = type;
        this.user = user;
        this.server = server;
        this.remark = remark;
        this.port = port;
        this.platformIds = platformIds;
        this.password = password;
        this.name = name;
        this.flag = flag;
        this.channel = channel;
    }

    public SmsConfig(String type, String user, String server, String remark, Integer port, String platformIds, 
    		String password, String name, Integer flag, Integer channel,Date switchTime,String priority,String totalCounts,String areadyCounts, Integer flag1) {
        this.type = type;
        this.user = user;
        this.server = server;
        this.remark = remark;
        this.port = port;
        this.platformIds = platformIds;
        this.password = password;
        this.name = name;
        this.flag = flag;
        this.channel = channel;
        this.switchTime=switchTime;
        this.priority=priority;
        this.totalCounts=totalCounts;
        this.areadyCounts=areadyCounts;
        this.flag1=flag1;
    }
	
	public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlatformIds() {
        return platformIds;
    }

    public void setPlatformIds(String platformIds) {
        this.platformIds = platformIds;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
  

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	
	public Date getSwitchTime() {
		return switchTime;
	}

	public void setSwitchTime(Date switchTime) {
		this.switchTime = switchTime;
	}
	
	public String getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(String totalCounts) {
		this.totalCounts = totalCounts;
	}

	public String getAreadyCounts() {
		return areadyCounts;
	}

	public void setAreadyCounts(String areadyCounts) {
		this.areadyCounts = areadyCounts;
	}
	
	public Integer getFlag1() {
		return flag1;
	}

	public void setFlag1(Integer flag1) {
		this.flag1 = flag1;
	}

	@Override
    public String toString() {
        return "SmsConfig{" +
                "channel=" + channel +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", flag=" + flag +
                ", server='" + server + '\'' +
                ", port='" + port + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", platformIds='" + platformIds + '\'' +
                ", remark='" + remark + '\'' +
                ", priority='" + priority + '\'' +
                ", switchTime='" + switchTime + '\'' +
                ", totalCounts='" + totalCounts + '\'' +
                ", areadyCounts='" + areadyCounts + '\'' +
                ", flag1='" + flag1 + '\'' +
                '}';
    }
}
