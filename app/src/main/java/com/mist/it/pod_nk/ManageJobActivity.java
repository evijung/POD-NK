package com.mist.it.pod_nk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageJobActivity extends AppCompatActivity {

    @BindView(R.id.txtMJAJobNo)
    TextView jobNoTextView;
    @BindView(R.id.lisMJAStore)
    ListView storeListView;
    @BindView(R.id.txtMJAStartTime)
    TextView startTimeTextView;
    @BindView(R.id.txtMJAStartMiles)
    TextView startMilesTextView;
    @BindView(R.id.txtMJAStopTime)
    TextView stopTimeTextView;
    @BindView(R.id.txtMJAStopMiles)
    TextView stopMilesTextView;
    @BindView(R.id.btnMJAStart)
    Button startButton;
    @BindView(R.id.btnMJAStop)
    Button stopButton;

    String dateString, tripNoString;
    String[] loginStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_job);
        ButterKnife.bind(this);

        dateString = getIntent().getStringExtra("Date");
        tripNoString = getIntent().getStringExtra("Position");
        loginStrings = getIntent().getStringArrayExtra("Login ");
    }

    @OnClick({R.id.btnMJAStart, R.id.btnMJAStop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnMJAStart:
                break;
            case R.id.btnMJAStop:
                break;
        }
    }
}
