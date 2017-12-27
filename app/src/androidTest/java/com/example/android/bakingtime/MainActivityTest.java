package com.example.android.bakingtime;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.android.bakingtime.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static org.hamcrest.CoreMatchers.allOf;


/**
 * Class to Test Main Activity
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnFood_OpenFoodRecipeDetailsAndNext() {


        onView(withId(R.id.gv_main_selectrecipe)).perform(scrollTo(hasDescendant(withText("Yellow Cake"))));
        onView(withItemText("Yellow Cake")).perform(click());

        onView(allOf(withId(R.id.tv_recipedetail_name))).check(matches(isDisplayed()));


        onView(withId(R.id.btn_nextitem)).perform(click());


        onView(withId(R.id.tv_recipedetail_name)).check(matches(withText("Cheesecake")));

        onView(withId(R.id.lv_recipedetail)).perform(scrollTo(hasDescendant(withText("Start water bath."))));

        onView(withItemText("Start water bath.")).check(matches(isDisplayed()));


    }

    /**
     * Functionality taken from Codelabs
     * https://github.com/googlecodelabs/android-testing/blob/master/app/src/androidTest/java/com/example/android/testing/notes/notes/NotesScreenTest.java
     * <p>
     * Custom Matcher is used to handle RecyclerView items
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

}
