package bkp.stock.virtual.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bkp.stock.virtual.R;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.Validations;

/*
 * Forgot Password
 * */
public class ForgotPass extends AppCompatActivity {

    private TextInputEditText et1;
    private Dialog loader;
    private FirebaseAuth mAuth;


    /*
     * this method connects Ui xml file with java file
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        loader = Const.getLoader(ForgotPass.this,getString(R.string.loading));
        mAuth = FirebaseAuth.getInstance();
        initUI();
    }

    private void initUI() {
        TextView title_txt= (TextView) findViewById(R.id.title);
        title_txt.setText(getString(R.string.forgot_pass));
        et1 = findViewById(R.id.et1);
    }


    /*
     * submit click listner
     * */
    public void LoginNow(View view){

        if(Validations.valEditEmail(this,et1,getString(R.string.email1))
        ){
            loader.show();
            mAuth.sendPasswordResetEmail(et1.getText().toString());
            loader.dismiss();
            et1.setText("");
            Const.showMsg(ForgotPass.this,"Email Sent for reset password",null);


        }

    }




    /*
     * home icon click listner
     * */
    public void NavNow(View v){
        finish();
    }


    /*
     * back button click listner
     * */
    @Override
    public void onBackPressed() {
        finish();
    }
}
