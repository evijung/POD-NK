package com.mist.it.pod_nk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReturnActivity extends AppCompatActivity {

    @BindView(R.id.txtRAInvoiceNo)
    TextView invoiceNoTextView;
    @BindView(R.id.lisRAItemList)
    ListView itemListView;
    @BindView(R.id.imgRAOne)
    ImageView firstImageView;
    @BindView(R.id.imgRATwo)
    ImageView secondImageView;
    @BindView(R.id.btnRASave)
    Button saveButton;
    @BindView(R.id.btnRAReturnAll)
    Button returnAllButton;
    @BindView(R.id.btnRAConfirm)
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        //Bind Widget
        ButterKnife.bind(this);

    }

    //Set On Click Listener
    @OnClick({R.id.btnRASave, R.id.btnRAReturnAll, R.id.btnRAConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRASave:
                break;
            case R.id.btnRAReturnAll:
                break;
            case R.id.btnRAConfirm:
                break;
        }
    }
}
