package com.ibeetl.admin.core.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.TailBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibeetl.admin.core.annotation.Dict;
import com.ibeetl.admin.core.entity.CoreDict;
import com.ibeetl.admin.core.util.PlatformException;
import com.ibeetl.admin.core.util.enums.DelFlagEnum;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * 描述:
 * @author : xiandafu
 */
public class CoreBaseService<T> {

    @Autowired
    protected CoreDictService dictUtil;
    @Autowired
	@Qualifier("baseDataSourceSqlManagerFactoryBean")
    protected SQLManager sqlManager;
    
    

    /**
     * 根據id查詢對象，如果主鍵ID不存在
     * @param id
     * @return
     */
    public T queryById(Object id) {
        T t = sqlManager.single(getCurrentEntityClassz(), id);
        queryEntityAfter((Object) t);			
        return t;
    }

    /**
     * 根據id查詢
     * @param classz 返回的對象類型
     * @param id     主鍵id
     * @return
     */
    public T queryById(Class<T> classz, Object id) {
        T t = sqlManager.unique(classz, id);
        queryEntityAfter((Object) t);
        return t;
    }

    /**
     * 新增一條數據
     * @param model 實體類
     * @return
     */
    public boolean save(T model) {
        return sqlManager.insert(model,true) > 0;
    }

   

    /**
     * 刪除數據（一般為邏輯刪除，更新del_flag字段為1）
     * @param ids
     * @return
     */
    public boolean deleteById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new PlatformException("刪除數據ID不能為空");
        }
        
        for (Long id : ids) {
          
        }

        List<Object> list = new ArrayList<>();
        for (Long id : ids) {
            Map map = new HashMap();
            // always id,delFlag for pojo
            map.put("id", id);
            map.put("delFlag", DelFlagEnum.DELETED.getValue());
          
            list.add(map);
        }
        int[] count = sqlManager.updateBatchTemplateById(getCurrentEntityClassz(), list);
        int successCount = 0;
        for (int successFlag : count) {
            successCount += successFlag;
        }
        return successCount == ids.size();
    }

    public boolean deleteById(Long id) {
       
            Map map = new HashMap();
            // always id,delFlag for pojo
            map.put("id", id);
            map.put("delFlag", DelFlagEnum.DELETED.getValue());
            int ret = sqlManager.updateTemplateById(getCurrentEntityClassz(), map);
            return ret==1;
    }
    /**
     * 根據id刪除數據
     * @param id 主鍵值
     * @return
     */
    public int forceDelete(Long id) {
        return sqlManager.deleteById(getCurrentEntityClassz(), id);
    }

    /**
     * 根據id刪除數據
     * @param id 主鍵值
     * @return
     */
    public int forceDelete(Class<T> classz, Long id) {
        return sqlManager.deleteById(classz, id);
    }

    /**
     * 更新，隻更新不為空的字段
     * @param model
     * @return
     */
    public boolean updateTemplate(T model) {
        return sqlManager.updateTemplateById(model)>0;
    }

    /**
     * 更新所有字段
     * @param model
     * @return
     */
    public boolean update(T model) {
    		return sqlManager.updateById(model) > 0;
    }

  

    /**
     * 獲取當前註入泛型T的類型
     * @return 具體類型
     */
    @SuppressWarnings("unchecked")
    private Class<T> getCurrentEntityClassz() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    public void queryListAfter(List list) {
        for (Object bean : list) {
            queryEntityAfter(bean);
        }
    }

    public void queryEntityAfter(Object  bean) {
        if (bean == null) {
            return;
        }
        
        if(!(bean instanceof TailBean)){
        	throw new PlatformException("指定的pojo"+bean.getClass()+" 不能獲取數據字典，需要繼承TailBean");
        }
        
        TailBean ext  = (TailBean)bean;
        Class c = ext.getClass();
        do {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Dict.class)) {
                    field.setAccessible(true);
                    Dict dict = field.getAnnotation(Dict.class);
                    
                    try {
                        String display = "";
                        Object fieldValue = field.get(ext);
                        if (fieldValue != null) {
                            CoreDict  dbDict = dictUtil.findCoreDict(dict.type(),fieldValue.toString());
                            display = dbDict!=null?dbDict.getName():null;
                        }
                        ext.set(field.getName() + dict.suffix(), display);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
    
                }
            }
         c = c.getSuperclass();
        }while(c!=TailBean.class);
        
    }



}
