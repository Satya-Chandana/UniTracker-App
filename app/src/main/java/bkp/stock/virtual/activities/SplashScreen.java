package bkp.stock.virtual.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import bkp.stock.virtual.R;
import bkp.stock.virtual.other.MySharedPref;

public class SplashScreen extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(){
            @Override
            public void run() {
                try{
                    if(new MySharedPref().getData(getApplicationContext(),"ldata","").length()>0){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        sleep(2000);
                        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                    }
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
