package com.linkage.rainbow.dao;

import java.util.Map;


/**
 * 多数据源动态路由接口
 * @author cl
 *
 */
public interface IMultiDataSourceRoute {

	/**
	 * 调用路由规则
	 * 
	 */
	public void setRoutingRules(String str) ;
	
	public void setRoutingRules(Map map);
	
	/**
	 * 根据路由规则取得数据源KEY值
	 * @return
	 */
	public String getDataSourceKey();
}
