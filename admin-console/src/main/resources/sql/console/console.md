queryByCondition
===


    select 
    @pageTag(){
    t.*
    @}
    from cms_blog t
    where 1=1  
    @//數據權限，該sql語句功能點  
    and #function("console.query")#
    @if(!isEmpty(id)){
        and  t.id =#id#
    @}
    @if(!isEmpty(title)){
        and  t.title =#title#
    @}
    
    
    

batchDelCmsBlogByIds
===

* 批量邏輯刪除

    update cms_blog set del_flag = 1 where id in( #join(ids)#)
    
