package ptit.com.ptitmanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ptit.com.ptitmanager.Interface.IDialogListener;
import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.utils.DialogCustom;

public class EnterScoreActivity extends AppCompatActivity implements View.OnClickListener{
    private Spinner mSpiner;
    private Button mBtnSave;
    String arr[] = {
            "Điểm chuyên cần",
            "Điểm kiểm tra",
            "Điểm bài tập lớn",
            "Điểm thực hành",
            "Điểm thi"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_scores);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCustom dialogCustom = new DialogCustom(EnterScoreActivity.this, "Bạn muốn thoát mà không lưu chứ?", "YES", "NO", "");
                dialogCustom.createDialog(new IDialogListener() {
                    @Override
                    public void OnYesClicked() {
                        finish();
                    }
                });
            }
        });

        mBtnSave = findViewById(R.id.btnSaveScore);
        mBtnSave.setOnClickListener(this);

        mSpiner = findViewById(R.id.spnTypesScore);

        ArrayAdapter<String> typesScoreAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arr);

        typesScoreAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        mSpiner.setAdapter(typesScoreAdapter);

        mSpiner.setOnItemSelectedListener(new MyProcessEvent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSaveScore:
                finish();
                break;
            default:
                break;
        }
    }

    private class MyProcessEvent implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
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
