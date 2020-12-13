package com.ibeetl.admin.core.rbac;

/**
 * 數據權限算法結果
 * @author xiandafu
 *
 */
public enum AccessType {
	OnlyUser(1), OnlyOrg(2), AllOrg(3), NoneOrg(4);

	private int value;

	AccessType(int value) {
		this.value = value;
	}
}
