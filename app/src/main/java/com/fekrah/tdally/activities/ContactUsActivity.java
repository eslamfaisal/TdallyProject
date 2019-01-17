package com.fekrah.tdally.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void goWhatsApp(View view) {
        String contact = "+9660550411663"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "تطبيق واتساب غير مثبت", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void goMessages(View view) {

        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", "+9660550411663");
        smsIntent.putExtra("sms_body", "السلام عليكم \n تطبيق تدللى");
        startActivity(smsIntent);

    }

    public void goWebSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://tdlly.com")));
    }

    public void goEmail(View view) {

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@tdlly.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "السلام عليكم تطبيق تدللى");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }

    public void goCalls(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+9660550411663"));
        startActivity(intent);
    }
}
