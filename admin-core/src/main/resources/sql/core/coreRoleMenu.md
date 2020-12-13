queryMenuByUser
===

* 根據用戶和登入機構來獲取能訪問的菜單列錶

	select menu_id from core_role_menu  rm where rm.role_id in (select role_id from core_user_role where user_id=#userId# and org_id=#orgId#)
	
deleteRoleMenu
===

* 刪除菜單對應的角色關係

	delete from core_role_menu where menu_id in ( #join(ids)# )
	