layui.define(['table', 'roleApi'], function(exports) {
	var roleApi = layui.roleApi;
	var table=layui.table;
	var view = {
			init:function(){
				
			},
			delBatch:function(){
				var data = Common.getMoreDataFromTable(table,"roleTable");
				if(data==null){
					return ;
				}
				Common.openConfirm("確認要刪除這些角色?",function(){
					var ids =Common.concatBatchId(data);
					roleApi.del(ids,function(){
						Common.info("刪除成功");
						dataReload();
					})
				})
				
			}
				
	}
	 exports('del',view);
	
});