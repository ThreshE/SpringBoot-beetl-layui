layui.define(['table', 'userApi'], function(exports) {
	var userApi = layui.userApi;
	var table=layui.table;
	var view = {
			init:function(){
				
			},
			delBatch:function(){
				var data = Common.getMoreDataFromTable(table,"userRoleTable");
				if(data==null){
					return ;
				}
				Common.openConfirm("確認要刪除這些角色?",function(){
					var ids =Common.concatBatchId(data);
					userApi.delUserRole(ids,function(){
						Common.info("刪除成功");
						dataReload();
					})
				})
				
			}
				
	}
	 exports('roleDel',view);
	
});