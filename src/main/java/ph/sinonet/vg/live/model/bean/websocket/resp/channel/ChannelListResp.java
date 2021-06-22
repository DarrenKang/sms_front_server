package ph.sinonet.vg.live.model.bean.websocket.resp.channel;

import ph.sinonet.vg.live.model.bean.websocket.resp.AbstractRespMsg;

import java.util.List;

/**
 * Created by jay on 11/17/16.
 */
public class ChannelListResp extends AbstractRespMsg {

    private String queryId;
    private List<Integer> channels;

    public List<Integer> getChannels() {
        return channels;
    }

    public void setChannels(List<Integer> channels) {
        this.channels = channels;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @Override
    public String toString() {
        return "ChannelListResp{" +
                "channels=" + channels +
                ", queryId='" + queryId + '\'' +
                '}';
    }
}
