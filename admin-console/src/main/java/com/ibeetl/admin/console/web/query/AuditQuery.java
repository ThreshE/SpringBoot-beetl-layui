package com.ibeetl.admin.console.web.query;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ibeetl.admin.core.annotation.Query;
import com.ibeetl.admin.core.util.Tool;
import com.ibeetl.admin.core.web.query.PageParam;

/**
 * 審計查詢條件
 */
public class AuditQuery extends PageParam {

    @Query(name = "用戶名", display = true,fuzzy=true)
    private String userName;
	@Query(name = "類型名稱", display = true,fuzzy=true)
	private String functionName;
    @Query(name = "類型代號", display = true,fuzzy=true)
    private String functionCode;

    @Query(name="創建時間",display=true,type=Query.TYPE_DATE_BETWEEN)
    private String createDateRange;
    private Date createDateMin;
    private Date createDateMax;
    
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getCreateDateRange() {
        return createDateRange;
    }
    public void setCreateDateRange(String createDateRange) {
        this.createDateRange = createDateRange;
        if(StringUtils.isEmpty(createDateRange)) {
            return ;
        }
        Date[] ds = Tool.parseDataRange(createDateRange);
        this.createDateMin=ds[0];
        this.createDateMax =ds[1];
    }
    public Date getCreateDateMin() {
        return createDateMin;
    }
    public void setCreateDateMin(Date createDateMin) {
        this.createDateMin = createDateMin;
    }
    public Date getCreateDateMax() {
        return createDateMax;
    }
    public void setCreateDateMax(Date createDateMax) {
        this.createDateMax = createDateMax;
    }
    public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
    
	
    

}
