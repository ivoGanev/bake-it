package android.ivo.bake_it.screen.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.ivo.bake_it.R;
import android.os.Bundle;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Bundle bundle = getIntent().getExtras();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_step_fragment, RecipeDetailFragment.newInstance(getIntent().getExtras()))
                .commit();
    }
}