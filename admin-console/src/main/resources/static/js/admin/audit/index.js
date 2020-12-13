layui.define([ 'form', 'laydate', 'table' ], function(exports) {
	var form = layui.form;
	var laydate = layui.laydate;
	var table = layui.table;
	var auditTable = null;
	var view ={
		
		init:function(){
			this.initTable();
			this.initSearchForm();
			window.dataReload = function(){
				Lib.doSearchForm($("#auditSearchForm"),auditTable)
			}
			
			
		},
		initTable:function(){
			auditTable = table.render({
				elem : '#auditTable',
				height : 'full-180',
				method : 'post',
				url : Common.ctxPath+ '/admin/audit/list.json' //數據接口
				,page : {"layout":['count','prev', 'page', 'next']} //開啓分頁
				,limit : 10,
				cols : [ [ //錶頭
				{
					type : 'checkbox',
					fixed:'left',
				}, {
					field : 'id',
					title : 'id',
					width : 80,
					fixed:'left',
					sort : true
				},{
					field : 'userName',
					title : '用戶名',
					width : 120
				},{
					field : 'ip',
					title : '訪問IP',
					width : 150
				},{
					field : 'functionName',
					title : '類型名稱',
					width : 120,
					sort : true
				}, {
					field : 'functionCode',
					title : '類型代號',
					width : 120,
					sort : true
				}, {
					field : 'message',
					title : '消息',
					width : 150
				}, 
				 {
					field : 'success',
					title : '訪問狀態',
					width : 120,
					templet:function(d){
						return d.success == 1?'成功':'失敗';
					},
					sort : true
				},
				{
					field : 'createTime',
					title : '創建時間',
					width : 180,
					templet:function(d){
						return Common.getDate(d.createTime,'yyyy-MM-dd HH:mm:ss');
					},
					sort : true
				}

				] ]

			});
		},
		
		initSearchForm:function(){
			Lib.initSearchForm( $("#auditSearchForm"),auditTable,form);
		}
	}

	 exports('index',view);
	
});