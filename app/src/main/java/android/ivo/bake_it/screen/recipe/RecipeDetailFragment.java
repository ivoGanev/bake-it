package android.ivo.bake_it.screen.recipe;

import android.ivo.bake_it.BundleKeys;
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

public class RecipeDetailFragment extends Fragment {

    FragmentRecipeDetailBinding binding;

    Step step;

    SimpleExoPlayer exoPlayer;

    public static Fragment newInstance(Bundle bundle) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle extras = getArguments();
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        if (extras != null) {
            step = extras.getParcelable(BundleKeys.STEP_BUNDLE_KEY);
            if (step != null) {
                binding.fragmentRecipeDetailDescription.setText(step.getDescription());
                initializeExoPlayer();
            }
        }
        return binding.getRoot();
    }

    private void initializeExoPlayer() {
        if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            exoPlayer = new SimpleExoPlayer.Builder(requireContext()).build();
            binding.fragmentRecipeDetailExoPlayer.setPlayer(exoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireContext(),
                    Util.getUserAgent(requireContext(), "bake it"));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(step.getVideoURL()));
            exoPlayer.prepare(mediaSource);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
        binding = null;
        step = null;
    }
}

