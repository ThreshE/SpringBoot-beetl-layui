package com.ibeetl.admin.console.service;

import java.util.ArrayList;
import java.util.List;

import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibeetl.admin.console.dao.OrgConsoleDao;
import com.ibeetl.admin.core.entity.CoreOrg;
import com.ibeetl.admin.core.entity.CoreUser;
import com.ibeetl.admin.core.rbac.tree.OrgItem;
import com.ibeetl.admin.core.service.CoreBaseService;
import com.ibeetl.admin.core.service.CorePlatformService;
import com.ibeetl.admin.core.util.PlatformException;

/**
 * 
 * @author : xiandafu
 */
@Service
@Transactional
public class OrgConsoleService extends CoreBaseService<CoreOrg> {

    @Autowired
    private OrgConsoleDao orgDao;

    @Autowired
    private CorePlatformService platformService;

    /**
     * 根據條件查詢
     * @param query
     */
    public void queryByCondtion(PageQuery<CoreOrg> query) {
        orgDao.queryByCondtion(query);
        List<CoreOrg> list = query.getList();
        queryListAfter(list);
        OrgItem root = platformService.buildOrg();
        //處理父機構名稱顯示，冇有用sql查詢是考慮到跨數據庫
        for(CoreOrg org:list) {
        	Long parentId = org.getParentOrgId();
        	OrgItem item = root.findChild(parentId);
        	String name = item!=null?item.getName():"";
        	org.set("parentOrgText", name);
        }
    }
    
    public void queryUserByCondition(PageQuery<CoreUser> query) {
    	orgDao.queryUserByCondtion(query);
    	queryListAfter(query.getList());
    }


    /**
     * 獲取機構下麵的所以機構
     * @param orgId 機構id
     */
    public List<Long> getAllChildIdsByOrgId(Long orgId) {
        if (orgId == null)
            return null;

        OrgItem orgItem = platformService.buildOrg().findChild(orgId);
        if (orgItem == null) {
            return null;
        }
        List<Long> ids = orgItem.findAllChildrenId();
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(orgId);

        return ids;
    }
    
    @Override
    public boolean deleteById(List<Long> ids) {
    	OrgItem root = platformService.buildOrg();
        //檢查子節點
    	
        for (Long id : ids) {
        	OrgItem child = root.findChild(id);
        	if(child.getChildren().size()!=0){
        		throw new PlatformException("不能刪除 "+child.getOrg().getName()+",還包含子機構");
        	}
        }
        return super.deleteById(ids);
    }
    


}
