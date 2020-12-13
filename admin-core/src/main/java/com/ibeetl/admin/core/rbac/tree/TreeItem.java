package com.ibeetl.admin.core.rbac.tree;

import java.util.List;
/**
 *  菜單，功能點，組織機構等跟樹有關的結構的接口
 * @author lijiazhi
 *
 */
public interface TreeItem extends java.io.Serializable {
	public String getName();
	public Long getId();
	public List getChildren();
}
