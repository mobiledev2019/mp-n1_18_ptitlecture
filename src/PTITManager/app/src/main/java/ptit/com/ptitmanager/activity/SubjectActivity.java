package ptit.com.ptitmanager.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ptit.com.ptitmanager.Interface.IDialogListener;
import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.adapter.SubjectAdapter;
import ptit.com.ptitmanager.object.LichDay;
import ptit.com.ptitmanager.object.LopHoc;
import ptit.com.ptitmanager.object.MonHoc;
import ptit.com.ptitmanager.object.NhomMonHoc;
import ptit.com.ptitmanager.object.Subject;
import ptit.com.ptitmanager.utils.DialogCustom;

public class SubjectActivity extends AppCompatActivity {
    private static final String TAG = "SubjectActivity";
    private ArrayList<MonHoc> listMonHocDB = new ArrayList<>();
    private ArrayList<LichDay> listLichDayDB = new ArrayList<>();
    private ArrayList<LopHoc> listLopHocDB = new ArrayList<>();
    private ArrayList<NhomMonHoc> listNhomMonHocDB = new ArrayList<>();
    private SubjectAdapter adapter;
    private RecyclerView recyclerView;
    private SharedPreferences pref;
    private String GV_ID = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        trainData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCustom dialogCustom = new DialogCustom(SubjectActivity.this, "Bạn muốn thoát mà không lưu chứ?", "YES", "NO", "");
                dialogCustom.createDialog(new IDialogListener() {
                    @Override
                    public void OnYesClicked() {
                        finish();
                    }
                });
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoringPref();
    }

    public void getListLichDay() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lịch Dạy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listLichDayDB.add(new LichDay(document.getString("id"), document.getString("HocKyID"),
                                        document.getString("LopHocID"), document.getString("GiangVienID"),
                                        document.getString("Kip"), document.getString("NgayDay"),
                                        document.getString("CheckIn"), document.getString("KieuTietHoc")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getListLopHoc() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lớp Học")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listLopHocDB.add(new LopHoc(document.getString("id"), document.getString("NhomMonHocID"),
                                        document.getString("PhongHocID"), document.getString("typeLopHoc")));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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

                                listNhomMonHocDB.add(new NhomMonHoc(document.getString("id"), document.getString("MonHocID"),
                                        document.getString("STTNhom"), document.getString("LinkNhom")));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static ArrayList<MonHoc> getMonhoc(String GiangVienID, ArrayList<LichDay> listLD, ArrayList<LopHoc> listLH, ArrayList<MonHoc> listMH, ArrayList<NhomMonHoc> listNMH){
        ArrayList<MonHoc> list = new ArrayList<MonHoc>();
        ArrayList<String> index = new ArrayList<String>();
        for(int i = 0; i < listLD.size(); ++i )
            if(listLD.get(i).getGiangVienID().equals(GiangVienID)){
                for(int j = 0; j < listLH.size(); ++j)
                    if(listLD.get(i).getLopHocID().equals(listLH.get(j).getId())){
                        for(int k = 0; k < listNMH.size(); ++k)
                            if(listLH.get(j).getNhomMonHocID().equals(listNMH.get(k).getID())){
                                for(int h = 0; h < listMH.size(); ++h)
                                    if(listNMH.get(k).getMonHocID().equals(listMH.get(h).getID())){
                                        index.add(listMH.get(h).getID());
                                    }
                            }
                    }
            }
        if(index.size() == 0) return list;
        Collections.sort(index);
        for(int i =0 ; i < listMH.size(); ++i)
            if(listMH.get(i).getID().equals(index.get(0))) list.add(listMH.get(i));

        for(int j = 1; j < index.size(); ++j)
            if(!index.get(j).equals(index.get(j-1))){
                for(int i =0 ; i < listMH.size(); ++i)
                    if(listMH.get(i).getID().equals(index.get(j))) list.add(listMH.get(i));
            }

        return list;
    }

    public void restoringPref() {
        pref = getSharedPreferences("PTITManager", MODE_PRIVATE);
        GV_ID = pref.getString("GV_ID", "");
        Log.d(TAG, "onCreate:GV_ID = " + GV_ID);
    }

    public void trainData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getListMonHoc();
                getListLichDay();
                getListLopHoc();
                getListNhomMonHoc();
            }
        }, 2000);
    }

    public void getListMonHoc() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Môn Học")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listMonHocDB.add(new MonHoc(document.getString("id"), document.getString("TenMonHoc")));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void init() {
        ArrayList<MonHoc> monhoc = getMonhoc(GV_ID, listLichDayDB, listLopHocDB, listMonHocDB, listNhomMonHocDB);
        Log.d(TAG, "init: " + monhoc.size());

        recyclerView = findViewById(R.id.rcvListSubject);
        adapter = new SubjectAdapter(this, monhoc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DialogCustom dialogCustom = new DialogCustom(this, "Bạn muốn thoát mà không lưu chứ?", "YES", "NO", "");
        dialogCustom.createDialog(new IDialogListener() {
            @Override
            public void OnYesClicked() {
                finish();
            }
        });
    }
}
