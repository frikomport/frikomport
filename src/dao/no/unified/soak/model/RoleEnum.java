package no.unified.soak.model;

public enum RoleEnum {
	ANONYMOUS("anonymous"), EMPLOYEE("employee"), EVENTRESPONSIBLE_ROLE("eventresponsible"), EDITOR_ROLE("editor"), ADMIN_ROLE(
			"admin"), READER_ROLE("reader");
	private String javaDBValue;

	RoleEnum(String javaDBValue) {
		this.javaDBValue = javaDBValue;
	}

	public String getJavaDBRolename() {
		return javaDBValue;
	}
	
	public RoleEnum getRoleEnumFromJavaDBRolename(String javaDBValue) {
		for (RoleEnum roleEnum : values()) {
			if (roleEnum.getJavaDBRolename().equals(javaDBValue)) {
				return roleEnum;
			}
		}
		return null;
	}
}
