package com.ibeetl.admin.console.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibeetl.admin.console.dao.FunctionConsoleDao;
import com.ibeetl.admin.console.dao.RoleFunctionConsoleDao;
import com.ibeetl.admin.console.web.dto.RoleDataAccessFunction;
import com.ibeetl.admin.core.dao.CoreMenuDao;
import com.ibeetl.admin.core.dao.CoreRoleMenuDao;
import com.ibeetl.admin.core.entity.CoreFunction;
import com.ibeetl.admin.core.entity.CoreMenu;
import com.ibeetl.admin.core.entity.CoreRoleFunction;
import com.ibeetl.admin.core.entity.CoreRoleMenu;
import com.ibeetl.admin.core.rbac.tree.FunctionItem;
import com.ibeetl.admin.core.service.CoreBaseService;
import com.ibeetl.admin.core.service.CorePlatformService;
import com.ibeetl.admin.core.util.PlatformException;
/**
 * @author lijiazhi
 *
 */
@Service
@Transactional
public class FunctionConsoleService  extends CoreBaseService<CoreFunction> {
	
	@Autowired
	FunctionConsoleDao functionDao;
	@Autowired
	CoreMenuDao menuDao;	
	@Autowired
	RoleFunctionConsoleDao roleFunctionConsoleDao;	
	@Autowired
	CoreRoleMenuDao sysRoleMenuDao;	
	@Autowired
	CorePlatformService platformService;
	
	
	public void queryByCondtion(PageQuery<CoreFunction> query) {
		functionDao.queryByCondtion(query);
		List<CoreFunction> list = query.getList();
		this.queryListAfter(list);
		//處理父功能名稱顯示
		FunctionItem root = platformService.buildFunction();
        for(CoreFunction function:list) {
        	Long parentId = function.getParentId();
        	String name = "";
        	if(parentId != 0) {
        		FunctionItem item = root.findChild(parentId);
            	name = item!=null?item.getName():"";
        	}
        	function.set("parentFunctionText", name);
        }
		
	}
	
	
	public Long saveFunction(CoreFunction function){
		
		functionDao.insert(function,true);
		platformService.clearFunctionCache();
		return  function.getId();
	}
	
	
	
	/** 刪除功能點，跟菜單有關聯的無法刪除,刪除功能點導緻所有緩存都需要更新
	 * @param functionId
	 * @return
	 */
	public void deleteFunction(Long functionId){
		deleteFunctionId(functionId);
		platformService.clearFunctionCache();
		
	}
	
	
	public void batchDeleteFunction(List<Long> functionIds){
		for(Long id:functionIds){
			deleteFunctionId(id);
		}
		platformService.clearFunctionCache();
	}
	
	
	
	public void updateFunction(CoreFunction function){
		functionDao.updateById(function);
		platformService.clearFunctionCache();
	}
	
	public CoreFunction getFunction(Long functionId){
		return functionDao.unique(functionId);
	}
	
	public CoreFunction getFunction(String code){
		CoreFunction query = new CoreFunction();
		query.setCode(code);
		CoreFunction db = functionDao.templateOne(query);
		return db;
	}
	
	/**
	 * 得到角色對應的所有功能點
	 * @param roleId
	 * @return
	 */
	public List<Long> getFunctionByRole(Long roleId){
		return this.roleFunctionConsoleDao.getFunctionIdByRole(roleId);
	}
	
	/**
	 * 得到角色對應的所有數據權限功能點
	 * @param roleId
	 * @return
	 */
	public List<RoleDataAccessFunction> getQueryFunctionByRole(Long roleId){
		return this.roleFunctionConsoleDao.getQueryFunctionAndRoleData(roleId);
	}
	/**
	 * 更新角色對應的功能點所有,
	 * @param roleId
	 * @param data，必須包含id,和 dataAcerssType，採用模闆更新
	 */
	public void updateFunctionAccessByRole(List<RoleDataAccessFunction> data ){
		for(RoleDataAccessFunction fun:data){
			Long roleId = fun.getRoleId();
			Long functionId = fun.getId();
			int accessType= fun.getDataAccessType();
			
			CoreRoleFunction template = new CoreRoleFunction();
			template.setRoleId(roleId);
			template.setFunctionId(functionId);
			CoreRoleFunction ret = roleFunctionConsoleDao.templateOne(template);
			if(ret!=null) {
				ret.setDataAccessType(accessType);
				roleFunctionConsoleDao.updateById(ret);
			}else {
				template.setDataAccessType(accessType);
				template.setCreateTime(new Date());
				roleFunctionConsoleDao.insert(template);
			}
			
		}
		platformService.clearFunctionCache();
	}
	
	
	/** 給角色賦予功能同時，根據賦予的功能權限，更新能訪問的菜單
	 * @param adds
	 * @param updates
	 * @param dels
	 * @return  返回增加的項的id，用於前端
	 */
	public void updateSysRoleFunction(Long roleId,List<Long> adds,List<Long> dels){
		for(Long del:dels){
			//獲得功能關聯的菜單
			CoreRoleFunction temp = new CoreRoleFunction();
			temp.setRoleId(roleId);
			temp.setFunctionId(del);
			CoreRoleFunction roleFunction = roleFunctionConsoleDao.templateOne(temp);
			if(roleFunction==null){
				throw new PlatformException("已經被刪除了RoleId="+roleId+" functionId="+del);
			}
			CoreMenu menu = queryFunctionMenu(roleFunction.getFunctionId());
			roleFunctionConsoleDao.deleteById(roleFunction.getId());
			if(menu!=null){
				//同時，需要刪除與此功能關聯的菜單
				CoreRoleMenu sysRoleMenu = querySysRoleMenu(roleFunction.getRoleId(),menu.getId());
				if(sysRoleMenu!=null){
					sysRoleMenuDao.deleteById(sysRoleMenu.getId());
				}
				
			}
		}
		
		
			
		for(Long add:adds){
			CoreRoleFunction function = new CoreRoleFunction();
			function.setCreateTime(new Date());
			function.setRoleId(roleId);
			function.setFunctionId(add);
			this.sqlManager.insert(function);
			CoreMenu menu = queryFunctionMenu(add);
			if(menu!=null){
				//同時，需要增加菜單
				CoreRoleMenu roleMenu = new CoreRoleMenu();
				roleMenu.setMenuId(menu.getId());
				roleMenu.setRoleId(roleId);
				sysRoleMenuDao.insert(roleMenu);
			}
		}
		
		//清楚緩存
		platformService.clearFunctionCache();
			
	}
	
	
	private CoreMenu queryFunctionMenu(Long functionId){
		CoreMenu query = new CoreMenu();
		query.setFunctionId(functionId);
		List<CoreMenu> menus = menuDao.template(query);
		return menus.isEmpty()?null:menus.get(0);
	}
	
	
	private CoreRoleMenu querySysRoleMenu(Long roleId,Long menuId){
		CoreRoleMenu query= new CoreRoleMenu();
		query.setMenuId(menuId);
		query.setRoleId(roleId);		
		List<CoreRoleMenu> menus = sysRoleMenuDao.template(query);
		return menus.isEmpty()?null:menus.get(0);
	}
	
	
	/**
	 * 刪除某一個功能點及其子功能，對應的role-function 需要刪除，菜單對應的function需要設定成空
	 * @param functionId
	 */
	private void deleteFunctionId(Long functionId){
		FunctionItem root = platformService.buildFunction();
		FunctionItem fun = root.findChild(functionId);
		List<FunctionItem> all = fun.findAllItem();
		//也刪除自身
		all.add(fun);
		realDeleteFunction(all);
	}
	
	private void realDeleteFunction(List<FunctionItem> all){
		List<Long> ids = new ArrayList<>(all.size());
		for(FunctionItem item:all){
			ids.add(item.getId());
			this.functionDao.deleteById(item.getId());
		}
		//刪除角色和功能的關係
		this.roleFunctionConsoleDao.deleteRoleFunction(ids);
		//設定菜單對應的功能項為空
		menuDao.clearMenuFunction(ids);
			
	}
	
	
}
