package android.ivo.bake_it;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = findViewById(R.id.a_main_cl);
        if(v!=null) {
            Toast.makeText(this, "Phone", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Tablet", Toast.LENGTH_LONG).show();
        }
    }
}