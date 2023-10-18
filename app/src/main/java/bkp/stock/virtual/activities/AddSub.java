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
import bkp.stock.virtual.dto.SubDto;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.Validations;

public class AddSub extends AppCompatActivity {

    private TextInputEditText et1;
    private Dialog loader;
    private String uid = "";
    private SubDto subDto = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_add);
        initUI();
        loader = Const.getLoader(AddSub.this,getString(R.string.loading));
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.new_subject));
    }


    public void LoginNow(View v) {
        if (Validations.valEdit(this, et1, getString(R.string.title))
                && Const.isOnline(AddSub.this)
        ) {
            loader.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.sub_tbl);
            if(subDto==null) {
                subDto = new SubDto();
                subDto.setSub_id(myRef.push().getKey());
            }
            subDto.setSub_name(et1.getText().toString());

            myRef.child(subDto.getSub_id()).setValue(subDto);

            loader.dismiss();
            Const.showMsg(AddSub.this, "Subject Created", null);
            finish();
        }
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        uid = getIntent().getExtras().getString("uid");
        if(uid!=null && uid.length()>0) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.sub_tbl);
            myRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.getValue() != null) {
                        subDto = snapshot.getValue(SubDto.class);
                        if (subDto != null) {
                            et1.setText(subDto.getSub_name());
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
