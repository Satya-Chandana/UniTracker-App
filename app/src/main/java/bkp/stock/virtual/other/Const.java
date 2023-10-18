package bkp.stock.virtual.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bkp.stock.virtual.R;

public class Const {
    public static String spName = "sp_dsdjnfdjnfj334234";
    public static final String format1 = "yyyy-MM-dd";
    public static final String format2 = "dd-MM-yyyy";
    public static String ldata = "ldata";
    public static String title = "title";
    public static String desc = "desc";

    public static void showMsg(Activity activity, String msg, View v) {
        Toast.makeText(activity.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

    }
    public static void playNotificationSound(Context context,int noti_type) {
        try {
            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/" + noti_type);

            try {
                Ringtone r = RingtoneManager.getRingtone(context, soundUri);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
                Ringtone r = RingtoneManager.getRingtone(context, soundUri);
                r.play();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static long getUtcTime() {

        Date date = new Date();
        return date.getTime();
    }
    public static final String getLocalTime(long time,String format) {
        try {
            if(format.length()<=0){
                format = format1;
            }
            Date local = new Date((long) time);
            final CharSequence cur_date2 = DateFormat.format(format, local.getTime());

            return cur_date2.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Const.showMsg(activity,activity.getString(R.string.intr_msg),null);

            return false;
        }
    }

    public static Dialog getLoader(Activity activity, String msg){
        Dialog dialog = null;
        try{
            dialog = new Dialog(activity);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.my_pro_bar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            TextView txt_msg = dialog.findViewById(R.id.txt_msg);
            txt_msg.setText(msg);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return dialog;
    }

    public static long getLocalTimeStamp(String str_date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = (Date)formatter.parse(str_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
