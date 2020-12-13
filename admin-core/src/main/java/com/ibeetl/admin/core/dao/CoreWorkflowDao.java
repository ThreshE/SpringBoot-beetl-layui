package com.ibeetl.admin.core.dao;

import org.beetl.sql.core.annotatoin.SqlResource;
import org.beetl.sql.core.mapper.BaseMapper;

import com.ibeetl.admin.core.entity.CoreUser;

import java.util.List;

/**
 * 業務側工作流的基本功能，工作流的一些功能將在業務側完成，比如選人操作
 * @author lijiazhi
 */
@SqlResource("core.workflow")
public interface CoreWorkflowDao extends BaseMapper {

    /**
     * 根據角色id，機構id查詢用戶集合
     * @param roleId 角色id
     * @param orgs   機構id
     * @return
     */
    List<CoreUser> queryUsersByRole(Long roleId, List<Long> orgs);
}
