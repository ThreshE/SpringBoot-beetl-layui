queryUsersByRole
===

* 根據角色和組織機構查詢用，註意到用戶兼職情況，因此實際部門應該是work_org_id

	 select u.*,ur.org_id work_org_id from CORE_USER_ROLE ur,CORE_User u 
	 where ur.role_id = #roleId# and ur.org_id in( #join(orgs)#) and ur.user_id=u.id