layui.define(['table', 'functionApi'], function(exports) {
	var functionApi = layui.functionApi;
	var table=layui.table;
	var view = {
			init:function(){
				
			},
			delBatch:function(){
				var data = Common.getMoreDataFromTable(table,"functionTable");
				if(data==null){
					return ;
				}
				Common.openConfirm("確認要刪除這些Function?",function(){
					var ids =Common.concatBatchId(data);
					functionApi.del(ids,function(){
						Common.info("刪除成功");
						dataReload();
					})
				})
				
			}
				
	}
	 exports('del',view);
	
});