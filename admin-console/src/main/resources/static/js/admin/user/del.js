layui.define(['table', 'userApi'], function(exports) {
	var userApi = layui.userApi;
	var table=layui.table;
	var view = {
			init:function(){
				
			},
			delBatch:function(){
				var data = Common.getMoreDataFromTable(table,"userTable");
				if(data==null){
					return ;
				}
				Common.openConfirm("確認要刪除這些用戶?",function(){
					debugger;
					var ids =Common.concatBatchId(data);
					userApi.del(ids,function(){
						Common.info("刪除成功");
						dataReload();
					})
				})
				
			}
				
	}
	 exports('del',view);
	
});