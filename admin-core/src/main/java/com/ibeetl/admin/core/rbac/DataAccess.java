package com.ibeetl.admin.core.rbac;

import com.ibeetl.admin.core.service.CorePlatformService;

/**
 * 數據權限接口類
 * @author Administrator
 *
 */
public interface DataAccess {
	 DataAccessResullt getOrg(Long userId,Long orgId );
	 String getName();
	 Integer getType();
}
