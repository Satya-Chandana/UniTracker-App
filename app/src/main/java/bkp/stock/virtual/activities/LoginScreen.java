package bkp.stock.virtual.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import bkp.stock.virtual.other.MySharedPref;
import bkp.stock.virtual.other.Validations;

/*
 * Login screen
 * */
public class LoginScreen extends AppCompatActivity {

    private TextInputEditText et1,et2;
    private Dialog loader;
    private FirebaseAuth mAuth;


    /*
     * this method connects Ui xml file with java file
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loader = Const.getLoader(LoginScreen.this,getString(R.string.loading));
        mAuth = FirebaseAuth.getInstance();
        initUI();

    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        findViewById(R.id.fab1).setVisibility(View.GONE);
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.login));
    }


    /*
     * Login button click listner
     * */
    public void LoginNow(View view){
        if(Validations.valEdit(this,et1,getString(R.string.email1))
                && Validations.valEdit(this,et2,getString(R.string.password))
               ){
            loader.show();
            mAuth.signInWithEmailAndPassword(et1.getText().toString(), et2.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
                                myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        loader.dismiss();
                                        if(snapshot!=null && snapshot.getValue()!=null){
                                            UserDto userDto = snapshot.getValue(UserDto.class);
                                            if(userDto!=null){
                                                new MySharedPref().saveData(getApplicationContext(),"ldata",userDto.getUid());
                                                new MySharedPref().saveData(getApplicationContext(),"ltype",userDto.getType());
                                                new MySharedPref().saveData(getApplicationContext(),"name",userDto.getName());
                                                new MySharedPref().saveData(getApplicationContext(),"email",userDto.getEmail());
                                                new MySharedPref().saveData(getApplicationContext(),"rollno",userDto.getRollno());
                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                finish();
                                            }
                                            else{
                                                Const.showMsg(LoginScreen.this,"Login Failed",null);
                                            }
                                        }
                                        else{
                                            Const.showMsg(LoginScreen.this,"Login Failed",null);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        loader.dismiss();
                                        Const.showMsg(LoginScreen.this,"Login Failed",null);
                                    }
                                });
                            }
                            else{
                                loader.dismiss();
                                Const.showMsg(LoginScreen.this,"Login Failed",null);
                            }
                        }
                    });

        }
    }


    /*
     * Singup click listner
     * */
    public void RegNow(View view){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
        finish();
    }

    /*
     * forgot passsword click listner
     * */
    public void ForNow(View view){
        startActivity(new Intent(getApplicationContext(),ForgotPass.class));
    }

    /*
     * home icon click listner
     * */
    public void NavNow(View v){

//        startActivity(new Intent(getApplicationContext(),SplashScreen.class));
        finish();
    }

    /*
     * Back button click listner
     * */
    @Override
    public void onBackPressed() {

//        startActivity(new Intent(getApplicationContext(),SplashScreen.class));
        finish();
    }


}
