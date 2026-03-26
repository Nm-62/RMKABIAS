package ru.mirea.faizulinama.securesharedpreferences;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private TextView Name;
    private ImageView Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = findViewById(R.id.name);
        Photo = findViewById(R.id.photo);

        try {

            String mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secure_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String actorName = secureSharedPreferences.getString("secure", "Johnny Silverhand (1988 - 2023)");
            Name.setText(actorName);

            SharedPreferences.Editor editor = secureSharedPreferences.edit();
            editor.putString("secure", "Johnny Silverhand (1988 - 2023)");
            editor.putString("johnny_silverhand", "johnny_silverhand");
            editor.apply();

            String photoName = secureSharedPreferences.getString("johnny_silverhand", "johnny_silverhand");

            int imageResId = getResources().getIdentifier(photoName, "raw", getPackageName());

            Resources res = getResources();
            InputStream inputStream = res.openRawResource(imageResId);
            Drawable drawable = Drawable.createFromStream(inputStream, photoName);
            Photo.setImageDrawable(drawable);

        } catch (Exception e) {
            e.printStackTrace();
            Name.setText("Ошибка загрузки данных");
        }
    }
}