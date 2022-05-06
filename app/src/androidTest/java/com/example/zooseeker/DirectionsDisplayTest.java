package com.example.zooseeker;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DirectionsDisplayTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void directionsDisplayTest() {
        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("mammal"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")), withText("mammal"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete2.perform(pressImeActionButton());

        DataInteraction constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.search_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2)))
                .atPosition(2);
        constraintLayout.perform(click());

        DataInteraction constraintLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.search_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2)))
                .atPosition(1);
        constraintLayout2.perform(click());

        DataInteraction constraintLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.search_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2)))
                .atPosition(0);
        constraintLayout3.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_btn), withText("plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.path_result), withText("From: Entrance and Exit Gate\nTo: Arctic Foxes\n\n1. Walk 10.0 meters along Entrance Way from Entrance and Exit Gate to Entrance Plaza\n2. Walk 300.0 meters along Arctic Avenue from Entrance Plaza to Arctic Foxes\n"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
        textView.check(matches(withText("From: Entrance and Exit Gate\nTo: Arctic Foxes\n\n1. Walk 10.0 meters along Entrance Way from Entrance and Exit Gate to Entrance Plaza\n2. Walk 300.0 meters along Arctic Avenue from Entrance Plaza to Arctic Foxes\n")));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.next_btn), withText("NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.path_result), withText("From: Arctic Foxes\nTo: Lions\n\n1. Walk 300.0 meters along Arctic Avenue from Arctic Foxes to Entrance Plaza\n2. Walk 100.0 meters along Reptile Road from Entrance Plaza to Alligators\n3. Walk 200.0 meters along Sharp Teeth Shortcut from Alligators to Lions\n"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
        textView2.check(matches(withText("From: Arctic Foxes\nTo: Lions\n\n1. Walk 300.0 meters along Arctic Avenue from Arctic Foxes to Entrance Plaza\n2. Walk 100.0 meters along Reptile Road from Entrance Plaza to Alligators\n3. Walk 200.0 meters along Sharp Teeth Shortcut from Alligators to Lions\n")));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.next_btn), withText("NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.path_result), withText("From: Lions\nTo: Elephant Odyssey\n\n1. Walk 200.0 meters along Africa Rocks Street from Lions to Elephant Odyssey\n"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
        textView3.check(matches(withText("From: Lions\nTo: Elephant Odyssey\n\n1. Walk 200.0 meters along Africa Rocks Street from Lions to Elephant Odyssey\n")));
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
