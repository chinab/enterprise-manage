package com.juduowang.utils;

/**
 * 系统全局常量类
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 */
public class Constants {
	/**
	 * 当前应用系统中，使用的资源包（ResourceBundle）名称
	 */
    public static final String BUNDLE_KEY = "ApplicationResources";
    
    /** The name of the configuration hashmap stored in application scope. */
    public static final String CONFIG = "appConfig";
    
    /**
     * 系统属性中的文件分隔符
     */
    public static final String FILE_SEP = System.getProperty("file.separator");
    
    /**
     * 系统属性中的用户路径
     */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;
    

	public static final String UNVALID_VALUE = "-1";
	public static final String NORMAL_VALUE = "0";

	public static final String ID = "id";
	public static final String STATUS = "status";
	
	/**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    /**
     * The name of the User role, as specified in web.xml
     */
    public static final String USER_ROLE = "ROLE_USER";

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";
    
    /** The encryption algorithm key to be used for passwords */
    public static final String ENC_ALGORITHM = "algorithm";

    /** A flag to indicate if passwords should be encrypted */
    public static final String ENCRYPT_PASSWORD = "encryptPassword";
    
    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";
    
    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";
    
    /**
     * 定义时间类型常量
     * 结果单位，1：年，2：月，3：日，4：小时,5:分，6：秒，7：毫秒，8：周，9：季度，0：半年
     */
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int DAY = 3;
    public static final int HOUR = 4;
    public static final int MINUTE = 5;
    public static final int SECOND = 6;
    public static final int MIllISECOND = 7;
    public static final int WEEK = 8;
    public static final int SEASON = 9;
    public static final int HALFYEAR = 0;
    
    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";
    
    public static final String DEPARTMENT = "department";
    
    public static final String DEPARTMENTTEMP = "departmentTemp";
    
    public static final String ORGANIZE = "organize";
    
    public static final String STATION = "station";

    public static final String DUTYRANK = "dutyRank";
    
    public static final String POLITICS = "politics";
    
    public static final String SCHOOL = "school";
    
    public static final String NATION = "nation";
    
    public static final String DEGREE = "degree";
    
    public static final String TECHNICDUTY = "technicduty";
    
    public static final String TECHNICGRADE = "technicgrade";
    
    public static final String EDUCATION = "education";
    
    public static final String NATIVEPLACE = "nativePlace";
    
    public static final String RELATIVECALLS = "relativeCalls";
    
    public static final String SPECIALTY = "specialty";
    
    public static final String COMMISSIONENTERPRISE = "commissionEnterprise";
    
    public static final String EMPLOYEEINFORM = "employeeInform";
    
    public static final String EMPLOYEEDEGREE = "employeeDegree";
    
    public static final String DIMISSIONTYPE = "dimissionType";
    
    public static final String CONTRACTTERM = "contractTerm";
    
    public static final String VACATIONTYPE = "vacationType";
    
    public static final String BASETABLETYPE = "baseTableType";
    
    public static final String DEPARTMENTTYPE = "departmentType";
    
    public static final String DUTY = "duty";
    
    public static final String EMPLOYEETYPE = "employeeType";
    
    public static final String INCOMEITEM = "incomeItem";
    
    public static final String PERFORMANCEROLE = "performanceRole";
    
    public static final String PERFORMANCETYPE = "performanceType";
    
    public static final String REMOVETYPE = "removeType";
    
    public static final String SALARYREPORT = "salaryReport";
    
    public static final String STATIONSTATE = "stationState";
    
    public static final String WORKTYPE = "workType";
    
    public static final String LANGUAGE = "language";
    
    public static final String STUDYTYPE = "studyType";
    
    public static final String WORKTYPEKIND = "workTypeKind";
    
    public static final String ORGANIZATIONEVOLUTIONDTYPE = "organizationevolutiondType";
    
    public static final String BASECOLUMN = "baseColumn";
    
    public static final String CHECKITEM = "checkItem";
    
    public static final String KPI = "kpi";
    
    public static final String ROLE = "role";
    
    public static final String SALARYITEM = "salaryItem";
}

