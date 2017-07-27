package com.mist.it.pod_nk;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    String dateString, subJobNoString, tripNoString, storeString, storeIdString, arriveTimeString;
    String[] loginStrings, invoiceStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);
        ButterKnife.bind(this);

        dateString = getIntent().getStringExtra("Date");
        tripNoString = getIntent().getStringExtra("Position");
        loginStrings = getIntent().getStringArrayExtra("Login");
        subJobNoString = getIntent().getStringExtra("SubJobNo");
        storeString = getIntent().getStringExtra("Place");

        storeTextView.setText(getResources().getText(R.string.Store) + ": " + storeString);
        dateTextView.setText(getResources().getText(R.string.Date) + ": " + dateString);

        SynGetJobDetail synGetJobDetail = new SynGetJobDetail();
        synGetJobDetail.execute();
    }

    class SynGetJobDetail extends AsyncTask<Void, Void, String> {
        public SynGetJobDetail() {
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.d("TAG", "Send ==> " + subJobNoString + " , " + storeString);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("subjob_no", subJobNoString)
                        .add("invoiceNo", storeString)
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
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    storeIdString = jsonObject1.getString("StoreId");
                    arriveTimeString = jsonObject1.getString("ArrivalTime");

                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Invoice");
                    invoiceStrings = new String[jsonArray1.length()];

                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                        invoiceStrings[j] = jsonObject2.getString("Invoice");
                    }
                }

                arrivalTimeTextView.setText(getResources().getText(R.string.ArrivalTime) + ": " + arriveTimeString);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class InvoiceListAdaptor extends BaseAdapter {
        Context context;
        String[] invoiceStrings;
        InvoiceListViewHolder invoiceListViewHolder;

        public InvoiceListAdaptor(Context context, String[] invoiceStrings) {
            this.context = context;
            this.invoiceStrings = invoiceStrings;
        }

        @Override
        public int getCount() {
            return invoiceStrings.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.invoice_listview, null);
                invoiceListViewHolder = new InvoiceListViewHolder(convertView);
                convertView.setTag(invoiceListViewHolder);
            } else {
                invoiceListViewHolder = (InvoiceListViewHolder) convertView.getTag();
            }

            invoiceListViewHolder.invoiceTextView.setText(invoiceStrings[position]);

            return convertView;
        }

        class InvoiceListViewHolder {
            @BindView(R.id.txtILInvoice)
            TextView invoiceTextView;
            @BindView(R.id.imgILCamera)
            ImageView cameraImageView;

            InvoiceListViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    class SynUpdateStatusArrive extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("user_name", loginStrings[5])
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
