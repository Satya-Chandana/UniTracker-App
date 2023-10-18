package bkp.stock.virtual.other;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * its local shared prefrances class common for whole project
 * its work like session in app
 */
public class MySharedPref {

    public void saveData(Context context, String key, String value)
    {
        SharedPreferences sp = context.getSharedPreferences(Const.spName,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public String getData(Context context, String key, String value)
    {
        SharedPreferences sp = context.getSharedPreferences(Const.spName,context.MODE_PRIVATE);
        return sp.getString(key,value);
    }
    public void DeleteData(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(Const.spName,context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public void NullData(Context context , String key)
    {
        SharedPreferences sp = context.getSharedPreferences(Const.spName,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,null);
        editor.commit();
    }


}
