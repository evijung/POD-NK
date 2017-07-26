package com.mist.it.pod_nk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailJobActivity extends AppCompatActivity {

    @BindView(R.id.txtDJAStore)
    TextView storeTextView;
    @BindView(R.id.txtDJAArrivalTime)
    TextView arrivalTimeTextView;
    @BindView(R.id.txtDJADate)
    TextView dateTextView;
    @BindView(R.id.lisDJAInvoiceList)
    ListView invoiceListView;
    @BindView(R.id.imgDJAOne)
    ImageView firstImageView;
    @BindView(R.id.imgDJATwo)
    ImageView secondImageView;
    @BindView(R.id.imgDJAThree)
    ImageView thirdImageView;
    @BindView(R.id.imgDJAFour)
    ImageView fourthImageView;
    @BindView(R.id.btnDJAArrive)
    Button arriveButton;
    @BindView(R.id.btnDJASavePic)
    Button savePicButton;
    @BindView(R.id.btnDJASignature)
    Button signatureButton;
    @BindView(R.id.btnDJAConfirm)
    Button confirmButton;

    String dateString,subJobNoString, tripNoString, invoiceString;
    String[] loginStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);
        ButterKnife.bind(this);

        dateString = getIntent().getStringExtra("Date");
        tripNoString = getIntent().getStringExtra("Position");
        loginStrings = getIntent().getStringArrayExtra("Login");
        subJobNoString = getIntent().getStringExtra("SubJobNo");
        invoiceString = getIntent().getStringExtra("Place");

        SynGetJobDetail synGetJobDetail = new SynGetJobDetail();
        synGetJobDetail.execute();
    }

    class SynGetJobDetail extends AsyncTask<Void, Void, String> {
        public SynGetJobDetail() {
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.d("TAG", "Send ==> " + subJobNoString + " , " + invoiceString);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("subjob_no", subJobNoString)
                        .add("invoiceNo", invoiceString)
                        .add("isAdd", "true")
                        .build();
                Request request = builder.post(requestBody).url(MyConstant.urlGetJobDetail).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Tag", s);


        }
    }


    class SynUpdateStatusArrive extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("user_name", loginStrings[5] )
                        .add("dealerName", "")
                        .add("subjob_no", "")
                        .add("invoiceNo", "")
                        .add("gps_lat", "")
                        .add("gps_lon", "")
                        .add("timeStamp", "")
                        .build();
                Request request = builder.url(MyConstant.urlSaveArrivedToStore).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @OnClick({R.id.btnDJAArrive, R.id.btnDJASavePic, R.id.btnDJASignature, R.id.btnDJAConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnDJAArrive:

                break;
            case R.id.btnDJASavePic:

                break;
            case R.id.btnDJASignature:

                break;
            case R.id.btnDJAConfirm:

                break;
        }
    }
}
