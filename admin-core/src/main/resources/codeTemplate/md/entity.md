queryByCondition
===


    select 
    \@pageTag(){
    t.*
    \@}
    from ${entity.tableName} t
    where 1=1  
    \@//數據權限，該sql語句功能點,如果不考慮數據權限，可以刪除此行  
    and #function("${basicFunctionCode}.query")#
    @for(attr in entity.list){
    		@if(attr.showInQuery){
    		    @if(attr.dateRange || attr.dateTimeRange){
    \@if(!isEmpty(${strutil.replace (attr.name,"Range","")}Start)){
        and  t.${attr.colName} > #${strutil.replace (attr.name,"Range","")}Start#
    \@}
    \@if(!isEmpty(${strutil.replace (attr.name,"Range","")}End)){
        and  t.${attr.colName} < #${strutil.replace (attr.name,"Range","")}End#
    \@}
                @} else {
    \@if(!isEmpty(${attr.name})){
        and  t.${attr.colName} =#${attr.name}#
    \@}
                @}
    		@}
    @}
    
    
    

batchDel${entity.name}ByIds
===

* 批量邏輯刪除

    update ${entity.tableName} set del_flag = 1 where ${entity.idAttribute.colName}  in( #join(ids)#)
    
