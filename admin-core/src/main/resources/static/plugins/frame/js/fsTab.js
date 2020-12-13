/**
 * @Description: 菜單管理
 * @Copyright: 2017 www.fallsea.com Inc. All rights reserved.
 * @author: fallsea
 * @version 1.6.1
 * @License：MIT
 */
layui.define(['element'], function(exports){
  var element = layui.element,
  FsTab = function (){
  	this.config = {
  		topMenuFilter:"fsTopMenu",//頭部菜單
			leftMenuFilter:"fsLeftMenu",//左邊菜單
			tabFilter:"fsTab"//導航欄
		}
	};
	
	
	
	FsTab.prototype.render = function(options){
		var thisTab = this;
    $.extend(true, thisTab.config, options);
    
    thisTab.bindDeleteFilter();
    
    thisTab.bindTabFilter();
 

    //綁定左邊菜單點選。
    element.on('nav('+thisTab.config.leftMenuFilter+')', function(elem){
    	elem = $(elem).parent();
	  	var layId = $(elem).attr("lay-id");
	  	if($.isEmpty(layId)){
	  		layId = $.uuid();
	  		$(elem).attr("lay-id",layId);
	  		var dom =$(elem).find("a");
	  		var title = $(elem).find("a").html();
	  		var dataUrl = dom.attr("dataUrl");
	  		if(!$.isEmpty(dataUrl)){
	  			thisTab.add(title,dom.attr("dataUrl"),layId);
	  		}
	  	}
	  	
	  	
	  
	  	thisTab.tabChange(layId);
	  	$('body').removeClass('site-mobile');
    });
    
    
    element.on('tabDelete('+this.config.tabFilter+')', function(data){
		var li =$(this).parent();
		var layId = $(li).attr("lay-id");
		//刪除tab上標記
		var dl = $("#fsLeftMenu").find("[lay-id='"+layId+"']");
		$(dl).attr("lay-id","");
			
    });
    
	};
	
	/**
	 * 切換tab
	 */
	FsTab.prototype.tabChange = function(layId) {
		element.tabChange(this.config.tabFilter, layId);
	}
	
  
	/**
   * 新增
   */
	FsTab.prototype.add = function(title,dataUrl,layId) {
		
		element.tabAdd(this.config.tabFilter, {
		  title: title
		  ,content: '<iframe src="'+dataUrl+'"></iframe>' //支援傳入html
		  ,id: layId
		});
	};
  
  
	/**
   * 刪除監聽
   */
	FsTab.prototype.bindDeleteFilter = function(){
		element.on('tabDelete('+this.config.tabFilter+')', function(data){
	  	var layId = $(this).parentsUntil().attr("lay-id");
	  	$('.fsMenu .layui-nav-child>dd[lay-id="'+ layId +'"],.fsMenu>li[lay-id="'+ layId +'"]').removeAttr("lay-id");
		});
	}
	
	/**
	 * 監聽tab切換，處理菜單選中
	 */
	FsTab.prototype.bindTabFilter = function(){
		var thisTab = this;
		element.on('tab('+this.config.tabFilter+')', function(data){
			var layId = $(this).attr("lay-id");
			
			thisTab.menuSelectCss(layId);
			
		});
	}
	
	/**
	 * 菜單選中樣式
	 */
	FsTab.prototype.menuSelectCss = function(layId){
		if(!$.isEmpty(layId)){
			$('.fsMenu .layui-this').removeClass("layui-this");//清除樣式
			
			var dom =$('.fsMenu .layui-nav-child>dd[lay-id="'+ layId +'"],.fsMenu>li[lay-id="'+ layId +'"]');
			dom.addClass("layui-this");//追加樣式
			
			//處理頭部菜單
			if(dom.length==1){
				var dataPid = null;
				var tagName = dom.get(0).tagName;
				if(tagName == "LI"){
					dataPid = dom.attr("dataPid");
				}else if(tagName == "DD"){
					dataPid = dom.parentsUntil('li').parent().attr("dataPid");
				}
				if(!$.isEmpty(dataPid)){
					$('#fsTopMenu li[dataPid="'+ dataPid +'"]').click();
				}
			}
		}
	}
	
  
	var fsTab = new FsTab();
  //綁定按鈕
	exports("fsTab",fsTab);
  
});