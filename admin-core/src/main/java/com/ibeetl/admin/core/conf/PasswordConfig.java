package com.ibeetl.admin.core.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibeetl.admin.core.conf.PasswordConfig.PasswordEncryptService;

/**
 * 描述: 密碼工具，係統預設採用明文存儲,可以自動裝配一個加密的
 *
 * @author : xiandafu
 */
@Configuration
@ConditionalOnMissingBean(PasswordEncryptService.class)
public class PasswordConfig {

	
	public static interface PasswordEncryptService{
		public String password(String pwd);
	}
	
	
	public static class DefaultEncryptBean implements PasswordEncryptService {

		@Override
		public String password(String pwd) {
			// 採用明文，係統應該提供自己的EncryptBean實現以代替預設
			return pwd;
		}
		
	}
	
	@Bean
	public PasswordEncryptService passwordEncryptBean(){
		return new DefaultEncryptBean();
	}

}
