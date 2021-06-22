package ph.sinonet.vg.live.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Announcement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "apps")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Apps implements java.io.Serializable {


	private String url;
	private Integer channel;
	
	public Apps() {
	}


	// Property accessors
	@Id
	@Column(name = "url", unique = true)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "channel", nullable = false)
	public Integer getChannel() {
		return channel;
	}


	public void setChannel(Integer channel) {
		this.channel = channel;
	}


	@Override
	public String toString() {
		return "Apps [url=" + url + ", channel=" + channel + "]";
	}
	
	
	
	
}