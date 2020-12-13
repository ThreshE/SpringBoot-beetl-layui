package com.ibeetl.admin.console.service;

import java.util.List;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.engine.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibeetl.admin.console.dao.RoleConsoleDao;
import com.ibeetl.admin.core.entity.CoreRole;
import com.ibeetl.admin.core.entity.CoreUser;
import com.ibeetl.admin.core.rbac.tree.OrgItem;
import com.ibeetl.admin.core.service.CoreBaseService;
import com.ibeetl.admin.core.service.CorePlatformService;
import com.ibeetl.admin.core.util.PlatformException;
import com.ibeetl.admin.core.util.enums.RoleTypeEnum;

@Service
@Transactional
public class RoleConsoleService extends CoreBaseService<CoreRole> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleConsoleService.class);

    @Autowired
    private RoleConsoleDao roleDao;
    
    @Autowired
    private SQLManager sqlManager;

    @Autowired
    CorePlatformService platformService;

    /**
     * 獲取全部操作角色集合
     * @return
     */
    public List<CoreRole> queryAllPermissionList() {
    	CoreRole template = new CoreRole();
    	template.setType(RoleTypeEnum.ACCESS.getValue());
        return roleDao.template(template);
    }

    /**
     * 根據刪標記獲取全部角色集合
     * @param delFlagEnum 刪除標記
     * @return
     */
    public List<CoreRole> queryAllList() {
        return roleDao.all();
    }

    /**
     * 根據條件查詢
     * @param query
     */
    public void queryByCondtion(PageQuery query) {
        roleDao.queryByCondtion(query);
        super.queryListAfter(query.getList());
    }

    
    public PageQuery<CoreUser> queryRoleUser(PageQuery query) {
    	OrgItem root = platformService.buildOrg();
    	PageQuery<CoreUser>  ret = roleDao.queryUser(query);
    	List<CoreUser> list = ret.getList();
    	//從緩存裏取出組織機構名稱
    	for(CoreUser user:list) {
    		Long orgId = user.getOrgId();
    		Integer orgId1 = (Integer)user.get("orgId1");
    		user.set("orgIdText", root.findChild(orgId).getName());
    		user.set("orgId1Text", root.findChild((long)orgId1).getName());
    		
    	}
    	//再次處理數據字典
    	this.queryListAfter(list);
    	return ret;
    	 
    }

    /**
     * 根據編號查詢
     * @param code 角色編號
     * @return
     */
    public CoreRole queryByCode(String code) {
        CoreRole queryRole = new CoreRole();
        queryRole.setCode(code);
        CoreRole role = roleDao.templateOne(queryRole);
        return role;
    }


    public boolean deleteById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new PlatformException("刪除數據ID不能為空");
        }
        
        roleDao.batchDelByIds(ids);
        roleDao.batchDeleteRoleFunction(ids);
        roleDao.batchDeleteRoleMenu(ids);
        roleDao.batchDeleteUserRole(ids);
        return true;
    }
    
    
   

}
