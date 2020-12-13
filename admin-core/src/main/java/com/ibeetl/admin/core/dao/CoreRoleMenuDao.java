package com.ibeetl.admin.core.dao;

import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.SqlResource;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import com.ibeetl.admin.core.entity.CoreRoleMenu;

import java.util.List;

@SqlResource("core.coreRoleMenu")
public interface CoreRoleMenuDao extends BaseMapper<CoreRoleMenu> {

    /**
     * 根據用戶ID，機構ID查詢菜單
     * @param userId 用戶id
     * @param orgId  機構id
     * @return
     */
    List<Long> queryMenuByUser( Long userId, Long orgId);

    /**
     * 根據菜單id刪除角色和菜單關係
     * @param ids
     */
    void deleteRoleMenu(List<Long> ids);
}
