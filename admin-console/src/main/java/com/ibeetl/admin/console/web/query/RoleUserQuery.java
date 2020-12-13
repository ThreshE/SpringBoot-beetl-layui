package com.ibeetl.admin.console.web.query;

import com.ibeetl.admin.core.annotation.Query;
import com.ibeetl.admin.core.util.enums.CoreDictType;
import com.ibeetl.admin.core.web.query.PageParam;

/**
 * 描述:  角色李的用戶列錶
 */
public class RoleUserQuery extends PageParam {
    @Query(name = "用戶名", display = true)
    private String userCode;
    @Query(name = "姓名", display = true)
    private String userName;
   
    private Long roleId;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


  


}
