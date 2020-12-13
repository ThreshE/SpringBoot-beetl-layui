package com.ibeetl.admin.core.dao;

import java.util.List;
import java.util.Map;

import org.beetl.sql.core.annotatoin.SqlResource;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import com.ibeetl.admin.core.entity.CoreDict;

/**
 * 字典DAO接口
 */
@SqlResource("core.coreDict")
public interface CoreDictDao extends BaseMapper<CoreDict> {

    /**
     * 查詢某個類型下的字典集合
     * @param type 字典類型
     * @return
     */
    List<CoreDict> findAllList(String type);

    /**
     * 查詢字段類型列錶
     * @param delFlag 刪除標記
     * @return
     */
    @SqlStatement(returnType = Map.class)
    List<Map<String, String>> findTypeList(int delFlag);

  

    /**
     * 根據父節點Id查詢子節點數據
     * @param id 父節點id
     * @return
     */
    List<CoreDict> findChildByParent(Long id);

    int bathDelByValue(List<String> values);
}
