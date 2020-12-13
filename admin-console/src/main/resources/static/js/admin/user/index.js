layui.define([ 'form', 'laydate', 'table','userApi' ], function(exports) {
	var form = layui.form;
	var laydate = layui.laydate;
	var table = layui.table;
	var userApi=layui.userApi;
	var userTable = null;
	
	var view ={
		
		init:function(){
			this.initTable();
			this.initSearchForm();
			this.initToolBar();
			window.dataReload = function(){
				Lib.doSearchForm($("#searchForm"),userTable)
			}
			
			
		},
		initTable:function(){
			userTable = table.render({
				elem : '#userTable',
				height : Lib.getTableHeight(2),
				method : 'post',
				url : Common.ctxPath + '/admin/user/list.json' //數據接口
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
					title : '用戶名',
					width : 150
				}, {
					field : 'name',
					title : '姓名',
					width : 120,
					sort : true
				}, {
					field : 'orgName',
					title : '機構',
					width : 120,
					sort : true
				}, {
					field : 'stateText',
					title : '狀態',
					width : 120,
					sort : true
				},
				 {
					field : 'jobType0Text',
					title : '職位',
					width : 120,
					sort : true
				},
				 {
					field : 'jobType1Text',
					title : '職位明細',
					width : 80,
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
			Lib.initSearchForm( $("#searchForm"),userTable,form);
		},
		initToolBar:function(){
			toolbar = {
					add : function() { //獲取選中數據
						var url = "/admin/user/add.do";
						Common.openDlg(url,"用戶管理>新增");
					},
					edit : function() { //獲取選中數目
						var data = Common.getOneFromTable(table,"userTable");
						if(data==null){
							return ;
						}
						var url = "/admin/user/edit.do?id="+data.id;
						Common.openDlg(url,"用戶管理>編輯");
						
					},
					del : function() { 
						layui.use(['del'], function(){
							  var delView = layui.del
							  delView.delBatch();
						});
					},
					userRole : function() { //獲取選中數目
						var data = Common.getOneFromTable(table,"userTable");
						if(data==null){
							return ;
						}
						var url = "/admin/user/role/list.do?id="+data.id;
						Common.openDlg(url,"用戶管理>"+data.name+">角色管理");
						
					},
					changePassword:function(){
						var data = Common.getOneFromTable(table,"userTable");
						if(data==null){
							return ;
						}
						var url = "/admin/user/changePassword.do?id="+data.id;
						Common.openDlg(url,"用戶管理>更改密碼");

					},
					exportUsers:function(){
					    Common.openConfirm("確認要導出這些用戶?",function(){
					        userApi.exportUsers($("#searchForm"),function(fileId){
					           Lib.download(fileId);
					        })
		                })

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