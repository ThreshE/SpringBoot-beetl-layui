package com.ibeetl.admin.core.web;

/**
 * 描述: json格式數據返回對象，使用CustomJsonResultSerializer 來序列化
 * @author : lijiazhi
 */
public class JsonResult<T> {
  
    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "code=" + code + " message=" + msg + " data=" + data;
    }

    public static <T> JsonResult<T> fail() {
    	JsonResult<T> ret = new JsonResult<T>();
    	ret.setCode(JsonReturnCode.FAIL.getCode());
    	ret.setMsg(JsonReturnCode.FAIL.getDesc());
        return ret;
    }
    
    public static <T>  JsonResult<T> fail(T data) {
	    	JsonResult<T> ret = JsonResult.fail();
	    	ret.setData(data);
        return ret;
    }
    
    public static <T>  JsonResult<T> failMessage(String msg) {
	    	JsonResult<T> ret = JsonResult.fail();
	    	ret.setMsg(msg);
        return ret;
    }
    public static <T>  JsonResult<T> successMessage(String msg) {
	    	JsonResult<T> ret = JsonResult.success();
	    	ret.setMsg(msg);
	    return ret;
    }
    
   

    public static <T> JsonResult<T> success() {
    	JsonResult<T> ret = new JsonResult<T>();
    	ret.setCode(JsonReturnCode.SUCCESS.getCode());
    	ret.setMsg(JsonReturnCode.SUCCESS.getDesc());
        return ret;
    }
    
    
    
    public static <T> JsonResult<T> success(T data) {
	    	JsonResult<T> ret = JsonResult.success();
	    	ret.setData(data);
        return ret;
    }
    
    public static <T> JsonResult<T>  http404(T data) {
	    	JsonResult<T> ret = new JsonResult<T>();
	    	ret.setCode(JsonReturnCode.NOT_FOUND.getCode());
	    	ret.setMsg(JsonReturnCode.NOT_FOUND.getDesc());
	    	ret.setData(data);
        return ret;
    }
    
    public static <T> JsonResult<T> http403(T data) {
	    	JsonResult<T> ret = new JsonResult<T>();
	    	ret.setCode(JsonReturnCode.ACCESS_ERROR.getCode());
	    	ret.setMsg(JsonReturnCode.ACCESS_ERROR.getDesc());
	    	ret.setData(data);
        return ret;
    }


    //下面的代码为了配合APP的接口做的，后续有需要可删除
    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    private T result;

    public static <T> JsonResult<T> failApp() {
        JsonResult<T> ret = new JsonResult<T>();
        ret.setCode(JsonReturnCode.FAIL.getCode());
        ret.setMsg(JsonReturnCode.FAIL.getDesc());
        return ret;
    }

    public static <T>  JsonResult<T> failApp(T data) {
        JsonResult<T> ret = JsonResult.failApp();
        ret.setResult(data);
        return ret;
    }

    public static <T>  JsonResult<T> failMessageApp(String msg) {
        JsonResult<T> ret = JsonResult.failApp();
        ret.setMsg(msg);
        return ret;
    }
    public static <T>  JsonResult<T> successMessageApp(String msg) {
        JsonResult<T> ret = JsonResult.successApp();
        ret.setMsg(msg);
        return ret;
    }



    public static <T> JsonResult<T> successApp() {
        JsonResult<T> ret = new JsonResult<T>();
        ret.setCode(JsonReturnCode.SUCCESS.getCode());
        ret.setMsg(JsonReturnCode.SUCCESS.getDesc());
        return ret;
    }



    public static <T> JsonResult<T> successApp(T data) {
        JsonResult<T> ret = JsonResult.successApp();
        ret.setResult(data);
        return ret;
    }

    public static <T> JsonResult<T>  http404App(T data) {
        JsonResult<T> ret = new JsonResult<T>();
        ret.setCode(JsonReturnCode.NOT_FOUND.getCode());
        ret.setMsg(JsonReturnCode.NOT_FOUND.getDesc());
        ret.setResult(data);
        return ret;
    }

    public static <T> JsonResult<T> http403App(T data) {
        JsonResult<T> ret = new JsonResult<T>();
        ret.setCode(JsonReturnCode.ACCESS_ERROR.getCode());
        ret.setMsg(JsonReturnCode.ACCESS_ERROR.getDesc());
        ret.setResult(data);
        return ret;
    }
    //上面的代码为了配合APP的接口做的，后续有需要可删除
  
}
