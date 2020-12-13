package com.ibeetl.admin.core.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.UpdateIgnore;

import com.ibeetl.admin.core.annotation.Dict;
import com.ibeetl.admin.core.util.ValidateConfig;
import com.ibeetl.admin.core.util.enums.CoreDictType;

/**
 * 係統菜單
 */
public class CoreMenu extends BaseEntity {

    public static final String TYPE_SYSTEM = "MENU_S";
    public static final String TYPE_NAV = "MENU_N";
    public static final String TYPE_MENUITEM = "MENU_M";

	@NotNull(message = "ID不能為空", groups = ValidateConfig.UPDATE.class)
	@SeqID(name = ORACLE_CORE_SEQ_NAME)
	@AutoID
    protected Long id;

    //創建時間
	@UpdateIgnore
    protected Date createTime;

    //菜單代碼
    @NotBlank(message = "菜單代碼不能為空", groups = ValidateConfig.ADD.class)
    private String code;

    //功能id
    private Long functionId;

    //類型  /*1 係統 2 導航 3 菜單項(與功能點有關)*/
    @NotNull(message = "菜單類型不能為空")
    @Dict(type = CoreDictType.MENU_TYPE)
    private String type;

    //菜單名稱
    @NotBlank(message = "菜單名稱不能為空")
    private String name;

    //上層菜單id
    @NotNull(message = "上層菜單不能為空")
    private Long parentMenuId;

    //排序
    @NotNull(message = "排序不能為空")
    private Integer seq;

    //圖示
    private String icon;

    public CoreMenu() {
    }

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

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(Long parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
    
}
