package android.ivo.bake_it;

import android.content.Context;
import android.ivo.bake_it.matchers.ViewMatcher;
import android.ivo.bake_it.screen.main.MainActivity;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.logging.Handler;

import static android.ivo.bake_it.matchers.ViewMatcher.atPosition;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> intentsTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void selectRecyclerViewAtPosition_CheckCorrectTextDisplayed() {
        onView(withId(R.id.activity_main_rv))
                .perform(RecyclerViewActions.scrollToPosition(59))
                .check(matches(atPosition(59, hasDescendant(withText("Recipe 59")))));
    }
}