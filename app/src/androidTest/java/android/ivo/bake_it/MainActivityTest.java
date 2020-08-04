package android.ivo.bake_it;

import android.ivo.bake_it.screen.main.MainActivity;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.ivo.bake_it.matchers.ViewMatcher.atPosition;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
    public void selectRecyclerViewAtPosition_CheckStartsCorrectActivity1() {
        onView(withId(R.id.activity_main_rv))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText("Nutella Pie")))))
                .perform(click());

        onView(withId(R.id.fragment_recipe_title)).check(matches(withText("Nutella Pie")));
    }
    @Test
    public void selectRecyclerViewAtPosition_CheckStartsCorrectActivity2() {
        onView(withId(R.id.activity_main_rv))
                .perform(RecyclerViewActions.scrollToPosition(1))
                .perform(click());

        onView(withId(R.id.fragment_recipe_title)).check(matches(withText("Nutella Pie")));
    }

}