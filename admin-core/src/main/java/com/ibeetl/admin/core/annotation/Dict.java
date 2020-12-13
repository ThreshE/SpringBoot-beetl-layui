package com.ibeetl.admin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ibeetl.admin.core.util.enums.CoreDictType;

/**
 * 描述: 用來標註詞典字段
 * 
 *
 * @author : lijiazhi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dict {

    /**
     * 類型
     *
     * @return
     */
    public String type() default "";

    /**
     * 預設值
     *
     * @return
     */
    public String defaultDisplay() default "";

    /**
     * 字典文本的字尾
     *
     * @return
     */
    public String suffix() default "Text";
}
