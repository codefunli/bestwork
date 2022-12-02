package com.nineplus.bestwork.utils;

public class Enums {
	public enum TRole {
		SYS_ADMIN("sysadmin"), COMPANY_ADMIN("companyadmin"), SUB_COMPANY_ADMIN("sub-companyadmin"),
		COMPANY_USER("companyuser"), INVESTOR("investor"), SUPPLIER("supplier"), CONTRACTOR("contractor");

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
		NOT_YET_START("Not yet start"), IN_PROGRESS("In progress"), PENDING("Pending"), REVIEW("Review"), DONE("Done");

		private String value;

		private ProgressStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	public enum ProjectStatus {
		NOT_YET_START("Not yet start"), IN_PROGRESS("In progress"), DONE("Done"), NOT_YET_PAID("Not yet paid"),
		IS_PAID("Paid"), CANCEL("Cancel");

		private String value;

		private ProjectStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	public enum ConstructionStatus {
		NOT_YET_START("Not yet start"), IN_PROGRESS("In progress"), PENDING("Pending"), REVIEW("Review"), DONE("Done");

		private String value;

		private ConstructionStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	public enum AirWayBillStatus {
		NOT_YET_CUSTOMS_CLEARANCES(0, "Not yet customs clearance"),
		IN_CUSTOMS_CLEARANCES_PROGRESS(1, "In Customs Clearance Progress"), DONE(2, "Done");

		private String value;

		private final int status;

		private AirWayBillStatus(int status, String value) {
			this.status = status;
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		public int getStatus() {
			return this.status;
		}

		public int getStatusAsInt(String value) {
			return status;
		}

		public static String convertIntToStatus(int status) {
			for (AirWayBillStatus status1 : AirWayBillStatus.values()) {
				if (status1.getStatusAsInt(status1.getValue()) == status) {
					return status1.getValue();
				}
			}
			return null;
		}
	}

	public enum FolderType {
		INVOICE, PACKAGE, EVIDENCE_BEFORE, EVIDENCE_AFTER, CONSTRUCTION, PROGRESS, DEFAULT
	}

	public enum Nation {
		CHINA("China"), VIETNAM("Viet Nam"), LAOS("Laos"), THAILAND("Thailand");

		private String value;

		private Nation(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum NotifyStatus {
		UNREAD("Unread"), READ("Read");

		private String value;

		private NotifyStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}
}
