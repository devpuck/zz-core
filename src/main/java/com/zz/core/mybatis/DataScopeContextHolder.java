package com.zz.core.mybatis;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * 用户上下文
 */
@UtilityClass
public class DataScopeContextHolder {

	private final ThreadLocal<DataScopeConfig> THREAD_LOCAL_DATA_SCOPE = new TransmittableThreadLocal<>();
	
	/**
	 *  设置当前SQL 执行上下文
	 *
	 * @param scopeConfig
	 */
	public void setDataScopeConfig(DataScopeConfig scopeConfig) {
		THREAD_LOCAL_DATA_SCOPE.set(scopeConfig);
	}

	/**
	 * 获取当前当前SQL 执行上下文
	 *
	 * @return
	 */
	public DataScopeConfig getDataScopeConfig() {
		return THREAD_LOCAL_DATA_SCOPE.get();
	}

	public void clear() {
		THREAD_LOCAL_DATA_SCOPE.remove();
	}
}
