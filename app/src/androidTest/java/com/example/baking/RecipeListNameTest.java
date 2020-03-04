package com.example.baking;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeListNameTest {

    @Rule
    public ActivityTestRule<RecipeList> activityTestRule = new ActivityTestRule<>( RecipeList.class );


    @Test
    public void recipeNameCheck() {
       try
       {
           Thread.sleep( 10000 );
       }
       catch (InterruptedException e)
       {
           e.printStackTrace();
       }
        onData(anything()).inAdapterView(withId(R.id.gvRecipes)).atPosition(0).perform(click());
        onView(withText("Nutella Pie")).check(matches(withParent(withId(R.id.toolbar))));
    }
}
