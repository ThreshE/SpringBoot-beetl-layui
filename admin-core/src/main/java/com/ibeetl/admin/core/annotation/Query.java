package com.ibeetl.admin.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用來標準一個查詢類
 * @author lijiazhi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
	
	public static final  int TYPE_GENERAL = 1;
	public static final  int TYPE_DATE_BETWEEN = 2;
	//未實現
	public static final  int TYPE_DATETIME_BETWEEN = 3;
	public static final  int TYPE_VALUE_BETWEEN = 4;
	public static final  int TYPE_DICT = 5;
	//用戶自己定義
	public static final  int TYPE_CONTROL = 6;
	
	
	/**
	 * 中文名字
	 * @return
	 */
	public String name();
	/**
	 * 查詢類型,常規，範圍，字典，前端自定義
	 * @return
	 */
	public int type() default TYPE_GENERAL;
	
	/**
	 * 是否顯示在查詢界麵上
	 * @return
	 */
	public boolean display() default false;
	
	/**
	 * 模糊查詢，僅僅針對TYPE_GENERAL
	 * @return
	 */
	public boolean fuzzy() default false;
	
	/**
	 * 字典的主鍵，比如,"user_state"
	 * @return
	 */
	public String dict() default "";
	
	/*描述*/
	public String comment() default "";
	
	/**
	 * 控件名字，如組織機構麵闆
	 * @return
	 */
	public String control() default "";
	
	/**
	 * 順序，值越小，排在前麵
	 * @return
	 */
	public int order() default 0;
	
	/*控件組*/
	public String group() default "";
}
