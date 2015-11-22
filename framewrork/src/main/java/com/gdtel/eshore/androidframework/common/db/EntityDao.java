package com.gdtel.eshore.androidframework.common.db;

import java.io.Serializable;
import java.util.List;

/**
 * 基本DAO接口
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2013-10-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface EntityDao<T, PK extends Serializable> {
	
	/**
	 * 添加
	 * @param entity
	 */
	long save(final T entity) throws Exception;
	
	/**
	 * 移除记录（指定ID集）
	 * @param ids 可以有多个
	 */
	void remove(final PK... ids);
	
	/**
	 * 更新
	 * @param entity
	 * @throws Exception 
	 */
	void upDate(final T entity) throws Exception;

	/**
	 * 按ID查询对象
	 * @param id
	 * @return
	 */
	T find(final PK id);

	/**
	 * 分页查询
	 * @param startResult 开始位置
	 * @param maxResult 记录容量
	 * @return
	 * @throws Exception 
	 */
	List<T> getScroolData(Integer startResult, Integer maxResult);

	/**
	 * 返回记录总数
	 * @return
	 */
	public Long getCount();

}
