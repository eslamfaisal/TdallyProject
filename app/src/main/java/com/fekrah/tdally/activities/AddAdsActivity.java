package com.fekrah.tdally.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;
import com.fekrah.tdally.helper.SharedHelper;
import com.fekrah.tdally.models.CategoriesResponse;
import com.fekrah.tdally.models.RegisterResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdsActivity extends AppCompatActivity {

    @BindView(R.id.category)
    Spinner category;

    @BindView(R.id.sub_category)
    Spinner sub_category;

    @BindView(R.id.ad_type)
    Spinner adType;

    @BindView(R.id.ad_name)
    EditText name;

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.ad_image)
    SimpleDraweeView image;

    @BindView(R.id.phone)
    EditText phone;

    @BindView(R.id.description)
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);
        ButterKnife.bind(this);
        categoryView();
        setAdType();
        options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        phone.setText(SharedHelper.getKey(this,LoginActivity.USER_PHONE));
    }


    public void goMain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    List<String> categoryNames = new ArrayList<>();
    List<String> categoryIds = new ArrayList<>();
    List<CategoriesResponse.Data> catList = new ArrayList<>();
    public static String CAT_ID;

    List<String> subCategoryNames = new ArrayList<>();
    List<String> subCategoryIds = new ArrayList<>();
    List<CategoriesResponse.SubCategory> subCatList = new ArrayList<>();
    public static String SUB_CAT_ID;


    void categoryView() {
        Apis apis = BaseClient.getBaseClient().create(Apis.class);
        apis.getCategory("123456", "ar").enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.body() != null) {
                    catList = response.body().getData();
                    CAT_ID = catList.get(0).getId();
                    SUB_CAT_ID = catList.get(0).getSubCategory().get(0).getUbcCategoryId();
                    for (CategoriesResponse.Data category : catList) {
                        categoryNames.add(category.getName());
                        categoryIds.add(category.getId());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(AddAdsActivity.this, R.layout.layout_cities_spinner_item, categoryNames);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    category.setAdapter(adapter);
                    category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CAT_ID = String.valueOf(catList.get(position).getId());

                            subCatList = catList.get(position).getSubCategory();
                            subCategoryNames = new ArrayList<>();
                            subCategoryIds = new ArrayList<>();
                            for (CategoriesResponse.SubCategory category : subCatList) {
                                subCategoryNames.add(category.getNameSubCategory());
                                subCategoryIds.add(category.getUbcCategoryId());
                            }

                            ArrayAdapter adapter = new ArrayAdapter(AddAdsActivity.this, R.layout.layout_cities_spinner_item, subCategoryNames);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sub_category.setAdapter(adapter);
                            sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    SUB_CAT_ID = subCatList.get(position).getUbcCategoryId();

                                 //
                                    //   Toast.makeText(AddAdsActivity.this, "" + SUB_CAT_ID, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {

            }
        });

    }


    List<String> typeNames = new ArrayList<>();

    public static String AD_TYPE;

    void setAdType() {
        typeNames.add("بدون");
        typeNames.add("جديد");
        typeNames.add("مستعمل");


        ArrayAdapter adapter = new ArrayAdapter(AddAdsActivity.this, R.layout.layout_cities_spinner_item, typeNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adType.setAdapter(adapter);
        adType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AD_TYPE = typeNames.get(position);

               // Toast.makeText(AddAdsActivity.this, "" + AD_TYPE, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String REQUEST_FOR_PICTURE;
    private static final String PROFILE_IMAGE_REQUEST = "PROFILE_IMAGE_REQUEST";
    private Bitmap thumbBitmap = null;
    UCrop.Options options;
    byte[] profilebyte;
    private Uri imageUri;

    ProgressDialog progressDialog;

    @OnClick(R.id.choose_image)
    void chooseImage(){
        REQUEST_FOR_PICTURE = PROFILE_IMAGE_REQUEST;
        ImagePicker.create(this)
                .limit(1)
                .theme(R.style.UCrop)
                .folderMode(true)
                .start();
    }

    @OnClick(R.id.send_btn)
    void sendAd() {

        if (
                name.getText().toString().equals("") ||
                        address.getText().toString().equals("") ||
                        phone.getText().toString().equals("") ||
                        price.getText().toString().equals("") ||
                        description.getText().toString().equals("") ||
                        phone.getText().toString().equals("") ||
                        AD_TYPE == null ||
                        CAT_ID == null ||
                        SUB_CAT_ID == null||
                        imageUri==null

                ){
            Toast.makeText(this, getString(R.string.fill_all), Toast.LENGTH_SHORT).show();
            return;
        }else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.sending_ad));
            progressDialog.show();
            File file2 = new File(imageUri.getPath());
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file2);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", String.valueOf(System.currentTimeMillis() + ".jpg"), surveyBody);

            RequestBody toaken2 = RequestBody.create(MediaType.parse("text/plain"),  SharedHelper.getKey(this,LoginActivity.USER_TOKEN));

            RequestBody address2 = RequestBody.create(MediaType.parse("text/plain"),  address.getText().toString());
            RequestBody name2 = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
            RequestBody price2 = RequestBody.create(MediaType.parse("text/plain"), price.getText().toString());
            RequestBody phone2 = RequestBody.create(MediaType.parse("text/plain"), phone.getText().toString());
            RequestBody description2 = RequestBody.create(MediaType.parse("text/plain"), description.getText().toString());
            RequestBody AD_TYPE2 = RequestBody.create(MediaType.parse("text/plain"), AD_TYPE);
            RequestBody CAT_ID2 = RequestBody.create(MediaType.parse("text/plain"), CAT_ID);
            RequestBody SUB_CAT_ID2 = RequestBody.create(MediaType.parse("text/plain"), SUB_CAT_ID);


            Apis apis = BaseClient.getBaseClient().create(Apis.class);

            apis.adAdd(
                    toaken2,
                    name2,
                    address2,
                    CAT_ID2,
                    SUB_CAT_ID2,
                    price2,
                    AD_TYPE2,
                    phone2,
                    address2,
                    description2,
                    image

                    ).enqueue(new Callback<com.fekrah.tdally.models.Response>() {
                @Override
                public void onResponse(Call<com.fekrah.tdally.models.Response> call, Response<com.fekrah.tdally.models.Response> response) {
                    Log.d("eeeeeeeeeeeeeeeeee", "succc: "+response.body().getStatus());
                    progressDialog.dismiss();
                    if (response.body()!=null){
                        if (response.body().getStatus()){
                            Toast.makeText(AddAdsActivity.this, "تم ارسال الاعلان فى انتظار الموافقة", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AddAdsActivity.this, ""+response.body().getData().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<com.fekrah.tdally.models.Response> call, Throwable t) {
                    Log.d("eeeeeeeeeeeeeeeeee", "onFailure: "+t.getMessage());
                    progressDialog.dismiss();

                    Toast.makeText(AddAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            //Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            return;
        }
        String destinationFileName = "SAMPLE_CROPPED_IMAGE_NAME" + ".jpg";

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);

            Uri res_url = Uri.fromFile(new File((image.getPath())));
            //imageUri = image.getPath();
            CropImage(image, res_url);

        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            //  if (resultUri!=null)
            assert resultUri != null;
            bitmapCompress(resultUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);

            profilebyte = byteArrayOutputStream.toByteArray();
            image.setImageURI(resultUri);
            image.setVisibility(View.VISIBLE);


            imageUri = resultUri;

        }


    }

    private void CropImage(Image image, Uri res_url) {
        UCrop.of(res_url, Uri.fromFile(new File(this.getCacheDir(), image.getName())))
                .withOptions(options)
                .start(this);
    }

    private void bitmapCompress(Uri resultUri) {
        final File thumbFilepathUri = new File(resultUri.getPath());

        try {
            thumbBitmap = new Compressor(this)
                    .setQuality(50)
                    .compressToBitmap(thumbFilepathUri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
