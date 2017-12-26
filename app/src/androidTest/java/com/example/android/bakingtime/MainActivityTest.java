package com.example.android.bakingtime;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingtime.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.anything;


/**
 * Class to Test Main Activity
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnFood_OpenFoodRecipeDetailsAndNext() {
        onView(withId(R.id.gv_main_selectrecipe)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));


        onView(withId(R.id.tv_recipedetail_name)).check(matches(withText("Yellow Cake")));

        onView(withId(R.id.btn_nextitem)).perform(click());

        onView(withId(R.id.tv_recipedetail_name)).check(matches(withText("Cheesecake")));
        onData(anything()).inAdapterView(withId(R.id.lv_recipedetail)).atPosition(4).onChildView(withId(R.id.tv_item_shortDesc)).check(matches(withText("Start water bath.")));


    }
}
