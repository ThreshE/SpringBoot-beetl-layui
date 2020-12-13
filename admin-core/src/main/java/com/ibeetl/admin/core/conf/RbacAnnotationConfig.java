package com.ibeetl.admin.core.conf;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibeetl.admin.core.annotation.Function;
import com.ibeetl.admin.core.entity.CoreAudit;
import com.ibeetl.admin.core.entity.CoreFunction;
import com.ibeetl.admin.core.entity.CoreUser;
import com.ibeetl.admin.core.service.CoreAuditService;
import com.ibeetl.admin.core.service.CorePlatformService;
import com.ibeetl.admin.core.util.FunctionLocal;
import com.ibeetl.admin.core.util.HttpRequestLocal;
import com.ibeetl.admin.core.util.PlatformException;

@Aspect
@Component
public class RbacAnnotationConfig {
	@Autowired
	CorePlatformService platformService;
	@Autowired
	CoreAuditService sysAuditService;
	@Autowired
	HttpRequestLocal httpRequestLocal;

	@Autowired
	Environment env;

	ObjectMapper jsonMapper = new ObjectMapper();
	private final Log log = LogFactory.getLog(this.getClass());

	@org.aspectj.lang.annotation.Around("within(@org.springframework.stereotype.Controller *) && @annotation(function)")
	public Object functionAccessCheck(final ProceedingJoinPoint pjp, Function function) throws Throwable {
		// debug
		String funCode = null;
		CoreUser user = null;
		Method m = null;
		try {

			if (function != null) {
				funCode = function.value();
				user = platformService.getCurrentUser();
				Long orgId = platformService.getCurrentOrgId();
				boolean access = platformService.canAcessFunction(user.getId(), orgId, funCode);
				if (!access) {
					log.warn(jsonMapper.writeValueAsString(user) + "試圖訪問未授權功能 " + funCode);
					throw new PlatformException("試圖訪問未授權功能");
				}
				FunctionLocal.set(funCode);
			}

			Object o = pjp.proceed();
//			if (function != null) {
//				MethodSignature ms = (MethodSignature)pjp.getSignature();
//			    m = ms.getMethod();
//				createAudit(funCode,function.name(), user, true, "",m);
//			}
			return o;

		} catch (Throwable e) {
			if (function != null) {
				createAudit(funCode, function.name(),user, false, e.getMessage(),m);
			}
			throw e;
		}

	}

	private void createAudit(String functionCode, String functionName,CoreUser user, boolean success, String msg, Method m) {
		boolean enable = env.getProperty("audit.enable", Boolean.class, false);
		if (!enable) {
			return;
		}
		if(filter(m,functionCode)){
			return ;
		}

		CoreAudit audit = new CoreAudit();
		if(StringUtils.isEmpty(functionName)) {
		    CoreFunction fun = this.platformService.getFunction(functionCode);

	        if (fun == null) {
	            // 冇有在數據庫定義，但寫在代碼裏了
	            log.warn(functionCode + " 未在數據庫裏定義");
	            functionName = "未定義";
	        } else {
	            functionName = fun.getName();
	        }
		}
		audit.setCreateTime(new Date());
		audit.setFunctionCode(functionCode);
		audit.setFunctionName(functionName);
		audit.setUserId(user.getId());
		audit.setSuccess(success ? 1 : 0);
		audit.setUserName(user.getName());
		audit.setMessage(msg);

		audit.setIp(httpRequestLocal.getRequestIP(env.getProperty("localhost.ip.enable", Boolean.class, false)));
		sysAuditService.save(audit);
	}

	private boolean filter(Method m,String functionCode){
		if(functionCode.startsWith("audit.")){
			return true;
		}
		String uri = httpRequestLocal.getRequestURI();
		if(uri!=null&&uri.endsWith("/index/condition.json")){

			return true ;
		}else{
			return false;
		}
	}

}
