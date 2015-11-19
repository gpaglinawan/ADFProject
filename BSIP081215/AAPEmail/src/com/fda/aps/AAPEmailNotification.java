package com.fda.aps;


import com.fda.aps.model.appmodule.APSApplicationModuleImpl;
import com.fda.aps.model.viewobject.RequestsViewImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowIterator;

public class AAPEmailNotification {
    private static AAPEmailNotification emailSingleton;
    private List<String> recipients;
    private Properties props;
    private Session session;
        //= new ArrayList<String>();
    private AAPEmailNotification() {
        //super();
        props = new Properties();
        
        props.put("mail.smtp.host", "fdsupmail.fda.gov");
        props.put("mail.smtp.port", "25");
        session =
            Session.getInstance(props,null);
    }
    
    public static AAPEmailNotification getInstance(){
        if (emailSingleton == null){
            emailSingleton = new AAPEmailNotification();
        }    
        return emailSingleton;
    }
    
    public void sendMessage(List<String> recipients, String messageBody,
                            String sender, String subject, String centerLineNo){
        
        System.err.println("sender "+sender);
        System.err.println("messageBody "+messageBody);
        System.err.println("subject "+subject);
        for (String r:recipients){
            System.err.println("recipient "+r);
        }
        System.err.println("centerLineNo "+centerLineNo);
        
        try {
            String requestTitle = null;
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender));
            msg.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse(collectionToString(recipients,",")));
            Key key = new Key(new Object[] { centerLineNo });
            APSApplicationModuleImpl aapModule = (APSApplicationModuleImpl)ADFUtils.getApplicationModuleForDataControl("APSApplicationModuleDataControl");
            RequestsViewImpl searchByControlNo = aapModule.getSearchByControlKeyVO();
            RowIterator rowIter =
                searchByControlNo.findByAltKey("CenterItemAltKey", key, -1,
                                            Boolean.TRUE);

            if (rowIter.hasNext()) {

                Row row = rowIter.next();
                requestTitle = (String)row.getAttribute("RequestTitle");
            }
            msg.setSubject(subject +" "+requestTitle);
            msg.setText(messageBody);

            Transport.send(msg);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.getStackTrace();
        }
        catch(Exception ex){
            ex.getStackTrace();
        }
            
    }
    
    private String collectionToString(Collection list, String delim){
           StringBuilder strBuilder = new StringBuilder();
           
           if (list == null) return null;
           
           Iterator itr = list.iterator();
           while(itr.hasNext()){
              strBuilder.append((String)itr.next()) ;
                  if(itr.hasNext()){
                    strBuilder.append(delim) ;
                }
           }
           
           return strBuilder.toString();
    }
    
    
}
