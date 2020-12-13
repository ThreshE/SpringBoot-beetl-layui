package com.ibeetl.admin.core.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibeetl.admin.core.annotation.Dict;
import com.ibeetl.admin.core.util.ValidateConfig;
import com.ibeetl.admin.core.util.enums.CoreDictType;


/**
 * 機構對象，
 * 有母公司，一個係統隻有一個母公司，多個集團，集團下可以有多個公司，子公司，部門。如果係統不符合這個設定，需要修改·
 * 
 * <br/>
 * 映射了上級機構，可以通過org.parentOrg.xxx取上級機構的屬性
 */

public class CoreOrg extends BaseEntity {
    
    // 自增id
	@NotNull(message = "ID不能為空", groups = ValidateConfig.UPDATE.class)
	@SeqID(name = ORACLE_CORE_SEQ_NAME)
	@AutoID
    private Long id;

    //刪除標識
    @JsonIgnore
    protected Integer delFlag= 0;
    //創建時間
    protected Date createTime;

    // 機構編號
    @NotBlank(message = "組織編號不能為空", groups = ValidateConfig.ADD.class)
    private String code;

    // 機構名稱
    @NotBlank(message = "組織名稱不能為空", groups = {ValidateConfig.ADD.class, ValidateConfig.UPDATE.class})
    private String name;

    // 上層機構id
    private Long parentOrgId;

    // 機構類型 1 集團 2 公司，3 部門，4 小組
    @Dict(type = CoreDictType.ORG_TYPE)
    @NotBlank(message = "組織類型不能為空", groups = ValidateConfig.class)
    private String type;

  

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

   

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

   
}
