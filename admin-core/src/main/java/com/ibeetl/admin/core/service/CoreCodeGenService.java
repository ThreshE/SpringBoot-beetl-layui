package com.ibeetl.admin.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beetl.sql.core.JavaType;
import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.ClassDesc;
import org.beetl.sql.core.db.ColDesc;
import org.beetl.sql.core.db.MetadataManager;
import org.beetl.sql.core.db.TableDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibeetl.admin.core.entity.CoreFunction;
import com.ibeetl.admin.core.entity.CoreMenu;
import com.ibeetl.admin.core.gen.model.Attribute;
import com.ibeetl.admin.core.gen.model.Entity;

/**
 * 代碼生成，用於根據錶或者視圖生成entity，mapper，service，conroller
 * 未來可以生成swagger api，界麵
 * @author xiandafu
 */
@Service
public class CoreCodeGenService {
	@Autowired
	SQLManager sqlManager;
	@Autowired
	CorePlatformService platformService;
	
	Log log = LogFactory.getLog(CoreCodeGenService.class);
	
	public void refresh() {
		sqlManager.refresh();
	}
	
	public List<Entity> getAllEntityInfo(){
		MetadataManager meta = sqlManager.getMetaDataManager();
		Set<String> set = meta.allTable();
		List<Entity> list = new ArrayList<Entity>();
		for(String table:set) {
			list.add(getEntitySimpleInfo(table));
		}
		return list;
	}
	
	public Entity getEntitySimpleInfo(String table) {
		MetadataManager meta = sqlManager.getMetaDataManager();
		NameConversion nc = sqlManager.getNc();
		TableDesc tableDesc = meta.getTable(table);
		if(tableDesc==null) {
			return null;
		}
		ClassDesc classDesc = tableDesc .getClassDesc(nc);
		Entity e = new Entity();
		e.setName(nc.getClassName(table));
		e.setComment(tableDesc.getRemark());
		e.setTableName(table);
		return e;
	}
	
	public Entity getEntityInfo(String table) {
		MetadataManager meta = sqlManager.getMetaDataManager();
		NameConversion nc = sqlManager.getNc();
		TableDesc tableDesc = meta.getTable(table);
		if(tableDesc==null) {
			return null;
		}
		ClassDesc classDesc = tableDesc .getClassDesc(nc);
		Entity e = new Entity();
		e.setName(nc.getClassName(table));
		e.setComment(tableDesc.getRemark());
		e.setTableName(table);
		e.setCode(getEntityCode(e.getName()));
		
		Set<String> cols = tableDesc.getCols();
		ArrayList<Attribute> attrs = new ArrayList<Attribute>();
		int i=1;
		for(String col:cols) {
			ColDesc desc = tableDesc.getColDesc(col);
			Attribute attr = new Attribute();
			attr.setColName(col);
			attr.setName(nc.getPropertyName(col));
			if(tableDesc.getIdNames().contains(col)) {
				//TODO,代碼生成實際上用了一個Id，因此具備聯合主鍵的，不應該生成代碼
				attr.setId(true);
				e.setIdAttribute(attr);
			}
			attr.setComment(desc.remark);
			String type = JavaType.getType(desc.sqlType, desc.size, desc.digit);
			if(type.equals("Double")){
				type = "BigDecimal";
			}		
			if(type.equals("Timestamp")){
				type ="Date";
			}
			attr.setJavaType(type);
			setGetDisplayName(attr);
			attrs.add(attr);
			
			
			
		}
		e.setList(attrs);
		
		return e;
	}
	/**
	 * 
	 * @param data
	 * @param urlBase
	 * @return  增刪改查中的查
	 */
	public Long insertFunction(Entity data,String urlBase){
		String  preffix =  urlBase.replace('/', '.');
		String functionCode = preffix+"."+data.getCode();
		String indexFunctonCode = functionCode+".query";
		CoreFunction query = new CoreFunction();
		query.setCode(indexFunctonCode);
		Object o = sqlManager.templateOne(query);
		if(o != null){
			return -1l;
		}
		
		//設定父功能點
		CoreFunction rootFunction = new CoreFunction();
		rootFunction.setName(data.getDisplayName());
		rootFunction.setCode(functionCode);
		rootFunction.setCreateTime(new Date());
		rootFunction.setParentId(0L);
		rootFunction.setType("FN0");
		sqlManager.insert(rootFunction,true);
		Long parentId =rootFunction.getId();
		
		//設定曾刪改查功能點
		CoreFunction indexFunction = new CoreFunction();
		indexFunction.setName("查詢"+data.getDisplayName());
		indexFunction.setCode(indexFunctonCode);
		indexFunction.setCreateTime(new Date());
		indexFunction.setParentId(parentId);
		indexFunction.setAccessUrl("/"+urlBase+"/"+data.getCode()+"/index.do");
		//設定為查詢功能
		indexFunction.setType("FN1");
		sqlManager.insert(indexFunction,true);
		
		
		CoreFunction  upateFunction = new CoreFunction();
		String updateFunctonCode = functionCode+".edit";
		upateFunction.setName("修改"+data.getDisplayName());
		upateFunction.setCode(updateFunctonCode);
		upateFunction.setCreateTime(new Date());
		upateFunction.setParentId(parentId);
		upateFunction.setType("FN0");
		sqlManager.insert(upateFunction,true);
		
		CoreFunction  addFunction = new CoreFunction();
		String addFunctionCode = functionCode+".add";
		addFunction.setName("添加"+data.getDisplayName());
		addFunction.setCode(addFunctionCode);
		addFunction.setCreateTime(new Date());
		addFunction.setParentId(parentId);
		addFunction.setType("FN0");
		sqlManager.insert(addFunction,true);
		
		
		CoreFunction  delFunction = new CoreFunction();
		String delFunctionCode = functionCode+".delete";
		delFunction.setName("刪除"+data.getDisplayName());
		delFunction.setCode(delFunctionCode);
		delFunction.setCreateTime(new Date());
		delFunction.setParentId(parentId);
		delFunction.setType("FN0");
		sqlManager.insert(delFunction,true);
		
		//刷新緩存
		platformService.clearFunctionCache();
		
		return indexFunction.getId();
	}
	
	public boolean insertMenu(Long functionId,Entity data,String urlBase){
		CoreMenu query = new CoreMenu();
		query.setCode("代碼生成導航");
		query.setType("MENU_N");
		CoreMenu menu = this.sqlManager.templateOne(query);
		if(menu==null) {
			log.warn("未找到對應的父菜單:"+query.getCode());
			return false ;
		}
		Long parentId = menu.getId();
		CoreMenu newMenu = new CoreMenu();
		newMenu.setCode(data.getName()+".Manager");
		newMenu.setName(data.getDisplayName()+"管理");
		newMenu.setParentMenuId(parentId);
		newMenu.setFunctionId(functionId);
		newMenu.setType("MENU_M");
		//任意設定一個順序
		newMenu.setSeq(3);
		this.sqlManager.insert(newMenu);
		this.platformService.clearMenuCache();
		return true;
	}

	
	//根據類名提供一個變數名
	private String getEntityCode(String s) {
		//找到最後一個大寫字母，以此為變數名
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(s.charAt(0)))
					.append(s.substring(1)).toString();
		
	}
	/*根據數據庫註釋來判斷顯示名稱*/
	private void setGetDisplayName(Attribute attr) {
		String comment = attr.getComment();
		if(StringUtils.isEmpty(comment)) {
			attr.setDisplayName(attr.getName());
			return ;
		}
		String displayName = null;
		int index = comment.indexOf(",");
		if(index!=-1) {
			displayName =  comment.substring(0,index);
			attr.setDisplayName(displayName);
		}else {
			attr.setDisplayName(comment);
		}
	}
 
}
