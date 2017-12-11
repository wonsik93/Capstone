package com.example.wonsi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by wonsi on 2017-11-26.
 */

public class RegisterPetActivity extends AppCompatActivity implements View.OnClickListener {
    static final int REQUEST_PETTYPE = 1;
    static final int REQUEST_TAKE_PHOTO = 1000;
    static final int REQUEST_TAKE_ALBUM = 1001;
    static final int REQUEST_IMAGE_CROP = 1002;
    private static final int MULTIPLE_PERMISSIONS = 101;
    private static Uri mImageCaptureUri;
    Context mContext;
    String saveFolderName = "picture";
    String saveFileName = "photo.jpg";
    File mediaFile = null;
    int ageYearorMonth = 1;
    int breed = 0;
    String returnTypeID;
    boolean check = true;
    private String imagePath;
    private ArrayAdapter<CharSequence> adspin;
    private String logonUsername;
    private String spicies;
    private ImageView iv_petImage;
    private ImageButton btn_addPetImage;
    private Button btn_petagebymonth;
    private Button btn_petagebyyear;
    private Button btn_registerPetData;
    private EditText et_petType;
    private EditText et_petName;
    private EditText et_petAge;
    private EditText et_petWeight;
    private Spinner spinner_petSex;
    private Bitmap photoBitmap;
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    }; //권한 설정 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpet);
        mContext = this.getBaseContext();
        Intent intent = getIntent();
        logonUsername = intent.getStringExtra("username");
        spicies = intent.getStringExtra("Spicies");
        initializeLayout();
        checkPermissions();
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Pet_BTN_picture:
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPetActivity.this);
                builder.setMessage("업로드할 이미지를 선택하세요.");
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getGallery();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };
                builder.setTitle("업로드할 이미지 선택");
                //builder.setPositiveButton("사진촬영", cameraListener);
                builder.setNeutralButton("앨범선택", albumListener);
                builder.setNegativeButton("취소", cancelListener);
                builder.show();
                break;
            case R.id.Pet_BTN_agemonth:
                ageYearorMonth = 1;
                btn_petagebymonth.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_back)));
                btn_petagebymonth.setTextColor(getResources().getColor(R.color.color_white));
                btn_petagebyyear.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_white)));
                btn_petagebyyear.setTextColor(getResources().getColor(R.color.color_button_back));
                break;
            case R.id.Pet_BTN_ageyear:
                ageYearorMonth = 12;
                btn_petagebyyear.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_back)));
                btn_petagebyyear.setTextColor(R.color.color_white);
                btn_petagebymonth.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_white)));
                btn_petagebymonth.setTextColor(R.color.color_button_back);
                break;
            case R.id.Pet_BTN_registerpet:
                if (TextUtils.isEmpty(imagePath)) {
                    Toast.makeText(getApplicationContext(), "업로드할 사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String ImageUploadURL = "http://18.216.142.72/AndroidImageUpload/uploadPetImage.php";
                        String uploadID = UUID.randomUUID().toString();
                        Log.d("Photo", logonUsername + returnTypeID);
                        new MultipartUploadRequest(this, uploadID, ImageUploadURL)
                                .setUtf8Charset()
                                .addParameter("username", logonUsername)
                                .addParameter("name", et_petName.getText().toString())
                                .addParameter("age", String.valueOf(Integer.valueOf(et_petAge.getText().toString()) * ageYearorMonth))
                                .addParameter("weight", et_petWeight.getText().toString())
                                .addParameter("breed", String.valueOf(breed))
                                .addParameter("spicies", spicies)
                                .addParameter("typeID", returnTypeID)
                                .addFileToUpload(imagePath, "image")
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                finish();

                break;
            case R.id.Pet_ET_pettype:
                Intent petTypeIntent = new Intent(this, GetPetTypeActivity.class);
                startActivityForResult(petTypeIntent, REQUEST_PETTYPE);
                break;
        }
    }

    private void initializeLayout() {


        iv_petImage = (ImageView) findViewById(R.id.Pet_IV_petimage);
        btn_addPetImage = (ImageButton) findViewById(R.id.Pet_BTN_picture);
        btn_petagebymonth = (Button) findViewById(R.id.Pet_BTN_agemonth);
        btn_petagebyyear = (Button) findViewById(R.id.Pet_BTN_ageyear);
        btn_registerPetData = (Button) findViewById(R.id.Pet_BTN_registerpet);
        btn_addPetImage.setOnClickListener(this);
        btn_petagebymonth.setOnClickListener(this);
        btn_petagebyyear.setOnClickListener(this);
        btn_registerPetData.setOnClickListener(this);
        et_petType = (EditText) findViewById(R.id.Pet_ET_pettype);
        et_petType.setOnClickListener(this);
        et_petName = (EditText) findViewById(R.id.Pet_ET_petname);
        et_petAge = (EditText) findViewById(R.id.Pet_ET_age);
        et_petWeight = (EditText) findViewById(R.id.Pet_ET_weight);
        spinner_petSex = (Spinner) findViewById(R.id.Pet_SPIN_sex);
        adspin = ArrayAdapter.createFromResource(this, R.array.breed, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_petSex.setAdapter(adspin);
        spinner_petSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                breed = (int) adspin.getItemId(i) + 1;
                Toast.makeText(RegisterPetActivity.this, String.valueOf(breed), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void doTakePhotoAction() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(saveFileName));
        cameraIntent.putExtra("return-data", true);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }

    private void getGallery() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
        galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(chooserIntent, REQUEST_TAKE_ALBUM);
    }

    private Uri getImageUri(String saveFile) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/DCIM", saveFolderName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdir()) {
                Log.d("Make Dir", "fail to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (saveFile != null) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + saveFile);
        } else {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "pic_" + timeStamp + ".jpg");
        }
        mImageCaptureUri = Uri.fromFile(mediaFile);
        imagePath = mImageCaptureUri.getPath();
        Log.e("mimageCaptureUri: ", mImageCaptureUri.toString());
        Log.e("imagePath : ", imagePath);
        return mImageCaptureUri;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_TAKE_ALBUM:
                mImageCaptureUri = data.getData();
                Log.e("앨범이미지 CROP", mImageCaptureUri.getPath().toString());
                imagePath = getRealPathFromURI(mImageCaptureUri);
                Log.e("앨범이미지 경로", imagePath);
            case REQUEST_TAKE_PHOTO:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_IMAGE_CROP);
                break;
            case REQUEST_IMAGE_CROP:
                final Bundle extras = data.getExtras();

                String filePath = getImageUri(saveFileName).getPath();
                Log.e("mimageCaptureUri : ", "Croped" + mImageCaptureUri.toString());
                imagePath = filePath;
                if (extras != null) {
                    photoBitmap = (Bitmap) data.getExtras().get("data");
                    saveCropImage(photoBitmap, imagePath);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mediaFile)));
                } else {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }


                Picasso.with(mContext).load(new File(imagePath)).into(iv_petImage);
                break;
            case REQUEST_PETTYPE:
                et_petType.setText(data.getStringExtra("TypeName"));
                returnTypeID = data.getStringExtra("TypeID");

        }

    }

    private void saveCropImage(Bitmap bitmap, String filePath) {
        File copyFile = new File(filePath);
        BufferedOutputStream bufferedOutputStream = null;
        try {
            copyFile.createNewFile();
            int quality = 100;
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    //아래는 권한 요청 Callback 함수입니다. PERMISSION_GRANTED로 권한을 획득했는지 확인할 수 있습니다. 아래에서는 !=를 사용했기에
    //권한 사용에 동의를 안했을 경우를 if문으로 코딩되었습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }


    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImageCaptureUri != null) {
            File file = new File(mImageCaptureUri.getPath());
            if (file.exists()) {
                file.delete();
            }
            mImageCaptureUri = null;
        }
    }
}



