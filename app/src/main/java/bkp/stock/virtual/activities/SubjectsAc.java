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
import bkp.stock.virtual.adapters.SubAdp;
import bkp.stock.virtual.adapters.SudentAdp;
import bkp.stock.virtual.adapters.TeachAdp;
import bkp.stock.virtual.dto.AssignDto;
import bkp.stock.virtual.dto.ClassDto1;
import bkp.stock.virtual.dto.SubDto;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.fragments.Assignments;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.FrbDb;

import bkp.stock.virtual.other.MySharedPref;
public class SubjectsAc extends AppCompatActivity implements ItemClickListner {

    private RecyclerView rlv1;
    private List<SubDto> l1 = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_ac);
        initUI();

        addAdp();
    }
    public void  AddNow(View v){
        Intent intent = new Intent(getApplicationContext(), AddSub.class);
        intent.putExtra("uid","");
        startActivity(intent);
    }
    public void NavNow(View v){
        finish();
    }
    private void addAdp() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.sub_tbl);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                l1 = new ArrayList<>();
                if(snapshot.getValue()!=null && snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        l1.add(dataSnapshot.getValue(SubDto.class));
                    }
                }
                addAdp1();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                l1 = new ArrayList<>();
                addAdp1();
            }
        });


    }

    private void addAdp1() {
        SubAdp catAdp = new SubAdp(SubjectsAc.this,l1);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SubjectsAc.this, LinearLayoutManager.VERTICAL, false);
        rlv1.setLayoutManager(layoutManager);
        rlv1.setItemAnimator(new DefaultItemAnimator());
        rlv1.setNestedScrollingEnabled(false);
        rlv1.setAdapter(catAdp);
    }

    private void initUI() {
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.classrooms));
        rlv1 = findViewById(R.id.rlv1);
    }
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick1(int position) {

    }

    @Override
    public void onItemClick(int position, View v) {

    }

    @Override
    public void onItemClick(int position, int position1) {

    }
}
