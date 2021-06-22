package ph.sinonet.vg.live.model.bean.websocket;

import ph.sinonet.vg.live.model.sms.SmsConfig;
import ph.sinonet.vg.live.utils.StringUtil;

/**
 * Created by jay on 11/16/16.
 */
public class SmsReq {

    private String name;
    private String type;
    private String server;
    private Integer port;
    private String user;
    private String password;

    private String msg;
    private String phone;

    private String platform;

    private SmsReq() {
    }

    public static SmsReq parse(String platform, String name, String type, String server, Integer port, String user, String password) {
        SmsReq req = new SmsReq();
        req.name = name;
        req.type = type;
        req.server = server;
        req.port = port;
        req.user = user;
        req.password = password;

        return req;
    }

    public static SmsReq parse(String platform, SmsConfig config) {
        SmsReq req = new SmsReq();
        req.name = config.getName();
        req.type = config.getType();
        req.server = config.getServer();
        req.port = config.getPort() != null ? config.getPort() : 0;
        req.user = config.getUser();
        req.password = config.getPassword();
        req.platform = platform;

        return req;
    }

    public void setMsgInfo(String phone, String msg) {
        this.phone = phone;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getPort() {
        return port;
    }

    public String getServer() {
        return server;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getPlatform() {
        return platform;
    }

    @Override
    public String toString() {
        return "SmsReq{" +
                "msg='" + msg + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", server='" + server + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
