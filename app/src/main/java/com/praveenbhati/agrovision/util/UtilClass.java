package com.praveenbhati.agrovision.util;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Bhati on 11/8/2015.
 */
public class UtilClass {

    // Web service domain name
    public static final String DOMAIN = "http://agrovision.elasticbeanstalk.com/";//"http://192.168.1.7:8080/ERP_System/";

    //login webservice URL
    public static final String LOGIN_URL = DOMAIN + "mobileLogin.htm";

    //add lead webservice URL
    public static final String ADD_LEAD_URL = DOMAIN + "mobileAddLead.htm";

    //get lead webservice URL
    public static final String GET_LEAD_URL = DOMAIN + "mobileGetLeads.htm?userId=";

    //add expanse webservice URL
    public static final String ADD_EXPENSE_URL = DOMAIN + "mobileAddExpense.htm";

    public static boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }
}
