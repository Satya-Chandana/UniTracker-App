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
import bkp.stock.virtual.adapters.ClassAdp;
import bkp.stock.virtual.adapters.TeachAdp;
import bkp.stock.virtual.dto.ClassDto1;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.MySharedPref;

public class Teachers extends AppCompatActivity implements ItemClickListner {

    private RecyclerView rlv1;
    private List<UserDto> l1 = new ArrayList<>();
    private String type = UserConst.user;
    private UserDto ldata = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);
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

                            addAdp();

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

    public void  AddNow(View v){
        Intent intent = new Intent(getApplicationContext(),AddTeacher.class);
        intent.putExtra("type", type);
        intent.putExtra("uid", "");
        startActivity(intent);
    }

    public void NavNow(View v){
        finish();
    }

    private void addAdp() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.user_tbl);

        myRef.orderByChild(UserConst.type).equalTo(type)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                l1 = new ArrayList<>();
                if(snapshot.getValue()!=null && snapshot.hasChildren()){

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        UserDto userDto = dataSnapshot.getValue(UserDto.class);
                        if(ldata.getType().equals(UserConst.teacher) && userDto.getType().equals(UserConst.user)){

                            if(ldata.getRollno().equals(userDto.getRollno().substring(0, 6))) {
                                l1.add(userDto);
                            }

                        }
                        else {
                            l1.add(userDto);
                        }
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
        TeachAdp catAdp = new TeachAdp(Teachers.this,l1);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(Teachers.this, LinearLayoutManager.VERTICAL, false);
        rlv1.setLayoutManager(layoutManager);
        rlv1.setItemAnimator(new DefaultItemAnimator());
        rlv1.setNestedScrollingEnabled(false);
        rlv1.setAdapter(catAdp);
    }

    private void initUI() {
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.students));
        type = getIntent().getExtras().getString("type");
        if(type.equals(UserConst.teacher)){
            title_txt.setText(getString(R.string.teachers));
        }

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
