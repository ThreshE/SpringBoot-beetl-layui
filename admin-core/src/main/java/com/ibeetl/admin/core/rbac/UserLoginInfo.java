package com.ibeetl.admin.core.rbac;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.ibeetl.admin.core.entity.CoreOrg;
import com.ibeetl.admin.core.entity.CoreUser;

public class UserLoginInfo {
	//用戶所在機構
	List<CoreOrg> orgs = new ArrayList<CoreOrg>();
	//用戶信息
	CoreUser user;
	//用戶預設登入機構
	CoreOrg currentOrg = null;
	public List<CoreOrg> getOrgs() {
		return orgs;
	}
	public void setOrgs(List<CoreOrg> orgs) {
		this.orgs = orgs;
	}
	public CoreUser getUser() {
		return user;
	}
	public void setUser(CoreUser user) {
		this.user = user;
	}
	public CoreOrg getCurrentOrg() {
		return currentOrg;
	}
	public void setCurrentOrg(CoreOrg currentOrg) {
		this.currentOrg = currentOrg;
	}
	
}
