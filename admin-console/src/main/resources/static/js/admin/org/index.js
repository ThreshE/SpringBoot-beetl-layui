layui.define([ 'form', 'laydate', 'table' ], function(exports) {
	var form = layui.form;
	var laydate = layui.laydate;
	var table = layui.table;
	var userTable = null;
	var view ={
		
		init:function(){
			this.initTable();
			this.initSearchForm();
			this.initToolBar();
			window.dataReload = function(){
				Lib.doSearchForm($("#orgSearchForm"),userTable)
			}
			
			
		},
		initTable:function(){
			userTable = table.render({
				elem : '#orgTable',
				height : Lib.getTableHeight(2),
				method : 'post',
				url : Common.ctxPath + '/admin/org/list.json' //數據接口
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
				}, {
					field : 'code',
					title : '機構編號',
					width : 150
				}, {
					field : 'name',
					title : '機構名稱',
					width : 120,
					sort : true
				}, {
					field : 'parentOrgText',
					title : '上一級機構',
					width : 150,
					sort : true
				},
				{
					field : 'typeText',
					title : '機構類型',
					width : 120,
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
			Lib.initSearchForm( $("#orgSearchForm"),userTable,form);
		},
		initToolBar:function(){
			toolbar = {
					add : function() { //獲取選中數據
						var url = "/admin/org/add.do";
						Common.openDlg(url,"用戶管理>新增");
					},
					edit : function() { //獲取選中數目
						var data = Common.getOneFromTable(table,"orgTable");
						if(data==null){
							return ;
						}
						var url = "/admin/org/edit.do?id="+data.id;
						Common.openDlg(url,"用戶管理>編輯");
						
					},
					del : function() { 
						layui.use(['del'], function(){
							  var delView = layui.del
							  delView.delBatch();
						});
					},
					orgUser : function() { 
						var data = Common.getOneFromTable(table,"orgTable");
						if(data==null){
							return ;
						}
						var url = "/admin/org/user/list.do?orgId="+data.id;
						Common.openDlg(url,"組織管理>用戶列錶");
						
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