package bkp.stock.virtual.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.dto.AssignDto;
import bkp.stock.virtual.dto.ClassDto1;
import bkp.stock.virtual.dto.SubDto;
import bkp.stock.virtual.dto.UpDto;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.Validations;

import bkp.stock.virtual.other.MySharedPref;
public class AddUpdate extends AppCompatActivity {

    private TextInputEditText et1,et2;

    private UserDto ldata = null;
    private Dialog loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_add);
        loader = Const.getLoader(AddUpdate.this,getString(R.string.loading));
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.new_update));
        initUI();


        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
            myRef.child(new MySharedPref().getData(getApplicationContext(),"ldata","")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot!=null && snapshot.getValue()!=null){
                        ldata = snapshot.getValue(UserDto.class);
                        if(ldata!=null){


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LoginNow(View v){
        if(Validations.valEdit(this,et1,getString(R.string.title))
                && Validations.valEdit(this,et2,getString(R.string.desc))
                && Const.isOnline(AddUpdate.this)
        ){

            loader.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.up_tbl);

            UpDto assignDto = new UpDto();
            assignDto.setUp_id(myRef.push().getKey());
            assignDto.setTitle(et1.getText().toString());
            assignDto.setDesc(et2.getText().toString());
            assignDto.setDatetime(Const.getLocalTime(Const.getUtcTime(),"yyyy-MM-dd, HH:mm"));
            if(ldata.getType().equals(UserConst.teacher)){
                assignDto.setType(ldata.getType());
                assignDto.setClass_name(ldata.getRollno());
            }
            else if(ldata.getType().equals(UserConst.user)){
                if(ldata.getClassname().equals("1")){
                    assignDto.setType(UserConst.teacher);
                    assignDto.setClass_name(ldata.getRollno().substring(0, 6));
                }
                else {
                    assignDto.setType(ldata.getType());
                    assignDto.setClass_name(ldata.getRollno().substring(0, 6));
                }
            }
            else {
                assignDto.setType(ldata.getType());
                assignDto.setClass_name(ldata.getRollno().substring(0, 6));
            }
            assignDto.setBy(new MySharedPref().getData(getApplicationContext(),"ldata",""));

            assignDto.setDatetime(Const.getLocalTime(Const.getUtcTime(),""));

            myRef.child(assignDto.getUp_id()).setValue(assignDto);

            loader.dismiss();
            Const.showMsg(AddUpdate.this,"Update Published",null);
            finish();

        }
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
    }

    public void NavNow(View v){
        finish();
    }
}
