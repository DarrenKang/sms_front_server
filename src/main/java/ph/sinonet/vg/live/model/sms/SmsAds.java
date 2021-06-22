package ph.sinonet.vg.live.model.sms;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "smsAds")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SmsAds implements Serializable{
	@Id
    @Column(name = "channel", unique = true, nullable = false)
	    private Integer channel;
	 @javax.persistence.Column(name = "ads")
	    private String ads;
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public String getAds() {
		return ads;
	}
	public void setAds(String ads) {
		this.ads = ads;
	}
	@Override
	public String toString() {
		return "SmsAds [channel=" + channel + ", ads=" + ads + "]";
	}
	 
}
