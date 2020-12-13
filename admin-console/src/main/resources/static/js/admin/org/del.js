layui.define(['table', 'orgApi'], function(exports) {
	var orgApi = layui.orgApi;
	var table=layui.table;
	var view = {
			init:function(){
				
			},
			delBatch:function(){
				var data = Common.getMoreDataFromTable(table,"orgTable");
				if(data==null){
					return ;
				}
				Common.openConfirm("確認要刪除這些結構?",function(){
					var ids =Common.concatBatchId(data);
					orgApi.del(ids,function(){
						Common.info("刪除成功");
						dataReload();
					})
				})
				
			}
				
	}
	 exports('del',view);
	
});