package com.fda.aps.view.managedbean;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import weblogic.servlet.security.ServletAuthentication;

public class LoginBean {
    public LoginBean() {
        super();
    }
    
    public String onLogout(){  
         FacesContext fctx = FacesContext.getCurrentInstance();  
         ExternalContext ectx = fctx.getExternalContext();  
         //String url = ectx.getRequestContextPath() + "/adfAuthentication?logout=true&end_url=/faces/index.jspx";  
         String url = ectx.getRequestContextPath() +"/faces/welcome.jspx";  
         HttpSession session = (HttpSession)ectx.getSession(false);  
         session.invalidate();  
           
         HttpServletRequest request = (HttpServletRequest)ectx.getRequest();  
         ServletAuthentication.logout(request);  
         ServletAuthentication.invalidateAll(request);  
         ServletAuthentication.killCookie(request);  
           
         try{  
           ((HttpServletResponse)ectx.getResponse()).sendRedirect(url);  
         }  
         catch(Exception e){  
           e.printStackTrace();  
         }  
         fctx.responseComplete();  
           
         return "toLogout";  
       }  
}
