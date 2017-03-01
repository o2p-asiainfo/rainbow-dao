package com.linkage.rainbow.dao;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 动态切换数据源实现
 * @author 刘顺坤
 * 
 * 
 *
 */
public class MultiDataSource implements DataSource{

	private static final Log log = LogFactory.getLog(MultiDataSource.class);
	//private ApplicationContext applicationContext = null;
	private DataSource dataSource = null;
	private Map<String, DataSource> datasourceMap = null;
	private IMultiDataSourceRoute multiDataSourceRoute; //多数据源动态路由类
	
	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	public Map<String, DataSource> getDatasourceMap() {
		return datasourceMap;
	}

	public void setDatasourceMap(Map<String, DataSource> datasourceMap) {
		this.datasourceMap = datasourceMap;
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String arg0, String arg1) throws SQLException {
		return getDataSource().getConnection(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return getDataSource().getLogWriter();
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {
		return getDataSource().getLoginTimeout();
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter arg0) throws SQLException {
		getDataSource().setLogWriter(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int arg0) throws SQLException {
		getDataSource().setLoginTimeout(arg0);
	}

	/*public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}*/
	/**
	 * 获取当前dataSourceKey对应的数据源
	 */
	public DataSource getDataSource(String dataSourceKey) {
		log.debug("dataSourceKey:" + dataSourceKey);
		if (dataSourceKey == null || dataSourceKey.equals("")) {//默认数据源
			return this.dataSource;
		}
		return this.datasourceMap.get(dataSourceKey);

	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * 动态获取数据源（线程方式）
	 * @return
	 */
	public DataSource getDataSource() {
		String sp = multiDataSourceRoute.getDataSourceKey();
		return getDataSource(sp);
	}

	public IMultiDataSourceRoute getMultiDataSourceRoute() {
		return multiDataSourceRoute;
	}

	public void setMultiDataSourceRoute(IMultiDataSourceRoute multiDataSourceRoute) {
		this.multiDataSourceRoute = multiDataSourceRoute;
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}