layui.define([ 'form','codeApi'], function(exports) {
	var form = layui.form;
	var codeApi = layui.codeApi;

	var view = {
			init:function(){
				this.initSubmit();
			},
			initSubmit:function(){
			    $("#genProject").click(function(){
			        codeApi.genProject($('#projectForm'),function(){
                        codeApi.downloadProject($('#projectForm'));
                    });
                    
                    
                });
                
                $("#genProject-cancel").click(function(){
                    Lib.closeFrame();
                });
				
			}
	}
	
	
	
	 exports('project',view);
	
});