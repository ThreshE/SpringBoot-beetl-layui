deleteRoleFunction
===

* 刪除所有的功能的角色配置

	delete from core_role_function where function_id in ( #join(ids)# )
getFunctionIdByRole
===

* 獲得角色對應的功能id

    select  function_id from core_role_function where role_id=#roleId#
    

getQueryFunctionAndRoleData
===

* 獲得所有查詢功能，並查詢角色對應的功能信息。

	select  f.*,r.data_access_type from core_role_function r left join core_function f on r.function_id=f.id where r.role_id=#roleId#  and f.type='FN1'