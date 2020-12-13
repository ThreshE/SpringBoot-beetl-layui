package com.ibeetl.admin.core.dao;

import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.SqlResource;
import org.beetl.sql.core.mapper.BaseMapper;

import com.ibeetl.admin.core.entity.CoreUser;

import java.util.List;

@SqlResource("core.coreUser")
public interface CoreUserDao extends BaseMapper<CoreUser> {

    /**
     * 根據角色編碼查詢用戶集合
     * @param roleCode 角色編碼
     * @return
     */
    List<CoreUser> getUserByRole( String roleCode);


}
