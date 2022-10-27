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

	public enum ProgressStatus {
		TODO("Todo"), IN_PROGRESS("In progress"), PENDING("Pending"), REVIEW("Review"), DONE("Done");

		private String value;

		private ProgressStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	public enum ProjectStatus {
		NOT_YET_START("Not yet start"), IN_PROGRESS("In progress"), DONE("Done"), PAYMENTING("Paymenting"),
		IS_PAID("Paid"), CANCEL("Cancel");

		private String value;

		private ProjectStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

}
