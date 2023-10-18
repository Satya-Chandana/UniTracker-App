package bkp.stock.virtual.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.dto.ClassDto1;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.Validations;

public class AddTeacher extends AppCompatActivity {

    private Dialog loader;
    private FirebaseAuth mAuth;
    private TextInputEditText et1,et2,et3,et4;
    private String type = UserConst.user;
    private TextView class_name;
    private String uid = "";
    private UserDto userDto = null;
    private List<ClassDto1> classes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add);
        loader = Const.getLoader(AddTeacher.this,getString(R.string.loading));
        mAuth = FirebaseAuth.getInstance();
        initUI();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.class_tbl1);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null && snapshot.hasChildren()){
                    classes = new ArrayList<>();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        classes.add(dataSnapshot.getValue(ClassDto1.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void SelClass(View v){
        try{
            PopupMenu menu = new PopupMenu(this, findViewById(R.id.class_name));
            for(int x=0;x<classes.size();x++){
                menu.getMenu().add(0,x,x,classes.get(x).getClass_name());
            }
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    class_name.setText(classes.get(item.getItemId()).getClass_name());
                    return true;
                }
            });
            menu.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        class_name = findViewById(R.id.class_name);
        TextView title_txt = findViewById(R.id.title);
        type = getIntent().getExtras().getString("type");

        uid = getIntent().getExtras().getString("uid");
        if(uid!=null && uid.length()>0) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
            myRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.getValue() != null) {
                        userDto = snapshot.getValue(UserDto.class);
                        if (userDto != null) {
                            ((TextView)findViewById(R.id.bt1)).setText("Update");
                            et1.setText(userDto.getName());
                            et2.setText(userDto.getEmail());
                            et3.setText(userDto.getPass());
                            et4.setText(userDto.getRollno());
                            class_name.setText(userDto.getRollno());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        if(type.equals(UserConst.teacher)){
            title_txt.setText(getString(R.string.new_teacher));
            findViewById(R.id.et4l).setVisibility(View.GONE);
            findViewById(R.id.class_rl).setVisibility(View.VISIBLE);
        }
        else{
            title_txt.setText(getString(R.string.new_student));
            findViewById(R.id.et4l).setVisibility(View.VISIBLE);
            findViewById(R.id.class_rl).setVisibility(View.GONE);
        }

    }
    public void LoginNow(View view) {

        if(Validations.valEdit(this,et1,getString(R.string.full_name))
                && Validations.valEditEmail(this,et2,getString(R.string.email1))
                && Validations.valEditPassword(this,et3,getString(R.string.password))
                && Const.isOnline(AddTeacher.this)
        ){
            if(type.equals(UserConst.teacher)){

            }
            else{
                if(Validations.valEdit(this,et4,getString(R.string.roll_no))==false){
                    return;
                }
            }

            loader.show();

            mAuth.signInWithEmailAndPassword(et2.getText().toString(), et3.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createUser(mAuth.getCurrentUser());
//                                registerUser();
//                                loader.dismiss();
//                                Const.showMsg(AddTeacher.this,"User already exist",null);
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
                                Const.showMsg(AddTeacher.this,"Authentication failed.",null);
                            }
                        } else {
                            loader.dismiss();
                            Const.showMsg(AddTeacher.this,"Authentication failed.",null);
                        }
                    }
                });
    }

    private void createUser(FirebaseUser user) {
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
            if(userDto==null){
                userDto = new UserDto();
                userDto = new UserDto();
                userDto.setUid(user.getUid());

                userDto.setClassname("0");
                userDto.setTimestamp(Const.getLocalTime(Const.getUtcTime(), "yyyy-MM-dd hh:mm"));
                userDto.setType(type);
            }
            userDto.setName(et1.getText().toString());
            userDto.setEmail(et2.getText().toString());
            userDto.setPass(et3.getText().toString());
//            userDto.setRollno(et4.getText().toString());
            if(type.equals(UserConst.teacher)){
                if(class_name.getText().toString().equals(getString(R.string.select_class))){
                    Const.showMsg(AddTeacher.this,"Select Class",null);
                    return;
                }
                userDto.setRollno(class_name.getText().toString());

            }
            else{
                userDto.setRollno(et4.getText().toString());
            }


            myRef.child(user.getUid()).setValue(userDto);
            loader.dismiss();
            Const.showMsg(AddTeacher.this,"Registration Successful",null);
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void NavNow(View v){
        finish();
    }
}
