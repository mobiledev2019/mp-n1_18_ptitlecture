package ptit.com.ptitmanager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ptit.com.ptitmanager.Interface.IDialogListener;
import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.adapter.ScheduleAdapter;
import ptit.com.ptitmanager.adapter.StudyScheduleAdapter;
import ptit.com.ptitmanager.object.LichDay;
import ptit.com.ptitmanager.object.LopHoc;
import ptit.com.ptitmanager.object.MonHoc;
import ptit.com.ptitmanager.object.NhomMonHoc;
import ptit.com.ptitmanager.object.PhongHoc;
import ptit.com.ptitmanager.object.StudySchedule;
import ptit.com.ptitmanager.object.Subject;
import ptit.com.ptitmanager.utils.DialogCustom;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StudyScheduleAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";
    private String hocKy[] = {
            "Học Kì 1",
            "Học Kì 2",
    };
    private SharedPreferences pref, pref1;
    private String GV_ID = "";
    private List<StudySchedule> studySchedules;
    private RecyclerView rcvStudySchedule;
    private StudyScheduleAdapter adapter;
    private String weeks[] = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    String currentWeek = null;
    String startDate = null;
    String endDate = null;
    ArrayList<Pair<String, String>> pairs;
    ArrayList<String> listAllDays = new ArrayList<>();
    private ProgressBar prgLoadMain;

    private Spinner spnHocKy, spnWeek;
    private ImageView imgLine;
    private boolean isSpinnerTouched = false;
    private boolean checkLoadData = false;

    //schedule

    private RecyclerView rcvSchedule;
    private ScheduleAdapter mAdapter;
    private List<Subject> listSchedule;
    private String scheduleCurrentDay = "";

    private List<LichDay> listLichDayDB = new ArrayList<>();
    private List<LopHoc> listLopHocDB = new ArrayList<>();
    private List<MonHoc> listMonHocDB = new ArrayList<>();
    private List<NhomMonHoc> listNhomMonHocDB = new ArrayList<>();
    private List<PhongHoc> listPhongHocDB = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        imgLine = findViewById(R.id.imgLine);
        prgLoadMain = findViewById(R.id.prgLoadingMain);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ngayDayTrongTuan();
        trainData();
        init();
    }


    private void init() {
        //Học kì
        spnHocKy = findViewById(R.id.spnKiHoc);
        ArrayAdapter<String> typesScoreAdapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hocKy);
        typesScoreAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnHocKy.setAdapter(typesScoreAdapter1);
        spnHocKy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                studySchedules.clear();
                return false;
            }
        });
        spnHocKy.setOnItemSelectedListener(new MyProcessHocKyEvent());
//        imgLine = findViewById(R.id.imgLine);
        //Tuần
        spnWeek = findViewById(R.id.spnWeek);
        ArrayAdapter<String> typesScoreAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, weeks);
        typesScoreAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnWeek.setAdapter(typesScoreAdapter2);
        spnWeek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                studySchedules.clear();
                isSpinnerTouched = true;
                return false;
            }
        });
        spnWeek.setOnItemSelectedListener(new MyProcessWeekEvent());

        rcvStudySchedule = findViewById(R.id.rcvLichHoc);
        studySchedules = new ArrayList<>();
        adapter = new StudyScheduleAdapter(this, studySchedules);
        rcvStudySchedule.setAdapter(adapter);
        adapter.setOnItemClickListener(MainActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvStudySchedule.setLayoutManager(layoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        restoringPref();
    }

    public void restoringPref() {
        pref = getSharedPreferences("PTITManager", MODE_PRIVATE);
        GV_ID = pref.getString("GV_ID", "");
    }

    private void getWeek(int kiHoc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Học Kỳ").document(String.valueOf((kiHoc + 1)));

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String hocky = document.getString("TenHocKy");
                        if (hocky.equals("" + hocKy[1])) {
                            String week = document.getString("NgayBatDau");
                            Log.d(TAG, "Ngay bat dau: " + week);

                            try {
                                currentWeek = String.valueOf(getCurrentWeek(listAllWeek(week)));
                                Log.d(TAG, "Tuần hiện tại: " + currentWeek);
                                pairs = listAllWeek(week);
                                Log.d(TAG, "onComplete: " + pairs);
                                startDate = pairs.get(Integer.parseInt(currentWeek) - 1).first;
                                Log.d(TAG, "Ngay bat dau: " + startDate);
                                endDate = pairs.get(Integer.parseInt(currentWeek) - 1).second;
                                Log.d(TAG, "Ngay ket thuc: " + endDate);

                                for (int i = 0; i < pairs.size(); i++) {
                                    weeks[i] = "Tuần " + (i + 1) + " (" + pairs.get(i).first + " - " + pairs.get(i).second + ")";
                                    Log.d(TAG, "week: " + weeks[i] + "\n");
                                    if (i == Integer.parseInt(currentWeek)) {
                                        setSpinText(spnWeek, weeks[i - 1]);
                                    }
                                }

                                ArrayList<String> listDay = getListDay(startDate, endDate, listAllDays);
                                Log.d(TAG, "size List All Day: " + listAllDays.size());

                                ArrayList<Pair<Pair<Integer, String>, Integer>> schedulePerWeek = getSchedulePerWeek(listDay);
                                Log.d(TAG, "scheduleSize: " + schedulePerWeek.size());

                                if (schedulePerWeek.size() == 0) {
                                    prgLoadMain.setVisibility(View.INVISIBLE);
                                    imgLine.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "Tuần này không phải lên lớp nha", Toast.LENGTH_SHORT).show();
                                } else {
                                    imgLine.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < schedulePerWeek.size(); ++i) {
                                        studySchedules.add(new StudySchedule("Thứ " + schedulePerWeek.get(i).first.first, schedulePerWeek.get(i).second + " kíp", schedulePerWeek.get(i).first.second));
                                    }
                                }
                                prgLoadMain.setVisibility(View.INVISIBLE);

                                rcvStudySchedule.setVisibility(View.VISIBLE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void ngayDayTrongTuan() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lịch Dạy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "GV_ID_DEMO_1= " + document.getString("GiangVienID"));
                                Log.d(TAG, "GV_ID_DEMO_2= " + GV_ID);
                                if (document.getString("GiangVienID").equals(GV_ID)) {
                                    listAllDays.add(document.getString("NgayDay"));
                                    for (String day : listAllDays) {
                                        Log.d(TAG, "Day: " + day + "\n");
                                    }
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //hiển thị tuần hiện tại lên spinner tuần
    public void setSpinText(Spinner spin, String text) {
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            if (spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }

    }


    public static ArrayList<String> getListDay(String startDay, String endDay, ArrayList<String> listAllDay) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < listAllDay.size(); ++i) {
            Log.d(TAG, "getListDay: " + listAllDay.get(i) + "\n");
            if (sosanh(startDay, listAllDay.get(i)) <= 0 && sosanh(listAllDay.get(i), endDay) <= 0) {
                list.add(listAllDay.get(i));
            }
        }
        return list;
    }

    //lấy ra tất cả các tuần của kì học
    public static ArrayList<Pair<String, String>> listAllWeek(String startDay) throws
            ParseException {
        ArrayList<Pair<String, String>> list = new ArrayList<Pair<String, String>>();

        for (int i = 1; i <= 23; ++i) {
            String start = startDay;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(start));
            c.add(Calendar.DATE, 6);
            startDay = sdf.format(c.getTime());
            list.add(new Pair<String, String>(start, startDay));
            c.add(Calendar.DATE, 1);
            startDay = sdf.format(c.getTime());
        }
        return list;

    }

    //lấy tuần hiện tại
    public static int getCurrentWeek(ArrayList<Pair<String, String>> listWeek) {
        int cnt = 1;
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //Calendar c = Calendar.getInstance();
        Date c = new Date();
        String day = sdf.format(c);
        // System.out.println(day);
        for (int i = 0; i < listWeek.size(); ++i) {
            String st = listWeek.get(i).first;
            String ed = listWeek.get(i).second;
            if (sosanh(st, day) <= 0 && sosanh(day, ed) <= 0) {
                cnt = i + 1;
                break;
            }
        }

        return cnt;
    }

    @Override
    public void onItemClick(int position) {
        listSchedule = new ArrayList<>();
        scheduleCurrentDay = studySchedules.get(position).getCurrentDay();
        Log.d(TAG, "scheduleCurrent: " + scheduleCurrentDay);
        ArrayList<Subject> subjectByDay = getSubjectByDay(scheduleCurrentDay);
        for (Subject subject : subjectByDay) {
            int i = checkSubjectOnTime(subject);
            Log.d(TAG, "init: " + i);
            Log.d(TAG, "init: " + subject.getNgayDay());
            listSchedule.add(new Subject(subject.getLopHocID(), subject.getSubjectName(), subject.getSttNhom(), subject.getClassRoom(), subject.getStudyTime(), subject.getTypeSubject(), subject.getLichDayID(), String.valueOf(i), subject.getNgayDay()));
        }
        mAdapter = new ScheduleAdapter(this, listSchedule);
        rcvSchedule = findViewById(R.id.rcvSchedule);
        if (scheduleCurrentDay != null){
            rcvSchedule.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rcvSchedule.setLayoutManager(layoutManager1);
        rcvSchedule.setAdapter(mAdapter);
        rcvSchedule.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    //Xử lí sự kiện ở spinner tuần
    private class MyProcessWeekEvent implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(final AdapterView<?> adapterView, View view, final int position, long l) {
            if (!isSpinnerTouched) return;

            int selectedPosition = spnWeek.getSelectedItemPosition();
            spnWeek.setSelection(selectedPosition, false);

//            setSpinText(spnWeek, weeks[adapterView.getSelectedItemPosition()]);

            Log.d(TAG, "Position Selected: " + adapterView.getSelectedItemPosition());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("Học Kỳ").document("2");

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String hocky = document.getString("TenHocKy");
                            if (hocky.equals("" + hocKy[1])) {
                                String week = document.getString("NgayBatDau");
                                try {
                                    if (spnWeek.getSelectedItemPosition() == position) {

                                        String tuan = "" + spnWeek.getSelectedItem();
                                        currentWeek = tuan.substring(5, 7);
                                        pairs = listAllWeek(week);
                                        startDate = pairs.get(Integer.parseInt(currentWeek) - 1).first;
                                        Log.d(TAG, "NgayBatDau: " + startDate);
                                        endDate = pairs.get(Integer.parseInt(currentWeek) - 1).second;
                                        Log.d(TAG, "NgayKetThuc: " + endDate);
                                        ArrayList<String> listDay = getListDay(startDate, endDate, listAllDays);
                                        Log.d(TAG, "listdayselection: " + listDay.size());
                                        ArrayList<Pair<Pair<Integer, String>, Integer>> schedulePerWeek = getSchedulePerWeek(listDay);
                                        Log.d(TAG, "scheduleselection: " + schedulePerWeek.size());
                                        studySchedules = new ArrayList<>();
                                        if (schedulePerWeek.size() == 0) {
                                            prgLoadMain.setVisibility(View.INVISIBLE);
                                            imgLine.setVisibility(View.INVISIBLE);
                                            rcvSchedule.setVisibility(View.INVISIBLE);
                                            Toast.makeText(MainActivity.this, "Tuần này không phải lên lớp nha", Toast.LENGTH_SHORT).show();
                                        } else {
                                            imgLine.setVisibility(View.VISIBLE);
                                            rcvSchedule.setVisibility(View.VISIBLE);
                                            for (int i = 0; i < schedulePerWeek.size(); ++i) {
                                                studySchedules.add(new StudySchedule("Thứ " + schedulePerWeek.get(i).first.first, schedulePerWeek.get(i).second + " kíp", schedulePerWeek.get(i).first.second));
                                            }
                                            adapter = new StudyScheduleAdapter(adapterView.getContext(), studySchedules);
                                            rcvStudySchedule.setAdapter(adapter);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    //xử lí sự kiện ở spinner học kỳ.
    private class MyProcessHocKyEvent implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, final int position, long l) {
            if (position == 0) {
                spnWeek.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Kì này hiện đã qua", Toast.LENGTH_SHORT).show();
            }
            if (position == 1) {
                prgLoadMain.setVisibility(View.VISIBLE);
                spnWeek.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWeek(position);
                    }
                }, 2000);


            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    //hôm nay là thứ mấy?
    public static String getCurrentDay() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        return date;

    }

    //lấy tất cả lịch dạy có trong tuần
    public static ArrayList<Pair<Pair<Integer, String>, Integer>> getSchedulePerWeek(ArrayList<String> listDay) throws ParseException {
        ArrayList<Pair<Pair<Integer, String>, Integer>> list = new ArrayList<Pair<Pair<Integer, String>, Integer>>();
        if (listDay.size() == 0) {
            return list;
        } else {
            Collections.sort(listDay, new Comparator<String>() {

                @Override
                public int compare(String c1, String c2) {
                    // ignoring case
                    return sosanh(c1, c2);
                }
            });
            int cnt = 1;
            int b[] = new int[listDay.size()];
            for (int i = 1; i < listDay.size(); ++i) {
                if (listDay.get(i).compareTo(listDay.get(i - 1)) == 0) {
                    ++cnt;
                } else {
                    b[i - 1] = cnt;
                    cnt = 1;
                }
            }
            if (listDay.size() > 1) {
                if (listDay.get(listDay.size() - 1).compareTo(listDay.get(listDay.size() - 2)) == 0)
                    b[listDay.size() - 1] = cnt;
                else b[listDay.size() - 1] = 1;
            } else {
                b[listDay.size() - 1] = 1;
            }
            for (int i = 0; i < listDay.size(); ++i) {
                if (b[i] != 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(listDay.get(i)));
                    int day = c.get(Calendar.DAY_OF_WEEK);
                    list.add(new Pair<Pair<Integer, String>, Integer>(new Pair<Integer, String>(day, listDay.get(i)), b[i]));
                }
            }
        }
        return list;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DialogCustom dialogCustom = new DialogCustom(this, "Bạn có chắc chắn muốn đăng xuất?", "YES", "NO", "");
            dialogCustom.createDialog(new IDialogListener() {
                @Override
                public void OnYesClicked() {
                    finish();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            try {
                String currentDay = getCurrentDay();
                int temp = 0;
                for (String currentDays : listAllDays) {
                    if (currentDay.equals(currentDays)) {
                        intent.putExtra("CurrentDay", currentDay);
                        startActivity(intent);
                        temp++;
                    }
                }
                if (temp == 0) {
                    Toast.makeText(this, "Hôm nay được nghỉ nha", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_statistical) {
            Intent intent = new Intent(this, StatisticalActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_subject) {
            Intent intent = new Intent(this, SubjectActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //schedule


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

    public int checkSubjectOnTime(Subject sb) {
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
        if (sosanh(sb.getNgayDay(), day) < 0) {
            Log.d(TAG, "checkSubjectOnTime: " + 0);
            return 0;
        }
        if (sosanh(sb.getNgayDay(), day) > 0) {
            Log.d(TAG, "checkSubjectOnTime: " + 1);
            return 1;
        }

        String time = sb.getStudyTime();
        String p[] = time.split("-");

        String l[] = currentTime.split(":");
        String r[] = new String[2];
        r[0] = p[0].substring(0, 2);
        r[1] = p[1].substring(1, 3);
        if (l[0].compareTo(r[0]) < 0) {
            ans = 1;
        }
        if (l[0].compareTo(r[0]) >= 0 && l[0].compareTo(r[1]) < 0) {
            ans = 1;
        }
        if (l[0].compareTo(r[1]) == 0 && l[1].compareTo("00") == 0) {
            ans = 1;
        }

        return ans;
    }

    public void trainData() {
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

    public ArrayList<Subject> getSubjectByDay(String day) {
        ArrayList<Subject> list = new ArrayList<Subject>();
        for (int i = 0; i < listLichDayDB.size(); ++i) {
            if (day.equals(listLichDayDB.get(i).getNgayDay()) && listLichDayDB.get(i).getGiangVienID().equals(GV_ID)) {
                Subject sb = new Subject();
                sb.setNgayDay(listLichDayDB.get(i).getNgayDay());
                sb.setLopHocID(listLichDayDB.get(i).getLopHocID());
                String typeSchedule = listLichDayDB.get(i).getKieuTietHoc();
                sb.setTypeSubject(typeSchedule);
                sb.setLichDayID(listLichDayDB.get(i).getID());
                String kip = listLichDayDB.get(i).getKip();
                //  1 là tiết thực hành
                // 0 là tiết lý thuyết
                if (typeSchedule.equals("1")) {
                    if (kip.equals("1")) sb.setStudyTime("08h - 12h");
                    if (kip.equals("2")) sb.setStudyTime("12h - 16h");
                    if (kip.equals("3")) sb.setStudyTime("16h - 20h");
                }
                if (typeSchedule.equals("0")) {
                    if (kip.equals("1")) sb.setStudyTime("07h - 09h");
                    if (kip.equals("2")) sb.setStudyTime("09h - 11h");
                    if (kip.equals("3")) sb.setStudyTime("12h - 14h");
                    if (kip.equals("4")) sb.setStudyTime("14h - 16h");
                    if (kip.equals("5")) sb.setStudyTime("16h - 18h");
                    if (kip.equals("6")) sb.setStudyTime("18h - 20h");
                }

                for (int j = 0; j < listLopHocDB.size(); ++j)
                    if (listLichDayDB.get(i).getLopHocID().equals(listLopHocDB.get(j).getId())) {
                        for (int k = 0; k < listPhongHocDB.size(); ++k) {
                            if (listLopHocDB.get(j).getPhongHocID().equals(listPhongHocDB.get(k).getID())) {
                                sb.setClassRoom(listPhongHocDB.get(k).getTenPhongHoc());
                            }
                        }
                        for (int k = 0; k < listNhomMonHocDB.size(); ++k) {
                            if (listLopHocDB.get(j).getNhomMonHocID().equals(listNhomMonHocDB.get(k).getID())) {
                                sb.setSttNhom(listNhomMonHocDB.get(k).getSttNhom());
                                for (int h = 0; h < listMonHocDB.size(); ++h) {
                                    if (listNhomMonHocDB.get(k).getMonHocID().equals(listMonHocDB.get(h).getID())) {
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
