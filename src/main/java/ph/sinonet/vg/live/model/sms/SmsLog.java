package ph.sinonet.vg.live.model.sms;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jay on 11/15/16.
 */
@Entity
@Table(name = "sms_log")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SmsLog implements Serializable {

    @Id
    @GeneratedValue
    @javax.persistence.Column(name = "id")
    private Integer id;
    @javax.persistence.Column(name = "platform")
    private String platform;
    @javax.persistence.Column(name = "provider")
    private String provider;
    @javax.persistence.Column(name = "sms_name")
    private String smsName;
    @javax.persistence.Column(name = "phone_number")
    private String phoneNumber;
    @javax.persistence.Column(name = "content")
    private String content;
    @javax.persistence.Column(name = "api_response")
    private String apiResponse;
    @javax.persistence.Column(name = "create_time")
    private Date createTime;


    public String getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSmsName() {
        return smsName;
    }

    public void setSmsName(String smsName) {
        this.smsName = smsName;
    }
}
