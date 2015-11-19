package com.fda.aps;

public class StringUtils {
    public StringUtils() {
        super();
    }
    
    public static void main(String[] args){
        //String role = "BSIP-CBER-BDGT_APRV";
        String year = "2015";
        String role = " AAP_USERS";
//        int roleStartIndex = StringUtils.nthOccurrence(role, '-', 0);
//        int roleEndIndex = StringUtils.nthOccurrence(role, '-', 1);
//        System.err.println("roleStartIndex "+roleStartIndex);
//        System.err.println("roleEndIndex "+roleEndIndex);
//        System.err.println(role.substring(roleStartIndex+1, roleEndIndex-1));
//        System.err.println(year.substring(2));
//        
//        String originalCenterItemNo = "OC-15-I-234";
//        int dashFirstIndex = originalCenterItemNo.indexOf("-");
//        System.err.println(originalCenterItemNo.substring(0, dashFirstIndex));
//        int g = nthOccurrence(originalCenterItemNo, '-', 1);
//        System.err.println(g);
//      System.err.println(originalCenterItemNo.substring(g, g+1));  
//        int dashLastIndex = originalCenterItemNo.lastIndexOf("-");
//        String newCenterLine = originalCenterItemNo.substring(0, dashLastIndex);
//        System.err.println(newCenterLine);
      System.err.println(getAcqnLeadGroupFromRole(role));
      
    }
    
    public static int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos+1) + 1;
        return pos;
    }
    public static String getAcqnLeadGroupFromRole(String role) {

        //int idx = role.indexOf("-");
        int startIdx = StringUtils.nthOccurrence(role, '-', 0);
        int endIdx = StringUtils.nthOccurrence(role, '-', 1);
        String center = role.substring(startIdx+1, endIdx-1);
        String acquisitionGroupLead = center + "-" + "ACQ_APRV";
        return acquisitionGroupLead;

    }
    
    
}
