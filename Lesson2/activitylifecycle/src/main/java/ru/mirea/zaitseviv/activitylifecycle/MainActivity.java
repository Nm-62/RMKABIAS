package ru.mirea.zaitseviv.activitylifecycle;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityLifecycle";
    private EditText editText;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Activity создается");

        editText = findViewById(R.id.editText);
        statusTextView = findViewById(R.id.statusTextView);

        updateStatus("onCreate выполнен");

        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("editTextValue", "");
            editText.setText(savedText);
            Log.d(TAG, "onCreate: Восстановлен текст: " + savedText);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Activity становится видимым");
        updateStatus("onStart выполнен");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity готово для взаимодействия");
        updateStatus("onResume выполнен");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Activity приостанавливается");
        updateStatus("onPause выполнен");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Activity больше не видно");
        updateStatus("onStop выполнен");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Activity перезапускается");
        updateStatus("onRestart выполнен");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity уничтожается");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: Сохранение состояния");

        String text = editText.getText().toString();
        outState.putString("editTextValue", text);
        Log.d(TAG, "onSaveInstanceState: Сохранен текст: " + text);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: Восстановление состояния");
        updateStatus("onRestoreInstanceState выполнен");
    }

    private void updateStatus(String message) {
        String currentText = statusTextView.getText().toString();
        statusTextView.setText(currentText + "\n" + message);
    }
}