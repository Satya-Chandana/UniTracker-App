package bkp.stock.virtual.other;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import bkp.stock.virtual.R;


/**
 * Custom validation class created by us for validating fields when we tries to submit
 */

public class Validations {
    /*
    * Text fiels validation type 1
    * */
    public static boolean valEdit1(Activity activity, EditText editText, TextView textView, String name)
    {
        if(editText!=null && activity!=null && name!=null) {
            if (editText.getText().toString().length() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(activity.getString(R.string.edit_error_msg) + " " + name);
                return false;
            } else {
                return true;
            }
        }
        else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(activity.getString(R.string.edit_error_msg) + " " + name);
            return false;
        }
    }
    /*
     * Text fiels validation type 2
     * */
    public static boolean valEdit(Activity activity, EditText editText, String name)
    {
        if(editText!=null && activity!=null && name!=null) {
            if (editText.getText().toString().length() == 0) {

                Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
                return false;
            } else {
                return true;
            }
        }
        else{
            Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
            return false;
        }
    }
    /*
     * Text fiels validation type 3
     * */
    public static boolean valEdit(Activity activity, TextView editText, String name)
    {
        if(editText!=null && activity!=null && name!=null) {
            if (editText.getText().toString().length() == 0) {
                Const.showMsg(activity,activity.getString(R.string.please_sel)+ " " + name,editText);
                return false;
            } else {
                return true;
            }
        }
        else{
            Const.showMsg(activity,activity.getString(R.string.please_sel)+ " " + name,editText);
            return false;
        }
    }

    /*
     * Text fiels validation type password
     * */
    public static boolean valEditPassword1(Activity activity, EditText editText,TextView textView, String name)
    {
        if(editText!=null && activity!=null && name!=null) {
            if (editText.getText().toString().length() <= 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(activity.getString(R.string.edit_error_msg) + " " + name);
                return false;
            } else {

                return true;
            }
        }
        else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(activity.getString(R.string.edit_error_msg) + " " + name);
            return false;
        }
    }
    /*
     * Text fiels validation password type 2
     * */
    public static boolean valEditPassword(Activity activity, EditText editText, String name)
    {
        if(editText!=null && activity!=null && name!=null) {
            if (editText.getText().toString().length() <= 0) {
                Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
                return false;
            } else {
                if (editText.getText().toString().length() < 6) {
                    Const.showMsg(activity,activity.getString(R.string.pass_length_error),null);
                    return false;
                } else {
                    return true;
                }
            }
        }
        else{
            Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
            return false;
        }
    }
    /*
     * Text fiels validation email
     * */
    public static boolean valEditEmail(Activity activity, EditText editText, String name)
    {
        if(editText!=null && activity!=null && name!=null) {
            if (editText.getText().toString().length() == 0) {
                Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
                return false;
            } else {
                if (editText.getText().toString().contains("@") && editText.getText().toString().contains(".")) {
                    return true;
                } else {
                    Const.showMsg(activity,activity.getString(R.string.invalid_email),editText);
                    return false;
                }
            }
        }
        else{
            Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
            return false;
        }
    }

    /*
     * Text fiels validation mobile number
     * */
    public static boolean valEditMob(Activity activity, EditText editText, String name)
    {
        if(editText!=null && activity!=null && name!=null) {
            if (editText.getText().toString().length() == 0) {
                Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
                return false;
            } else {
                if (editText.getText().toString().length() <=12 && editText.getText().toString().length() >=10) {
                    String str = editText.getText().toString();
                    boolean valid = false;
                    for (int i = 0; i < str.length(); i++) {
                        try {
                            int x = Integer.parseInt(str.charAt(i) + "");
                            valid = true;
                        } catch (Exception e) {
                            Const.showMsg(activity,activity.getString(R.string.invalid_mobile),editText);
                            return false;
                        }
                    }
                    if (valid) {
                        return true;
                    } else {
                        Const.showMsg(activity,activity.getString(R.string.invalid_mobile),editText);
                        return false;
                    }
                } else {
                    Const.showMsg(activity,activity.getString(R.string.invalid_mobile),editText);
                    return false;
                }
            }
        }
        else{
            Const.showMsg(activity,activity.getString(R.string.edit_error_msg)+ " " + name,editText);
            return false;
        }
    }
}
