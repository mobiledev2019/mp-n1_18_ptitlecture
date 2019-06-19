package ptit.com.ptitmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ptit.com.ptitmanager.BuildConfig;
import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.object.DanhSachSinhVien;
import ptit.com.ptitmanager.object.LopHoc;
import ptit.com.ptitmanager.object.NhomMonHoc;
import ptit.com.ptitmanager.object.SinhVien;
import ptit.com.ptitmanager.object.Student;

public class ImportExcelActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1000;

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;
    private List<Student> studentList;
    private Uri uri;

    private ArrayList<DanhSachSinhVien> listDSSVDB = new ArrayList<>();
    private ArrayList<SinhVien> listSinhVienDB = new ArrayList<>();
    private ArrayList<LopHoc> listLopHocDB = new ArrayList<>();
    private ArrayList<NhomMonHoc> listNhomMonHocDB = new ArrayList<>();

    Button btnUpDirectory,btnSDCard;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;



    ListView lvInternalStorage;
    Button btnOpenSDcard,btnSendFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_excel);
        trainData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 1500);
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        final String monHocID = bundle.getString("MonHocID");
        lvInternalStorage = findViewById(R.id.lvFile);
        btnOpenSDcard = findViewById(R.id.btnOpenSDcard);
        btnSendFile = findViewById(R.id.btnSendFile);
        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(uri + "     aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                Intent intent = new Intent(getApplicationContext(), UploadFileToGmailActivity.class);
                intent.putExtra("link_file",uri.toString());
                startActivity(intent);
            }
        });

        if (checkPermissionForReadExtertalStorage() == false){
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            checkPermissionForReadExtertalStorage();
        }

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);

                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    try {
                        changeData(lastDirectory, monHocID, listNhomMonHocDB, listLopHocDB, listDSSVDB, listSinhVienDB);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(ImportExcelActivity.this, "" + lastDirectory, Toast.LENGTH_SHORT).show();
//                    finish();

                }else
                {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                }
            }
        });


        //Opens the SDCard or phone memory
        btnOpenSDcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
                checkInternalStorage();
            }
        });

    }


    public void trainData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getListDSSV();
                getListSinhVien();
                getListLopHoc();
                getListNhomMonHoc();
            }
        }, 1000);

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
                Log.d(TAG, "getDSSV: " + listLH.get(i).toString());
                lp = listLH.get(i);
                break;
            }
        }
        Log.d(TAG, "lp: " + lp.toString() + ", " + LopHocID);

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

    public void changeData(String filePath, String MonHocID, ArrayList<NhomMonHoc> listNMH, ArrayList<LopHoc> listLH, ArrayList<DanhSachSinhVien> listDSSVDB, ArrayList<SinhVien> listSinhVienDB) throws IOException {
//        String newFilePath = filePath.substring(0,filePath.length()-5) + "_changed.xlsx";
        String kkk[] = filePath.split("/");
        String s[] = kkk[kkk.length-1].split("\\.");
        String newFilePath = s[0] + "_changed.xlsx";
        String fileP = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + kkk[kkk.length-1];
        FileInputStream inputStream = new FileInputStream(fileP);
        Workbook wb2007 = new XSSFWorkbook(inputStream);
        Log.d(TAG, "changeData: " + wb2007.getSheet("Nhóm 1"));
        Sheet sheet;
        Row row;
        Cell cell;
        for(int i = 0; i < listNMH.size(); ++i){
            if(listNMH.get(i).getMonHocID().equals(MonHocID)){
                String NMH = listNMH.get(i).getSttNhom();
                String MaLH = null;
                for(int j = 0; j < listLH.size(); ++j){
                    if(listLH.get(j).getTypeLopHoc().equals("0") && listLH.get(j).getNhomMonHocID().equals(listNMH.get(i).getID())){
                        MaLH = listLH.get(j).getId();
                        break;
                    }
                }
                ArrayList<Student> listST = getDSSV(MaLH,listDSSVDB,listSinhVienDB,listLH); // đoạn này truyền giống bên điểm danh

                sheet = wb2007.getSheet(NMH);
                for(int k = 0; k < listST.size(); ++k){
                    row = sheet.getRow(1+k);
                    cell = row.getCell(3);
                    String maSV = cell.getStringCellValue();
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa " + maSV);
                    cell = row.createCell(4);
                    for(int h = 0; h < listST.size(); ++h){
                        if(listST.get(h).getMaSV().equals(maSV)){
                            cell.setCellValue(Double.parseDouble(listST.get(h).getDiemCC()));
                            cell = row.getCell(5);
                            System.out.println("diem KT: "+ cell.getNumericCellValue());
                            listST.get(h).setDiemKT(String.valueOf(cell.getNumericCellValue()));
                            cell = row.getCell(6);
                            listST.get(h).setDiemBT(String.valueOf(cell.getNumericCellValue()));
                            System.out.println("diem BT: "+ cell.getNumericCellValue());
                            break;
                        }
                    }

                }
                // đoạn này gửi dữ liệu điểm của 1 nhóm lên
                for (int j = 0; j < listST.size(); j++) {
                    Map<String, Object> update = new HashMap<>();
                    update.put("diemKT", listST.get(j).getDiemKT());
                    Log.d(TAG, "changeDataLLLLL: " + listST.get(j).getDiemKT() + ", " + listST.get(j).getDiemBT());
                    update.put("diemBT", listST.get(j).getDiemBT());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("Danh Sách Sinh Viên").document(listST.get(i).getDSSVID());
                    docRef.update("DiemKT", listST.get(j).getDiemKT());
                    docRef.update("DiemBT", listST.get(j).getDiemBT());
                }



            }
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),newFilePath);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        wb2007.write(out);
        out.close();
        uri = FileProvider.getUriForFile(ImportExcelActivity.this, BuildConfig.APPLICATION_ID,file);

    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            }
            else{
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

            for (int i = 0; i < listFile.length; i++)
            {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e){
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}


