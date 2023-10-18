package bkp.stock.virtual.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bkp.stock.virtual.R;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.MySharedPref;
import bkp.stock.virtual.other.Validations;

/*
 * Singup Screen
 * */
public class SignupActivity extends AppCompatActivity {

    private Dialog loader;
    private FirebaseAuth mAuth;
    private TextInputEditText et1,et2,et3,et4;
    /*
     * this method connects Ui xml file with java file
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loader = Const.getLoader(SignupActivity.this,getString(R.string.loading));
        mAuth = FirebaseAuth.getInstance();

        initUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            mAuth.signOut();

        }
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.sign_up));

    }

    /*
     * signup button click listner
     * */
    public void LoginNow(View view) {

        if(Validations.valEdit(this,et1,getString(R.string.full_name))
                && Validations.valEditEmail(this,et2,getString(R.string.email1))
                && Validations.valEditPassword(this,et3,getString(R.string.password))
                && Validations.valEdit(this,et4,getString(R.string.roll_no))
                && Const.isOnline(SignupActivity.this)
        ){
            loader.show();

            mAuth.signInWithEmailAndPassword(et2.getText().toString(), et3.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loader.dismiss();
                                Const.showMsg(SignupActivity.this,"User already exist",null);
                            } else {
                                registerUser();
                            }
                        }
                    });


        }
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(et2.getText().toString(), et3.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null) {
                                createUser(user);
                            }
                            else{
                                loader.dismiss();
                                Const.showMsg(SignupActivity.this,"Authentication failed.",null);
                            }
                        } else {
                            loader.dismiss();
                            Const.showMsg(SignupActivity.this,"Authentication failed.",null);
                        }
                    }
                });
    }

    private void createUser(FirebaseUser user) {
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.user_tbl);

            UserDto userDto = new UserDto();
            userDto.setUid(user.getUid());
            userDto.setName(et1.getText().toString());
            userDto.setEmail(et2.getText().toString());
            userDto.setPass(et3.getText().toString());
            userDto.setRollno(et4.getText().toString());
            userDto.setClassname("0");
            userDto.setTimestamp(Const.getLocalTime(Const.getUtcTime(),"yyyy-MM-dd hh:mm"));
            userDto.setType(UserConst.user);

            myRef.child(user.getUid()).setValue(userDto);


            loader.dismiss();
            Const.showMsg(SignupActivity.this,"Registration Successful",null);
            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
     * back button click listner
     * */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
        finish();
    }

    /*
     * home icon click listner
     * */
    public void NavNow(View v) {
        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
        finish();
    }

}
