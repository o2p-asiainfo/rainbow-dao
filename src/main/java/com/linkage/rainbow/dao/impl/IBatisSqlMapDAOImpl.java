package com.linkage.rainbow.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.linkage.rainbow.util.context.ContextUtil;
import com.linkage.rainbow.dao.sqlDebug.SqlDebugUtil;
import com.linkage.rainbow.dao.SqlMapDAO;

/**
 * <p>iBatis DAO 数据库操作工具类</p>
 *
 * @version 	0.0.1
 */
public class IBatisSqlMapDAOImpl extends SqlMapClientDaoSupport  implements SqlMapDAO {
	
	private SqlMapClientTemplate smcTemplate = this.getSqlMapClientTemplate(); //Spring结合iBatis工具类

	private List statementNameList = new ArrayList();
	private List parameterObjectList = new ArrayList();
	/**
	 * 查询返回List
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	public List queryForList(String statementName, Object parameterObject){

		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return null;
		else
			return this.smcTemplate.queryForList(statementName, parameterObject);
	}
	/**
	 * 查询返回List
	 * @param statementName
	 * @param parameterObject
	 * @param skipResults
	 * @param maxResults
	 * @return
	 */
	public List queryForList(String statementName, Object parameterObject, int skipResults, int maxResults){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject, skipResults, maxResults);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return null;
		else
			return this.smcTemplate.queryForList(statementName, parameterObject, skipResults, maxResults);
	}
	/**
	 * 查询返回Map
	 * @param statementName
	 * @param parameterObject
	 * @param keyProperty
	 * @return
	 */
	public Map queryForMap(String statementName, Object parameterObject, String keyProperty){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return null;
		else
			return this.smcTemplate.queryForMap(statementName, parameterObject, keyProperty);
	}
	/**
	 * 查询返回Map
	 * @param statementName
	 * @param parameterObject
	 * @param keyProperty
	 * @param valueProperty
	 * @return
	 */
	public Map queryForMap(String statementName, Object parameterObject, String keyProperty, String valueProperty){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return null;
		else
			return this.smcTemplate.queryForMap(statementName, parameterObject, keyProperty, valueProperty);
	}
	/**
	 * 查询返回Object
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	public Object queryForObject(String statementName, Object parameterObject){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return null;
		else
			return this.smcTemplate.queryForObject(statementName, parameterObject);
	}
	/**
	 * 查询返回Object
	 * @param statementName
	 * @param parameterObject
	 * @param resultObject
	 * @return
	 */
	public Object queryForObject(String statementName, Object parameterObject, Object resultObject){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return null;
		else
			return this.smcTemplate.queryForObject(statementName, parameterObject, resultObject);
	}
	/**
	 * 更新并返回影响行数，可适用于update、insert、delete
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	public int update(String statementName, Object parameterObject){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return 0;
		else
			return this.smcTemplate.update(statementName, parameterObject);
	}
	/**
	 * 执行update语句
	 * @param statementName
	 * @param parameterObject
	 * @param requiredRowsAffected
	 */
	public void update(String statementName, Object parameterObject, int requiredRowsAffected){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(!isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			this.smcTemplate.update(statementName, parameterObject, requiredRowsAffected);
	}
	/**
	 * 执行insert语句
	 * @param statementName
	 * @param parameterObject
	 */
	public Object insert(String statementName, Object parameterObject){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			return null;
		else
			return this.smcTemplate.insert(statementName, parameterObject);
	}
	
	/**
	 * 执行delete语句
	 * @param statementName
	 * @param parameterObject
	 */
	public void delete(String statementName, Object parameterObject){
		SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
		if(!isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
			this.smcTemplate.delete(statementName, parameterObject);
	}
	
	/**
	 * 添加准备批量操作的SQL
	 * @param statementName ibatis　sqlMapId
	 * @param parameterObject 查询参数
	 */
	public void addBatch(String statementName,Object parameterObject){
		
		/**批量操作语句存入线程变量*/
	    List list = (List)ContextUtil.get("executeBatchSqlList");
        if(list ==null)
     	   list=new ArrayList();
        Map map = new HashMap();
        map.put("statementName",statementName);
        map.put("parameterObject",parameterObject);
        list.add(map);
        ContextUtil.put("executeBatchSqlList",list);
        /**批量操作语句存入线程变量 end*/
		
	}
	
	/**
	 * 运行通过addBatch()添加的批处理语句，如果全部命令执行成功，则返回更新计数组成的数组。<br>
	 * 返回数组的 int 元素的排序对应于批中的命令，批中的命令根据被添加到批中的顺序排序。
	 * @return int[] 更新计数组成的数组
	 */
	public int[] executeBatch(){
		int result[]=new int[0];
		List list = (List)ContextUtil.get("executeBatchSqlList");
		if(list != null){
			result=new int[list.size()];
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String statementName = (String)map.get("statementName");
				Object parameterObject =  map.get("parameterObject");
				SqlDebugUtil.addSqlDebug(smcTemplate,statementName, parameterObject);
				if(isExportExcel(parameterObject)) //判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
					result[i]=0;
				else
					result[i]= this.smcTemplate.update(statementName, parameterObject);
			}
			ContextUtil.put("executeBatchSqlList",null);
		}
		return result;
	}
	/**
	 * 生成序列值
	 * @param seqName
	 * @return
	 */
	public String getSequence(String seqName){
		SqlDebugUtil.addSqlDebug(smcTemplate,"rainbow-dao.getSequenceValue", seqName);
		return (String)this.smcTemplate.queryForObject("rainbow-dao.getSequenceValue", seqName);
	}
	

	public SqlMapClientTemplate getSmcTemplate() {
		return smcTemplate;
	}
	public void setSmcTemplate(SqlMapClientTemplate smcTemplate) {
		this.smcTemplate = smcTemplate;
	}
	
	/**
	 * 判断为EXCEL导出时,是否只将执行的SQL放入线程变量,由导出类通过JDBC执行.因ibatis映射到java对象时,大数据量引起内存溢出.
	 * 
	 * @param parameterObjec
	 * @return
	 */
	private boolean isExportExcel(Object parameterObjec){
		boolean isExportExcel = false;
		if(parameterObjec !=null && parameterObjec instanceof Map){
			Map map = (Map)parameterObjec;
			//isExportExcelSqlToThread 是由公共组件在excel导出时,自动添加此参数
			//对于大数据量导出时,采用ibatis生成list会引起内存溢出,各模块要提高性能,在excel导出调用的bean方法时,
			//在最后个是返回结果的SQL执行,增加isDomainSqlToThread=true,如果此返回结果的SQL有涉及临时表,则添加临时表的sql也在添加isDomainSqlToThread参数.
			if(map.get("isExportExcelSqlToThread") != null && map.get("isExportExcelSqlToThread").toString().equalsIgnoreCase("true")
					&&map.get("isDomainSqlToThread")!=null && map.get("isDomainSqlToThread").toString().equalsIgnoreCase("true")){
				isExportExcel = true;
			}
		}
		return isExportExcel;
	}
}
