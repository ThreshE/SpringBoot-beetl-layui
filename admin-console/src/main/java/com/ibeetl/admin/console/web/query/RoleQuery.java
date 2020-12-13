package com.ibeetl.admin.console.web.query;

import com.ibeetl.admin.core.annotation.Query;
import com.ibeetl.admin.core.util.enums.CoreDictType;
import com.ibeetl.admin.core.web.query.PageParam;

/**
 * 描述:  角色查詢條件
 *
 */
public class RoleQuery extends PageParam {
    @Query(name = "角色代碼", display = true)
    private String code;
    @Query(name = "角色名稱", display = true)
    private String name;
    @Query(name = "業務角色類型", type = Query.TYPE_DICT,dict=CoreDictType.ROLE_TYPE)
    private String type;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	


}
