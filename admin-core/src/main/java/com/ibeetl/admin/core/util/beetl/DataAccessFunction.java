package com.ibeetl.admin.core.util.beetl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.beetl.sql.core.engine.SQLParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibeetl.admin.core.entity.CoreRoleFunction;
import com.ibeetl.admin.core.entity.CoreUser;
import com.ibeetl.admin.core.rbac.DataAccess;
import com.ibeetl.admin.core.rbac.DataAccessFactory;
import com.ibeetl.admin.core.rbac.DataAccessResullt;
import com.ibeetl.admin.core.service.CorePlatformService;
import com.ibeetl.admin.core.util.FunctionLocal;

/**
 * 數據權限拚sql，配合DataAccessFactory
 * @author lijiazhi
 *
 */
@Component
public class DataAccessFunction implements Function {
	
	Log log = LogFactory.getLog(DataAccessFunction.class);
	
	@Autowired
	CorePlatformService platFormService;
	@Autowired
	DataAccessFactory dataAccessFactory;
	
	private static Map defaultTargets = new HashMap();
	static{
		//數據庫預設的跟組織和用戶相關字段
		defaultTargets.put("org", "org_id");
		defaultTargets.put("user", "user_id");
	}
	private static final String 	SQL_MY_DATA = "user_id=? ";
	private static final String 	SQL_MY_ORG_DATA = "org_id=? ";
	

	
	public Object call(Object[] paras, Context ctx){
		//項目初期，總是返回1==1，避免數據權限帶來的麻煩 
		CoreUser user = platFormService.getCurrentUser(); 
		//{"org":"org_id","user","user_id"}
		Map targets  = this.defaultTargets;
		//用戶調用conroller 結果"user.view"
		String functionCode  = FunctionLocal.get();	
		
		if(paras.length==1){
			Object o = paras[0];
			if(o instanceof String){
				functionCode = (String)o;
			}else if(o instanceof Map){
//				targets  = (Map)paras[1];
				targets.putAll((Map)paras[1]);
			}
		}else if(paras.length==2){
			functionCode = (String)paras[0];
//			targets  = (Map)paras[1];
			targets.putAll((Map)paras[1]);
		}
		
		
		
		if(platFormService.isSupperAdmin(user)){
			return " 1=1 /* admin */ ";
		}
		Long currentOrgId = platFormService.getCurrentOrgId();
		
		List<CoreRoleFunction> roleFuns = platFormService.getRoleFunction(user.getId(),currentOrgId,functionCode);
		if(roleFuns.isEmpty()){
			//如果冇有配置數據權限，是1=1,因此為角色指定功能的時候，需要設定數據權限，否則查詢到所有數據
			return " 1=0 /* empty data access */ ";
		}
		
		
		
		List<Object> list = (List<Object>)ctx.getGlobal("_paras");
		StringBuilder sb = new StringBuilder(" (");
		//數據權限範圍劃定
		boolean hasAppend = false;
		for(int i=0;i<roleFuns.size();i++){
			CoreRoleFunction fun = roleFuns.get(i);
			Integer accessType = fun.getDataAccessType();
			if(accessType==null){
				continue;
			}
			if(hasAppend){
				sb.append(" or ");
			}
			hasAppend = true;
			DataAccess data = dataAccessFactory.getDataAccess(accessType);
			DataAccessResullt ret = data.getOrg(user.getId(), currentOrgId);
			
			switch(ret.getStatus()){
			case NoneOrg:{
				sb.append(targets.get("org")+" in (-1) ");
				break;
			}
			case AllOrg:{
				//sql 不包含組織機構過濾信息
				sb.append(" 1=1 /* AllOrg */ ");
				break;
			}
			case OnlyUser:{
				List<Long> ids = ret.getUserIds();
				sb.append(targets.get("user"));
				if(ids.size()==0){
					sb.append("=-1/*指定用戶，但冇有候選用戶*/");
					continue;
				}
					
				if(ids.size()==1){
					sb.append(" =? ");
					list.add(new SQLParameter(ids.get(0)));
					continue;
				}
				sb.append(" in (");
				for(int z=0;z<ids.size();z++){
					sb.append(" ? ");
					list.add(new SQLParameter(ids.get(z)));
					if(z!=ids.size()-1){
						sb.append(",");
					}
				}
				sb.append(") ");
				break;
			
			}
			case OnlyOrg:{
				List<Long> ids = ret.getOrgIds();
				sb.append(targets.get("org"));
				if(ids.size()==0){
					sb.append("=-1/*指定機構，但冇有候選機構*/");
					continue;
				}
					
				if(ids.size()==1){
					sb.append(" =? ");
					list.add(new SQLParameter(ids.get(0)));
					continue;
				}
				sb.append(" in (");
				for(int z=0;z<ids.size();z++){
					sb.append("?");
					list.add(new SQLParameter(ids.get(z)));
					if(z!=ids.size()-1){
						sb.append(",");
					}
				}
				sb.append(") ");
				break;
			}
			default:{
				log.warn("錯誤的"+ret.getStatus().toString());
				throw new UnsupportedOperationException(ret.getStatus().toString());
			}
			}

			
		}
		sb.append(") ");
		//Joe 修複在冇設定數據權限時查詢出現的bug
		sb = sb.toString().equals(" () ") ? sb.replace(0," () ".length()," 1=0 ") : sb;
		
		return sb.toString();
	}
	

	
	
}
