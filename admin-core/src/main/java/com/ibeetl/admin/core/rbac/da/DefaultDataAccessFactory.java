package com.ibeetl.admin.core.rbac.da;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.ibeetl.admin.core.rbac.DataAccess;
import com.ibeetl.admin.core.rbac.DataAccessFactory;
import com.ibeetl.admin.core.rbac.tree.OrgItem;
/**
 * 係統提供的數據訪問權限設定，適合大多數情況。如果隻是小型公司，隻需要配置數據字典組織機構類型和數據訪問類型為係統預設的子集即可
 * 
 * @author xiandafu
 *
 */
public class DefaultDataAccessFactory implements DataAccessFactory {
	
	
	public static final String GROUP_TYPE = "ORGT0"; // 集團
    public static final String COMPANY_TYPE = "ORGT1"; // 公司
    public static final String DEPARTMENT_TYPE = "ORGT2"; // 部門
    public static final String TEAM_TYPE = "ORGT3"; //小組
    public static final String PARENT_CORP_TYPE = "ORGT99"; // 母公司

	
	protected  Map<Integer,DataAccess> map = new HashMap<Integer,DataAccess> ();
	protected ApplicationContext  applicationContext;
	public  DefaultDataAccessFactory(ApplicationContext  applicationContext) {
		this.applicationContext = applicationContext;
		DataAccess da1 = applicationContext.getBean(OwnerDataAccess.class);
		DataAccess da2 = applicationContext.getBean(SameCompanyAllDataAccess.class);
		DataAccess da3 = applicationContext.getBean(SameOrgDataAccess.class);
		DataAccess da4 = applicationContext.getBean(SameDeparmentAllDataAccess.class);
		DataAccess da5 = applicationContext.getBean(AllGroupAccess.class);
		DataAccess da6 = applicationContext.getBean(GroupDataAccess.class);
		DataAccess da7 = applicationContext.getBean(ParentCorpDataAccess.class);
		DataAccess da8 = applicationContext.getBean(GroupOnlyDataAccess.class);
		
		this.addDataAccessType(da1);
		this.addDataAccessType(da2);
		this.addDataAccessType(da3);
		this.addDataAccessType(da4);
		this.addDataAccessType(da5);
		this.addDataAccessType(da6);
		this.addDataAccessType(da6);
		this.addDataAccessType(da7);
		this.addDataAccessType(da8);
		
	}
	
	public DataAccess getDataAccess(Integer type){
		return map.get(type);
	}
	
	public List<DataAccess> all(){
		return new ArrayList<DataAccess>(map.values());
	}
	
	public  void addDataAccessType(DataAccess da){
		if(da.getType()>255){
			throw new RuntimeException("數據權限類型支援1-255");
		}
		map.put(da.getType(), da);
	}

	@Override
	public OrgItem getUserOrgTree(OrgItem item) {
		  if (item != null ) {
			  	String orgType = item.getOrg().getType();
			  	if(orgType.equals(COMPANY_TYPE)||orgType.equals(PARENT_CORP_TYPE)||orgType.equals(GROUP_TYPE)) {
			  		return item;
			  	}
	           
	        }

	    return null;
	}


	

}
