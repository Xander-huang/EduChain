package top.zy68.personservice.enums.enums;

public enum LoginTypeEnum {

    PSSWORD(0, "账号密码登录"),

    SMS(1, "短信验证码登录"),

    ;

    private Integer type;

    private String message;

    LoginTypeEnum(Integer type, String message) {
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
