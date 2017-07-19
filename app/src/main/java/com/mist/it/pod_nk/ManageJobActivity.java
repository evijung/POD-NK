package com.mist.it.pod_nk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static com.mist.it.pod_nk.MyConstant.urlGetJob;

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

    String dateString, tripNoString, subJobNoString;
    String[] loginStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_job);
        ButterKnife.bind(this);

        dateString = getIntent().getStringExtra("Date");
        tripNoString = getIntent().getStringExtra("Position");
        loginStrings = getIntent().getStringArrayExtra("Login");
        subJobNoString = getIntent().getStringExtra("SubJobNo");

        jobNoTextView.setText("Trip " + tripNoString);


        SyncGetJob syncGetJob = new SyncGetJob();
        syncGetJob.execute();

    }

    class SyncGetJob extends AsyncTask<Void, Void, String> {
        String[] subJobNoStrings, deliveryDateStrings, truckStrings, driverNameStrings, driverSirNameStrings, deliveryTripNoStrings, tripStartTimeStrings;
        String[] tripStopTimeStrings, tripStartMileStrings, tripStopMileStrings;
        String[][] detailListStrings, provinceStrings, arriveTimeStrings;
        String[][][] jobNoStrings;
        String[][][][] invoiceStrings, amountStrings;

        @Override

        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("truck_id", loginStrings[0])
                        .add("subjob_no", subJobNoString)
                        .add("isAdd", "true")
                        .build();
                Request request = builder.post(requestBody).url(urlGetJob).build();
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

                JSONArray dataJsonArray = jsonObject.getJSONArray("data");
                subJobNoStrings = new String[dataJsonArray.length()];
                deliveryDateStrings = new String[dataJsonArray.length()];
                truckStrings = new String[dataJsonArray.length()];
                driverNameStrings = new String[dataJsonArray.length()];
                driverSirNameStrings = new String[dataJsonArray.length()];
                deliveryTripNoStrings = new String[dataJsonArray.length()];
                tripStartMileStrings = new String[dataJsonArray.length()];
                tripStartTimeStrings = new String[dataJsonArray.length()];
                tripStopMileStrings = new String[dataJsonArray.length()];
                tripStopTimeStrings = new String[dataJsonArray.length()];

                detailListStrings = new String[dataJsonArray.length()][];
                provinceStrings = new String[dataJsonArray.length()][];
                arriveTimeStrings = new String[dataJsonArray.length()][];
                jobNoStrings = new String[dataJsonArray.length()][][];
                invoiceStrings = new String[dataJsonArray.length()][][][];
                amountStrings = new String[dataJsonArray.length()][][][];

                for (int i = 0;i < dataJsonArray.length();i++) {
                    JSONObject jsonObject1 = dataJsonArray.getJSONObject(i);
                    subJobNoStrings[i] = jsonObject1.getString("SubJobNo");
                    deliveryDateStrings[i] = jsonObject1.getString("DeliveryDate");
                    truckStrings[i] = jsonObject1.getString("Truck");
                    driverNameStrings[i] = jsonObject1.getString("DriverName");
                    driverSirNameStrings[i] = jsonObject1.getString("DriverSirname");
                    deliveryTripNoStrings[i] = jsonObject1.getString("DeliveryTripNo");
                    tripStartMileStrings[i] = jsonObject1.getString("TripStartMile");
                    tripStartTimeStrings[i] = jsonObject1.getString("TripStartTime");
                    tripStopMileStrings[i] = jsonObject1.getString("TripEndMile");
                    tripStopTimeStrings[i] = jsonObject1.getString("TripEndTime");

                    JSONArray delivPlaceJsonArray = jsonObject1.getJSONArray("DeliveryPlace");
                    detailListStrings[i] = new String[delivPlaceJsonArray.length()];
                    provinceStrings[i] = new String[delivPlaceJsonArray.length()];
                    arriveTimeStrings[i] = new String[delivPlaceJsonArray.length()];

                    jobNoStrings[i] = new String[delivPlaceJsonArray.length()][];
                    invoiceStrings[i] = new String[delivPlaceJsonArray.length()][][];
                    amountStrings[i] = new String[delivPlaceJsonArray.length()][][];

                    for (int j = 0;j < delivPlaceJsonArray.length();j++) {
                        JSONObject jsonObject2 = delivPlaceJsonArray.getJSONObject(i);
                        detailListStrings[i][j] = jsonObject2.getString("DetailList");
                        provinceStrings[i][j] = jsonObject2.getString("PROVINCE");
                        arriveTimeStrings[i][j] = jsonObject2.getString("ArrivalTime");

                        JSONArray jobNoJsonArray = jsonObject2.getJSONArray("JobNo");
                        jobNoStrings[i][j] = new String[jobNoJsonArray.length()];

                        invoiceStrings[i][j] = new String[jobNoJsonArray.length()][];
                        amountStrings[i][j] = new String[jobNoJsonArray.length()][];

                        for (int k = 0;k < jobNoJsonArray.length();k++) {
                            JSONObject jsonObject3 = jobNoJsonArray.getJSONObject(i);
                            jobNoStrings[i][j][k] = jsonObject3.getString("JobNo");

                            JSONArray invoiceJsonArray = jsonObject3.getJSONArray("Invoice");
                            invoiceStrings[i][j][k] = new String[invoiceJsonArray.length()];
                            amountStrings[i][j][k] = new String[invoiceJsonArray.length()];

                            for (int l = 0;l < invoiceJsonArray.length();l++) {
                                JSONObject jsonObject4 = invoiceJsonArray.getJSONObject(i);
                                invoiceStrings[i][j][k][l] = jsonObject4.getString("Invoice");
                                amountStrings[i][j][k][l] = jsonObject4.getString("Amount");
                            }
                        }
                    }
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
