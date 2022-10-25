package com.nineplus.bestwork.utils;

public class Enums {
	public enum TRole {
		SYS_ADMIN("sysadmin"), COMPANY_ADMIN("companyadmin"), COMPANY_USER("user");

		private String value;

		private TRole(String value) {
			this.value = value;
		}

		// getter & setter
		public String getValue() {
			return this.value;
		}

	}
	
	public enum RoleProject {
		ALL("all"), VIEW("view"), EDIT("edit");

		private String value;

		private RoleProject(String value) {
			this.value = value;
		}

		// getter & setter
		public String getValue() {
			return this.value;
		}

	}
}
