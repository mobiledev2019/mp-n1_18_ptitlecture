package ptit.com.ptitmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ptit.com.ptitmanager.R;

public class ProfileActivity extends AppCompatActivity{
    private TextView tvFullName, tvMaSV, tvDiemCC;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
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
        String hoTen = bundle.getString("HoTen");
        String maSV = bundle.getString("MaSV");
        String diemCC = bundle.getString("DiemCC");
        tvFullName = findViewById(R.id.tvNameProfile);
        tvFullName.setText(hoTen);
        tvMaSV = findViewById(R.id.tvMaSVProfile);
        tvMaSV.setText(maSV);
        tvDiemCC = findViewById(R.id.tvDCCDetail);
        tvDiemCC.setText(diemCC);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
