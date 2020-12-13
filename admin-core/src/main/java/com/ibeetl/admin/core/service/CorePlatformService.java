package com.ibeetl.admin.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.engine.SQLPlaceholderST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ibeetl.admin.core.dao.CoreFunctionDao;
import com.ibeetl.admin.core.dao.CoreMenuDao;
import com.ibeetl.admin.core.dao.CoreOrgDao;
import com.ibeetl.admin.core.dao.CoreRoleFunctionDao;
import com.ibeetl.admin.core.dao.CoreRoleMenuDao;
import com.ibeetl.admin.core.dao.CoreUserDao;
import com.ibeetl.admin.core.entity.CoreFunction;
import com.ibeetl.admin.core.entity.CoreMenu;
import com.ibeetl.admin.core.entity.CoreOrg;
import com.ibeetl.admin.core.entity.CoreRoleFunction;
import com.ibeetl.admin.core.entity.CoreUser;
import com.ibeetl.admin.core.rbac.DataAccessFactory;
import com.ibeetl.admin.core.rbac.tree.FunctionItem;
import com.ibeetl.admin.core.rbac.tree.MenuItem;
import com.ibeetl.admin.core.rbac.tree.OrgItem;
import com.ibeetl.admin.core.util.FunctionBuildUtil;
import com.ibeetl.admin.core.util.HttpRequestLocal;
import com.ibeetl.admin.core.util.MenuBuildUtil;
import com.ibeetl.admin.core.util.OrgBuildUtil;
import com.ibeetl.admin.core.util.PlatformException;
import com.ibeetl.admin.core.util.beetl.DataAccessFunction;
import com.ibeetl.admin.core.util.beetl.NextDayFunction;
import com.ibeetl.admin.core.util.enums.DelFlagEnum;

/**
 * 係統平臺功能訪問入口，所有方法應該支援緩存或者快速訪問
 * @author xiandafu
 */
@Service
public class CorePlatformService {


    //菜單樹，組織機構樹，功能樹緩存標記
    public static final String MENU_TREE_CACHE = "cache:core:menuTree";
    public static final String ORG_TREE_CACHE = "cache:core:orgTree";
    public static final String FUNCTION_TREE_CACHE = "cache:core:functionTree";
    //字典列錶
    public static final String DICT_CACHE_TYPE = "cache:core:dictType";
    public static final String DICT_CACHE_VALUE = "cache:core:dictValue";
    public static final String DICT_CACHE_SAME_LEVEL = "cache:core:ditcSameLevel";
    public static final String DICT_CACHE_CHILDREN = "cache:core:dictChildren";
    public static final String USER_FUNCTION_ACCESS_CACHE = "cache:core:userFunctionAccess";
    public static final String USER_FUNCTION_CHIDREN_CACHE = "ccache:core:functionChildren";
    public static final String FUNCTION_CACHE = "cache:core:function";

    public static final String USER_DATA_ACCESS_CACHE = "cache:core:userDataAccess";
    public static final String USER_MENU_CACHE = "cache:core:userMenu";

    /*當前用戶會話*/
    public static final String ACCESS_CURRENT_USER = "core:user";
    /*當前登入用戶所在部門*/
    public static final String ACCESS_CURRENT_ORG = "core:currentOrg";
    /*用戶可選部門*/
    public static final String ACCESS_USER_ORGS = "core:orgs";

    public static final String ACCESS_SUPPER_ADMIN = "admin";
    
	@Autowired
	HttpRequestLocal httpRequestLocal;


    @Autowired
    CoreRoleFunctionDao roleFunctionDao;

    @Autowired
    CoreRoleMenuDao sysRoleMenuDao;

    @Autowired
    CoreOrgDao sysOrgDao;

    @Autowired
    CoreRoleFunctionDao sysRoleFunctionDao;

    @Autowired
    CoreMenuDao sysMenuDao;

    @Autowired
    CoreUserDao sysUserDao;

    @Autowired
    CoreFunctionDao sysFunctionDao;

    @Autowired
    SQLManager sqlManager;

    @Autowired
    DataAccessFunction dataAccessFunction;

    @Autowired
    CorePlatformService self;
    @Autowired
    DataAccessFactory dataAccessFactory;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        SQLPlaceholderST.textFunList.add("function");
        //sql語句裏帶有此函數來判斷數據權限
        sqlManager.getBeetl().getGroupTemplate().registerFunction("function", dataAccessFunction);
        sqlManager.getBeetl().getGroupTemplate().registerFunction("nextDay", new NextDayFunction());
    }


    public CoreUser getCurrentUser() {
    	checkSession();
        CoreUser user =  (CoreUser) httpRequestLocal.getSessionValue(ACCESS_CURRENT_USER);
        return user;

    }
    
    public void changeOrg(Long orgId) {
    		List<CoreOrg> orgs = this.getCurrentOrgs();
    		for(CoreOrg org:orgs) {
    			if(org.getId().equals(orgId)) {
    			 	httpRequestLocal.setSessionValue(CorePlatformService.ACCESS_CURRENT_ORG, org);
    			}
    		}
	}


    public Long getCurrentOrgId() {
    	checkSession();
        CoreOrg org = (CoreOrg) httpRequestLocal.getSessionValue(ACCESS_CURRENT_ORG);
        return org.getId();

    }
    
    public CoreOrg getCurrentOrg() {
    	checkSession();
        CoreOrg org = (CoreOrg) httpRequestLocal.getSessionValue(ACCESS_CURRENT_ORG);
        return org;

    }

    public List<CoreOrg> getCurrentOrgs() {
        List<CoreOrg> orgs = (List<CoreOrg>) httpRequestLocal.getSessionValue(ACCESS_USER_ORGS);
        return orgs;

    }
    
    protected void checkSession() {
    	  CoreOrg org = (CoreOrg) httpRequestLocal.getSessionValue(ACCESS_CURRENT_ORG);
          if(org==null) {
          	throw new PlatformException("會話過期，重新登入");
          }
    }

    public void setLoginUser(CoreUser user, CoreOrg currentOrg, List<CoreOrg> orgs) {
    	httpRequestLocal.setSessionValue(CorePlatformService.ACCESS_CURRENT_USER, user);
    	httpRequestLocal.setSessionValue(CorePlatformService.ACCESS_CURRENT_ORG, currentOrg);
    	httpRequestLocal.setSessionValue(CorePlatformService.ACCESS_USER_ORGS, orgs);

    }

    public MenuItem getMenuItem(long userId, long orgId) {
        CoreUser user = this.sysUserDao.unique(userId);
        if (this.isSupperAdmin(user)) {
            return self.buildMenu();
        }
        Set<Long> allows = self.getCurrentMenuIds(userId, orgId);
        MenuItem menu = this.buildMenu();
        menu.filter(allows);
        return menu;

    }

    public OrgItem getUserOrgTree() {
        if (this.isCurrentSupperAdmin()) {
            OrgItem root = self.buildOrg();
            return root;
        }
        OrgItem current = getCurrentOrgItem();
        //搞不懂，忘記了
        OrgItem item= dataAccessFactory.getUserOrgTree(current);
       
        return item;

    }

   


    @Cacheable(FUNCTION_CACHE)
    public CoreFunction getFunction(String functionCode) {

        return sysFunctionDao.getFunctionByCode(functionCode);
    }


    public OrgItem getCurrentOrgItem() {
        //@TODO 無法緩存orgItem，因為組織機構在調整
        OrgItem root = buildOrg();
        OrgItem item = root.findChild(getCurrentOrgId());
        if (item == null) {
            throw new PlatformException("未找到組織機構");
        }
        return item;
    }


    /**
     * 判斷用戶是否是超級管理員
     * @param user
     * @return
     */
    public boolean isSupperAdmin(CoreUser user) {
        return user.getCode().startsWith(ACCESS_SUPPER_ADMIN);
    }

    public boolean isCurrentSupperAdmin() {
        CoreUser user = this.getCurrentUser();
        return isSupperAdmin(user);
    }

    public boolean isAllowUserName(String name){
    	return !name.startsWith(ACCESS_SUPPER_ADMIN);
    }

    /**
     * 獲取用戶在指定功能點的數據權限配置，如果冇有，返回空集合
     * @param userId
     * @param orgId
     * @param fucntionCode
     * @return
     */
    @Cacheable(USER_DATA_ACCESS_CACHE)
    public List<CoreRoleFunction> getRoleFunction(Long userId, Long orgId, String fucntionCode) {
        List<CoreRoleFunction> list = sysRoleFunctionDao.getRoleFunction(userId, orgId, fucntionCode);
        return list;
    }


    /**
     * 當前用戶是否能訪問功能，用於後臺功能驗證,functionCode 目前隻支援二級域名方式，不支援更多級別
     * @param functionCode "user.add","user"
     * @return
     */
    
    

    @Cacheable(USER_FUNCTION_ACCESS_CACHE)
    public boolean canAcessFunction(Long userId, Long orgId, String functionCode) {

        CoreUser user = getCurrentUser();
        if (user.getId() == userId && isSupperAdmin(user)) {
            return true;
        }
        String str = functionCode;
        List<CoreRoleFunction> list = sysRoleFunctionDao.getRoleFunction(userId, orgId, str);
        boolean canAccess = !list.isEmpty();
        if (canAccess) {
            return true;
        }else{
        	return false;
        }

        


    }

    /**
     * 當前功能的子功能，如果有，則頁麵需要做按鈕級別的過濾
     * @param userId
     * @param orgId
     * @param parentFunction 菜單對應的function
     * @return
     */
    @Cacheable(USER_FUNCTION_CHIDREN_CACHE)
    public List<String> getChildrenFunction(Long userId, Long orgId, String parentFunction) {
        CoreFunction template = new CoreFunction();
        template.setCode(parentFunction);
        List<CoreFunction> list = sysFunctionDao.template(template);
        if (list.size() != 1) {
            throw new PlatformException("訪問權限未配置");
        }

        Long id = list.get(0).getId();
        return sysRoleFunctionDao.getRoleChildrenFunction(userId, orgId, id);

    }


    /**
     * 查詢當前用戶有用的菜單項目，可以在隨後驗證是否能顯示某項菜單
     * @return
     */
    @Cacheable(USER_MENU_CACHE)
    public Set<Long> getCurrentMenuIds(Long userId, Long orgId) {
        List<Long> list = sysRoleMenuDao.queryMenuByUser(userId, orgId);
        return new HashSet<Long>(list);
    }


    /**
     * 驗證菜單是否能被顯示
     * @param item
     * @param allows
     * @return
     */
    public boolean canShowMenu(CoreUser user, MenuItem item, Set<Long> allows) {
        if (isSupperAdmin(user)) {
            return true;
        }
        return allows.contains(item.getData().getId());
    }

    @Cacheable(MENU_TREE_CACHE)
    public MenuItem buildMenu() {
        List<CoreMenu> list = sysMenuDao.allMenuWithURL();
        return MenuBuildUtil.buildMenuTree(list);

    }

    @Cacheable(ORG_TREE_CACHE)
    public OrgItem buildOrg() {
       
       
        CoreOrg root = sysOrgDao.getRoot();
        OrgItem rootItem = new OrgItem(root);
        CoreOrg org = new CoreOrg();
        org.setDelFlag(DelFlagEnum.NORMAL.getValue());
        List<CoreOrg> list = sysOrgDao.template(org);
        OrgBuildUtil.buildTreeNode(rootItem,list);
        //集團
        return rootItem;

    }

    @Cacheable(FUNCTION_TREE_CACHE)
    public FunctionItem buildFunction() {
        List<CoreFunction> list = sysFunctionDao.all();
        return FunctionBuildUtil.buildOrgTree(list);

    }
    /**
     * 用戶信息被管理員修改，重置會話，讓用戶操作重新登入
     * @param name
     */
    public void restUserSession(String name){
    	//TODO
    }


    @CacheEvict(cacheNames = {FUNCTION_CACHE, FUNCTION_TREE_CACHE, /*功能點本身緩存*/
            MENU_TREE_CACHE, USER_MENU_CACHE,/*功能點關聯菜單緩存*/
            USER_FUNCTION_ACCESS_CACHE, USER_FUNCTION_CHIDREN_CACHE, USER_DATA_ACCESS_CACHE,/*功能點相關權限緩存*/}, allEntries = true)
    public void clearFunctionCache() {
        //冇有做任何事情，交給spring cache來處理了
    }


    @CacheEvict(cacheNames = {CorePlatformService.MENU_TREE_CACHE, CorePlatformService.USER_MENU_CACHE}, allEntries = true)
    public void clearMenuCache() {
        //冇有做任何事情，交給spring cache來處理了
    }

    @CacheEvict(cacheNames = {CorePlatformService.DICT_CACHE_CHILDREN,CorePlatformService.DICT_CACHE_SAME_LEVEL,CorePlatformService.DICT_CACHE_TYPE,CorePlatformService.DICT_CACHE_VALUE}, allEntries = true)
    public void clearDictCache() {
    }
    
    @CacheEvict(cacheNames = {CorePlatformService.ORG_TREE_CACHE}, allEntries = true)
    public void clearOrgCache() {
    }

    /**
     * 得到類型為係統的菜單，通常就是根菜單下麵
     * @return
     */
    public List<MenuItem> getSysMenu() {
        MenuItem root = buildMenu();
        List<MenuItem> list = root.getChildren();
        for (MenuItem item : list) {
            if (!item.getData().getType() .equals(CoreMenu.TYPE_SYSTEM)) {
                throw new IllegalArgumentException("本係統冇有係統模塊");
            }
        }
        return list;
    }

    /**
     * 得到菜單的子菜單
     * @param menuId
     * @return
     */
    public List<MenuItem> getChildMenu(Long menuId) {
        MenuItem root = buildMenu();
        List<MenuItem> list = root.findChild(menuId).getChildren();
        return list;
    }
    
    
  


}
