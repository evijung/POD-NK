package com.mist.it.pod_nk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignatureActivity extends AppCompatActivity {

    @BindView(R.id.edtSAFullName)
    EditText fullNameEditText;
    @BindView(R.id.linSACanvas)
    LinearLayout canvasLinearLayout;
    @BindView(R.id.btnSASave)
    Button saveButton;
    @BindView(R.id.btnSAClear)
    Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnSASave, R.id.btnSAClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSASave:
                break;
            case R.id.btnSAClear:
                break;
        }
    }
}
