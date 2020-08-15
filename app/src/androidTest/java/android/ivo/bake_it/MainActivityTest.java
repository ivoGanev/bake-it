package android.ivo.bake_it;

import android.ivo.bake_it.idlingresource.SimpleIdlingResource;
import android.ivo.bake_it.screen.main.MainActivity;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.ivo.bake_it.matchers.ViewMatcher.atPosition;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenario
            = new ActivityScenarioRule<>(MainActivity.class);
    SimpleIdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        activityScenario.getScenario().onActivity(activity ->
        {
            BakeItApplication application = (BakeItApplication) activity.getApplication();
            idlingResource = application.getIdlingResource();
            IdlingRegistry.getInstance().register(idlingResource);
        });
    }

    @Test
    public void uiIsVisible_WhenDataIsLoaded() {
        onView(withId(R.id.activity_main_tv_no_network)).check(matches(not(isDisplayed())));
        onView(withId(R.id.activity_main_pb)).check(matches(not(isDisplayed())));
        onView(withId(R.id.activity_main_rv)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfFirstRecyclerViewItem_matchesApiItem() {
        onView(withId(R.id.activity_main_rv)).check(matches(atPosition(0, hasDescendant(withText("Nutella Pie")))));
    }


    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null)
            IdlingRegistry.getInstance().unregister(idlingResource);
    }

}