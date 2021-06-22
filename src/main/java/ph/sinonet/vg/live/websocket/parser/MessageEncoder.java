package ph.sinonet.vg.live.websocket.parser;

import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by jay on 7/14/16.
 */
public class MessageEncoder implements Encoder.Text<ResponseMessage> {


    @Override
    public String encode(ResponseMessage responseMessage) throws EncodeException {
        return responseMessage.toJson();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
