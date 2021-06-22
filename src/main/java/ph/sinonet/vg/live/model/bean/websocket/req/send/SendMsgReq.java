package ph.sinonet.vg.live.model.bean.websocket.req.send;

import ph.sinonet.vg.live.model.bean.websocket.req.AbstractReqMsg;

/**
 * Created by jay on 11/10/16.
 *
 *
 * sendMessage 短信接口

 使用POST提交字符串的方式进行业务数据传输。

 注：系统设定延时时间为3分钟，sms系统可以根据platform 设定该通道的延时时间，同时短信系统支持模板配置。

 检查URL：http： 请求参数格式以及参数次序：

 {
 "channel":"1"，
 "tel": "19123321312",
 "platformId":"",
 "message":"",
 "delay":"1"
 }
 请求参数说明：

 channel 通道号,
 tel  电话号码，
 platformId 平台ID，
 message  短信内容，
 delay  是否启用延时，
 注：delay 为1 启用系统设定延时时间，
 delay 为 0  关闭系统延时，实时发送
 */
public class SendMsgReq extends AbstractReqMsg {

    public static final String NAME = "sendMessage";

    private String channel;
    private String tel;
    private String platformId;
    private String message;
    private Integer delay;

    public String getChannel() {
        return channel;
    }

    public Integer getDelay() {
        return delay;
    }

    public String getMessage() {
        return message;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getTel() {
        return tel;
    }
    
    public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	@Override
    public String toString() {
        return "SendMsgReq{" +
                "channel='" + channel + '\'' +
                ", tel='" + tel + '\'' +
                ", platformId='" + platformId + '\'' +
                ", message='" + message + '\'' +
                ", delay=" + delay +
                '}';
    }
}
