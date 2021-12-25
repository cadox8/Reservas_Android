package es.ivan.espinardo.activities.utils;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import es.ivan.espinardo.R;

public class ErrorActivity extends AppCompatActivity {

    private static String MESSAGE = "Error!";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_error);

        ((TextView)this.findViewById(R.id.error_message)).setText(MESSAGE);
    }

    public static ErrorActivity with(String msg) {
        ErrorActivity.MESSAGE = msg;
        return new ErrorActivity();
    }
}
