package com.ibeetl.admin.core.rbac;

import java.util.ArrayList;
import java.util.List;

import com.ibeetl.admin.core.rbac.tree.OrgItem;

public interface   DataAccessFactory {
	/**
	 * 得到數去訪問權限
	 * @param type
	 * @return
	 */
	public DataAccess getDataAccess(Integer type);
	/**
	 * 得到用戶能看到得到組織機構樹，比如，用戶隻能看到公司級別的組織機構樹。
	 * DefaultDataAccessFactory 預設實現了能看到所在公司（集團，母公司）的組織機構樹
	 * @param OrgItem
	 * @return
	 */
	public OrgItem getUserOrgTree(OrgItem item) ;
	
	
	public List<DataAccess> all();
}
