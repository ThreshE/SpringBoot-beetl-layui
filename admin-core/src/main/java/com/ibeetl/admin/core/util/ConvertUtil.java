package com.ibeetl.admin.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 數據格式轉化類
 * @author xiandafu
 *
 */
public class ConvertUtil {
	/**
	 * 轉化逗號分隔的id到long數組，通常用於批量操作
	 * @param str
	 * @return
	 */
	public static List<Long> str2longs(String str){
		if(str.length()==0){
			return Collections.EMPTY_LIST;
		}
		String[] array = str.split(",");
		List<Long> rets = new ArrayList(array.length);
		int i = 0;
		for(String id:array){
			try{
				rets.add(Long.parseLong(id));
			}catch(Exception ex){
				throw new RuntimeException("轉化 "+str+ " 到Long數組出錯");
			}
			
		}
		return rets;
	}
}
