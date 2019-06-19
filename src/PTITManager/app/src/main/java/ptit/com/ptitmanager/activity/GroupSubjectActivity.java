package ptit.com.ptitmanager.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ptit.com.ptitmanager.Interface.CallBackData;
import ptit.com.ptitmanager.Interface.IDialogListener;
import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.adapter.SubjectGroupAdapter;
import ptit.com.ptitmanager.object.MonHoc;
import ptit.com.ptitmanager.object.NhomMonHoc;
import ptit.com.ptitmanager.utils.DialogCustom;

public class GroupSubjectActivity extends AppCompatActivity implements CallBackData {
    private SubjectGroupAdapter adapter;
    private RecyclerView recyclerView;
    private List<MonHoc> listMonHocDB = new ArrayList<>();
    private List<NhomMonHoc> listNhomMonHocDB = new ArrayList<>();
    private static final String TAG = "GroupSubjectActivity";
    private String monHocID = "";
    private String tenMonHoc = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_subject);

        trainData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        monHocID = bundle.getString("MonHocID");
        tenMonHoc = bundle.getString("TenMonHoc");
        Log.d(TAG, "onCreate: " + monHocID);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 3000);

    }

    public void trainData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getListNhomMonHoc();
            }
        }, 2000);
    }

    public void getListNhomMonHoc() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Nhóm Môn Học")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("MonHocID").equals(monHocID)) {
                                    listNhomMonHocDB.add(new NhomMonHoc(document.getString("id"), document.getString("MonHocID"),
                                            document.getString("STTNhom"), document.getString("LinkNhom")));
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    private void init() {
        adapter = new SubjectGroupAdapter(this, tenMonHoc, listNhomMonHocDB, getSupportFragmentManager(), this);
        recyclerView = findViewById(R.id.rcvGroupSubject);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void called(View.OnClickListener listener) {
//        listNhomMonHocDB.clear();
//        trainData();
//        adapter = new SubjectGroupAdapter(this, tenMonHoc, listNhomMonHocDB, getSupportFragmentManager(), this);
//        adapter.notifyDataSetChanged();
        finish();

    }
}
