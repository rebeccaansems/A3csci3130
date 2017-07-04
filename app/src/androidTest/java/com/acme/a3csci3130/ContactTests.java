package com.acme.a3csci3130;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.DrawableContainer;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.base.MainThread;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.acme.a3csci3130.MyApplicationData.firebaseDBInstance;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactTests {

    private Contact newContact, editedContact;
    private MyApplicationData appState;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    public void createContacts(boolean willEdit){
        if(willEdit){
        } else {
            newContact = new Contact(appState.firebaseReference.push().getKey(),
                    1995, "Boats and Stuff", "Fisher", "123 Boat Street", "BC");
        }
    }

    @Test
    public void contactCreated(){
        createContacts(false);

        onView(withId(R.id.submitButton)).perform(click());

        onView(withId(R.id.businessNumber))
                .perform(clearText(), typeText(String.valueOf(newContact.businessNum)), closeSoftKeyboard());

        onView(withId(R.id.name))
                .perform(clearText(), typeText(newContact.name), closeSoftKeyboard());

        onView(withId(R.id.primaryBusiness)).perform(click());
        onData(anything()).atPosition(0).perform(click());

        onView(withId(R.id.address))
                .perform(clearText(), typeText(newContact.address), closeSoftKeyboard());

        onView(withId(R.id.provinceTerritory)).perform(click());
        onData(anything()).atPosition(1).perform(click());

        onView(withId(R.id.submitInfoButton)).perform(click());

        int index = MainActivity.firebaseAdapter.getCount()-1;

        Assert.assertEquals(newContact.businessNum, MainActivity.firebaseAdapter.getItem(index).businessNum);
        Assert.assertEquals(newContact.name, MainActivity.firebaseAdapter.getItem(index).name);
        Assert.assertEquals(newContact.primaryBusiness, MainActivity.firebaseAdapter.getItem(index).primaryBusiness);
        Assert.assertEquals(newContact.address, MainActivity.firebaseAdapter.getItem(index).address);
        Assert.assertEquals(newContact.province, MainActivity.firebaseAdapter.getItem(index).province);
    }

    @Test
    public void contactEdited() throws InterruptedException {
        editedContact = new Contact(MainActivity.firebaseAdapter.getItem(0).uid,
                2017, "Fish and Stuff", "Processor", "123 Boat Street", "NB");

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.t_editBusinessNumber))
                .perform(clearText(), typeText(String.valueOf(editedContact.businessNum)), closeSoftKeyboard());

        onView(withId(R.id.t_editName))
                .perform(clearText(), typeText(editedContact.name), closeSoftKeyboard());

        onView(withId(R.id.s_editPrimaryBusiness)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(withId(R.id.t_editAddress))
                .perform(clearText(), typeText(editedContact.address), closeSoftKeyboard());

        onView(withId(R.id.s_editProvince)).perform(click());
        onData(anything()).atPosition(3).perform(click());

        onView(withId(R.id.b_Update)).perform(click());

        Assert.assertEquals(editedContact.businessNum, MainActivity.firebaseAdapter.getItem(0).businessNum);
        Assert.assertEquals(editedContact.name, MainActivity.firebaseAdapter.getItem(0).name);
        Assert.assertEquals(editedContact.primaryBusiness, MainActivity.firebaseAdapter.getItem(0).primaryBusiness);
        Assert.assertEquals(editedContact.address, MainActivity.firebaseAdapter.getItem(0).address);
        Assert.assertEquals(editedContact.province, MainActivity.firebaseAdapter.getItem(0).province);
    }

    @Test
    public void contactRemoved(){
        int currentNumberContacts = MainActivity.firebaseAdapter.getCount();

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.b_Delete)).perform(click());

        Assert.assertEquals(currentNumberContacts-1, MainActivity.firebaseAdapter.getCount());
    }
}
