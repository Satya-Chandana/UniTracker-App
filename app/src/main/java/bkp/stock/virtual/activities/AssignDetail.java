package bkp.stock.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.adapters.AssAdp;
import bkp.stock.virtual.adapters.ClassAdp;
import bkp.stock.virtual.adapters.SudentAdp;
import bkp.stock.virtual.adapters.TeachAdp;
import bkp.stock.virtual.dto.AssignDto;
import bkp.stock.virtual.dto.ClassDto1;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.fragments.Assignments;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.FrbDb;

import bkp.stock.virtual.other.MySharedPref;
public class AssignDetail extends AppCompatActivity {

    private TextView txt1,txt2,txt20,txt3,txt4;
    private AssignDto assignDto = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ass_dt);
        initUI();

        setData();
    }

    private void setData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.assign_tbl);
        myRef.child(getIntent().getExtras().getString("uid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getValue() != null) {
                    assignDto = snapshot.getValue(AssignDto.class);
                    if (assignDto != null) {
                        txt1.setText(assignDto.getTitle());
                        txt2.setText(assignDto.getLast_date());
                        txt4.setText(assignDto.getDesc());
                        txt3.setText("Subject: "+assignDto.getAss_subject());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
                        myRef.child(assignDto.getCreated_by()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot!=null && snapshot.getValue()!=null){
                                    UserDto userDto = snapshot.getValue(UserDto.class);
                                    if(userDto!=null){
                                        txt20.setText("By: "+userDto.getName());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void NavNow(View v){
        finish();
    }


    private void initUI() {
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.assigns));
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt20 = findViewById(R.id.txt20);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);

    }
}
