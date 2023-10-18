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
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.Validations;

import bkp.stock.virtual.other.MySharedPref;
public class AddAssgin extends AppCompatActivity {

    private DatePickerDialog StartTime = null;
    private TimePickerDialog mTimePicker = null;
    private TextInputEditText et1,et3;
    private List<SubDto> subjects = new ArrayList<>();
    private TextView datetime1,subject;
    private String datestr = "";
    private UserDto ldata = null;
    private Dialog loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_add);
        loader = Const.getLoader(AddAssgin.this,getString(R.string.loading));
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.new_assign));
        initUI();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.sub_tbl);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null && snapshot.hasChildren()){
                    subjects = new ArrayList<>();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        subjects.add(dataSnapshot.getValue(SubDto.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try{
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference(FrbDb.user_tbl);
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

    public void SelSub(View v){
        try{
            PopupMenu menu = new PopupMenu(this, findViewById(R.id.subject));
            for(int x=0;x<subjects.size();x++){
                menu.getMenu().add(0,x,x,subjects.get(x).getSub_name());
            }
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    subject.setText(subjects.get(item.getItemId()).getSub_name());
                    return true;
                }
            });
            menu.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LoginNow(View v){
        if(Validations.valEdit(this,et1,getString(R.string.title))
                && Validations.valEdit(this,datetime1,getString(R.string.last_date))
                && Validations.valEdit(this,et3,getString(R.string.desc))
                && Const.isOnline(AddAssgin.this)
        ){
            if(datetime1.getText().toString().equals(getString(R.string.select_last_date))){
                Const.showMsg(AddAssgin.this,"Select Last Date",null);
                return;
            }
            if(subject.getText().toString().equals(getString(R.string.select_subject))){
                Const.showMsg(AddAssgin.this,"Select Subject",null);
                return;
            }
            loader.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.assign_tbl);

            AssignDto assignDto = new AssignDto();
            assignDto.setAss_id(myRef.push().getKey());
            assignDto.setTitle(et1.getText().toString());
            assignDto.setAss_subject(subject.getText().toString());
            assignDto.setDesc(et3.getText().toString());
            assignDto.setLast_date(datetime1.getText().toString());
            if(ldata.getType().equals(UserConst.teacher)){
                assignDto.setCreated_by_type(ldata.getType());
                assignDto.setClass_name(ldata.getRollno());
            }
            else if(ldata.getType().equals(UserConst.user)){
                if(ldata.getClassname().equals("1")){
                    assignDto.setCreated_by_type(UserConst.teacher);
                    assignDto.setClass_name(ldata.getRollno().substring(0, 6));
                }
                else {
                    assignDto.setCreated_by_type(ldata.getType());
                    assignDto.setClass_name(ldata.getRollno().substring(0, 6));
                }
            }
            else {
                assignDto.setCreated_by_type(ldata.getType());
                assignDto.setClass_name(ldata.getRollno().substring(0, 6));
            }
            assignDto.setCreated_by(new MySharedPref().getData(getApplicationContext(),"ldata",""));

            assignDto.setDatetime(Const.getLocalTime(Const.getUtcTime(),""));

            myRef.child(assignDto.getAss_id()).setValue(assignDto);

            loader.dismiss();
            Const.showMsg(AddAssgin.this,"Assignment Created",null);
            finish();

        }
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        subject = findViewById(R.id.subject);
        et3 = findViewById(R.id.et3);
        datetime1 = findViewById(R.id.datetime1);
    }

    public void SelTime(){
        if(mTimePicker!=null && mTimePicker.isShowing()){
            mTimePicker.dismiss();
            mTimePicker = null;
        }
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(AddAssgin.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                datetime1.setText( datestr+", "+selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();


    }

    public void SelDateTime(View v){
        if(StartTime!=null && StartTime.isShowing()){
            StartTime.dismiss();
            StartTime = null;
        }
        final Calendar newCalendar = Calendar.getInstance();
        StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                datestr = Const.getLocalTime(newDate.getTime().getTime(),"dd-MM-yyyy");
                SelTime();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        StartTime.show();
    }

    public void NavNow(View v){
        finish();
    }
}
