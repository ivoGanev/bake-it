package android.ivo.bake_it.screen.recipe;

import android.content.Context;
import android.ivo.bake_it.BundleKeys;
import android.ivo.bake_it.R;
import android.ivo.bake_it.databinding.FragmentRecipeDetailBinding;
import android.ivo.bake_it.model.Step;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class RecipeDetailFragment extends Fragment implements View.OnClickListener {

    FragmentRecipeDetailBinding binding;

    SimpleExoPlayer exoPlayer;

    OnStepNavigationListener stepNavigationListener;

    public static Fragment newInstance(Bundle bundle) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        try {
            stepNavigationListener = (OnStepNavigationListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getSimpleName() +
                    "Must implement: " + OnStepNavigationListener.class.getSimpleName());
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        binding.fragmentRecipeDetailBtnNext.setOnClickListener(this);
        binding.fragmentRecipeDetailBtnPrev.setOnClickListener(this);

        initializeExoPlayer();

        if (getArguments() != null) {
            Step step = getNonNullStep(getArguments());
            updateUi(getArguments());
            prepareExoPlayer(step.getVideoURL());
        }

        return binding.getRoot();
    }

    private Step getNonNullStep(@NotNull Bundle extras) {
        Step step = extras.getParcelable(BundleKeys.STEP_BUNDLE_KEY);
        if (step == null)
            throw new NullPointerException("The provided step is required to be non-null.");
        return step;
    }

    private void initializeExoPlayer() {
        exoPlayer = new SimpleExoPlayer.Builder(requireContext()).build();
        binding.fragmentRecipeDetailExoPlayer.setPlayer(exoPlayer);
    }

    @NotNull
    private MediaSource toMediaSource(String uriString) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireContext(),
                Util.getUserAgent(requireContext(), "bake it"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(uriString));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        if (binding != null) {
            binding = null;
        }
    }

    public void updateUi(@NotNull Bundle stepBundle) {
        setArguments(stepBundle);
        Step step = getNonNullStep(stepBundle);
        if (binding != null) {
            int currentPage = stepBundle.getInt(BundleKeys.STEP_DETAILS_CURRENT_PAGE);
            binding.fragmentRecipeDetailCurrentPage.setText("" + currentPage);
            binding.fragmentRecipeDetailDescription.setText(step.getDescription());
            prepareExoPlayer(step.getVideoURL());
        } else {
            throw new NullPointerException(RecipeDetailFragment.class.getSimpleName() +
                    ":  The view binding has not being set yet.");
        }
    }

    private void prepareExoPlayer(@NotNull String stringUri) {
        if (exoPlayer == null)
            throw new NullPointerException("Make sure the Exo player is initialised before" +
                    "calling this method");
        exoPlayer.stop();

        if (stringUri.equals("")) {
            binding.fragmentRecipeDetailExoPlayer.setPlayer(null);
            return;
        }
        binding.fragmentRecipeDetailExoPlayer.setPlayer(exoPlayer);
        MediaSource mediaSource = toMediaSource(stringUri);
        exoPlayer.prepare(mediaSource);
    }

    @Override
    public void onClick(View view) {
        if (getArguments() == null) {
            Timber.e("Step bundle cannot be null.");
            return;
        }
        int currentPage = getArguments().getInt(BundleKeys.STEP_DETAILS_CURRENT_PAGE);

        if (view.getId() == R.id.fragment_recipe_detail_btn_next) {
            if (stepNavigationListener != null)
                stepNavigationListener.onClickNextStep(currentPage);
        } else if (view.getId() == R.id.fragment_recipe_detail_btn_prev) {
            if (stepNavigationListener != null)
                stepNavigationListener.onClickPreviousStep(currentPage);
        }
    }

    public interface OnStepNavigationListener {
        void onClickNextStep(int position);

        void onClickPreviousStep(int position);
    }
}

