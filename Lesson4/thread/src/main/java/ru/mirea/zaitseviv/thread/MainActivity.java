package ru.mirea.zaitseviv.thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.zaitseviv.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parString = binding.editTextPary.getText().toString();
                String daysString = binding.editTextDNY.getText().toString();

                if (TextUtils.isEmpty(parString) || TextUtils.isEmpty(daysString)) {
                    binding.textViewText.setText("Не все поля заполнены");
                    return;
                }

                int numberOfPairs = Integer.parseInt(parString);
                int numberOfDays = Integer.parseInt(daysString);
                new CalculateAverageTask().execute(numberOfPairs, numberOfDays);
            }
        });
    }
    private class CalculateAverageTask extends AsyncTask<Integer, Void, Double> {
        @Override
        protected Double doInBackground(Integer... params) {
            int numberOfPairs = params[0];
            int numberOfDays = params[1];
            if (numberOfDays == 0) {
                return 0.0;
            }
            return (double) numberOfPairs / numberOfDays;
        }
        @Override
        protected void onPostExecute(Double result) {
            binding.textViewText.setText(String.format("Среднее количество пар в день: %.2f", result));
        }
    }
}