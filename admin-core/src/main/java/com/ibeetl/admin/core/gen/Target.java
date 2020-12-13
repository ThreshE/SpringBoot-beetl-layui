package com.ibeetl.admin.core.gen;

import org.beetl.core.GroupTemplate;

/**
 * 描述如何輸出代碼，有列印後臺，頁麵輸出，或者直接生成到項目裏
 * @author lijiazhi
 *
 */
public interface Target {
	public void flush(AutoGen gen,String content);
	public GroupTemplate getGroupTemplate();
}
