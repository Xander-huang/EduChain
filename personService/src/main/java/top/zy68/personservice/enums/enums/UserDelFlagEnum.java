package top.zy68.personservice.enums.enums;

public enum UserDelFlagEnum {

	ENABLE("0", "启用"),

	REVOKE("1", "吊销"),

	;

	private String delFalg;

	private String message;

	UserDelFlagEnum(String delFalg, String message) {
		this.delFalg = delFalg;
		this.message = message;
	}

	public String getDelFalg() {
		return delFalg;
	}

	public String getMessage() {
		return message;
	}
}
