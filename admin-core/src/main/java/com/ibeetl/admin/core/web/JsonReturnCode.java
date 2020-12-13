package com.ibeetl.admin.core.web;

/**
 * 描述: json格式數據返回碼
 *<ul>
 *      <li>100 : 用戶未登入 </li>
 *      <li>200 : 成功 </li>
 *      <li>300 : 失敗 </li>
 * </ul>
 * @author : Administrator
 */
public enum JsonReturnCode {

    NOT_LOGIN("401","未登入"),
    SUCCESS ("200","成功"),
    FAIL ("500","內部失敗"),
	ACCESS_ERROR ("403","禁止訪問"),
	NOT_FOUND ("404","頁麵未發現");
    private String code;
    private String desc;

    JsonReturnCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
