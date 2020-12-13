package com.ibeetl.admin.core.util.beetl;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.stereotype.Component;

import com.ibeetl.admin.core.annotation.Query;
import com.ibeetl.admin.core.util.AnnotationUtil;

/**
 * 描述: 通過解析註解，獲取錶達查詢條件信息
 *
 * @author : lijiazhi
 */
@Component
public class SearchCondtionFunction implements Function {

	
    /**
     *
     * @param paras 查詢條件類名
     * @param ctx
     * @return
     */
    @Override
    public Object call(Object[] paras, Context ctx) {
        String className = (String) paras[0];
        try {
            List<Map<String, Object>> list = AnnotationUtil.getInstance().getAnnotations(Query.class, Class.forName(className));
            for (Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext();) {
				Map<String, Object> map = iterator.next();
				if (Boolean.valueOf(map.get("display").toString()) == false)
					iterator.remove();
			}
            return list;
        } catch (ClassNotFoundException e ) {
            e.printStackTrace();
            return null;
        }
    }
}
