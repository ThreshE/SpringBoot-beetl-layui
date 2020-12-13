layui.define([ 'form', 'laydate', 'table' ], function(exports) {
	var form = layui.form;
	var laydate = layui.laydate;
	var table = layui.table;
	var dictTable = null;

	var view = {

		init : function() {
			this.initTable();
			this.initSearchForm();
			this.initToolBar();
			window.dataReload = function() {
				Lib.doSearchForm($("#searchForm"), dictTable)
			}

		},
		initTable : function() {
			dictTable = table.render({
				elem : '#dictTable',
				height : Lib.getTableHeight(1),
				method : 'post',
				url : Common.ctxPath + '/admin/dict/list.json' //數據接口
				,
				page : Lib.tablePage //開啓分頁
				,
				limit : 10,
				cols : [ [ //錶頭
				{
					type : 'checkbox',
					fixed : 'left',
				}, {
					field : 'id',
					title : 'id',
					width : 80,
					fixed : 'left',
					sort : true
				}, {
					field : 'value',
					title : '字典值',
					fixed : 'left',
					width : 120,
				}, {
					field : 'name',
					title : '字典名稱',
					width : 180,
				}, {
					field : 'type',
					title : '字典類型',
					width : 180,
				}, {
					field : 'typeName',
					title : '字典類型名稱',

					width : 180,
				}, {
					field : 'sort',
					title : '排序',
					width : 60,
				}, {
					field : 'parent',
					title : '父字典',
					width : 100,
				},

				{
					field : 'remark',
					title : '備註',

					width : 100,
				}, {
					field : 'createTime',
					title : '創建時間',

					width : 100,
				}

				] ]

			});
		},

		initSearchForm : function() {
			Lib.initSearchForm($("#searchForm"), dictTable, form);
		},
		initToolBar : function() {
			toolbar = {
				add : function() { //獲取選中數據
					var url = "/admin/dict/add.do";
					Common.openDlg(url, "字典數據管理>新增");
				},
				edit : function() { //獲取選中數目
					var data = Common.getOneFromTable(table, "dictTable");
					if (data == null) {
						return;
					}
					var url = "/admin/dict/edit.do?id=" + data.id;
					Common.openDlg(url, "字典數據管理>" + data.value + ">編輯");

				},
				del : function() {
					layui.use([ 'del' ], function() {
						var delView = layui.del
						delView.delBatch();
					});
				},
				exportExcel : function() {
					layui.use([ 'dictApi' ], function() {
						var dictApi = layui.dictApi
						Common.openConfirm("確認要導出這些字典數據?", function() {
							dictApi.exportExcel($("#searchForm"), function(fileId) {
								Lib.download(fileId);
							})
						})
					});

				},
				importExcel:function(){
				    //上載路徑
				    var uploadUrl = Common.ctxPath+"/admin/dict/excel/import.do";
				    //模闆
				    var templatePath= "/admin/dict/dict_upload_template.xls";
				    //公共的簡單上載文件處理
				    var url = "/core/file/simpleUpload.do?uploadUrl="+uploadUrl+"&templatePath="+templatePath;
                    Common.openDlg(url, "字典數據管理>上載");
				}

			};
			$('.ext-toolbar').on('click', function() {
				var type = $(this).data('type');
				toolbar[type] ? toolbar[type].call(this) : '';
			});
		}
	}

	exports('index', view);

});