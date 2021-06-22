package ph.sinonet.vg.live.model.bean.websocket.resp;

import ph.sinonet.vg.live.model.enums.ErrorCode;

/**
 * Created by jay on 7/15/16.
 */
public class AbstractRespMsg {


    private Integer status;
//    private String requestId;
    private String errorMsg;

    public AbstractRespMsg() {
    }

//    public AbstractRespMsg(ErrorCode error) {
//        this.status = error.getCode();
//        this.errorMsg = error.getText();
//    }
//
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatusAndMsg(ErrorCode errorCode) {
        this.setStatus(errorCode.getCode());
        this.setErrorMsg(errorCode.getText());
    }

    public String getStatusAndMsg(){
    	return "Code:"+this.getStatus()+",Message:"+this.getErrorMsg();
    }

}
