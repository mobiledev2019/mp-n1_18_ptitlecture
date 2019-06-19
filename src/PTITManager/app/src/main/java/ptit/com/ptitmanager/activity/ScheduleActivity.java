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
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.adapter.ScheduleAdapter;
import ptit.com.ptitmanager.object.LichDay;
import ptit.com.ptitmanager.object.LopHoc;
import ptit.com.ptitmanager.object.MonHoc;
import ptit.com.ptitmanager.object.NhomMonHoc;
import ptit.com.ptitmanager.object.PhongHoc;
import ptit.com.ptitmanager.object.StudySchedule;
import ptit.com.ptitmanager.object.Subject;

public class ScheduleActivity extends AppCompatActivity {
    private static final String TAG = "ScheduleActivity";
    private RecyclerView rcvSchedule;
    private ScheduleAdapter mAdapter;
    private List<Subject> listSchedule;
    private String scheduleCurrentDay = "";
    private ProgressBar prgLoadingSchedule;

    private List<LichDay> listLichDayDB = new ArrayList<>();
    private List<LopHoc> listLopHocDB = new ArrayList<>();
    private List<MonHoc> listMonHocDB = new ArrayList<>();
    private List<NhomMonHoc> listNhomMonHocDB = new ArrayList<>();
    private List<PhongHoc> listPhongHocDB = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        prgLoadingSchedule = findViewById(R.id.prgLoadingSchedule);
        trainData();
        getSize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        scheduleCurrentDay = bundle.getString("CurrentDay");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listSchedule = new ArrayList<>();

        ArrayList<Subject> subjectByDay = getSubjectByDay(scheduleCurrentDay);
        for (Subject subject : subjectByDay) {
            int i = checkSubjectOnTime(subject);
            Log.d(TAG, "init: " + i);
            Log.d(TAG, "init: " + subject.getNgayDay());
            listSchedule.add(new Subject(subject.getLopHocID(), subject.getSubjectName(), subject.getSttNhom(), subject.getClassRoom(), subject.getStudyTime(), subject.getTypeSubject(), subject.getLichDayID(), String.valueOf(i), subject.getNgayDay()));
        }
        mAdapter = new ScheduleAdapter(this, listSchedule);
        rcvSchedule = findViewById(R.id.rcvSchedule);
        rcvSchedule.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvSchedule.setLayoutManager(layoutManager);
        rcvSchedule.setAdapter(mAdapter);
        rcvSchedule.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());

    }


    public static int sosanh(String x, String y) {
        String xx[] = x.split("/");
        String yy[] = y.split("/");
        if (xx[2].compareTo(yy[2]) > 0) {
            return 1;
        }
        if (xx[2].compareTo(yy[2]) < 0) {
            return -1;
        }
        if (xx[2].compareTo(yy[2]) == 0) {
            if (xx[1].compareTo(yy[1]) > 0) {
                return 1;
            }
            if (xx[1].compareTo(yy[1]) < 0) {
                return -1;
            }
            if (xx[1].compareTo(yy[1]) == 0) {
                if (xx[0].compareTo(yy[0]) > 0) {
                    return 1;
                }
                if (xx[0].compareTo(yy[0]) < 0) {
                    return -1;
                }
                if (xx[0].compareTo(yy[0]) == 0) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public int checkSubjectOnTime(Subject sb){
        int ans = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(cal.getTime());
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date c = new Date();
        String day = sdf.format(c);



        Log.d(TAG, "checkSubjectOnTime: " + day);
        Log.d(TAG, "checkSubjectOnTime: " + sb.getNgayDay());
        //System.out.println(currentTime);
        if(sosanh(sb.getNgayDay(),day) <0){
            Log.d(TAG, "checkSubjectOnTime: " + 0);
            return 0;
        }
        if(sosanh(sb.getNgayDay(),day) >0){
            Log.d(TAG, "checkSubjectOnTime: " + 1);
            return 1;
        }

        String time = sb.getStudyTime();
        String p[] = time.split("-");

        String l[] = currentTime.split(":");
        String r[] = new String[2];
        r[0] = p[0].substring(0,2);
        r[1] = p[1].substring(1,3);
        if(l[0].compareTo(r[0]) <0){
            ans = 1;
        }
        if(l[0].compareTo(r[0]) >=0 && l[0].compareTo(r[1]) <0){
            ans = 1;
        }
        if(l[0].compareTo(r[1]) == 0 && l[1].compareTo("00") == 0){
            ans = 1;
        }

        return ans;
    }

    public void trainData() {
        prgLoadingSchedule.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getListLichDay();
                getListLopHoc();
                getListMonHoc();
                getListNhomMonHoc();
                getListPhongHoc();
            }
        }, 2000);

    }

    public void getSize(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prgLoadingSchedule.setVisibility(View.INVISIBLE);
                init();
            }
        }, 3000);
    }

    public void getListPhongHoc() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Phòng Học")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listPhongHocDB.add(new PhongHoc(document.getString("id"), document.getString("TenPhongHoc")));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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

    public  ArrayList<Subject> getSubjectByDay(String day){
        ArrayList<Subject> list = new ArrayList<Subject>();
        for(int i = 0; i < listLichDayDB.size(); ++i){
            if(day.equals(listLichDayDB.get(i).getNgayDay())){
                Subject sb = new Subject();
                sb.setNgayDay(listLichDayDB.get(i).getNgayDay());
                sb.setLopHocID(listLichDayDB.get(i).getLopHocID());
                String typeSchedule = listLichDayDB.get(i).getKieuTietHoc();
                sb.setTypeSubject(typeSchedule);
                sb.setLichDayID(listLichDayDB.get(i).getID());
                String kip = listLichDayDB.get(i).getKip();
                //  1 là tiết thực hành
                // 0 là tiết lý thuyết
                if(typeSchedule.equals("1")){
                    if(kip.equals("1")) sb.setStudyTime("08h - 12h");
                    if(kip.equals("2")) sb.setStudyTime("12h - 16h");
                    if(kip.equals("3")) sb.setStudyTime("16h - 20h");
                }
                if(typeSchedule.equals("0")){
                    if(kip.equals("1")) sb.setStudyTime("07h - 09h");
                    if(kip.equals("2")) sb.setStudyTime("09h - 11h");
                    if(kip.equals("3")) sb.setStudyTime("12h - 14h");
                    if(kip.equals("4")) sb.setStudyTime("14h - 16h");
                    if(kip.equals("5")) sb.setStudyTime("16h - 18h");
                    if(kip.equals("6")) sb.setStudyTime("18h - 20h");
                }

                for(int j = 0; j < listLopHocDB.size(); ++j)
                    if(listLichDayDB.get(i).getLopHocID().equals(listLopHocDB.get(j).getId())){
                        for(int k = 0; k < listPhongHocDB.size(); ++k){
                            if(listLopHocDB.get(j).getPhongHocID().equals(listPhongHocDB.get(k).getID())){
                                sb.setClassRoom(listPhongHocDB.get(k).getTenPhongHoc());
                            }
                        }
                        for(int k = 0; k < listNhomMonHocDB.size(); ++k){
                            if(listLopHocDB.get(j).getNhomMonHocID().equals(listNhomMonHocDB.get(k).getID())){
                                sb.setSttNhom("Nhóm " + listNhomMonHocDB.get(k).getSttNhom());
                                for(int h = 0; h < listMonHocDB.size(); ++h){
                                    if(listNhomMonHocDB.get(k).getMonHocID().equals(listMonHocDB.get(h).getID())){
                                        sb.setSubjectName(listMonHocDB.get(h).getTenMonHoc());
                                    }
                                }

                            }

                        }
                    }
                list.add(sb);
            }
        }

        return list;

    }

}
