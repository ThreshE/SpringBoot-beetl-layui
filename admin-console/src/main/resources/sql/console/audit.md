queryByCondtion
===

    select 
    @pageTag(){
    a.*
    @}
    from core_audit a where 1=1 
    @ // 添加數據權限
    and  #function("trace")#
    @if(isNotEmpty(functionName)){
    		and function_name like #'%'+functionName+'%'#
    @}
    @if(isNotEmpty(functionCode)){
    		and function_code like #'%'+functionCode+'%'#
    @}
    @if(isNotEmpty(userName)){
    		and user_name like #'%'+userName+'%'#
    @}
    @if(!isEmpty(createDateMin)){
         and  create_time>= #createDateMin#
    @}
    @if(!isEmpty(createDateMax)){
        and  create_time< #nextDay(createDateMax)#
    @}
    @pageIgnoreTag(){
    order by a.create_time desc
    @}
    
	
