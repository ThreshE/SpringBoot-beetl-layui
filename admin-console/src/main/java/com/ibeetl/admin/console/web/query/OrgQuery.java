package com.ibeetl.admin.console.web.query;

import com.ibeetl.admin.core.annotation.Query;
import com.ibeetl.admin.core.util.enums.CoreDictType;
import com.ibeetl.admin.core.web.query.PageParam;

/**
 * 字典錶單查詢條件
 */
public class OrgQuery extends PageParam {

    @Query(name = "機構編號", display = true)
    private String code;
    @Query(name = "機構名稱", display = true)
    private String name;
	@Query(name="機構類型",display=true,type=Query.TYPE_DICT,dict=CoreDictType.ORG_TYPE)
	private String type;
	@Query(name="上一級機構",display=true,type=Query.TYPE_CONTROL,control="org")
	private String parentOrgId;
    
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

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

}
