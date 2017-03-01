package com.linkage.rainbow.dao.sqlDebug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;

import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;   
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;   
import com.ibatis.sqlmap.engine.mapping.sql.Sql;   
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;  

/**
 * SqlDebugSession<br>
 * 存放在SqlDebugSession中,用于保存当前的Sql调试信息
 * <p>
 * @version 1.0
 * @author 陈亮 2009-3-18
 * <hr>
 * 修改记录
 * <hr>
 * 1、修改人员:陈亮    修改时间:2009-3-18<br>       
 *    修改内容:新建
 * <hr>
 */
public class SqlDebugSession  implements Serializable {
	private List<Map> sqls = new ArrayList();
	private boolean isDebug= false;
	private int maxSize = 30;    //最多显示多少条
	private int refreshRate = 5; //每多少秒刷新一次
	


	/**
	 * 添加一条ibatis执行的SQL
	 * @param strSQL 带?号的原始SQL
	 * @param para 参数值数组
	 * @param strFullSQL 将?号用参数代替后的完整SQL
	 */
	public void addSqlDebug(String strSQL,Object para[],String strFullSQL){
		if(isDebug) {
			int size = sqls.size();
			if(size>=maxSize)
				sqls.remove(0);
			Map sqlLog = new HashMap(); 
			sqlLog.put("SQL_SRC",strSQL);
			sqlLog.put("PARA",para);
			sqlLog.put("SQL",strFullSQL);
			sqlLog.put("DATE",new Date());
			sqls.add(sqlLog);
//			System.out.println(sqlLog);
		}
	}
	
	/**
	 * 添加一条直接执行的SQL
	 * @param strSQL
	 */
	public void addSqlDebug(String strSQL){
		if(isDebug) {
			int size = sqls.size();
			if(size>=maxSize)
				sqls.remove(0);
			Map sqlLog = new HashMap();
			sqlLog.put("SQL",strSQL);
			sqlLog.put("DATE",new Date());
			sqls.add(sqlLog);
		}
	}
	
	/**
	 * 清除原有SQL信息
	 *
	 */
	public void clear(){
		sqls =  new ArrayList();
	}


	/**
	 * 是否记录Sql调试信息
	 * @return
	 */
	public boolean isDebug() {
		return isDebug;
	}

	/**
	 * 设置是否记录Sql调试信息
	 * @param isDebug
	 */
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
		if(!isDebug)
			clear();
	}

	/**
	 * 取得调试的SQL信息
	 * @return
	 */
	public List getSqls() {
		return sqls;
	}

	public void setSqls(List sqls) {
		this.sqls = sqls;
	}
	
	/**
	 * 取得会话期中最大保存多少条SQL语句
	 * @return
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * 设置会话期中最大保存多少条SQL语句
	 * @param maxSize 
	 */
	public void setMaxSize(int maxSize) {
		if(maxSize>0)
			this.maxSize = maxSize;
	}

	/**
	 * 取得刷新的频率,每多少秒刷新一次
	 * @return
	 */
	public int getRefreshRate() {
		return refreshRate;
	}

	/**
	 * 设置刷新的频率,每多少秒刷新一次
	 * @param refreshRate
	 */
	public void setRefreshRate(int refreshRate) {
		if(refreshRate>1)
			this.refreshRate = refreshRate;
	}

	
}
