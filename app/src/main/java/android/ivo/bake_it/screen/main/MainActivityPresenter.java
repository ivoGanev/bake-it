package android.ivo.bake_it.screen.main;

import android.ivo.bake_it.api.ApiClientRemote;
import android.ivo.bake_it.model.Recipe;

public class MainActivityPresenter {
    private ApiClientRemote apiClientRemote;
    private View view;
    private Recipe recipe;

    public MainActivityPresenter(View view) {
        this.view = view;
    }

    interface View {

    }
}
