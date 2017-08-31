package com.mist.it.pod_nk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mist.it.pod_nk.MyConstant.projectString;
import static com.mist.it.pod_nk.MyConstant.serverString;
import static com.mist.it.pod_nk.MyConstant.urlClearArrivedData;
import static com.mist.it.pod_nk.MyConstant.urlSaveImagePerInvoice;
import static com.mist.it.pod_nk.MyConstant.urlSaveImagePerStore;
import static com.mist.it.pod_nk.MyConstant.urlUploadPicture;

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
    @BindView(R.id.btnDJAReject)
    Button rejectButton;
    @BindView(R.id.linDJABottom)
    LinearLayout linDJABottom;

    private String invFirstPosition, dateString, placeString, subJobNoString, inTimeString, outTimeString, tripNoString, signatureFileNameString, consigneeNameString, storeString, storeIdString, arriveTimeString, pathImgFirstString, pathImgSecondString, pathImgThirdString, pathImgFourthString, pathImgInviceFirstString;
    private String[] loginStrings, invoiceStrings, isImageStrings, haveImageStrings;
    private Uri firstUri, secondUri, thirdUri, fourthUri, invFirstUri;
    private Boolean imgFirstFlagABoolean, imgSecondFlagABoolean, imgThirdFlagABoolean, imgFourthFlagABoolean, imgInvoiceFirstABoolean, flagSaveABoolean, flagDoubleBackPress;
    private Bitmap imgFirstBitmap = null;
    private Bitmap imgSecondBitmap = null;
    private Bitmap imgThirdBitmap = null;
    private Bitmap imgFourthBitmap = null;
    private View view1;
    private ConfirmViewHolder confirmViewHolder;
    private ODOViewHolder odoViewHolder;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                Intent intent = new Intent(DetailJobActivity.this, DetailJobActivity.class);
                intent.putExtra("Date", dateString);
                intent.putExtra("Position", tripNoString);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("SubJobNo", subJobNoString);
                intent.putExtra("Place", storeString);
                startActivity(intent);
                finish();
                break;
            case R.id.information:
                intent = new Intent(DetailJobActivity.this, VideoViewerActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (flagDoubleBackPress) {
            Intent intent = new Intent(DetailJobActivity.this, ManageJobActivity.class);
            intent.putExtra("Date", dateString);
            intent.putExtra("Position", tripNoString);
            intent.putExtra("Login", loginStrings);
            intent.putExtra("SubJobNo", subJobNoString);
            startActivity(intent);
            finish();
        }

        flagDoubleBackPress = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flagDoubleBackPress = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);
        ButterKnife.bind(this);

        //Set flag img
        imgFirstFlagABoolean = false;
        imgSecondFlagABoolean = false;
        imgThirdFlagABoolean = false;
        imgFourthFlagABoolean = false;
        flagDoubleBackPress = false;
        imgInvoiceFirstABoolean = false;

        dateString = getIntent().getStringExtra("Date");
        tripNoString = getIntent().getStringExtra("Position");
        loginStrings = getIntent().getStringArrayExtra("Login");
        subJobNoString = getIntent().getStringExtra("SubJobNo");
        storeString = getIntent().getStringExtra("Place");

        Log.d("NK-Tag-DJA", dateString);

        dateTextView.setText(dateString);

        SynGetJobDetail synGetJobDetail = new SynGetJobDetail();
        synGetJobDetail.execute();
    }

    @OnClick(R.id.btnDJAReject)
    public void onViewClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailJobActivity.this, R.style.RejectAlertDialogTheme);
        view1 = View.inflate(getBaseContext(), R.layout.custom_alert, null);

        confirmViewHolder = new ConfirmViewHolder(view1);
        confirmViewHolder.alertImageView.setImageResource(R.drawable.caution);
        confirmViewHolder.descriptTextView.setText(R.string.reject_d);
        confirmViewHolder.headerTextView.setText(R.string.reject_h);
        confirmViewHolder.descriptTextView.setTextColor(getResources().getColor(R.color.MediumBlue, null));
        confirmViewHolder.headerTextView.setTextColor(getResources().getColor(R.color.MediumBlue, null));

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SynClearArrived synClearArrived = new SynClearArrived();
                synClearArrived.execute();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Not do anything
            }
        });

        builder.setView(view1);
        builder.show();
    }

    @OnClick({R.id.btnDJAArrive, R.id.btnDJASavePic, R.id.btnDJASignature, R.id.btnDJAConfirm, R.id.imgDJAOne, R.id.imgDJATwo, R.id.imgDJAThree, R.id.imgDJAFour})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnDJAArrive:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailJobActivity.this, R.style.AlertDialogTheme);
                View view2 = View.inflate(getBaseContext(), R.layout.set_odo_dialog, null);

                odoViewHolder = new ODOViewHolder(view2);
                odoViewHolder.headerTextView.setText(getResources().getText(R.string.Arrival));
                odoViewHolder.headerTextView.setTextColor(Color.parseColor("#f5f5f5"));
                builder1.setView(view2);

                builder1.setPositiveButton(getResources().getText(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Log.d("Tag", "In edit text ==> " + dialogViewHolder.odoNoEditText.getText().toString());
                        if (odoViewHolder.noEditText.getText().toString().equals("")) {
                            Toast.makeText(DetailJobActivity.this, getResources().getText(R.string.err_odo), Toast.LENGTH_LONG).show();

                        } else {
                            GPSManager gpsManager = new GPSManager(DetailJobActivity.this);
                            if (gpsManager.setLatLong(0)) {
                                SynUpdateStatusArrive synUpdateStatusArrive = new SynUpdateStatusArrive(gpsManager.getLatString(), gpsManager.getLongString(), gpsManager.getDateTime(), gpsManager.getTimeString(), odoViewHolder.noEditText.getText().toString());
                                synUpdateStatusArrive.execute();
                            } else {
                                Toast.makeText(getBaseContext(), getResources().getText(R.string.err_gps), Toast.LENGTH_LONG).show();
                            }

                        }


                    }
                });


                builder1.show();

                break;
            case R.id.btnDJASavePic:
                Log.d("NK-Tag-DJA", (pathImgFirstString != null) + " , " + !(pathImgFirstString == null));
                Log.d("NK-Tag-DJA", (pathImgSecondString != null) + " , " + !(pathImgSecondString == null));
                Log.d("NK-Tag-DJA", pathImgSecondString + pathImgFirstString);

                if (imgFirstBitmap != null) {
                    SynUploadImage synUploadImage = new SynUploadImage(DetailJobActivity.this, imgFirstBitmap, invoiceStrings[0], subJobNoString, "sto_first.png", storeIdString);
                    synUploadImage.execute();
                    imgFirstBitmap = null;
                }
                if (imgSecondBitmap != null) {
                    SynUploadImage synUploadImage = new SynUploadImage(DetailJobActivity.this, imgSecondBitmap, invoiceStrings[0], subJobNoString, "sto_second.png", storeIdString);
                    synUploadImage.execute();
                    imgSecondBitmap = null;
                }
                if (imgThirdBitmap != null) {
                    SynUploadImage synUploadImage = new SynUploadImage(DetailJobActivity.this, imgThirdBitmap, invoiceStrings[0], subJobNoString, "sto_third.png", storeIdString);
                    synUploadImage.execute();
                    imgThirdBitmap = null;
                }
                if (imgFourthBitmap != null) {
                    SynUploadImage synUploadImage = new SynUploadImage(DetailJobActivity.this, imgFourthBitmap, invoiceStrings[0], subJobNoString, "sto_fourth.png", storeIdString);
                    synUploadImage.execute();
                    imgFourthBitmap = null;

                }
                break;
            case R.id.btnDJASignature:
                Intent intent = new Intent(DetailJobActivity.this, SignatureActivity.class);
                intent.putExtra("Date", dateString);
                intent.putExtra("Position", tripNoString);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("SubJobNo", subJobNoString);
                intent.putExtra("Place", storeString);
                intent.putExtra("StoreId", storeIdString);
                intent.putExtra("SignatureFileName", signatureFileNameString);
                intent.putExtra("ConsigneeName", consigneeNameString);
                startActivity(intent);
                finish();
                break;
            case R.id.btnDJAConfirm:
                Boolean aBoolean = false;
                for (int i = 0; i < haveImageStrings.length; i++) {
                    if (haveImageStrings[i].equals("N")) {
                        aBoolean = true;
                    }
                }
                if (aBoolean) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailJobActivity.this, R.style.ConfirmAlertDialogTheme);
                    view1 = View.inflate(getBaseContext(), R.layout.custom_alert, null);

                    confirmViewHolder = new ConfirmViewHolder(view1);
                    confirmViewHolder.alertImageView.setImageResource(R.drawable.caution);
                    confirmViewHolder.descriptTextView.setText(R.string.err_confirm_d);
                    confirmViewHolder.headerTextView.setText(R.string.err_confirm_h);
                    confirmViewHolder.descriptTextView.setTextColor(getResources().getColor(R.color.MediumBlue, null));
                    confirmViewHolder.headerTextView.setTextColor(getResources().getColor(R.color.MediumBlue, null));

                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Don't do anything
                        }
                    });

                    builder.setView(view1);
                    builder.show();
                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailJobActivity.this, R.style.ConfirmAlertDialogTheme);
                    view1 = View.inflate(getBaseContext(), R.layout.custom_alert, null);

                    confirmViewHolder = new ConfirmViewHolder(view1);
                    confirmViewHolder.alertImageView.setImageResource(R.drawable.caution);
                    confirmViewHolder.descriptTextView.setText(R.string.confirm_d);
                    confirmViewHolder.headerTextView.setText(R.string.confirm_h);
                    confirmViewHolder.descriptTextView.setTextColor(getResources().getColor(R.color.MediumBlue, null));
                    confirmViewHolder.headerTextView.setTextColor(getResources().getColor(R.color.MediumBlue, null));

                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GPSManager manager = new GPSManager(DetailJobActivity.this);
                            if (manager.setLatLong(0)) {
                                SynUpdateConfirmStatus synUpdateConfirmStatus = new SynUpdateConfirmStatus(manager.getLatString(), manager.getLongString(), manager.getDateTime());
                                synUpdateConfirmStatus.execute();
                            } else {
                                Toast.makeText(getBaseContext(), getResources().getText(R.string.err_gps), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Not do anything
                        }
                    });

                    builder.setView(view1);
                    builder.show();
                }

                break;
            case R.id.imgDJAOne:
                if (!imgFirstFlagABoolean) {
                    File originalFile1 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "sto_first.png");
                    Intent cameraIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    firstUri = Uri.fromFile(originalFile1);
                    cameraIntent1.putExtra(MediaStore.EXTRA_OUTPUT, firstUri);
                    startActivityForResult(cameraIntent1, 1);

                }
                break;
            case R.id.imgDJATwo:
                if (!imgSecondFlagABoolean) {
                    File originalFile2 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "sto_second.png");
                    Intent cameraIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    secondUri = Uri.fromFile(originalFile2);
                    cameraIntent2.putExtra(MediaStore.EXTRA_OUTPUT, secondUri);
                    startActivityForResult(cameraIntent2, 2);
                }
                break;
            case R.id.imgDJAThree:
                if (!imgThirdFlagABoolean) {
                    File originalFile3 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "sto_third.png");
                    Intent cameraIntent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    thirdUri = Uri.fromFile(originalFile3);
                    cameraIntent3.putExtra(MediaStore.EXTRA_OUTPUT, thirdUri);
                    startActivityForResult(cameraIntent3, 3);
                }
                break;
            case R.id.imgDJAFour:
                if (!imgFourthFlagABoolean) {
                    File originalFile4 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "sto_fourth.png");
                    Intent cameraIntent4 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fourthUri = Uri.fromFile(originalFile4);
                    cameraIntent4.putExtra(MediaStore.EXTRA_OUTPUT, fourthUri);
                    startActivityForResult(cameraIntent4, 4);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    pathImgFirstString = firstUri.getPath();
                    try {
                        imgFirstBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(firstUri));
                        if (imgFirstBitmap.getHeight() < imgFirstBitmap.getWidth()) {
                            imgFirstBitmap = rotateBitmap(imgFirstBitmap);
                        }
                        firstImageView.setImageBitmap(imgFirstBitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    pathImgSecondString = secondUri.getPath();
                    try {
                        imgSecondBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(secondUri));
                        if (imgSecondBitmap.getHeight() < imgSecondBitmap.getWidth()) {
                            imgSecondBitmap = rotateBitmap(imgSecondBitmap);
                        }
                        secondImageView.setImageBitmap(imgSecondBitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case 3:
                if (resultCode == RESULT_OK) {
                    pathImgThirdString = thirdUri.getPath();
                    try {
                        imgThirdBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(thirdUri));
                        if (imgThirdBitmap.getHeight() < imgThirdBitmap.getWidth()) {
                            imgThirdBitmap = rotateBitmap(imgThirdBitmap);
                        }
                        thirdImageView.setImageBitmap(imgThirdBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case 4:
                if (resultCode == RESULT_OK) {
                    pathImgFourthString = fourthUri.getPath();
                    try {
                        imgFourthBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fourthUri));
                        if (imgFourthBitmap.getHeight() < imgFourthBitmap.getWidth()) {
                            imgFourthBitmap = rotateBitmap(imgFourthBitmap);
                        }
                        fourthImageView.setImageBitmap(imgFourthBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap imgInvoiceFirstBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(invFirstUri));
                        Log.d("NK-Tag-DJA", "Before Call  ==> " + imgInvoiceFirstBitmap + " , " + invoiceStrings[Integer.parseInt(invFirstPosition)] + " , " + subJobNoString);
                        if (imgInvoiceFirstBitmap.getHeight() < imgInvoiceFirstBitmap.getWidth()) {
                            imgInvoiceFirstBitmap = rotateBitmap(imgInvoiceFirstBitmap);
                        }


                        SynUploadImagePerInv synUploadImage = new SynUploadImagePerInv(DetailJobActivity.this, imgInvoiceFirstBitmap, invoiceStrings[Integer.parseInt(invFirstPosition)], subJobNoString, "inv_first.png");
                        synUploadImage.execute();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    private Bitmap rotateBitmap(Bitmap src) {

        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(90);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

    static class ConfirmViewHolder {
        @BindView(R.id.imgCAAlert)
        ImageView alertImageView;
        @BindView(R.id.txtCAHeader)
        TextView headerTextView;
        @BindView(R.id.txtCADescript)
        TextView descriptTextView;

        ConfirmViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ODOViewHolder {
        @BindView(R.id.txtSODHeader)
        TextView headerTextView;
        @BindView(R.id.edtSODNo)
        EditText noEditText;

        ODOViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class InvoiceListAdaptor extends BaseAdapter {
        Context context;
        String[] invoiceStrings, isImgStrings;
        InvoiceListViewHolder invoiceListViewHolder;

        InvoiceListAdaptor(Context context, String[] invoiceStrings, String[] isImgStrings) {
            this.context = context;
            this.invoiceStrings = invoiceStrings;
            this.isImgStrings = isImgStrings;
            Log.d("NK-Tag-DJA", String.valueOf(invoiceStrings.length));
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.invoice_listview, null);
                invoiceListViewHolder = new InvoiceListViewHolder(convertView);
                convertView.setTag(invoiceListViewHolder);
            } else {
                invoiceListViewHolder = (InvoiceListViewHolder) convertView.getTag();
            }
            Log.d("NK-Tag-DJA", invoiceStrings[position]);

            invoiceListViewHolder.invoiceTextView.setText(invoiceStrings[position]);

            if (isImgStrings[position].equals("0")) {
                invoiceListViewHolder.cameraImageView.setBackground(getResources().getDrawable(R.color.Crimson, null));
            } else {
                invoiceListViewHolder.cameraImageView.setBackground(getResources().getDrawable(R.color.MediumAquaMarine, null));
            }

            invoiceListViewHolder.invoiceTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NK-Tag-DJA", inTimeString);
                    if (!inTimeString.equals("null")) {
                        Intent intent = new Intent(DetailJobActivity.this, ReturnActivity.class);
                        intent.putExtra("Date", dateString);
                        intent.putExtra("Position", tripNoString);
                        intent.putExtra("Login", loginStrings);
                        intent.putExtra("SubJobNo", subJobNoString);
                        intent.putExtra("Place", storeString);
                        intent.putExtra("StoreId", storeIdString);
                        intent.putExtra("Invoice", invoiceStrings[position]);
                        startActivity(intent);
                    }
                }
            });

            invoiceListViewHolder.cameraImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!inTimeString.equals("null")) {

                        File originalFile1 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "inv_first.png");
                        invFirstPosition = String.valueOf(position);
                        Intent cameraIntent5 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        invFirstUri = Uri.fromFile(originalFile1);
                        cameraIntent5.putExtra(MediaStore.EXTRA_OUTPUT, invFirstUri);
                        startActivityForResult(cameraIntent5, 5);
                    }
                }
            });

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

    private class SynUploadImage extends AsyncTask<Void, Void, String> {
        private Context context;
        private Bitmap bitmap;
        private String invoiceNoString, subjobNoString, mFileNameString, storeIdString;
        private UploadImageUtils uploadImageUtils;


        SynUploadImage(Context context, Bitmap bitmap, String invoiceNoString, String subjobNoString, String mFileNameString, String storeIdString) {
            this.context = context;
            this.bitmap = bitmap;
            this.invoiceNoString = invoiceNoString;
            this.subjobNoString = subjobNoString;
            this.mFileNameString = mFileNameString;
            this.storeIdString = storeIdString;
        }

        @Override
        protected String doInBackground(Void... params) {
            uploadImageUtils = new UploadImageUtils();
            final String result = uploadImageUtils.uploadFile(mFileNameString, urlUploadPicture, bitmap, storeIdString, "P", subjobNoString, invoiceNoString);
            if (result.equals("NOK")) {
                return "NOK";

            } else {
                try {
                    GPSManager gpsManager = new GPSManager(DetailJobActivity.this);
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormEncodingBuilder()
                            .add("isAdd", "true")
                            .add("subjob_no", subjobNoString)
                            .add("invoiceNo", invoiceNoString)
                            .add("File_Name", result)
                            .add("user_name", loginStrings[5])
                            .add("StoreId", storeIdString)
                            .add("timeStamp", gpsManager.getDateTime())
                            .build();
                    Request.Builder builder = new Request.Builder();
                    Request request = builder.post(requestBody).url(urlSaveImagePerStore).build();
                    Response response = okHttpClient.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("NK-Tag-DJA", String.valueOf(e) + " Line: " + e.getStackTrace()[0].getLineNumber());
                    return null;
                }

            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("NK-Tag-DJA", "____ Save image / store _______________" + s);
            if (s.equals("OK")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getResources().getText(R.string.save_img_success), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getResources().getText(R.string.save_img_unsuccessful), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private class SynUploadImagePerInv extends AsyncTask<Void, Void, String> {
        private Context context;
        private Bitmap bitmap;
        private String invoiceNoString, subjobNoString, mFileNameString;
        private UploadImageUtils uploadImageUtils;


        SynUploadImagePerInv(Context context, Bitmap bitmap, String invoiceNoString, String subjobNoString, String mFileNameString) {
            this.context = context;
            this.bitmap = bitmap;
            this.invoiceNoString = invoiceNoString;
            this.subjobNoString = subjobNoString;
            this.mFileNameString = mFileNameString;
        }

        @Override
        protected String doInBackground(Void... params) {

            uploadImageUtils = new UploadImageUtils();
            final String result = uploadImageUtils.uploadFile(mFileNameString, urlUploadPicture, bitmap, storeIdString, "I", subjobNoString, invoiceNoString);
            if (result.equals("NOK")) {
                return "NOK";

            } else {
                try {
                    GPSManager gpsManager = new GPSManager(DetailJobActivity.this);

                    Log.d("NK-Tag-DJA", "send ==> " + subjobNoString + " , " + invoiceNoString + " , " + result + " , " + loginStrings[0] + " , " + gpsManager.getDateTime());
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormEncodingBuilder()
                            .add("isAdd", "true")
                            .add("subjob_no", subJobNoString)
                            .add("invoiceNo", invoiceNoString)
                            .add("File_Name", result)
                            .add("user_name", loginStrings[5])
                            .add("gps_timeStamp", gpsManager.getDateTime())
                            .build();
                    Request.Builder builder = new Request.Builder();
                    Request request = builder.post(requestBody).url(urlSaveImagePerInvoice).build();
                    Response response = okHttpClient.newCall(request).execute();

                    return response.body().string();
                } catch (IOException e) {
                    Log.d("NK-Tag-DJA", String.valueOf(e) + " Line: " + e.getStackTrace()[0].getLineNumber());
                    e.printStackTrace();
                    return null;
                }

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("NK-Tag-DJA", "________Save image / invoice___________" + s);
            if (s.equals("OK")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getResources().getText(R.string.save_img_success), Toast.LENGTH_SHORT).show();
                        haveImageStrings[Integer.parseInt(invFirstPosition)] = "Y";
                        isImageStrings[Integer.parseInt(invFirstPosition)] = "1";


                        InvoiceListAdaptor invoiceListAdaptor = new InvoiceListAdaptor(DetailJobActivity.this, invoiceStrings, isImageStrings);
                        invoiceListView.setAdapter(invoiceListAdaptor);

                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getResources().getText(R.string.save_img_unsuccessful), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private class SynUpdateConfirmStatus extends AsyncTask<Void, Void, String> {
        String latString, longString, timeString;

        SynUpdateConfirmStatus(String latString, String longString, String timeString) {
            this.latString = latString;
            this.longString = longString;
            this.timeString = timeString;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("user_name", loginStrings[5])
                        .add("subjob_no", subJobNoString)
                        .add("gps_lat", latString)
                        .add("gps_lon", longString)
                        .add("timeStamp", timeString)
                        .add("StoreId", storeIdString)
                        .build();
                Request request = builder.url(MyConstant.urlSaveConfirmedOfStore).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("NK-Tag-DJA", String.valueOf(e) + " Line: " + e.getStackTrace()[0].getLineNumber());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("NK-Tag-DJA", s);
            if (s.equals("OK")) {
                Intent intent = new Intent(DetailJobActivity.this, ManageJobActivity.class);
                intent.putExtra("Date", dateString);
                intent.putExtra("Position", tripNoString);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("SubJobNo", subJobNoString);
                startActivity(intent);
                finish();

            } else if (s.equals("NOK")) {
                Toast.makeText(getBaseContext(), getResources().getText(R.string.save_incomp), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class SynUpdateStatusArrive extends AsyncTask<Void, Void, String> {
        String latString, longString, timeString, t, odoString;

        SynUpdateStatusArrive(String latString, String longString, String timeString, String t, String odoString) {
            this.latString = latString;
            this.longString = longString;
            this.timeString = timeString;
            this.odoString = odoString;
            this.t = t;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.d("NK-Tag-DJA", "Send ==> " + loginStrings[5] + " , " + storeString + " , " + subJobNoString + " , " + invoiceStrings[0] + " , " + latString + " , " + longString + " , " + timeString);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody;
                Request request;
                if (storeIdString.equals("null")) {
                    requestBody = new FormEncodingBuilder()
                            .add("isAdd", "true")
                            .add("user_name", loginStrings[5])
                            .add("dealerName", storeString)
                            .add("subjob_no", subJobNoString)
                            .add("invoiceNo", invoiceStrings[0])
                            .add("gps_lat", latString)
                            .add("gps_lon", longString)
                            .add("timeStamp", timeString)
                            .add("pODO", odoString)
                            .build();
                    request = builder.url(MyConstant.urlSaveArrivedToStore).post(requestBody).build();
                } else {
                    requestBody = new FormEncodingBuilder()
                            .add("isAdd", "true")
                            .add("user_name", loginStrings[5])
                            .add("StoreId", storeIdString)
                            .add("gps_lat", latString)
                            .add("gps_lon", longString)
                            .add("timeStamp", timeString)
                            .add("pODO", odoString)
                            .build();
                    request = builder.url(MyConstant.urlUpdateArrivedData).post(requestBody).build();
                }
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("NK-Tag-DJA", String.valueOf(e) + " Line: " + e.getStackTrace()[0].getLineNumber());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("NK-Tag-DJA", s);

            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    storeIdString = jsonObject.getString("StoreId");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("NK-Tag-DJA", String.valueOf(e) + " Line: " + e.getStackTrace()[0].getLineNumber());
                }

                arriveButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
                savePicButton.setVisibility(View.VISIBLE);
                signatureButton.setVisibility(View.VISIBLE);
                firstImageView.setVisibility(View.VISIBLE);
                secondImageView.setVisibility(View.VISIBLE);
                thirdImageView.setVisibility(View.VISIBLE);
                fourthImageView.setVisibility(View.VISIBLE);

                inTimeString = t;


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), getResources().getText(R.string.arrive_comp), Toast.LENGTH_LONG).show();
                    }
                });
            } else {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), getResources().getText(R.string.err_arr), Toast.LENGTH_LONG).show();
                    }
                });
            }


        }
    }

    private class SynGetJobDetail extends AsyncTask<Void, Void, String> {
        SynGetJobDetail() {
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.d("NK-Tag-DJA", "Send ==> " + subJobNoString + " , " + storeString);
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
                Log.d("NK-Tag-DJA", String.valueOf(e) + " Line: " + e.getStackTrace()[0].getLineNumber());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("NK-Tag-DJA", s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    storeIdString = jsonObject1.getString("StoreId");
                    arriveTimeString = jsonObject1.getString("ArrivalTime");
                    placeString = jsonObject1.getString("DetailDesc");
                    signatureFileNameString = jsonObject1.getString("signatureFileName");
                    consigneeNameString = jsonObject1.getString("consigneeName");

                    pathImgFirstString = jsonObject1.getString("ImgFileName_1");
                    pathImgSecondString = jsonObject1.getString("ImgFileName_2");
                    pathImgThirdString = jsonObject1.getString("ImgFileName_3");
                    pathImgFourthString = jsonObject1.getString("ImgFileName_4");

                    inTimeString = jsonObject1.getString("InTime");
                    outTimeString = jsonObject1.getString("OutTime");

                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Invoice");
                    invoiceStrings = new String[jsonArray1.length()];
                    isImageStrings = new String[jsonArray1.length()];
                    haveImageStrings = new String[jsonArray1.length()];

                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                        invoiceStrings[j] = jsonObject2.getString("Invoice");
                        isImageStrings[j] = jsonObject2.getString("HaveImageInvoice");
                        if (isImageStrings[j].equals("1")) {
                            haveImageStrings[j] = "Y";
                        } else {
                            haveImageStrings[j] = "N";
                        }
                    }
                }

                storeTextView.setText(placeString);
                arrivalTimeTextView.setText(arriveTimeString);
                InvoiceListAdaptor invoiceListAdaptor = new InvoiceListAdaptor(DetailJobActivity.this, invoiceStrings, isImageStrings);
                invoiceListView.setAdapter(invoiceListAdaptor);


                Log.d("NK-Tag-DJA", inTimeString + " " + outTimeString);
                Log.d("NK-Tag-DJA", "Bool ==> " + (outTimeString.equals("null")));

                if (inTimeString.equals("null")) {
                    arriveButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                    savePicButton.setVisibility(View.GONE);
                    signatureButton.setVisibility(View.GONE);
                    firstImageView.setVisibility(View.INVISIBLE);
                    secondImageView.setVisibility(View.INVISIBLE);
                    thirdImageView.setVisibility(View.INVISIBLE);
                    fourthImageView.setVisibility(View.INVISIBLE);

                } else if (outTimeString.equals("null")) {
                    arriveButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.VISIBLE);
                    confirmButton.setVisibility(View.VISIBLE);
                    savePicButton.setVisibility(View.VISIBLE);
                    signatureButton.setVisibility(View.VISIBLE);
                    firstImageView.setVisibility(View.VISIBLE);
                    secondImageView.setVisibility(View.VISIBLE);
                    thirdImageView.setVisibility(View.VISIBLE);
                    fourthImageView.setVisibility(View.VISIBLE);
                    if (!pathImgFirstString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgFirstString).into(firstImageView);
                    }
                    if (!pathImgSecondString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgSecondString).into(secondImageView);
                    }
                    if (!pathImgThirdString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgThirdString).into(thirdImageView);
                    }
                    if (!pathImgFourthString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgFourthString).into(fourthImageView);
                    }

                } else {
                    arriveButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                    savePicButton.setVisibility(View.VISIBLE);
                    signatureButton.setVisibility(View.GONE);
                    firstImageView.setVisibility(View.VISIBLE);
                    secondImageView.setVisibility(View.VISIBLE);
                    thirdImageView.setVisibility(View.VISIBLE);
                    fourthImageView.setVisibility(View.VISIBLE);
                    if (!pathImgFirstString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgFirstString).into(firstImageView);
                    }
                    if (!pathImgSecondString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgSecondString).into(secondImageView);
                    }
                    if (!pathImgThirdString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgThirdString).into(thirdImageView);
                    }
                    if (!pathImgFourthString.equals("")) {
                        Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgFourthString).into(fourthImageView);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("NK-Tag-DJA", String.valueOf(e) + " Line: " + e.getStackTrace()[0].getLineNumber());
            }

        }
    }

    private class SynClearArrived extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                GPSManager gpsManager = new GPSManager(DetailJobActivity.this);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("user_name", loginStrings[5])
                        .add("StoreId", storeIdString)
                        .add("timeStamp", gpsManager.getDateTime())
                        .build();
                Request request = builder.url(urlClearArrivedData).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Log.d("NK-Tag-DJA", "Line ==> " + e.getStackTrace()[0].getLineNumber() + " Error ==> " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("NK-Tag-DJA", s);

            if (s.equals("OK")) {
                arriveButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.GONE);
                confirmButton.setVisibility(View.GONE);
                savePicButton.setVisibility(View.GONE);
                signatureButton.setVisibility(View.GONE);
                firstImageView.setVisibility(View.INVISIBLE);
                secondImageView.setVisibility(View.INVISIBLE);
                thirdImageView.setVisibility(View.INVISIBLE);
                fourthImageView.setVisibility(View.INVISIBLE);

                inTimeString = "null";
            } else {
                arriveButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
                savePicButton.setVisibility(View.VISIBLE);
                signatureButton.setVisibility(View.VISIBLE);
                firstImageView.setVisibility(View.VISIBLE);
                secondImageView.setVisibility(View.VISIBLE);
                thirdImageView.setVisibility(View.VISIBLE);
                fourthImageView.setVisibility(View.VISIBLE);
                if (!pathImgFirstString.equals("")) {
                    Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgFirstString).into(firstImageView);
                }
                if (!pathImgSecondString.equals("")) {
                    Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgSecondString).into(secondImageView);
                }
                if (!pathImgThirdString.equals("")) {
                    Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgThirdString).into(thirdImageView);
                }
                if (!pathImgFourthString.equals("")) {
                    Glide.with(DetailJobActivity.this).load(serverString + projectString + "/app/CenterService/" + pathImgFourthString).into(fourthImageView);
                }
            }


        }
    }

}
