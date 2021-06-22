package ph.sinonet.vg.live.websocket.parser;

import com.google.gson.JsonParseException;
import org.apache.log4j.Logger;
import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Created by jay on 7/14/16.
 */
public class MessageDecoder implements Decoder.Text<RequestMessage> {

	private static final Logger log = Logger.getLogger(MessageDecoder.class);
	@Override
	public RequestMessage decode(String s) throws DecodeException {
		try {
			log.info("convert:" + s);
			return RequestMessage.convert(s);
		} catch (JsonParseException e) {
			// e.printStackTrace();
			log.info("Invalid parameters: " + e.getLocalizedMessage());
		}
		return null;
	}

	@Override
	public boolean willDecode(String s) {
		// TODO : check how to do this
		return true;
	}

	@Override
	public void init(EndpointConfig endpointConfig) {
	}

	@Override
	public void destroy() {
	}

}
