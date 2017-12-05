package com.example.android.bakingtime.ui;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.android.bakingtime.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Yellow Cake"),
                        childAtPosition(
                                withId(R.id.gv_main_selectrecipe),
                                2),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("Starting prep"),
                        childAtPosition(
                                allOf(withId(R.id.lv_recipedetail),
                                        childAtPosition(
                                                withId(R.id.recipedetail_container),
                                                0)),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("Starting prep")));

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Starting prep"),
                        childAtPosition(
                                allOf(withId(R.id.lv_recipedetail),
                                        withParent(withId(R.id.recipedetail_container))),
                                2),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.nextbutton),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.stepcontainer),
                                        0),
                                1),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("Recipe Introduction"),
                        childAtPosition(
                                allOf(withId(R.id.lv_recipedetail),
                                        withParent(withId(R.id.recipedetail_container))),
                                1),
                        isDisplayed()));
        appCompatTextView3.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
