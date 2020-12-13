layui.define(['table', 'menuApi'], function(exports) {
	var menuApi = layui.menuApi;
	var table=layui.table;
	var view = {
			init:function(){
				
			},
			delBatch:function(){
				var data = Common.getMoreDataFromTable(table,"menuTable");
				if(data==null){
					return ;
				}
				Common.openConfirm("確認要刪除這些菜單?",function(){
					var ids =Common.concatBatchId(data);
					menuApi.del(ids,function(){
						Common.info("刪除成功");
						dataReload();
					})
				})
				
			}
				
	}
	 exports('del',view);
	
});