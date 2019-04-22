package com.chinaairlines.component;

public class Variable {
	/*App variable*/
	public static final String APP_Platform = "A";
	public static final String APP_CODE = "CA01";
	public static final String APP_CODE_TEST = "CA91";

	public final static boolean isDebug = true;

	public static final int LICENSE_FILE_BUNDLE = 0;//R.raw.streams;

	public static final String STORAGE_DATA_ROOT = "emp";


	//OIDC logout
	public final static String OIDCSignOutURL = "https://iam.china-airlines.com/pkmslogout";


	/** Database */	
	//log
	public static final String TBName_Log = "log";
	public static final String L_ERR_VERSION = "err_version";
	public static final String L_ERR_FUNC = "err_func";
	public static final String L_ERR_DESC = "err_desc";
	public static final String L_ERR_TIME = "err_time";
	public static final String L_LAST_UPDATED_DATE = "last_updated_date";
	//user_info
	public static final String TBName_UserInfo = "user_info";
	public static final String U_TESTUSER = "testuser";
	public static final String U_SHAREUSER = "shareuser";
	public static final String U_USER_NAME_TW = "user_name_tw";
	public static final String U_USER_NAME_US = "user_name_us";
	public static final String U_USER_ID = "user_id";
	public static final String U_USER_UNIT_TW = "user_unit_tw";
	public static final String U_USER_UNIT_US = "user_unit_us";
	public static final String U_USER_LEAD_UT = "user_lead_ut";
	public static final String U_USER_UNIT_CD = "user_unit_cd";
	public static final String U_USER_TRD_UNT = "user_trd_unt";
	public static final String U_USER_AUTH_LVF = "user_auth_lvf";
	public static final String U_USER_AUTH_LVS = "user_auth_lvs";
	public static final String U_USER_AUTH_SYSCD = "user_auth_syscd";
	public static final String U_USER_FIRSTNAME_EN = "user_firstname_en";
	public static final String U_USER_LASTNAME_EN = "user_lastname_en";
	public static final String U_USER_FIRSTNAME_TW = "user_firstname_tw";
	public static final String U_USER_LASTNAME_TW = "user_lastname_tw";
	public static final String U_USER_PHONE_NUMBER_1 = "user_phone_number_1";
	public static final String U_USER_PHONE_NUMBER_2 = "user_phone_number_2";
	public static final String U_USER_MOBILE_NUMBER_1 = "user_mobile_number_1";
	public static final String U_USER_MOBILE_NUMBER_2 = "user_mobile_number_2";
	public static final String U_USER_EMAIL_1 = "user_email_1";
	public static final String U_USER_EMAIL_2 = "user_email_2";
	public static final String U_REGISTRANTION_TIME = "registrantion_time";
	public static final String U_LAST_UPDATED_DATE = "last_updated_date";
	public static final String U_USER_STATUS = "user_status";
	public static final String U_DEVID = "DevId";
	//message
	public static final String TBName_Message = "message";
	public static final String M_MSG_ORDER= "msg_order";
	public static final String M_MSG_ID= "msg_id";
	public static final String M_MSG_STATUS= "msg_status";
	public static final String M_MSG_TITLE= "msg_title";
	public static final String M_MSG_TIME= "msg_time";
	public static final String M_MSG_HTML_CONTENT= "msg_html_content";
	public static final String M_MSG_URL="msg_url";
	
	//Language
	// 英文
	public static final String ENGLISH = "en";
	// 繁体中文
	public static final String TRADITIONAL_CHINESE = "tw";

}
