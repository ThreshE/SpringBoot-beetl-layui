package com.ibeetl.admin.core.util.beetl;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibeetl.admin.core.rbac.tree.OrgItem;
import com.ibeetl.admin.core.service.CoreRoleService;

/**
 * 獲取係統的所有角色列錶
 * @author xiandafu
 *
 */
@Component
public class RoleFunction implements Function {

	@Autowired
	CoreRoleService  coreRoleService;
	
	
	public Object call(Object[] paras, Context ctx) {
		
		String type = null;
		if(paras.length!=0) {
			type = (String)paras[0];
		}
		return coreRoleService.getAllRoles(type);
		
	}
	
	

}
