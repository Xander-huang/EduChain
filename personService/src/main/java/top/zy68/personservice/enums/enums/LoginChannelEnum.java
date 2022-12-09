package top.zy68.personservice.enums.enums;

public enum LoginChannelEnum {

    PC(0, "PC"),    //

    Android(1, "安卓"),//

    IOS(2, "IOS"),

    ;

    private Integer type;

    private String message;

    LoginChannelEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
