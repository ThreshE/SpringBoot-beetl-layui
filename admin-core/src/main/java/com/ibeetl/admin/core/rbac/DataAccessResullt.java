package com.ibeetl.admin.core.rbac;

import java.util.List;
/**
 * 通過DataAccess 得出跟查詢相關的用戶或者組織機構列錶
 * @author lijiazhi
 *
 */
public class DataAccessResullt {
	private List<Long> userIds;
	private List<Long> orgIds;
	//1 結果僅僅包含用戶， 2 ，結果僅僅包含組織機構 3 結果匹配所有組織結構 4 結果不匹配任何組織機構
	private AccessType status ;
	
	
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	public List<Long> getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}
	public AccessType getStatus() {
		return status;
	}
	public void setStatus(AccessType status) {
		this.status = status;
	}
	
	
}
