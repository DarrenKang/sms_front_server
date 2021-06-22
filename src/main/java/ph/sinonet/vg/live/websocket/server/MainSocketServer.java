package ph.sinonet.vg.live.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.service.impl.WebSocketServiceProvider;
import ph.sinonet.vg.live.service.interfaces.SmsService;

import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

@Component
public class MainSocketServer extends Endpoint {

	@Autowired
	private WebSocketServiceProvider provider;

	@Autowired
	private SmsService smsService;
	//初始在线人数
    private static int online_num = 0;
    //线程安全的socket集合
    private static CopyOnWriteArraySet<MainSocketServer> webSocketSet = new CopyOnWriteArraySet<MainSocketServer>();

	private String ipaddr;
	private Session session;
	private final static Logger LOGGER = LoggerFactory.getLogger(MainSocketServer.class);

	@Override
	public void onClose(Session session, CloseReason closeReason) {
		if (session != null && session.getId() != null)
//			LOGGER.info("Sessoin close " + session.getId());
			//LOGGER.info("连接关闭");
			LOGGER.info("连接从"+ipaddr+"关闭");
		webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("有链接关闭,当前连接数为:"+getOnline_num());

			super.onClose(session, closeReason);
	}

	@Override
	public void onError(Session session, Throwable thr) {
		super.onError(session, thr);
	}

	@Override
	public void onOpen(Session session, EndpointConfig endpointConfig) {
//		LOGGER.info("sessionid :" + session.getId());
		//LOGGER.info("连接打开");
		this.session = session;
		ipaddr=(String) session.getUserProperties().get("ClientIP");//将客户端IP地址从WebSocket的session中取出
		LOGGER.info("连接从"+ipaddr+"打开");
		webSocketSet.add(this);
        addOnlineCount();
        System.out.println("有链接加入，当前连接数为:"+getOnline_num());

		session.addMessageHandler(new CustomMessageHandler(session));
	}
	public synchronized int getOnline_num(){
        return MainSocketServer.online_num;
    }
    public synchronized int subOnlineCount(){
        return MainSocketServer.online_num--;
    }
    public synchronized int addOnlineCount(){
        return MainSocketServer.online_num++;
    }


	private class CustomMessageHandler implements javax.websocket.MessageHandler.Whole<RequestMessage> {

		final Session session;

		public CustomMessageHandler(Session session) {
			this.session = session;
		}

		public Session getSession() {
			return session;
		}

		public void sendMsg(ResponseMessage msg) {
			LOGGER.info("send response msg: " + msg);
			LOGGER.info("send response msg: " + msg.toJson());
			if (this.getSession().isOpen())
				this.getSession().getAsyncRemote().sendObject(msg);
			else {
				//connect();
				LOGGER.info("server is down, please try to reconnect... ");
				LOGGER.info("failed to send message " + msg);
			}
		}

		@Override
		public void onMessage(RequestMessage req) {
			try {
				LOGGER.info("现在处于onMessage方法里面，新的请求过来了received request onMessage: " + req);
				if (req != null) {
//					ResponseMessage resp = smsService.processWSReq(req);
					ResponseMessage resp = provider.processReq(req);
					sendMsg(resp);
				} else {
					LOGGER.info("received message is null, need to respond invalid params...");
//					session.getAsyncRemote().sendObject(createInvalidResponse(ErrorCode.INVALID_PARAMETER));
				}
			} catch (Exception e) {
				LOGGER.info("Internal error occurred...");
//				session.getAsyncRemote().sendObject(createInvalidResponse(ErrorCode.INTERNAL_ERROR));
				e.printStackTrace();
			}

		}

//		private ResponseMessage createInvalidResponse(ErrorCode code) {
//			return new ResponseMessage(new AbstractRespMsg(code));
//		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str="{\"msg\":\"success\",\"code\":200}";
		JSONObject jsonObj  = JSONObject.fromObject(str);
		System.out.println("字符串三：" + str);  
		Integer i =(Integer)jsonObj.get("code");
	        System.out.println(i); 
	}
}
