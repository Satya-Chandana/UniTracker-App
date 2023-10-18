package bkp.stock.virtual.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bkp.stock.virtual.R;
import bkp.stock.virtual.dto.AssignDto;
import bkp.stock.virtual.dto.ClassDto1;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.Validations;

public class AddClass extends AppCompatActivity {

    private TextInputEditText et1;
    private Dialog loader;
    private String uid = "";
    private ClassDto1 classDto1 = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_add);
        initUI();
        loader = Const.getLoader(AddClass.this,getString(R.string.loading));
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.new_class));
    }


    public void LoginNow(View v) {
        if (Validations.valEdit(this, et1, getString(R.string.title))
                && Const.isOnline(AddClass.this)
        ) {
            loader.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.class_tbl1);
            if(classDto1==null) {
                classDto1 = new ClassDto1();
                classDto1.setClass_id(myRef.push().getKey());
            }
            classDto1.setClass_name(et1.getText().toString());

            myRef.child(classDto1.getClass_id()).setValue(classDto1);

            loader.dismiss();
            Const.showMsg(AddClass.this, "Class Created", null);
            finish();
        }
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        uid = getIntent().getExtras().getString("uid");
        if(uid!=null && uid.length()>0) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.class_tbl1);
            myRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.getValue() != null) {
                        classDto1 = snapshot.getValue(ClassDto1.class);
                        if (classDto1 != null) {
                            et1.setText(classDto1.getClass_name());
                            ((TextView)findViewById(R.id.bt1)).setText("Update");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void NavNow(View v){
        finish();
    }
}
