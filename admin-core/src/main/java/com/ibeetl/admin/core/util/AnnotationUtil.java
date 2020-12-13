package com.ibeetl.admin.core.util;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述: 獲取註解的值
 *
 * @author : xiandafu
 */
public class AnnotationUtil {

    private AnnotationUtil() {
    }

    public static AnnotationUtil getInstance() {
        return AnnotationUtilHolder.instance;
    }

    /**
     * 獲取一個類註解的名稱和值
     *
     * @param annotationClasss   註解定義類
     * @param useAnnotationClass 使用註解的類
     * @return List<Map<String, Object>>
     * @throws Exception
     */
    public List<Map<String, Object>> getAnnotations(Class annotationClasss, Class useAnnotationClass) {
        List<Map<String, Object>> annotationMapList = new ArrayList<>();

        Field[] fields = useAnnotationClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClasss)) {
                Annotation p = field.getAnnotation(annotationClasss);
                Map map = AnnotationUtils.getAnnotationAttributes(p);
                map.put("fieldName", field.getName());
                annotationMapList.add(map);
            }
        }

        return annotationMapList;
    }

    private static class AnnotationUtilHolder {
        private static AnnotationUtil instance = new AnnotationUtil();

        private AnnotationUtilHolder() {
        }
    }
}
