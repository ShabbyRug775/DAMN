package com.example.hilos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.core.content.FileProvider;

import java.io.*;

public class MainActivity extends Activity {
    Button jbn;
    RadioButton jrb1, jrb2;
    Intent i;
    int TAKE_PICTURE = 1;
    int SELECT_PICTURE = 2;
    String filePath = "";
    Uri photoUri;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        File imageFile = new File(getExternalFilesDir(null), "test.jpg");
        filePath = imageFile.getAbsolutePath();

        jbn = findViewById(R.id.xbn1);
        jbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jrb1 = findViewById(R.id.xrb1);
                jrb2 = findViewById(R.id.xrb2);

                int code = TAKE_PICTURE;

                if (jrb1.isChecked()) {
                    i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoUri = FileProvider.getUriForFile(
                            MainActivity.this,
                            getApplicationContext().getPackageName() + ".provider",
                            imageFile
                    );
                    i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else if (jrb2.isChecked()) {
                    i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    code = SELECT_PICTURE;
                } else {
                    Toast.makeText(MainActivity.this, "Selecciona una opción primero", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivityForResult(i, code);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView iv = findViewById(R.id.xiv1);

        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
                if (photoUri != null) {
                    iv.setImageBitmap(BitmapFactory.decodeFile(filePath));
                }
            } else if (requestCode == SELECT_PICTURE && data != null) {
                Uri image = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(image);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);
                    iv.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
