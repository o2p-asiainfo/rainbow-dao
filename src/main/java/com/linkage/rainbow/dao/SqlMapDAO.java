package com.linkage.rainbow.dao;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * DAO操作公共类，提供ibatis XML配置与数据库配置两种实现
 * @author cl
 *
 */
public interface  SqlMapDAO {

	
	/**
	 * 查询返回List
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 * @return
	 */
	public List queryForList(String statementName, Object parameterObject);
	
	
	/**
	 * 查询返回List
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 * @param skipResults 跳过条数，用于分页,此方式只适合小数据量分页，大数据要采用SQL语句中rownum方式
	 * @param maxResults 最大条数,用于分页,此方式只适合小数据量分页，大数据要采用SQL语句中rownum方式
	 * @return
	 */
	public List queryForList(String statementName, Object parameterObject, int skipResults, int maxResults);
	
	/**
	 * 查询返回Map
	 * @param statementName  SQLMap ID 
	 * @param parameterObject 查询条件
	 * @param keyProperty 跳过条数，用于分页,此方式只适合小数据量分页，大数据要采用SQL语句中rownum方式
	 * @return
	 */
	public Map queryForMap(String statementName, Object parameterObject, String keyProperty);
	
	
	/**
	 * 查询返回Map
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 * @param keyProperty
	 * @param valueProperty
	 * @return
	 */
	public Map queryForMap(String statementName, Object parameterObject, String keyProperty, String valueProperty);
	
	/**
	 * 查询返回Object
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 * @return
	 */
	public Object queryForObject(String statementName, Object parameterObject);
	
	/**
	 * 查询返回Object
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 * @param resultObject 
	 * @return
	 */
	public Object queryForObject(String statementName, Object parameterObject, Object resultObject);
	
	/**
	 * 更新并返回影响行数，可适用于update、insert、delete
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 * @return
	 */
	public int update(String statementName, Object parameterObject);
	
	/**
	 * 执行update语句
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 * @param requiredRowsAffected
	 */
	public void update(String statementName, Object parameterObject, int requiredRowsAffected);
	
	/**
	 * 执行insert语句
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 */ 
	public Object insert(String statementName, Object parameterObject);
	
	/**
	 * 执行delete语句
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询条件
	 */
	public void delete(String statementName, Object parameterObject);
	
	/**
	 * 添加准备批量操作的SQL
	 * @param statementName SQLMap ID 
	 * @param parameterObject 查询参数
	 */
	public void addBatch(String statementName,Object parameterObject);
	
	/**
	 * 运行通过addBatch()添加的批处理语句，如果全部命令执行成功，则返回更新计数组成的数组。<br>
	 * 返回数组的 int 元素的排序对应于批中的命令，批中的命令根据被添加到批中的顺序排序。
	 * @return int[] 更新计数组成的数组
	 */
	public int[] executeBatch();
	
	/**
	 * 生成序列值
	 * @param seqName 系列名
	 * @return
	 */
	public String getSequence(String seqName);
	
	
	public SqlMapClientTemplate getSmcTemplate();

	public void setSmcTemplate(SqlMapClientTemplate smcTemplate);
}
