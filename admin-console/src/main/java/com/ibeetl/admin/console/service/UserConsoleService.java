package com.ibeetl.admin.console.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibeetl.admin.console.dao.UserConsoleDao;
import com.ibeetl.admin.console.exception.DeletedException;
import com.ibeetl.admin.console.exception.NoResourceException;
import com.ibeetl.admin.console.web.dto.UserExcelExportData;
import com.ibeetl.admin.console.web.query.UserRoleQuery;
import com.ibeetl.admin.core.conf.PasswordConfig.PasswordEncryptService;
import com.ibeetl.admin.core.entity.CoreDict;
import com.ibeetl.admin.core.entity.CoreUser;
import com.ibeetl.admin.core.entity.CoreUserRole;
import com.ibeetl.admin.core.file.FileService;
import com.ibeetl.admin.core.rbac.tree.OrgItem;
import com.ibeetl.admin.core.service.CoreBaseService;
import com.ibeetl.admin.core.service.CoreDictService;
import com.ibeetl.admin.core.service.CorePlatformService;
import com.ibeetl.admin.core.util.PlatformException;
import com.ibeetl.admin.core.util.enums.CoreDictType;
import com.ibeetl.admin.core.util.enums.DelFlagEnum;
import com.ibeetl.admin.core.util.enums.GeneralStateEnum;

@Service
@Transactional
public class UserConsoleService extends CoreBaseService<CoreUser> {

	@Autowired
	UserConsoleDao userDao;
	
	
	@Autowired
    FileService fileService;

	@Autowired
	PasswordEncryptService passwordEncryptService;
	@Autowired
	CoreDictService dictService;

	
	@Autowired
	CorePlatformService platformService;
	/**
	 * 根據條件查詢
	 *
	 * @param query
	 */
	public void queryByCondtion(PageQuery<CoreUser> query) {
		PageQuery<CoreUser> ret = userDao.queryByCondtion(query);
		queryListAfter(ret.getList());
	}

	/**
	 * 插入一條用戶數據
	 *
	 * @param user
	 */
	public void saveUser(CoreUser user) {
		CoreUser query = new CoreUser();
		query.setCode(user.getCode());
		CoreUser dbUser = userDao.templateOne(query);
		if (dbUser != null) {
			throw new PlatformException("保存用戶信息失敗,用戶已經存在");
		}
		user.setCreateTime(new Date());
		user.setState(GeneralStateEnum.ENABLE.getValue());
		user.setPassword(passwordEncryptService.password(user.getPassword()));
		user.setDelFlag(DelFlagEnum.NORMAL.getValue());
		userDao.insert(user, true);
		if(StringUtils.isNotEmpty(user.getAttachmentId())){
		    //更新附件詳細信息,關聯到這個用戶
		    fileService.updateFile(user.getAttachmentId(), User.class.getSimpleName(), String.valueOf(user.getId()));
		}
		

	}

	/**
	 * 根據用戶id查詢一條數據
	 *
	 * @param userId
	 */
	public CoreUser queryUserById(Long userId) {
		return userDao.unique(userId);
	}

	/**
	 * 更新用戶 隻更新不為空的字段
	 *
	 * @param user
	 * @return
	 */
	public int updateSysUser(CoreUser user) {
		return userDao.updateTemplateById(user);
	}

	/**
	 * 刪除用戶
	 *
	 * @param userId
	 *            用戶id
	 */
	public void delSysUser(Long userId) {
		CoreUser user = queryUserById(userId);
		if (user == null) {
			throw new NoResourceException("用戶不存在!");
		}
		if (user.getDelFlag() == DelFlagEnum.DELETED.getValue()) {
			throw new DeletedException("用戶已被刪除!");
		}
		user = new CoreUser();
		user.setId(userId);
		user.setDelFlag(DelFlagEnum.DELETED.getValue());
		userDao.updateTemplateById(user);
	}

	/**
	 * 批量刪除用戶 <br/>
	 * 軟刪除 標記用戶刪除狀態
	 *
	 * @param userIds
	 *            用戶id
	 */
	public void batchDelSysUser(List<Long> userIds) {
		try {
			userDao.batchDelUserByIds(userIds);
		} catch (Exception e) {
			throw new PlatformException("批量刪除用戶失敗", e);
		}
	}

	/**
	 * 批量停用用戶 <br/>
	 * 標記用戶狀態 停用
	 *
	 * @param userIds
	 *            用戶id
	 */
	public void batchUpdateUserState(List<Long> userIds, GeneralStateEnum stateEnum) {
		userDao.batchUpdateUserState(userIds, stateEnum);
	}

	/**
	 * 重置用戶密碼
	 *
	 * @param uId
	 * @param password
	 */
	public int resetPassword(Long id, String password) {
		CoreUser user = new CoreUser();
		user.setId(id);
		user.setPassword(passwordEncryptService.password(password));
		user.setUpdateTime(new Date());
		return userDao.updateTemplateById(user);
	}

	public List<CoreUserRole> getUserRoles(UserRoleQuery roleQuery) {
		return userDao.queryUserRole(roleQuery.getUserId(), roleQuery.getOrgId(), roleQuery.getRoleId());
	}

	public void deleteUserRoles(List<Long> ids) {
		// 考慮到這個操作較少使用，就不做批處理優化了
		for (Long id : ids) {
			sqlManager.deleteById(CoreUserRole.class, id);
		}

	}

	public void saveUserRole(CoreUserRole userRole) {

		long queryCount = sqlManager.templateCount(userRole);

		if (queryCount > 0) {
			throw new PlatformException("已存在用戶角色關係");
		}
		sqlManager.insert(userRole);
	}
	
	public List<UserExcelExportData> queryExcel(PageQuery<CoreUser> query) {
		PageQuery<CoreUser> ret = userDao.queryByCondtion(query);
		List<CoreUser> list = ret.getList();
		OrgItem orgRoot = platformService.buildOrg();
		List<UserExcelExportData> items = new ArrayList<>();
		for(CoreUser user:list) {
			UserExcelExportData userItem = new UserExcelExportData();
			userItem.setCode(user.getCode());
			userItem.setId(user.getId());
			userItem.setName(user.getName());
			CoreDict dict = dictService.findCoreDict(CoreDictType.USER_STATE,user.getState());
			userItem.setStateText(dict.getName());
			
			if(StringUtils.isNotEmpty(user.getJobType1())){
				dict = dictService.findCoreDict("job_type",user.getJobType1());
				userItem.setJobType1Text(dict.getName());
			}
			
			String orgName = orgRoot.findChild(user.getOrgId()).getName();
			userItem.setOrgText(orgName);
			items.add(userItem);
			
		}
		return items;
		
	}

}
