package com.ibeetl.admin.console.dao;

import java.util.List;

import org.beetl.sql.core.annotatoin.SqlResource;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import com.ibeetl.admin.console.web.query.RoleUserQuery;
import com.ibeetl.admin.core.entity.CoreRole;
import com.ibeetl.admin.core.entity.CoreUser;

@SqlResource("console.role")
public interface RoleConsoleDao extends BaseMapper<CoreRole> {

    /**
     * 根據條件分頁查詢
     * @param query 查詢條件
     */
    void queryByCondtion(PageQuery query);

    /**
     * 批量刪除角色
     * @param ids 角色id
     */
    void batchDelByIds(List<Long> ids);
    
    void batchDeleteRoleFunction(List<Long> ids);
    void batchDeleteRoleMenu(List<Long> ids);
    void batchDeleteUserRole(List<Long> ids);
    
    
    PageQuery<CoreUser>  queryUser(PageQuery query);
    


   
}
