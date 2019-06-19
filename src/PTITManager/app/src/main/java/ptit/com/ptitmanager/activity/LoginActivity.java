package ptit.com.ptitmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.object.DanhSachSinhVien;
import ptit.com.ptitmanager.object.DiemDanh;
import ptit.com.ptitmanager.object.GiangVien;
import ptit.com.ptitmanager.object.HocKy;
import ptit.com.ptitmanager.object.LichDay;
import ptit.com.ptitmanager.object.LopHoc;
import ptit.com.ptitmanager.object.MonHoc;
import ptit.com.ptitmanager.object.NhomMonHoc;
import ptit.com.ptitmanager.object.PhongHoc;
import ptit.com.ptitmanager.object.SinhVien;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private String username = "admin";
    private String password = "admin";
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private ProgressBar prbLogin;

    private List<DanhSachSinhVien> listDSSVDB = new ArrayList<>();
    private List<SinhVien> listSinhVienDB = new ArrayList<>();
    private List<DiemDanh> listDiemDanhDB = new ArrayList<>();
    private List<GiangVien> listGiangVienDB = new ArrayList<>();
    private List<HocKy> listHocKyDB = new ArrayList<>();
    private List<LichDay> listLichDayDB = new ArrayList<>();
    private List<LopHoc> listLopHocDB = new ArrayList<>();
    private List<MonHoc> listMonHocDB = new ArrayList<>();
    private List<NhomMonHoc> listNhomMonHocDB = new ArrayList<>();
    private List<PhongHoc> listPhongHocDB = new ArrayList<>();

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;
    private int maGV = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pre = getSharedPreferences("PTITManager", MODE_PRIVATE);
        editor = pre.edit();
        trainData();
        init();
    }

    private void init() {

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        prbLogin = findViewById(R.id.progressBar);
        prbLogin.setVisibility(View.INVISIBLE);
        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Handler handler = new Handler();
                    prbLogin.setVisibility(View.VISIBLE);
                    for (int i = 0; i < listGiangVienDB.size(); i++) {
                        Log.d(TAG, i + ": " + listGiangVienDB.get(i).getEmail());
//                        if (etUsername.getText().toString().equals("")) {
//                            prbLogin.setVisibility(View.INVISIBLE);
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    prbLogin.setVisibility(View.INVISIBLE);
//                                    etUsername.setError("Chưa nhập tên đăng nhập");
//                                }
//                            }, 1500);
//
//                        } else if (!etUsername.getText().toString().equals(listGiangVienDB.get(i).getEmail())) {
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    prbLogin.setVisibility(View.INVISIBLE);
//                                    etUsername.setError("Vui lòng nhập đúng tên đăng nhập");
//                                }
//                            }, 1500);
//                        } else if (etPassword.getText().toString().equals("")) {
//                            prbLogin.setVisibility(View.INVISIBLE);
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    prbLogin.setVisibility(View.INVISIBLE);
//                                    etPassword.setError("Chưa nhập mật khẩu");
//                                }
//                            }, 1500);
//                        } else if (!etPassword.getText().toString().equals(listGiangVienDB.get(i).getPassword())) {
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    prbLogin.setVisibility(View.INVISIBLE);
//                                    etPassword.setError("Vui lòng nhập đúng mật khẩu");
//                                }
//                            }, 1500);
//
//                        } else {
                            if (etUsername.getText().toString().equals(listGiangVienDB.get(i).getEmail()) &&
                                    etPassword.getText().toString().equals(listGiangVienDB.get(i).getPassword())) {
                                editor.clear();
                                maGV = i;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        prbLogin.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 3000);

                            }
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGV_ID(maGV);
    }

    public void saveGV_ID(int i){
        editor.putString("GV_ID", listGiangVienDB.get(i).getID());
        editor.commit();
    }
    public void trainData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                getListDSSV();
//                getListDiemDanh();
                getListGiangVien();
//                getListHocKy();
//                getListLichDay();
//                getListLopHoc();
//                getListMonHoc();
//                getListNhomMonHoc();
//                getListPhongHoc();
//                getListSinhVien();
            }
        }, 2000);
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

    public void getListDiemDanh() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Điểm Danh")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listDiemDanhDB.add(new DiemDanh(document.getString("id"), document.getString("LichDayID"),
                                        document.getString("DanhSachSVID"), document.getString("TrangThai")));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getListGiangVien() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Giảng Viên")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listGiangVienDB.add(new GiangVien(document.getString("id"), document.getString("TenGiangVien"),
                                        document.getString("Email"), document.getString("Password")));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getListHocKy() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Học Kỳ")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listHocKyDB.add(new HocKy(document.getString("id"), document.getString("TenHocKy"),
                                        document.getString("NgayBatDau"), document.getString("NgayKetThuc")));

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
}
