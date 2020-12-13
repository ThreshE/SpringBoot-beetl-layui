package com.ibeetl.admin.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.ibeetl.admin.core.conf.MVCConf;

/**
 *  保留用戶會話，以方便在業務代碼任何地方調用
 *  {@link MVCConf}
 * @author lijiazhi
 *
 */
@Component
public class HttpRequestLocal {
	
	public  HttpRequestLocal(){
		
	}
	
	  private static final ThreadLocal<HttpServletRequest> requests =
		         new ThreadLocal<HttpServletRequest>() {
		             @Override protected HttpServletRequest initialValue() {
		                 return null;
		         }
		     };
		     
	 public  Object getSessionValue(String attr){
		 return  requests.get().getSession().getAttribute(attr);
	 }
	 
	 public  void setSessionValue(String attr,Object obj){
		   requests.get().getSession().setAttribute(attr,obj);
	 }
	 
	 
	 
	 public  Object getRequestValue(String attr){
		 return  requests.get().getAttribute(attr);
	 }
	 
	 public String getRequestURI(){
		 return  requests.get().getRequestURI();
	 }
	 
	 public String getRequestIP(boolean localIpEnable){
		 return getIpAddr(requests.get(),localIpEnable);
	 }
	 
	 public  void set(HttpServletRequest request){
		 requests.set(request);
	 }
	 
	 /** 
	     * 獲取當前網路ip 
	     * @param request 
	     * @return 
	     */  
	    public String getIpAddr(HttpServletRequest request, boolean localIpEnable){
	        String ipAddress = request.getHeader("x-forwarded-for");  
	            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	                ipAddress = request.getHeader("Proxy-Client-IP");  
	            }  
	            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
	            }  
	            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	                ipAddress = request.getRemoteAddr();  
	                if(localIpEnable && (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1"))){
	                    //根據網卡取本機配置的IP  
	                    InetAddress inet=null;  
	                    try {  
	                        inet = InetAddress.getLocalHost();  
	                    } catch (UnknownHostException e) {  
	                        e.printStackTrace();  
	                    }  
	                    ipAddress= inet.getHostAddress();  
	                }  
	            }  
	            //對於通過多個代理的情況，第一個IP為客戶端真實IP,多個IP按照','分割  
	            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
	                if(ipAddress.indexOf(",")>0){  
	                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
	                }  
	            }  
	            return ipAddress;   
	    }
}
