package com.ibeetl.admin.console.web.query;

import java.util.Date;

import com.ibeetl.admin.core.annotation.Query;
import com.ibeetl.admin.core.web.query.PageParam;

/**
 * 字典錶單查詢條件
 */
public class WorkflowQuery extends PageParam {

    @Query(name = "用戶列錶", display = true,fuzzy=true)
    private String userIds;
    @Query(name = "角色列錶", display = true,fuzzy=true)
    private String roleIds;
    @Query(name = "機構列錶", display = true,fuzzy=true)
    private String orgIds;
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
  
    
    
    
	
    

}
