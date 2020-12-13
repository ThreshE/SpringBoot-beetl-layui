package com.ibeetl.admin.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用來標註功能id
 * <pre>
 * &#064;Function("user.add")
 * public String addUser(){
 * }
 * </pre>
 *
 * 隻有擁有此項功能的角色才能操作，否則，權限不足
 * @author lijiazhi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Function {
	public String value();
	public String name() default "";
	public String orderType() default "";//訂單類型（主訂單、子訂單、其他）
	public String type() default "";//類型（增刪改查）
}
