clearMenuFunction
===

* 當功能點被刪除的時候，將菜單對應的功能列錶設定為空

	update core_menu set FUNCTION_ID=null where FUNCTION_ID in (#join(functionIds)#)

queryByCondtion
===============
* 根據條件查詢



allMenuWithURL
===

* 獲得菜單和對應功能點的URL

	select m.*,f.access_url from core_menu m left join core_function  f on m.function_id=f.id 

