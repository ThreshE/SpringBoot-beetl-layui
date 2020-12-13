getRoleFunction
===

* 查詢指定的登入用戶是否能訪問某個功能

	select * from core_role_function where role_id in (
		select role_id  from core_user_role where user_id =#userId# and org_id=#orgId#
	) and FUNCTION_ID = (select id from core_function where code=#code#)



getRoleChildrenFunction
===

* 查詢指定角色和所在機構的人某個功能下的子功能列錶

	select sf.code from core_role_function  rf left join core_function sf on rf.function_id=sf.id where rf.role_id in (
		select role_id  from core_user_role where user_id =#userId# and org_id=#orgId#
	) and rf.FUNCTION_ID in (select id from core_function where parent_id=  #parentId#	)
	



