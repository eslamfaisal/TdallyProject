package com.fekrah.tdally.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;
import com.fekrah.tdally.helper.SamplePresenter;
import com.fekrah.tdally.models.RegisterResponse;
import com.fekrah.tdally.server.Apis;
import com.fekrah.tdally.server.BaseClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.lamudi.phonefield.PhoneInputLayout;
import com.yalantis.ucrop.UCrop;
import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.ProcessType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

public class RegisterActivity extends LocationBaseActivity implements
        GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, SamplePresenter.SampleView {

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.password)
    EditText pass;

    @BindView(R.id.re_password)
    EditText re_password;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.city)
    EditText city;

    @BindView(R.id.location)
    TextView locationTv;

    @BindView(R.id.phone)
    PhoneInputLayout phoneInputLayout;

    @BindView(R.id.choose_image)
    FrameLayout choose_image;

    @BindView(R.id.profile_image)
    SimpleDraweeView image;

    boolean expanded;
    BottomSheetBehavior termsSheet;

    private SamplePresenter samplePresenter;
    private Location location;
    private String locationAdress;

    private String REQUEST_FOR_PICTURE;
    private static final String PROFILE_IMAGE_REQUEST = "PROFILE_IMAGE_REQUEST";
    private Bitmap thumbBitmap = null;
    UCrop.Options options;
    byte[] profilebyte;
    private Uri imageUri;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.creating_account));
        RelativeLayout llBottomSheet = findViewById(R.id.terms_sheet);
        termsSheet = BottomSheetBehavior.from(llBottomSheet);
        samplePresenter = new SamplePresenter(this);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        phoneInputLayout.setDefaultCountry("SA");
        phoneInputLayout.getTextInputLayout().getEditText().setTextColor(getResources().getColor(R.color.white));
       // phoneInputLayout.setTextColor(R.color.white);
        options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REQUEST_FOR_PICTURE = PROFILE_IMAGE_REQUEST;
                ImagePicker.create(RegisterActivity.this)
                        .limit(1)
                        .theme(R.style.UCrop)
                        .folderMode(true)
                        .start();
            }
        });

    }

    @OnClick(R.id.signBtn)
    void creatAccount() {
        progressDialog.show();
        if (
                username.getText().equals("") ||
                        name.getText().equals("") ||
                        pass.getText().equals("") ||
                        re_password.getText().equals("") ||
                        city.getText().equals("") ||
                        phoneInputLayout.getTextInputLayout().getEditText().getText().toString().equals("") ||
                        email.getText().equals("")||
                        imageUri==null
                ) {
            progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.fill_all), Toast.LENGTH_LONG).show();
            return;
        } else {
            if (!pass.getText().toString().equals(re_password.getText().toString())) {
                progressDialog.dismiss();
                Toast.makeText(this, getString(R.string.pass_not_match), Toast.LENGTH_SHORT).show();
                return;
            }
            if (location == null) {
                progressDialog.dismiss();
                Toast.makeText(this, getString(R.string.no_address_found), Toast.LENGTH_SHORT).show();
                return;
            }

            Apis apis = BaseClient.getBaseClient().create(Apis.class);

            File file2 = new File(imageUri.getPath());
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file2);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", String.valueOf(System.currentTimeMillis() + ".jpg"), surveyBody);

            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), "123456");
            RequestBody name2 = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
            RequestBody email2 = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
            RequestBody phone2 = RequestBody.create(MediaType.parse("text/plain"), phoneInputLayout.getTextInputLayout().getEditText().getText().toString());
            RequestBody city2 = RequestBody.create(MediaType.parse("text/plain"), city.getText().toString());
            RequestBody pass2 = RequestBody.create(MediaType.parse("text/plain"), pass.getText().toString());
            RequestBody username2 = RequestBody.create(MediaType.parse("text/plain"), username.getText().toString());
            RequestBody location2 = RequestBody.create(MediaType.parse("text/plain"), location.getLatitude()+","+location.getLongitude());


            apis.registerClient(
                    token,
                    name2,
                    email2,
                    username2,
                    pass2,
                    city2,
                    phone2,
                    city2,
                    city2,
                    location2,
                    image
                    ).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body()!=null){
                        if (response.body().getStatus()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            progressDialog.dismiss();
                            finish();
                        }else {
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                }
            });
        }

    }


    private static final String TAG = "MainActivityDriver";

    private GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();
    }


    public void showTerms(View view) {
        expanded = true;
        termsSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onBackPressed() {
        if (expanded) {
            termsSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            expanded = false;
        } else
            super.onBackPressed();
    }

//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent == null) {
//            return;
//        }
//        String errorMessage = "";
//
//        // Get the location passed to this service through an extra.
//        Location location = intent.getParcelableExtra(
//                Constants.LOCATION_DATA_EXTRA);
//
//        // ...
//
//        List<Address> addresses = null;
//
//        try {
//            addresses = geocoder.getFromLocation(
//                    location.getLatitude(),
//                    location.getLongitude(),
//                    // In this sample, get just a single address.
//                    1);
//        } catch (IOException ioException) {
//            // Catch network or other I/O problems.
//            errorMessage = getString(R.string.service_not_available);
//            Log.e(TAG, errorMessage, ioException);
//        } catch (IllegalArgumentException illegalArgumentException) {
//            // Catch invalid latitude or longitude values.
//            errorMessage = getString(R.string.invalid_lat_long_used);
//            Log.e(TAG, errorMessage + ". " +
//                    "Latitude = " + location.getLatitude() +
//                    ", Longitude = " +
//                    location.getLongitude(), illegalArgumentException);
//        }
//
//        // Handle case where no address was found.
//        if (addresses == null || addresses.size()  == 0) {
//            if (errorMessage.isEmpty()) {
//                errorMessage = getString(R.string.no_address_found);
//                Log.e(TAG, errorMessage);
//            }
//            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
//        } else {
//            Address address = addresses.get(0);
//            ArrayList<String> addressFragments = new ArrayList<String>();
//
//            // Fetch the address lines using getAddressLine,
//            // join them, and send them to the thread.
//            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//            Log.i(TAG, getString(R.string.address_found));
//            deliverResultToReceiver(Constants.SUCCESS_RESULT,
//                    TextUtils.join(System.getProperty("line.separator"),
//                            addressFragments));
//        }
//    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    String string;

    @Override
    public String getText() {
        return string;
    }

    @Override
    public void setText(String text) {
        string = text;
    }

    @Override
    public void updateProgress(String text) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(text);
        }
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        samplePresenter.onProcessTypeChanged(processType);
    }

    @Override
    public void onLocationChanged(Location location2) {
        location = location2;
        samplePresenter.onLocationChanged(location);
        locationTv.setText("" + location2.getLatitude() + " " + location2.getLongitude());
        getName();
    }

    @Override
    public void onLocationFailed(int type) {
        samplePresenter.onLocationFailed(type);
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration(getString(R.string.get_location_permistion),
                getString(R.string.gps_message));
    }

    public static final int PLACE_PICKER_REQUEST = 3;
    public static final int MY_PERMISSION = 34;

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationManager.onActivityResult(requestCode, resultCode, data);
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

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgress();
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage(getString(R.string.getting_locations));
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (samplePresenter != null)
            samplePresenter.destroy();
    }

    void getName() {
        String errorMessage = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        // ...

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.no_internet);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            // deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            // ArrayList<String> addressFragments = new ArrayList<String>();
            locationAdress = address.getAddressLine(0);
            city.setText(address.getAddressLine(0).toString() + " " + address.getFeatureName());
//            // Fetch the address lines using getAddressLine,
//            // join them, and send them to the thread.
//            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//            Log.i(TAG, getString(R.string.address_found));
////            deliverResultToReceiver(Constants.SUCCESS_RESULT,
////                    TextUtils.join(System.getProperty("line.separator"),
////                            addressFragments));
        }
    }
}
