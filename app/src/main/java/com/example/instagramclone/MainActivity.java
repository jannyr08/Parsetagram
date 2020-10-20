package com.example.instagramclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private Button btnLogout;
    private Button btnNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNewPost = findViewById(R.id.btnNewPost);

        btnLogout = findViewById(R.id.btnLogout);

        ImageView ivNewPost = findViewById(R.id.ivNewPost);
        ImageView ivUser = findViewById(R.id.ivUser);

        btnNewPost.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);

        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goToNewPost();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                goToLogInPage();
            }
        });

        ivNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNewPost.setVisibility(View.VISIBLE);
            }
        });

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goToNewPost() {
        Intent i = new Intent(this, NewPost.class);
        startActivity(i);
        finish();
    }

    private void goToLogInPage() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}