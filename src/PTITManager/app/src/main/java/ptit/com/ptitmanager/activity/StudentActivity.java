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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ptit.com.ptitmanager.Interface.IDialogListener;
import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.adapter.StudentAdapter;
import ptit.com.ptitmanager.object.DanhSachSinhVien;
import ptit.com.ptitmanager.object.LopHoc;
import ptit.com.ptitmanager.object.SinhVien;
import ptit.com.ptitmanager.object.Student;
import ptit.com.ptitmanager.utils.DialogCustom;

import static android.support.constraint.Constraints.TAG;

public class StudentActivity extends AppCompatActivity {
    private RecyclerView rcvStudent;
    private StudentAdapter studentAdapter;
    private List<Student> listStudent;
    private ArrayList<DanhSachSinhVien> listDSSVDB = new ArrayList<>();
    private ArrayList<SinhVien> listSinhVienDB = new ArrayList<>();
    private ArrayList<LopHoc> listLopHocDB = new ArrayList<>();
    private static final String TAG = "StudentActivity";
    private String lopHocID = "";

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_student);
        trainData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 1000);
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        lopHocID = bundle.getString("LopHocID");
        Log.d(TAG, "lophocid: " + lopHocID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCustom dialogCustom = new DialogCustom(StudentActivity.this,
                        "Bạn muốn thoát mà không lưu chứ?", "YES",
                        "NO", "");
                dialogCustom.createDialog(new IDialogListener() {
                    @Override
                    public void OnYesClicked() {
                        finish();
                    }
                });
            }
        });

        listStudent = new ArrayList<>();
        ArrayList<Student> dssv = getDSSV(lopHocID, listDSSVDB, listSinhVienDB, listLopHocDB);
        Log.d(TAG, "LLLLLLLL: " + dssv.size() + "dssv");
        Log.d(TAG, "LLLLLLLL: " + listDSSVDB.size() + "DSSV");
        for (Student student : dssv) {
            listStudent.add(student);
        }
        Log.d(TAG, "init: " + listStudent.size());
        studentAdapter = new StudentAdapter(StudentActivity.this, listStudent);
        rcvStudent = findViewById(R.id.rcvListStudent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvStudent.setLayoutManager(layoutManager);
        rcvStudent.setAdapter(studentAdapter);
    }

    public void trainData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getListDSSV();
                getListSinhVien();
                getListLopHoc();
            }
        }, 500);

    }

    public void getListSinhVien() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Sinh Viên")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listSinhVienDB.add(new SinhVien(document.getString("id"), document.getString("TenSinhVien"),
                                        document.getString("MaSV"), document.getString("Lop")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getListDSSV() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Danh Sách Sinh Viên")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listDSSVDB.add(new DanhSachSinhVien(document.getString("id"), document.getString("LopHocID"),
                                        document.getString("SinhVienID"), document.getString("DiemCC"),
                                        document.getString("DiemBT"), document.getString("DiemKT"),
                                        document.getString("DiemTH"), document.getString("DiemHK"), document.getString("DiemTB")));

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

    public static ArrayList<Student> getDSSV(String LopHocID, ArrayList<DanhSachSinhVien> listDSSV, ArrayList<SinhVien> listSV, ArrayList<LopHoc> listLH) {
        ArrayList<Student> list = new ArrayList<Student>();
        LopHoc lp = new LopHoc();
        for (int i = 0; i < listLH.size(); ++i) {
            if (listLH.get(i).getId().equals(LopHocID)) {
                lp = listLH.get(i);
                break;
            }
        }

        if (lp.getTypeLopHoc().equals("1")) {
            for (int i = 0; i < listLH.size(); ++i) {
                if (listLH.get(i).getNhomMonHocID().equals(lp.getNhomMonHocID()) && !listLH.get(i).getId().equals(LopHocID)) {
                    LopHocID = new String(listLH.get(i).getId());
                    break;
                }
            }
        }
        Log.d(TAG, "getDSSV: " + LopHocID);
        for (int i = 0; i < listDSSV.size(); ++i) {
            Log.d(TAG, "getDSSV: " + listDSSV.get(i));
            if (listDSSV.get(i).getLopHocID().equals(LopHocID)) {
                for (int j = 0; j < listSV.size(); ++j) {
                    Log.d(TAG, "getDSSV: " + listDSSV.get(i).getSinhVienID() + ", " + listSV.get(j).getID());
                    if (listDSSV.get(i).getSinhVienID().equals(listSV.get(j).getID())) {
                        Log.d(TAG, "getDSSV: " + i +", " +j);
                        Student st = new Student();
                        st.setImage(R.drawable.avt);
                        st.setFullName(listSV.get(j).getTenSV());
                        st.setMaSV(listSV.get(j).getMaSV());
                        st.setDiemCC(listDSSV.get(i).getDiemCC());
                        st.setDSSVID(listDSSV.get(i).getID());
                        list.add(st);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_data, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_data) {
            DialogCustom dialogCustom = new DialogCustom(this, "Lưu dữ liệu",
                    "YES", "NO", "");
            dialogCustom.createDialog(new IDialogListener() {
                @Override
                public void OnYesClicked() {
                    for (int i = 0; i < listStudent.size(); i++) {
                        Log.d(TAG, "OnYesClicked: " + listStudent.get(i).getDSSVID() + ", " + listStudent.get(i).getDiemCC());
                        applyTexts(listStudent.get(i).getDiemCC(), listStudent.get(i).getDSSVID());
                    }
                    finish();
                }
            });
        }
        if (id == R.id.filter_data) {
            Log.d(TAG, "onOptionsItemSelected: " + listStudent.size());
            for (int i = listStudent.size() - 1; i >= 0; i--) {
                Log.d(TAG, "onOptionsItemSelected: " + listStudent.get(i).getDiemCC());
                if (listStudent.get(i).getDiemCC().equals("10")) {
                    listStudent.remove(i);
                }
            }
            studentAdapter = new StudentAdapter(StudentActivity.this, listStudent);
            rcvStudent = findViewById(R.id.rcvListStudent);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rcvStudent.setLayoutManager(layoutManager);
            rcvStudent.setAdapter(studentAdapter);
            studentAdapter.notifyDataSetChanged();
        }


        return super.onOptionsItemSelected(item);
    }

    public void applyTexts(String diemCC, String document) {
        Log.d(TAG, "applyTexts: " + document);

        Map<String, Object> update = new HashMap<>();
        update.put("diem", diemCC);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Danh Sách Sinh Viên").document(document);
        docRef.update("DiemCC", diemCC);

    }

    @Override
    public void onBackPressed() {
        DialogCustom dialogCustom = new DialogCustom(this,
                "Bạn muốn thoát mà không lưu chứ?", "YES",
                "NO", "");
        dialogCustom.createDialog(new IDialogListener() {
            @Override
            public void OnYesClicked() {
                finish();
            }
        });
    }

}
