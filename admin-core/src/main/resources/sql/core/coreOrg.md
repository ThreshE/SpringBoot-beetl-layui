queryOrgByUser
===

* 根據用戶id查詢可能所在的部門，考慮到兼職部門

	select * from core_org where id in ( select org_id from core_user_role where user_id=#userId# group by org_id) and del_flag = 0 order by id desc
	
queryAllOrgCode
===

* 根據id對應的code，目前用於傳遞給工作流係統

	select code from core_org where id  in( #join(orgIds)#)

