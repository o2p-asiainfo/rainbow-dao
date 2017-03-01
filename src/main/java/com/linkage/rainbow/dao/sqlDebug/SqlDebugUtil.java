package com.linkage.rainbow.dao.sqlDebug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.linkage.rainbow.util.config.DefaultSettings;
import com.linkage.rainbow.util.context.ContextUtil;



/**
 * SqlDebugUtil<br>
 * Sql璋冭瘯淇℃伅宸ュ叿绫�鐢ㄤ簬鍙栧緱褰撳墠浼氳瘽淇℃伅涓殑璋冭瘯淇℃伅,娣诲姞璋冭瘯淇℃伅,璁剧疆璋冭瘯淇℃伅鐩稿叧鍙傛暟绛�
 * <p>
 * @version 1.0
 * @author 闄堜寒 2009-3-18
 * <hr>
 * 淇敼璁板綍
 * <hr>
 * 1銆佷慨鏀逛汉鍛�闄堜寒    淇敼鏃堕棿:2009-3-18<br>       
 *    淇敼鍐呭:鏂板缓
 * <hr>
 */
public class SqlDebugUtil {
	private static final Log log = LogFactory.getLog(SqlDebugUtil.class);
	
	/**
	 * 瀛樺湪浼氳瘽鏈熶腑鐨凷ql璋冭瘯淇℃伅鐨剆ession key.
	 */
	public static String SQL_DEBUG_SESSION="SQL_DEBUG_SESSION";

	/**
	 * 娣诲姞鏌ヨ鏃ュ織
	 * @param strSQL
	 */
	public static void addSqlDebug(String strSQL){
		SqlDebugSession sqlDebugSession = getSqlDebugSession();
		sqlDebugSession.addSqlDebug(strSQL);
		
		//濡傛灉鏄痚xcel瀵煎嚭锛屽垯灏唖ql鍔犲叆绾跨▼鍙橀噺涓�
		addSqlToThread(strSQL);
	}
	
	private static void addSqlToThread(String strSQL){
		addSqlToThread(strSQL,null);
	}
	/**
	 * 濡傛灉鏄痚xcel瀵煎嚭锛屽垯灏唖ql鍔犲叆绾跨▼鍙橀噺涓�
	 * @param strSQL
	 * @param para
	 */
	private static void addSqlToThread(String strSQL,Object para){
		
		if(isExportExcel(para)){
		   /**鏌ヨ璇彞瀛樺叆绾跨▼鍙橀噺*/
		   List list = (List)ContextUtil.get("executeSqlList");
	        if(list ==null)
	     	   list=new ArrayList();
	        list.add(strSQL);
	        ContextUtil.put("executeSqlList",list);
		   /**鏌ヨ璇彞瀛樺叆绾跨▼鍙橀噺 end*/
		}
	}
	
	/**
	 * 娣诲姞鏌ヨ鏃ュ織
	 * 
	 * @param smcTemplate
	 * @param sqlMapId
	 * @param parameterObject
	 */
	public static void addSqlDebug(SqlMapClientTemplate smcTemplate,
			String sqlMapId, Object parameterObject) {
		addSqlDebug(smcTemplate, sqlMapId, parameterObject, -99,
				-99);
	}

	/**
	 * 娣诲姞鏌ヨ鏃ュ織
	 * 
	 * @param smcTemplate
	 * @param sqlMapId
	 * @param parameterObject
	 * @param skipResults
	 * @param maxResults
	 */
	public static void addSqlDebug(SqlMapClientTemplate smcTemplate,
			String sqlMapId, Object parameterObject, int skipResults,
			int maxResults) {
		try {
			

			SqlDebugSession sqlDebugSession = getSqlDebugSession();
			
			String strSQL = "";
			Object para[] = null;
			// SqlMapExecutorDelegate鏄竴涓浉褰撴牳蹇冪殑绫�浠栧瓨鏀句簡閰嶇疆鏂囦欢鎵�湁淇℃伅鍜宩ava杩炴帴瀵硅薄,鏈変竴涓細璇濇睜鍜屼竴涓姹傛睜,杩樻湁sql瑙ｆ瀽鍏�浠ュ強涓�釜鍏蜂綋sql璇彞鐨勬墽琛岃�
			SqlMapExecutorDelegate delegate = ((ExtendedSqlMapClient) (smcTemplate
					.getSqlMapClient())).getDelegate();
			// 杩欎釜绫荤敤鏉ュ瓨鏀炬煇涓猧d鍚嶇殑Statement淇℃伅
			MappedStatement ms = delegate.getMappedStatement(sqlMapId);
			// sql绫诲氨鏄煇涓�被鍨�Statement鐨勫搴攕ql鎷艰瑙ｆ瀽绫�
			Sql sql = ms.getSql();
			SessionScope sessionScope = new SessionScope();
			StatementScope statementScope = new StatementScope(sessionScope);
			statementScope.setStatement(ms);
			// 鐒跺悗璋冪敤getSql鏂规硶,鎶婂弬鏁板�鏁扮粍浼犺繘鍘�
			strSQL = sql.getSql(statementScope, parameterObject);
			//System.out.println(strSQL);
	
			 // 鍙栧緱鏌ヨ鍙傛暟
			 ParameterMap paraMap = ms.getParameterMap();
			 // 濡傛灉涓虹┖鍙栧緱鍔ㄦ�璇彞鐨勫弬鏁癿ap
			 if (paraMap == null)
				 paraMap = statementScope.getDynamicParameterMap();
			 if (paraMap != null) {
				 para = paraMap.getParameterObjectValues(statementScope,parameterObject);
			 }
	
			// 鍙栧緱鏌ヨ鍙傛暟
//			ParameterMap pm = sql.getParameterMap(statementScope, parameterObject);
//			if(pm != null)
//				para = pm.getParameterObjectValues(statementScope, parameterObject);
//	
			// 鍙栧緱灏�鍙风敤鍙傛暟浠ｆ浛鍚庣殑瀹屾暣SQL
			String strFullSQL = getFullSql(strSQL, para);
			if(sqlDebugSession.isDebug()){
				sqlDebugSession.addSqlDebug(strSQL, para, strFullSQL);		
			}
		
			addSqlToThread(strFullSQL,parameterObject);
		} catch (Exception e) {
			//e.printStackTrace();
			// TODO: handle exception
		}
		

	}
	
	/**
	 * 娣诲姞涓�潯ibatis鎵ц鐨凷QL,浼氳嚜鍔ㄥ皢?鍙风敤鍙傛暟浠ｆ浛鍚庣敓鎴愬畬鏁碨QL
	 * @param strSQL 甯�鍙风殑鍘熷SQL
	 * @param para 鍙傛暟鍊兼暟缁�
	 */
	public void addSqlDebug(String strSQL,Object para[]){

		SqlDebugSession sqlDebugSession = getSqlDebugSession();
		// 鍙栧緱灏�鍙风敤鍙傛暟浠ｆ浛鍚庣殑瀹屾暣SQL
		String strFullSQL = getFullSql(strSQL, para);
		sqlDebugSession.addSqlDebug(strSQL, para, strFullSQL);		
		
		//濡傛灉鏄痚xcel瀵煎嚭锛屽垯灏唖ql鍔犲叆绾跨▼鍙橀噺涓�
		addSqlToThread(strFullSQL,para);
	}
	
	/**
	 * 娣诲姞涓�潯ibatis鎵ц鐨凷QL
	 * @param strSQL 甯�鍙风殑鍘熷SQL
	 * @param para 鍙傛暟鍊兼暟缁�
	 * @param strFullSQL 灏�鍙风敤鍙傛暟浠ｆ浛鍚庣殑瀹屾暣SQL
	 */
	public void addSqlDebug(String strSQL,Object para[],String strFullSQL){
		SqlDebugSession sqlDebugSession = getSqlDebugSession();
		sqlDebugSession.addSqlDebug(strSQL, para, strFullSQL);		
		
		//濡傛灉鏄痚xcel瀵煎嚭锛屽垯灏唖ql鍔犲叆绾跨▼鍙橀噺涓�
		addSqlToThread(strFullSQL,para);
	}
	
	/**
	 * 璁剧疆浼氳瘽鏈熶腑鏈�ぇ淇濆瓨澶氬皯鏉QL璇彞
	 * @param maxSize 
	 */
	public static void setMaxSize(int maxSize) {
		SqlDebugSession sqlDebugSession = getSqlDebugSession();
		sqlDebugSession.setMaxSize(maxSize);
	}
	
	/**
	 * 璁剧疆浼氳瘽鏈熶腑鏈�ぇ淇濆瓨澶氬皯鏉QL璇彞
	 * @param maxSize 
	 */
	public static void setMaxSize(String maxSize) {
		try {
			if(maxSize != null && !maxSize.equals("")){
				int iMaxSize = Integer.parseInt(maxSize);
				SqlDebugSession sqlDebugSession = getSqlDebugSession();
				sqlDebugSession.setMaxSize(iMaxSize);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 璁剧疆浼氳瘽鏈熶腑鏈�ぇ淇濆瓨澶氬皯鏉QL璇彞
	 * @param maxSize 
	 */
//	public static void setMaxSize(HttpSession session,int maxSize) {
//		SqlDebugSession sqlDebugSession = getSqlDebugSession(session);
//		sqlDebugSession.setMaxSize(maxSize);
//	}
	
	/**
	 * 璁剧疆浼氳瘽鏈熶腑鏈�ぇ淇濆瓨澶氬皯鏉QL璇彞
	 * @param maxSize 
	 */
//	public static void setMaxSize(HttpSession session,String maxSize) {
//		try {
//			if(maxSize != null && !maxSize.equals("")){
//				int iMaxSize = Integer.parseInt(maxSize);
//				SqlDebugSession sqlDebugSession = getSqlDebugSession(session);
//				sqlDebugSession.setMaxSize(iMaxSize);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
	
	/**
	 * 璁剧疆鍒锋柊鐨勯鐜�姣忓灏戠鍒锋柊涓�
	 * @param session
	 * @param refreshRate
	 */
//	public static void setRefreshRate(HttpSession session, int refreshRate) {
//		SqlDebugSession sqlDebugSession = getSqlDebugSession(session);
//		sqlDebugSession.setRefreshRate(refreshRate);
//	}

	/**
	 * 璁剧疆鍒锋柊鐨勯鐜�姣忓灏戠鍒锋柊涓�
	 * @param session
	 * @param refreshRate
	 */
//	public static void setRefreshRate(HttpSession session, String refreshRate) {
//		try {
//			if (refreshRate != null && !refreshRate.equals("")) {
//				int iRefreshRate = Integer.parseInt(refreshRate);
//				SqlDebugSession sqlDebugSession = getSqlDebugSession(session);
//				sqlDebugSession.setRefreshRate(iRefreshRate);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}

	/**
	 * 璁剧疆鍒锋柊鐨勯鐜�姣忓灏戠鍒锋柊涓�
	 * @param refreshRate
	 */
	public static void setRefreshRate(int refreshRate) {
		SqlDebugSession sqlDebugSession = getSqlDebugSession();
		sqlDebugSession.setRefreshRate(refreshRate);
	}

	/**
	 * 璁剧疆鍒锋柊鐨勯鐜�姣忓灏戠鍒锋柊涓�
	 * @param refreshRate
	 */
	public static void setRefreshRate(String refreshRate) {
		try {
			if (refreshRate != null && !refreshRate.equals("")) {
				int iRefreshRate = Integer.parseInt(refreshRate);
				SqlDebugSession sqlDebugSession = getSqlDebugSession();
				sqlDebugSession.setRefreshRate(iRefreshRate);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	/**
	 * 鍙栧緱浼氭湡涓殑璋冭瘯淇℃伅绫�
	 * @param session
	 * @return
	 */
	public static SqlDebugSession getSqlDebugSession(){
		SqlDebugSession sqlDebugSession = null;
//		if(session != null){
//			sqlDebugSession = (SqlDebugSession)session.getAttribute(SQL_DEBUG_SESSION);
//			if(sqlDebugSession == null){
//				sqlDebugSession = new SqlDebugSession();
//				session.setAttribute(SQL_DEBUG_SESSION,sqlDebugSession);
//			}
//		} else {
//			Map sessionMap = ActionContext.getContext().getSession();
//			sqlDebugSession = (SqlDebugSession) sessionMap
//					.get(SQL_DEBUG_SESSION);
//		}
		
/***********************************************************************************/
//		try {
//			com.linkage.rainbow.util.context.ContextImpl context=(com.linkage.rainbow.util.context.ContextImpl) com.linkage.rainbow.util.context.ContextHolder.getContext();
//			if(context != null)
//				sqlDebugSession = (SqlDebugSession)context.get(SQL_DEBUG_SESSION);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		if(sqlDebugSession == null){
//			sqlDebugSession = new SqlDebugSession();
//		}
		/***********************************************************************************/
		if(null != ServletActionContext.getRequest().getSession()){
			try {
				sqlDebugSession = (SqlDebugSession)ServletActionContext.getRequest().getSession().getAttribute(SQL_DEBUG_SESSION);
				if(sqlDebugSession == null){
					sqlDebugSession = new SqlDebugSession();
					ServletActionContext.getRequest().getSession().setAttribute(SQL_DEBUG_SESSION,sqlDebugSession);
				}
			} catch (Exception e) {
				log.error(e.getStackTrace());
			}
		}
		if(sqlDebugSession == null){
			sqlDebugSession = new SqlDebugSession();
		}
		return sqlDebugSession;
	}
	

	/**
	 * 閫氳繃甯�鍙风殑棰勭紪璇慡QL涓庢煡璇㈠弬鏁�鏇挎崲SQL涓殑?鍙�鍙栧緱瀹屾暣鐨凷QL
	 * @param strSQL
	 * @param para
	 * @return
	 */
	public static String getFullSql(String strSQL, Object para[]) {
		int currIndex = 0;
		StringBuffer newstrSQL = new StringBuffer();
		int len = strSQL.length();
		int currParaIndex = 0;
		for (; currIndex < len; currIndex++) {
			char c = strSQL.charAt(currIndex);
			if (c == '\'') {
				newstrSQL.append(c);
				int index = currIndex + 1;
				if (index < len) {
					if (strSQL.charAt(index) == '\'') {
						currIndex++;
						newstrSQL.append(strSQL.charAt(currIndex));
						continue;
					}
				}
				currIndex++;
				for (; currIndex < len; currIndex++) {
					char ch = strSQL.charAt(currIndex);
					newstrSQL.append(ch);
					if (ch == '\'') {
						index = currIndex + 1;
						if (index < len) {
							if (strSQL.charAt(index) == '\'') {
								currIndex++;
								newstrSQL.append(strSQL.charAt(currIndex));
								continue;
							}
						}
						break;
					}
				}
			} else if (c == '?') {
				Object obj = para[currParaIndex];
				if (obj instanceof String) {
					newstrSQL.append("'").append(obj).append("'");
				} else {
					newstrSQL.append(obj);
				}
				currParaIndex++;

			} else {
				newstrSQL.append(c);
			}

		}
		return newstrSQL.toString();

	}
	
	/**
	 * 鍙栧緱瑕佸湪鐣岄潰涓婃樉绀虹殑SQL璋冭瘯鍥炬爣.
	 * 鍦ㄥ叕鍏辩粍浠堕厤缃枃浠朵腑com/linkage/rainbow/uidefault.properties涓�璁剧疆鐨勫彲鏌ョ湅璋冭瘯淇℃伅鐨勭櫥褰曡�鏍囪瘑
	 * #閫氳繃姝essionKey鍙栧緱瀛樺偍鍦ㄤ細璇濇湡涓櫥褰曡�淇℃伅
	 * comm.sqlDebug.loginSessionKey=StaffInfo
	 * #浠巗ession鍙栧緱瀵硅薄鍚庯紝閫氳繃浠ヤ笅鏂规硶鍙栧緱褰撳墠鐧诲綍鑰呮爣璇�
	 * comm.sqlDebug.getSessionStaffID=getSTAFF_ID
	 * #鍏佽SQL璋冭瘯鍔熻兘鐨勭櫥褰曡�鏍囪瘑,澶氫釜浜哄憳閫氳繃锛屽彿鍒嗛殧
	 * comm.sqlDebug.staffIds =123456,3
	 * 濡傛灉褰撳墠鐧诲綍鑰呮槸閰嶇疆鏂囦欢涓厤缃殑浜哄憳,鍒欐樉绀篠QL璋冭瘯鐨勫浘鏍�
	 * @return
	 */
	public static String getIcon(HttpServletRequest request){
		String icon="";
		try {
			//閫氳繃姝essionKey鍙栧緱瀛樺偍鍦ㄤ細璇濇湡涓櫥褰曡�淇℃伅
			String loginSessionKey = DefaultSettings.getProperty("comm.sqlDebug.loginSessionKey");
			//浠巗ession鍙栧緱瀵硅薄鍚庯紝閫氳繃浠ヤ笅鏂规硶鍙栧緱褰撳墠鐧诲綍鑰呮爣璇�
			String getSessionStaffID = DefaultSettings.getProperty("comm.sqlDebug.getSessionStaffID");
			//鍏佽SQL璋冭瘯鍔熻兘鐨勭櫥褰曡�鏍囪瘑,澶氫釜浜哄憳閫氳繃锛屽彿鍒嗛殧
			String staffIds = DefaultSettings.getProperty("comm.sqlDebug.staffIds");
			String path=request.getContextPath();
			String link = "<a href=\"javascript:\" onclick=\"window.open('"+path+"/comm/servlet.shtml?servlet_name=sqlDebug','comm_show_sql_debug','width=800,height=650,toolbar=no,left=200,top=50,toolbar=no,location=no,directories=no,status=not,menubar=no,scrollbars=yes,resizable=yes');\" ><img width=18 height=18 border=0 src=\""+path+"/struts/simple/sqlDebug/resource/icon.jpg\"/></a>";
			//anybody浠ｈ〃浠讳綍浜�
			if(staffIds !=null && (","+staffIds+",").toLowerCase().indexOf(",anybody,")>=0){
				icon = link;
			}
			
			Object loginSessionObj = null;
			if(loginSessionKey != null && loginSessionKey.trim().length()>0 && staffIds != null && staffIds.trim().length()>0){
				HttpSession session = request.getSession();
				loginSessionObj = session.getAttribute(loginSessionKey);
	
				if(loginSessionObj !=null){
					Class argsClass[] = new Class[0];
		            java.lang.reflect.Method  method = null;
		            Object value = null;
		            try {
		        	   method = loginSessionObj.getClass().getMethod(getSessionStaffID,argsClass);
				       value = method.invoke(loginSessionObj,new Object[0]);
		            }
		            catch (Exception ex) {
		            	//ex.printStackTrace();
		            }
		            if(value != null){
		            	
		            	String strValue = ","+value.toString()+",";
		            	staffIds = ","+staffIds+",";
		            	//濡傛灉褰撳墠鐧诲綍鑰呮槸閰嶇疆鏂囦欢涓厤缃殑浜哄憳,鍒欐樉绀篠QL璋冭瘯鐨勫浘鏍�
		            	if(staffIds.indexOf(strValue)>=0){
		            		icon = link;
		            	}
		            }
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return icon;
	}

	/**
	 * 鍒ゆ柇涓篍XCEL瀵煎嚭鏃�鏄惁鍙皢鎵ц鐨凷QL鏀惧叆绾跨▼鍙橀噺,鐢卞鍑虹被閫氳繃JDBC鎵ц.鍥爄batis鏄犲皠鍒癹ava瀵硅薄鏃�澶ф暟鎹噺寮曡捣鍐呭瓨婧㈠嚭.
	 * @param parameterObjec
	 * @return
	 */
	private static boolean isExportExcel(Object parameterObjec){
		boolean isExportExcel = false;
		if(parameterObjec !=null && parameterObjec instanceof Map){
			Map map = (Map)parameterObjec;
//			if(map.get("isExportExcelSqlToThread") != null && map.get("isExportExcelSqlToThread").toString().equalsIgnoreCase("true")){
			if(map.get("isExportExcelSqlToThread") != null && map.get("isExportExcelSqlToThread").toString().equalsIgnoreCase("true")
					&&map.get("isDomainSqlToThread")!=null && map.get("isDomainSqlToThread").toString().equalsIgnoreCase("true")){
				isExportExcel = true;
			}
		}
		return isExportExcel;
	}
	
	public static void main(String[] args) {
/*		SqlDebugUtil a = new SqlDebugUtil();
		String strSQL = "select a.menu_id node,a.super_id f_node,a.menu_name text,'/sys/manage/menu/menuList.shtml'||'?vo.SUPER_ID='||a.menu_id||'&amp;'||'vo.HAVE_SUB='||a.have_sub||'&amp;'||'vo.IS_MENU='||a.is_menu url,a.is_menu,a.menu_order,a.title      from ts_menu_info a     where a.state = 'A'            AND     is_menu = ?                 and super_id = ?       order by a.menu_order        ";

		String para[] = new String[] { "123456789", "987655444" };
		String strFullSQL = a.getFullSql(strSQL, para);
		System.out.println(strFullSQL);*/
	}
}
