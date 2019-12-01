package com.xhat.aminem.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SessionManager {
    public static final String SP_AOE_APP       = "spAmikomOnlineExamApp";
    public static final String SP_TOKEN         = "spToken";
    public static final String SP_USER_ID       = "spUserId";
    public static final String SP_SUDAH_LOGIN   = "spSudahLogin";
    public static final String SP_IMAGE_SLIDER  = "";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SessionManager(Context context){
        sp = context.getSharedPreferences(SP_AOE_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPToken(){

        return sp.getString(SP_TOKEN, "");
    }

    public String getSPImageSlider(){

        return sp.getString(SP_IMAGE_SLIDER, "");
    }

    public Integer getSPUserId(){

        return sp.getInt(SP_USER_ID, 0);
    }

    public Boolean getSPSudahLogin(){

        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
