package ph.sinonet.vg.live.model.bean.websocket.req.platform;

import ph.sinonet.vg.live.model.bean.websocket.req.AbstractReqMsg;
import ph.sinonet.vg.live.validator.annotation.JsonRequired;

/**
 * Created by kierpagdato on 8/3/16.
 */
public class AbstractPlatformReqMsg extends AbstractReqMsg {

    @JsonRequired
    protected String platformId;
    @JsonRequired
    protected String code;
    @JsonRequired
    protected String name;
//    @JsonRequired
    protected String description;

    public String getPlatformId() {
        return platformId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
