package com.ibeetl.admin.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibeetl.admin.core.dao.CoreDictDao;
import com.ibeetl.admin.core.entity.CoreDict;
import com.ibeetl.admin.core.util.enums.DelFlagEnum;

/**
 * 描述: 字典 service，包含常規字典和級聯字典的操作。
 * @author : xiandafu
 */
@Service
@Transactional
public class CoreDictService extends CoreBaseService<CoreDict> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreDictService.class);

    @Autowired
    private CoreDictDao dictDao;

    @Autowired
    CorePlatformService platformService;

    @Autowired
    CoreDictService self ;


    /**
     * 根據類型獲取字典集合
     * @param type 字典類型，
     * @return List
     */
    @Cacheable(value = CorePlatformService.DICT_CACHE_TYPE)
    public List<CoreDict> findAllByType(String type) {
        return dictDao.findAllList(type);
    }

    /**
     * 級聯字典查詢，必須提供一個字典類型
     * @param group
     * @param value
     * @return
     */
    @Cacheable(value = CorePlatformService.DICT_CACHE_CHILDREN)
    public List<CoreDict> findAllByGroup(String type,String value) {
       List<CoreDict> list = self.findAllByType(type);
       return  _search(list,value);

    }

    /**
     * 級聯字段下一級的字段列錶
     * @param parentValue
     * @return
     */
    @Cacheable(value = CorePlatformService.DICT_CACHE_CHILDREN)
    public List<CoreDict> findChildByParent(Long id) {
        return dictDao.findChildByParent(id);
    }

    @Cacheable(value = CorePlatformService.DICT_CACHE_VALUE)
    public CoreDict findCoreDict(String type,String value) {
       List<CoreDict> list = self.findAllByGroup(type, value);
       if(list==null) {
           return null;
       }
       for(CoreDict dict:list) {
           if(dict.getValue().equals(value)) {
               return dict;
           }
       }

 	   return null;
    }

    /*通過名字反嚮查找數據字典，通常用於excel導入*/
    public Map<String,CoreDict> mapDictByName(String type){
        List<CoreDict> list = self.findAllByType(type);
        Map<String,CoreDict> map = new HashMap<String,CoreDict>();
        for(CoreDict dict:list) {
            map.put(dict.getName(), dict);
        }
        return  map;
    }



   /*遞歸查找*/
    private List<CoreDict> _search(List<CoreDict> list,String value) {
        for(CoreDict dict:list) {
            if(dict.getValue().equals(value)) {
                return list;
            }else {
                List<CoreDict> children = findChildByParent(dict.getId());
                if(children.isEmpty()) {
                    continue;
                }else {
                    List<CoreDict> ret = _search(children,value);
                    if(ret!=null) {
                        return ret;
                    }
                }

            }
        }
        return null;
    }


    /**
     * 查詢字段類型列錶
     * @return
     */
    public List<Map<String, String>> findTypeList() {
        return dictDao.findTypeList(DelFlagEnum.NORMAL.getValue());
    }

    /**
     * @Description 根據單個字典值查詢字典名
     * @param //[type, value]
     * @return java.lang.String
     * @author Anko
     * @date 2020/10/9 11:06
     */
    public String queryNameByValue(String type,String value) {
        CoreDict coreDict = new CoreDict();
        coreDict.setType(type);
        coreDict.setValue(value);
        CoreDict cd = (CoreDict)this.sqlManager.templateOne(coreDict);
        if(value==null || value.isEmpty() || cd==null){
            return value;
        }
        return cd.getName();
    }

    /**
     * 校验是否存在字典值
     * @param value
     * @return
     */
    public Boolean checkExistence(String value){
        CoreDict coreDict = new CoreDict();
        coreDict.setValue(value);
        CoreDict cd = this.sqlManager.templateOne(coreDict);
        if (cd==null){
            return false;
        }
        return true;
    }
}
