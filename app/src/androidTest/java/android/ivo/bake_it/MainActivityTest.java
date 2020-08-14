package android.ivo.bake_it;

import android.ivo.bake_it.screen.main.MainActivity;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
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
    public ActivityScenarioRule<MainActivity> activityScenario
            = new ActivityScenarioRule<>(MainActivity.class);
    IdlingResource idlingResource;

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
    public void hello() {

    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null)
            IdlingRegistry.getInstance().unregister(idlingResource);
    }
    // check if the button we clicked has the same name as the next activity title

    // check if we click on a recipe if the fragment has the steps fragment has the same content
    // as the master fragment

    // check if we click next and previous buttons will display correct steps

    // check idling resources if we are properly displaying loading indicator

    // check idling resources for correctly retrieving data

    // check recycler view if all the positions are marked with 1,2,3,4

}