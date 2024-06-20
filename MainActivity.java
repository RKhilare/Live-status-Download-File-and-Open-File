package com.example.livestatusdownloadfile;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button button;

    private EditText url;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url=findViewById(R.id.url);
        button = findViewById(R.id.btn_download);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar(download(url));
            }
        });

        button=findViewById(R.id.openBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

    }


    public MainActivity download(EditText url) {
        String getUrl = url.getText().toString();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrl));
        String title = URLUtil.guessFileName(getUrl, null, null);

        request.setTitle(title);
        request.setDescription("Downloading file");
        String cookie = CookieManager.getInstance().getCookie(getUrl);
        request.addRequestHeader("cookie", cookie);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
        DownloadManager mager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mager.enqueue(request);

        Toast.makeText(MainActivity.this, "Downloading started", Toast.LENGTH_SHORT).show();
        return null;
    }


    public MainActivity progressBar(MainActivity download)
    {

        final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Downloading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while (i<=100)
                {
                    try{
                        progressDialog.setProgress(i);
                        i++;
                        Thread.sleep(200);
                    }catch (InterruptedException e){
                        Log.d(("Name"),"Something wrong");
                    }
                }
                progressDialog.dismiss();
            }
        }).start();
        return null;
    }

    public MainActivity open()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        String filePath = "Downloads";
        intent.setDataAndType(Uri.parse(filePath), "*/*");

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity
            startActivity(intent);
        } else {
            Toast.makeText(this, "File this is not there", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}
