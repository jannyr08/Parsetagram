package com.example.instagramclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class NewPost extends AppCompatActivity {

    public static final String TAG = "New Post";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private EditText etDescription;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private Button btnCaptureImage;

    private File photoFile; // new post
    public String photoFileName = "photo.jpg";

    private ImageView ivPost;
    private TextView tvCaption;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        etDescription = findViewById(R.id.etDescription);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPost = findViewById(R.id.ivPost);

        ivPost = findViewById(R.id.ivPost);
        tvCaption = findViewById(R.id.tvCaption);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        // queryPosts();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(NewPost.this, "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(NewPost.this, "There is no image.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
                goToMainActivity();

            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(NewPost.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // head to New Post
                ivPostImage.setImageBitmap(takenImage);

                // Load the taken image into a preview
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePost(final String description, ParseUser currentUser, final File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving");
                    Toast.makeText(NewPost.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                etDescription.setText("");
                ivPostImage.setImageResource(0);
            }
        });
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
            }
        });
    }
}