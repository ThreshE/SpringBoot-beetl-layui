package com.ibeetl.admin.core.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibeetl.admin.core.rbac.DataAccessFactory;
import com.ibeetl.admin.core.rbac.da.DefaultDataAccessFactory;

/**
 * 數據權限，可以自動裝配
 */
@Configuration
public class RbacDataAccessConfig {
	@ConditionalOnMissingBean(DataAccessFactory.class)
	@Bean
	public DataAccessFactory dataAccessFatory(ApplicationContext  applicationContext) {
		return new DefaultDataAccessFactory(applicationContext);
	}
}
