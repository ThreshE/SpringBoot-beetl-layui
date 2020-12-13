package com.ibeetl.admin.console.web.query;

import com.ibeetl.admin.core.annotation.Query;
import com.ibeetl.admin.core.util.enums.CoreDictType;
import com.ibeetl.admin.core.web.query.PageParam;
/**
 *功能查詢
 */
public class CoreFunctionQuery extends PageParam {
    @Query(name = "名稱", display = true)
    private String name;
    @Query(name = "訪問路徑", display = true)
    private String accessUrl;
    @Query(name = "功能點類型", display = true)
    private String type;
	public String getName(){
        return  name;
    }
    public void setName(String name ){
        this.name = name;
    }
	public String getAccessUrl(){
        return  accessUrl;
    }
    public void setAccessUrl(String accessUrl ){
        this.accessUrl = accessUrl;
    }
	public String getType(){
        return  type;
    }
    public void setType(String type ){
        this.type = type;
    }
 
}
