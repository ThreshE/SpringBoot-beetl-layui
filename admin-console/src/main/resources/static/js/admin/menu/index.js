layui.define([ 'form', 'laydate', 'table' ], function(exports) {
	var form = layui.form;
	var laydate = layui.laydate;
	var table = layui.table;
	var menuTable = null;
	var view ={
		
		init:function(){
			this.initTable();
			this.initSearchForm();
			this.initToolBar();
			window.dataReload = function(){
				Lib.doSearchForm($("#menuSearchForm"),menuTable)
			}
			
			
		},
		initTable:function(){
			menuTable = table.render({
				elem : '#menuTable',
				height : 'full-280',
				method : 'post',
				url : Common.ctxPath + '/admin/menu/list.json' //數據接口
				,page : {"layout":['count','prev', 'page', 'next']} //開啓分頁
				,limit : 10,
				cols : [ [ //錶頭
				{
					type : 'checkbox',
					fixed:'left',
				}, 
				{
					field : 'id',
					title : 'id',
					width : 80,
					fixed:'left',
					sort : true
				}, {
					field : 'code',
					title : '菜單代碼',
					width : 120
				}, {
					field : 'name',
					title : '菜單名稱',
					width : 120,
					sort : true
				}, {
					field : 'accessUrl',
					title : '菜單入口地址',
					width : 250,
					sort : true
				} , {
					field : 'icon',
					title : '圖示',
					width : 80
				},{
					field : 'seq',
					title : '排序',
					width : 80,
					sort : true
				},{
					field : 'parentMenuName',
					title : '上一級菜單',
					width : 120,
					sort : true
				},{
					field : 'typeText',
					title : '菜單類型',
					width : 100,
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
			Lib.initSearchForm( $("#menuSearchForm"),menuTable,form);
		},
		initToolBar:function(){
			toolbar = {
					add : function() { //獲取選中數據
						var url = "/admin/menu/add.do";
						Common.openDlg(url,"菜單管理>新增");
					},
					edit : function() { //獲取選中數目
						var data = Common.getOneFromTable(table,"menuTable");
						if(data==null){
							return ;
						}
						var url = "/admin/menu/edit.do?id="+data.id;
						Common.openDlg(url,"菜單管理>編輯");
						
					},
					del : function() { 
						layui.use(['del'], function(){
							  var delView = layui.del
							  delView.delBatch();
						});
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