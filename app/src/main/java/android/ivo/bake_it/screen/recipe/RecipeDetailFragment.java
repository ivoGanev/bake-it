package android.ivo.bake_it.screen.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.ivo.bake_it.BakeItApplication;
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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class RecipeDetailFragment extends Fragment implements View.OnClickListener {

    FragmentRecipeDetailBinding binding;

    SimpleExoPlayer player;

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
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getSimpleName() +
                    "Must implement: " + OnStepNavigationListener.class.getSimpleName());
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        if (binding.fragmentRecipeDetailBtnNext != null) {
            binding.fragmentRecipeDetailBtnNext.setOnClickListener(this);
        }
        if (binding.fragmentRecipeDetailBtnPrev != null) {
            binding.fragmentRecipeDetailBtnPrev.setOnClickListener(this);
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            updateUi(arguments);
        }
        return binding.getRoot();
    }

    private String getVideoUri() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Step step = getNonNullStep(arguments);
            return step.getVideoURL();
        }
        return null;
    }

    private Step getNonNullStep(@NotNull Bundle extras) {
        Step step = extras.getParcelable(StepActivity.STEP_BUNDLE_KEY);
        if (step == null)
            throw new NullPointerException("The provided step is required to be non-null.");
        return step;
    }

    private void initializePlayer(String uri) {
        PlayerView playerView = binding.fragmentRecipeDetailExoPlayer;
        if (uri.equals("")) {
            playerView.setVisibility(View.GONE);
            return;
        } else {
            playerView.setVisibility(View.VISIBLE);
        }

        if (player != null)
            player.stop();

        if (player==null) {
            player = new SimpleExoPlayer.Builder(requireContext()).build();
            binding.fragmentRecipeDetailExoPlayer.setPlayer(player);
        }

        BakeItApplication application = (BakeItApplication) requireContext().getApplicationContext();
        if (application.deviceIsInLandscapeMode()) {
            binding.fragmentRecipeDetailExoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        MediaSource mediaSource = toMediaSource(uri);
        player.prepare(mediaSource);

        player.setPlayWhenReady(false);
        player.seekTo(0, 0);
    }

    @NotNull
    private MediaSource toMediaSource(String uriString) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireContext(),
                Util.getUserAgent(requireContext(), "bake it"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(uriString));
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        binding.fragmentRecipeDetailExoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void updateUi(@NotNull Bundle stepBundle) {
        setArguments(stepBundle);
        Step step = getNonNullStep(stepBundle);
        if (binding != null) {
            initializePlayer(step.getVideoURL());

            int currentPage = stepBundle.getInt(StepActivity.CURRENT_STEP_PAGE);
            if (binding.fragmentRecipeDetailCurrentPage != null) {
                binding.fragmentRecipeDetailCurrentPage.setText("" + currentPage);
            }
            if (binding.fragmentRecipeDetailDescription != null) {
                binding.fragmentRecipeDetailDescription.setText(step.getDescription());
            }
        } else {
            throw new NullPointerException(RecipeDetailFragment.class.getSimpleName() +
                    ":  The view binding has not being set yet.");
        }
    }

    @Override
    public void onClick(View view) {
        if (getArguments() == null) {
            Timber.e("Step bundle cannot be null.");
            return;
        }
        int currentPage = getArguments().getInt(StepActivity.CURRENT_STEP_PAGE);

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

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24)
            initializePlayer(getVideoUri());
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer(getVideoUri());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        if (binding != null) {
            binding = null;
        }
    }
}

