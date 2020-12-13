package com.ibeetl.admin.core.gen;


/**
 * 用於代碼生成
 * @author xiandafu
 *
 */

public class ConsoleTarget extends  BaseTarget {
	public ConsoleTarget() {
		
	}
	@Override
	public void flush(AutoGen gen, String content) {
		System.out.println("=========="+gen.getClass().getSimpleName()+"=============");
		System.out.println(content);

	}

	

}
