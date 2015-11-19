package com.fda.aps.view.managedbean;

import com.fda.aps.ADFUtils;

import java.io.Serializable;

import java.security.Principal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;
import oracle.adf.share.security.SecurityContext;

import oracle.adf.view.rich.component.rich.RichPopup;



import oracle.security.idm.IMException;
import oracle.security.idm.IdentityStore;
import oracle.security.idm.Property;
import oracle.security.idm.PropertySet;
import oracle.security.jps.JpsContext;
import oracle.security.idm.User;

import oracle.security.idm.UserProfile;
import oracle.security.jps.JpsContextFactory;

import oracle.security.jps.JpsException;
import oracle.security.jps.service.idstore.IdentityStoreService;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

import org.apache.myfaces.trinidad.util.Service;

import weblogic.security.spi.WLSGroup;


public class AAPManagedBean{
    private ArrayList<String> userGroups;
    private RichPopup profilePopup;
    private IdentityStore idStore = null;
    private String email;
    private JpsContext jpsCtx;
    private UserProfile userProfile = null;
    private static ADFLogger _logger = 
            ADFLogger.createADFLogger(AAPManagedBean.class); 

    public AAPManagedBean() {
        super();
    }
    
    public SecurityContext getAAPSecurityContext(){
        return getAapAdfContext().getSecurityContext();
    }
            
    public ADFContext getAapAdfContext(){
           return ADFContext.getCurrent();   
    }
    
    public List<String> getUserGroups(){
          //List<String> userGroups;
//          UserProfile profile = getAAPSecurityContext().getUserProfile();
//          System.out.println("middle name "+profile.getMiddleName());
//          userGroups = ADFUtils.getLoginUserEPRoles(); 
          return userGroups;
    }

//    public void setUserGroups(List<String> userGroups) {
//        this.userGroups = userGroups;
//    }
//    
//    public void testUser(ActionEvent evt){
//            UserProfile profile = getAAPSecurityContext().getUserProfile();
//            System.out.println("middle name "+profile.getMiddleName());
//            userGroups = ADFUtils.getLoginUserEPRoles();    
//            System.out.println("userGroups "+userGroups); 
//        }

    public void setProfilePopup(RichPopup profilePopup) {
        this.profilePopup = profilePopup;
    }

    public RichPopup getProfilePopup() {
        return profilePopup;
    }

    public void launchMyProfile(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering launchMyProfile");
        Set<Principal> principals = ADFContext.getCurrent().getSecurityContext().getSubject().getPrincipals();
        ADFContext adfCtx = ADFContext.getCurrent();
        SecurityContext secCntx = adfCtx.getSecurityContext();

        if (principals != null){
              userGroups = new ArrayList<String>();
            //roles = new ArrayList<String>();
            for (Principal role:principals){
                if (role instanceof WLSGroup){
                    userGroups.add(role.getName());
                }    
            }
        }
        
        try {
                    jpsCtx = JpsContextFactory.getContextFactory().getContext();
                    IdentityStoreService service =
                          jpsCtx.getServiceInstance(IdentityStoreService.class);
                    idStore = service.getIdmStore();

                    User user = idStore.searchUser(secCntx.getUserName());
                    if (user != null) {
                        userProfile = user.getUserProfile();
                        PropertySet propSet = userProfile.getAllUserProperties();
                        Iterator it = propSet.getAll();
                        while (it.hasNext()){
                        Property prop = (Property)it.next(); 
                        String name = prop.getName(); 
                        List values =     prop.getValues();
                           // if (name.equalsIgnoreCase("BUSINESS_EMAIL")){
                            System.err.println(name); 
                            System.err.println(values); 
                        }
                    }
                } catch (JpsException e) {
                    e.printStackTrace();
                    _logger.severe("Error in identity Store "+e.getMessage());
                } catch (IMException e) {
                    e.printStackTrace();
                    _logger.severe("Error in identity Store "+e.getMessage());
                }
        //email = ADFContext.getCurrent().getSecurityContext().getUserProfile().get
        ADFUtils.showPopup(profilePopup);
    }

    public void setUserGroups(ArrayList<String> userGroups) {
        this.userGroups = userGroups;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    public String closeWindow() {
            // Add event code here...
            _logger.info("Entering closeWindow");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExtendedRenderKitService service =
                Service.getRenderKitService(facesContext,
                                            ExtendedRenderKitService.class);
            service.addScript(facesContext, "window.open('', '_self', ''); window.close();");
            return null;

        }
}
