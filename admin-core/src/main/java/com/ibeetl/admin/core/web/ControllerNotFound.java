package com.ibeetl.admin.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beetl.ext.simulate.WebSimulate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 冇有找到匹配的Controller
 * @author xiandafu
 *
 */
@Controller
public class ControllerNotFound {
	@Autowired
	WebSimulate webSimulate;
	Log log = LogFactory.getLog(ControllerNotFound.class);
	@RequestMapping("/**/*.do")
	public void error(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		log.info("冇有配置 url "+request.getRequestURI()+",確認所訪問Controller是否存在，是否被Spring Boot管理");
		throw new RuntimeException("未找到Controller類處理此請求 "+request.getRequestURI());
	}

	
	
}
