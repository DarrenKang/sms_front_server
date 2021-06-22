package ph.sinonet.vg.live.model.sms;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jay on 11/15/16.
 */
@Entity
@Table(name = "sms_serverUser")
@DynamicInsert
@DynamicUpdate
public class ServerInFo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "serverIp")
    private String serverIp;
    @Column(name = "platformId")
    private String platformId;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "remark")
    private String remark;
    @Column(name = "type")
    private String type;
    @Column(name = "channel")
    private Integer channel;
    @Column(name = "name")
    private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "ServerInFo [id=" + id + ", serverIp=" + serverIp + ", platformId=" + platformId + ", username="
				+ username + ", password=" + password + ", remark=" + remark + ", type=" + type + ", channel=" + channel
				+ ", name=" + name + "]";
	}
    
}
