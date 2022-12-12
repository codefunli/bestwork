package com.nineplus.bestwork.utils;

public class CommonConstants {
	public class Authentication {
		public static final String ACCESS_TOKEN = "access_token";
		public static final String REFRESH_TOKEN = "refresh_token";
		public static final String PREFIX_TOKEN = "prefix";
		public static final String ROLES = "roles";
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
	}

	public class RoleName {
		public static final String SYS_ADMIN = "sysadmin";
		public static final String SYS_COMPANY_ADMIN = "sys-companyadmin";
		public static final String CMPNY_ADMIN = "companyadmin";
		public static final String CPMY_USER = "companyuser";
	}

	public class ApiStatus {
		public static final String STATUS_OK = "OK";
		public static final String STATUS_ERROR = "ERROR";
		public static final String STATUS_SUCCESS = "success";

	}

	public class Image {
		public static final String[] IMAGE_EXTENSION = { "png", "jpg", "jpeg", "bmp", "JPEG", "PNG", "JPG", "BMP" };
	}

	public class MediaType {
		/** The Constant MEDIA_TYPE_STREAM. */
		public static final String MEDIA_TYPE_STREAM = "application/octet-stream";

		public static final String MEDIA_TYPE_PDF = "application/pdf";

		public static final String MEDIA_TYPE_ZIP = "application/zip";

		/** The Constant CONTENT_DISPOSITION. */
		public static final String CONTENT_DISPOSITION = "attachment;filename=";
	}

	public class Character {
		public static final String USER = "User";
		public static final String USER_ID = "User Id";
		public static final String ORG_ID = "Org Id";
		public static final String ORG_NAME = "Org Name";
		public static final String USER_MAIL = "User Mail";
		public static final String USER_NAME = "User Name";
		public static final String CMPNY_NAME = "Company Name";
		public static final String PROJECT = "Project";
		public static final String TYPE_POST_INVOICE = "invoice";
		public static final String TYPE_POST_PACKAGE = "package";
		public static final String TYPE_POST_IMAGE_BEFORE = "imageBefore";

		public static final String COMPANY = "COMPANY";
		public static final String USERNAME_OR_EMAIL = "USER or EMAIL";
		public static final String PASSWORD = "Password";
		public static final String Device = "Device";
		public static final String UNDERSCORE = "_";
		public static final String COMMA = ",";
		public static final String KC_PASSWORD_DEFAULT = "123456";
		public static final String ADMIN = "Admin";

		public static final String CONSTRUCTION_NAME = "Construction Name";
		public static final String AIR_WAY_BILL = "Air Way Bill code";

		public static final String EDITOR = "editor";
		public static final String VIEWER = "viewer";
		public static final int STRING_LEN = 250;

		public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	}

	public class MessageCode {
		public static final String S1X0001 = "s1X0001";
		public static final String S1X0002 = "s1X0002";
		public static final String S1X0003 = "s1X0003";
		public static final String S1X0004 = "s1X0004";
		public static final String S1X0005 = "s1X0005";
		public static final String S1X0006 = "s1X0006";
		public static final String S1X0007 = "s1X0007";
		public static final String S1X0008 = "s1X0008";
		public static final String S1X0009 = "s1X0009";
		public static final String S1X0010 = "s1X0010";
		public static final String S1X0011 = "s1X0011";
		public static final String S1X0012 = "s1X0012";
		public static final String S1X0013 = "s1X0013";
		public static final String S1X0014 = "s1X0014";
		public static final String S1X0015 = "s1X0015";
		public static final String S1X0016 = "s1X0016";
		public static final String E1X0001 = "e1X0001";
		public static final String E1X0002 = "e1X0002";
		public static final String E1X0003 = "e1X0003";
		public static final String E1X0004 = "e1X0004";
		public static final String E1X0005 = "e1X0005";
		public static final String E1X0006 = "e1X0006";
		public static final String E1X0007 = "e1X0007";
		public static final String E1X0008 = "e1X0008";
		public static final String E1X0009 = "e1X0009";
		public static final String E1X0010 = "e1X0010";
		public static final String E1X0011 = "e1X0011";
		public static final String E1X0012 = "e1X0012";
		public static final String E1X0013 = "e1X0013";
		public static final String E1X0015 = "e1X0015";
		public static final String E1X0014 = "e1X0014";
		public static final String E1X0016 = "e1X0016";
		public static final String E1X0017 = "e1X0017";
		public static final String E1X0018 = "e1X0018";
		public static final String E1X0019 = "e1X0019";
		public static final String E1X0020 = "e1X0020";
		public static final String E1X0021 = "e1X0021";
		public static final String E1X0022 = "e1X0022";
		public static final String E1X0023 = "e1X0023";
		public static final String E1X0024 = "e1X0024";
		public static final String E1X0025 = "e1X0025";
		public static final String E1X0026 = "e1X0026";
		public static final String E1X0027 = "e1X0027";
		public static final String E1X0028 = "e1X0028";
		public static final String E1X0029 = "e1X0029";
		public static final String E1X0030 = "e1X0030";
		public static final String E1X0031 = "e1X0031";
		public static final String E1X0032 = "e1X0032";
		public static final String SYS_E1X001 = "syse1x001";
		public static final String E1X0033 = "e1X0033";
		public static final String E1X0034 = "e1X0034";
		public static final String E1X0035 = "e1X0035";
		public static final String E1X0036 = "e1X0036";
		public static final String E1X0037 = "e1X0037";
		public static final String E1X0038 = "e1X0038";
		public static final String E1X0039 = "e1X0039";
		public static final String E1X0040 = "e1X0040";
		public static final String E1X0041 = "e1X0041";
		public static final String E1X0042 = "e1X0042";
		public static final String E1X0043 = "e1X0043";
		public static final String E1X0044 = "e1X0044";

		public static final String I1X0001 = "i1X0001";
		public static final String I1X0002 = "i1X0002";
		public static final String I1X0003 = "i1X0003";
		public static final String CPN0001 = "CPN0001";
		public static final String CPN0002 = "CPN0002";
		public static final String CPN0003 = "CPN0003";
		public static final String SU0001 = "su0001";
		public static final String SU0002 = "su0002";
		public static final String SU0003 = "su0003";
		public static final String SU0004 = "su0004";
		public static final String SU0005 = "su0005";
		public static final String sUS0001 = "sUS0001";
		public static final String sCPN0004 = "sCPN0004";

		public static final String CPN0004 = "CPN0004";
		public static final String CPN0005 = "CPN0005";
		public static final String CPN0006 = "CPN0006";
		public static final String CPN0007 = "CPN0007";
		public static final String EMP0001 = "EMP0001";
		public static final String US0002 = "EMP0002";
		public static final String EXS0003 = "EXS0003";
		public static final String EXS0004 = "EXS0004";
		public static final String EXS0005 = "EXS0005";
		public static final String EXS0006 = "EXS0006";
		public static final String EXS0007 = "EXS0007";
		public static final String EXS0008 = "EXS0008";
		public static final String EXS0009 = "EXS0009";
		public static final String FILE0001 = "FILE0001";
		public static final String FILE0002 = "FILE0002";
		public static final String FILE0003 = "FILE0003";

		public static final String RLS0001 = "RLS0001";
		public static final String RLS0002 = "RLS0002";
		public static final String RLF0001 = "RLF0001";
		public static final String RLF0002 = "RLF0001";
		public static final String RLS0003 = "RLS0003";
		public static final String PMS0001 = "PMS0001";
		public static final String eR0002 = "eR0002";

		public static final String SPS0001 = "SPS0001";
		public static final String RLS0004 = "RLS0004";
		public static final String sU0006 = "sU0006";

		public static final String SCU0001 = "scu0001";
		public static final String ECU0001 = "ecu0001";
		public static final String SCU0002 = "scu0002";
		public static final String ECU0002 = "ecu0002";
		public static final String SCU0003 = "scu0003";
		public static final String ECU0003 = "ecu0003";
		public static final String SCU0004 = "scu0004";
		public static final String ECU0004 = "ecu0004";
		public static final String ECU0005 = "ecu0005";
		public static final String ECU0006 = "ecu0006";
		public static final String ECU0007 = "ecu0007";

		public static final String SPOST0001 = "sPost0001";
		public static final String EPOST0001 = "ePost0001";
		public static final String SPOST0002 = "sPost0002";
		public static final String EPOST0002 = "ePost0002";
		public static final String SPOST0003 = "sPost0003";
		public static final String EPOST0003 = "ePost0003";

		public static final String sPu00001 = "sPu0001";
		public static final String sPu00002 = "sPu0002";
		public static final String sPu00003 = "sPu0003";
		public static final String ePu0003 = "ePu0003";
		public static final String sPu0004 = "sPu0004";
		public static final String sPu0005 = "sPu0005";
		public static final String sPu0006 = "sPu0006";
		public static final String ePu0001 = "ePu0001";
		public static final String ePu0002 = "ePu0002";
		public static final String S1X0017 = "s1X0017";

		public static final String SNU0001 = "snu0001";
		public static final String ENU0001 = "enu0001";
		public static final String SNU0002 = "snu0002";
		public static final String ENU0002 = "enu0002";
		public static final String SNU0003 = "snu0003";
		public static final String ENU0003 = "enu0003";
		public static final String ENU0004 = "enu0004";
		public static final String SNU0005 = "snu0005";
		public static final String ENU0005 = "enu0005";
		public static final String SNU0006 = "snu0006";
		public static final String SNU0007 = "snu0007";
		public static final String ENU0007 = "enu0007";
		public static final String SNU0008 = "snu0008";

		public static final String TNU0004 = "tnu0004";
		public static final String CNU0004 = "cnu0004";
		public static final String CNU0005 = "cnu0005";
		public static final String TNU0006 = "tnu0006";
		public static final String CNU0006 = "cnu0006";
		public static final String TNU0007 = "tnu0007";
		public static final String CNU0007 = "cnu0007";
		public static final String TNU0008 = "tnu0008";
		public static final String CNU0008 = "cnu0008";
		public static final String TNU0009 = "tnu0009";
		public static final String CNU0009 = "cnu0009";
		public static final String TNU0010 = "tnu0010";
		public static final String CNU0010 = "cnu0010";

		public static final String eF0001 = "eF0001";
		public static final String eF0002 = "eF0002";
		public static final String eF0003 = "eF0003";
		public static final String eF0004 = "eF0004";
		public static final String sF0001 = "sF0001";
		public static final String sF0002 = "sF0002";
		public static final String sF0003 = "sF0003";

		public static final String sA0001 = "sA0001";
		public static final String sA0002 = "sA0002";
		public static final String sA0003 = "sA0003";
		public static final String sA0004 = "sA0004";
		public static final String sA0005 = "sA0005";
		public static final String sA0006 = "sA0006";
		public static final String sA0007 = "sA0007";
		public static final String eA0001 = "eA0001";
		public static final String eA0002 = "eA0002";
		public static final String eA0003 = "eA0003";
		public static final String eA0004 = "eA0004";
		public static final String eA0005 = "eA0005";
		public static final String eA0006 = "eA0006";

		// for invoice post
		public static final String sI0001 = "sI0001";
		public static final String sI0002 = "sI0002";
		public static final String eI0003 = "eI0003";
		public static final String eI0004 = "eI0004";
		public static final String sI0003 = "sI0003";
		public static final String eI0001 = "eI0001";
		public static final String sI0004 = "sI0004";

		public static final String SCS0001 = "scs0001";

		public static final String SCN001 = "SCN001";
		public static final String ECS0001 = "ecs0001";
		public static final String SCS0002 = "scs0002";
		public static final String ECS0002 = "ecs0002";
		public static final String ECS0003 = "ecs0003";
		public static final String SCS0003 = "scs0003";
		public static final String SCS0004 = "scs0004";
		public static final String ECS0004 = "ecs0004";
		public static final String SCS0005 = "scs0005";
		public static final String ECS0005 = "ecs0005";
		public static final String ECS0006 = "ecs0006";
		public static final String ECS0007 = "ecs0007";
		public static final String SCS0008 = "scs0008";
		public static final String ECS0008 = "ecs0008";
		public static final String SCS0009 = "scs0009";

		// For package AWB message
		public static final String sP0001 = "sP0001";
		public static final String sP0002 = "sP0002";
		public static final String sP0003 = "sP0003";
		public static final String eP0001 = "eP0001";
		public static final String eP0003 = "eP0003";
		public static final String eP0004 = "eP0004";

		public static final String EXM001 = "EXM001";
		public static final String SCM001 = "SCM001";

		// For evidence before
		public static final String eEB0001 = "eEB0001";
		public static final String eEB0002 = "eEB0002";
		public static final String eEB0003 = "eEB0003";
		public static final String sEB0001 = "sEB0001";
		public static final String sEB0002 = "sEB0002";
		public static final String sEB0003 = "sEB0003";

		// For evidence after
		public static final String eEA0001 = "eEA0001";
		public static final String eEA0002 = "eEA0002";
		public static final String eEA0003 = "eEA0003";
		public static final String sEA0001 = "sEA0001";
		public static final String sEA0002 = "sEA0002";
		public static final String sEA0003 = "sEA0003";

		// For nation
		public static final String ENA0001 = "ena0001";
		public static final String SNA0002 = "sna0002";
		public static final String ENA0002 = "ena0002";

		public static final String TL0001 = "tl0001";
	}

	public class Url {
		public static final String URL0001 = "url0001";
		public static final String URL0002 = "url0002";
	}

	public class SpringMail {

		public static final String M1X0002 = "m1x0002";
		public static final String M1X0003 = "m1x0003";
		public static final String M1H0004 = "m1h0004";
		public static final String M1P0005 = "m1p0005";
		public static final String M1U0006 = "m1u0006";
		public static final String M1W0007 = "m1w0007";
	}

	public class ApiPath {
		public static final String BASE_PATH = "/api/v1";
	}
}
