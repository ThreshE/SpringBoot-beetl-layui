layui.define([ 'form', 'laydate', 'table' ], function(exports) {
	var form = layui.form;
	var laydate = layui.laydate;
	var table = layui.table;
	var roleTable = null;
	var view ={
		
		init:function(){
			this.initTable();
			this.initSearchForm();
			this.initToolBar();
			window.dataReload = function(){
				Lib.doSearchForm($("#searchForm"),roleTable)
			}
			
			
		},
		initTable:function(){
			roleTable = table.render({
				elem : '#roleTable',
				height : Lib.getTableHeight(2),
				method : 'post',
				url : Common.ctxPath + '/admin/role/list.json' //數據接口
				,page : Lib.tablePage //開啓分頁
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
				}, {
					field : 'code',
					title : '角色代碼',
					width : 150
				}, {
					field : 'name',
					title : '角色名稱',
					width : 120,
					sort : true
				}, {
					field : 'typeText',
					title : '角色類型',
					width : 300,
					sort : true
				},
				{
					field : 'createTime',
					title : '創建時間',
					width : 120,
					templet:function(d){
						return Common.getDate(d.createTime);
					},
					sort : true
				}

				] ]

			});
		},
		
		initSearchForm:function(){
			Lib.initSearchForm( $("#searchForm"),roleTable,form);
		},
		initToolBar:function(){
			toolbar = {
					add : function() { //獲取選中數據
						var url = "/admin/role/add.do";
						Common.openDlg(url,"角色管理>新增");
					},
					edit : function() { //獲取選中數目
						var data = Common.getOneFromTable(table,"roleTable");
						if(data==null){
							return ;
						}
						var url = "/admin/role/edit.do?id="+data.id;
						Common.openDlg(url,"角色管理>>編輯");
						
					},
					del : function() { 
						layui.use(['del'], function(){
							  var delView = layui.del
							  delView.delBatch();
						});
					},
					roleUser : function() { 
						var data = Common.getOneFromTable(table,"roleTable");
						if(data==null){
							return ;
						}
						var url = "/admin/role/user/list.do?roleId="+data.id;
						Common.openDlg(url,"角色管理>"+data.name+">用戶列錶");
						
					}
					
				};
			$('.ext-toolbar').on('click', function() {
				var type = $(this).data('type');
				toolbar[type] ? toolbar[type].call(this) : '';
			});
		}
	}

	 exports('index',view);
	
});