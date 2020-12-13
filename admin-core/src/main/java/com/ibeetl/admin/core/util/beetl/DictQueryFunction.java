package com.ibeetl.admin.core.util.beetl;

import java.util.Collections;
import java.util.List;

import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibeetl.admin.core.entity.CoreDict;
import com.ibeetl.admin.core.service.CoreDictService;
@Component
public class DictQueryFunction  {

	@Autowired
	CoreDictService dictService;
	public List<CoreDict> dictDownQuery(String type) {

		return dictService.findAllByType(type);
	}
	
	
	public List<CoreDict> dictListByValue(String group,String value){
	    
	    return dictService.findAllByGroup(group,value);
	}
	
	/**
	 * 根據字典類型和值，找到對應的字典類型定義
	 * 數據字典在一個type下隻有唯一的值
	 * @param type
	 * @param value
	 * @return
	 */
	public CoreDict getDict(String type,String value) {
	    return dictService.findCoreDict(type, value);
	}

	public List<CoreDict> invertList(List<CoreDict> list){
		Collections.reverse(list);
		return list;
	}

}
