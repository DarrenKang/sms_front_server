package ph.sinonet.vg.live.model.enums;

/**
 * Created by jay on 7/22/16.
 * TODO : no description for other error code
 */
public enum ErrorCode {

    SUCCESS(200, "操作成功"),

    EMPTY_MESSAGE(401, "Empty message"),
    MSG_LENGTH_EXCEED(402, "Message exceeds length"),
    FAIL_SEND(413, "Fail on sending message"),
    EMPTY_CELLPHONE(420,"Fail on sending message,Because the phone number is empty"),
    FORMATERROE_CELLPHONE(420,"Fail on sending message,Because The phone number is not in the right format"),
    CONTENT_ERROR(420,"Channel 1 contains advertisements"),
    WAITINGRESPONSE(420,"等待响应数据，接收响应数据之后修改状态")
    ;


    ErrorCode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    private int code;
    private String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
