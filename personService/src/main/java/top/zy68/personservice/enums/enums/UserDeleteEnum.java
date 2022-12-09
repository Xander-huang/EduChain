package top.zy68.personservice.enums.enums;

public enum UserDeleteEnum {

	NORMAL("0", "正常"),

	DELETE("1", "删除"),

	;

	private String isDelete;

	private String message;

	UserDeleteEnum(String isDelete, String message) {
		this.isDelete = isDelete;
		this.message = message;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public String getMessage() {
		return message;
	}
}
