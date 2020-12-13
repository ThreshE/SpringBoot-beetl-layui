layui.define(['table', 'dictApi'], function(exports) {
	var dictApi = layui.dictApi;
	var table=layui.table;
	var view = {
			init:function(){
				
			},
			delBatch:function(){
				var data = Common.getMoreDataFromTable(table,"dictTable");
				if(data==null){
					return ;
				}
				Common.openConfirm("確認要刪除這些數據字典?",function(){
					var ids =Common.concatBatchId(data,"id");
					dictApi.del(ids,function(){
						Common.info("刪除成功");
						dataReload();
					})
				})
				
			}
				
	}
	 exports('del',view);
	
});