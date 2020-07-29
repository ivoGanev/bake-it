package android.ivo.bake_it.screen.main;

import android.ivo.bake_it.api.ApiClient;
import android.ivo.bake_it.model.Recipe;
import android.view.View;
import android.widget.Toast;

public class MainActivityPresenter {
    private ApiClient apiClient;
    private View view;
    private Recipe recipe;

    public MainActivityPresenter(View view) {
        this.view = view;
    }

    interface View {

    }
}
