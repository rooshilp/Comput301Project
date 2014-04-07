package ca.ualberta.cs.cmput301t02project.test;

import java.util.ArrayList;

import ca.ualberta.cs.cmput301t02project.activity.CreateCommentActivity;
import ca.ualberta.cs.cmput301t02project.activity.MainMenuActivity;
import ca.ualberta.cs.cmput301t02project.model.CommentModel;
import ca.ualberta.cs.cmput301t02project.model.GPSLocation;
import ca.ualberta.cs.cmput301t02project.model.TopLevelCommentList;
import ca.ualberta.cs.cmput301t02project.model.User;
import android.app.Activity;
import android.app.Instrumentation;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class CreateCommentActivityTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

	Activity activity;
	String username = "default";
	String text = "the comment";

	protected void setUp() throws Exception {
		super.setUp();
		User.login(username, getInstrumentation().getContext());
		activity = getActivity();
	}

	public void initializeComment() {
		Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CreateCommentActivity.class.getName(), null , false);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Button button1 = (Button) activity.findViewById(ca.ualberta.cs.cmput301t02project.R.id.create);
				assertNotNull(button1);
				button1.performClick();
			}
		});
		
		getInstrumentation().waitForIdleSync();
		CreateCommentActivity childActivity = (CreateCommentActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
		final EditText edit = (EditText) childActivity.findViewById(ca.ualberta.cs.cmput301t02project.R.id.create_text);
		final Button button = (Button) childActivity.findViewById(ca.ualberta.cs.cmput301t02project.R.id.create_post);
		assertNotNull(edit);
		assertNotNull(button);
		childActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				edit.setText(text);
				button.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
	}
	
	public CreateCommentActivityTest() {
		super(MainMenuActivity.class);
		// TODO Auto-generated constructor stub
	}

	
	public void testCreateTopLevelComment() throws Throwable {
		initializeComment();
		ArrayList<CommentModel> list = TopLevelCommentList.getInstance(activity.getApplicationContext()).getList();
		int len = list.size();
		assertTrue("list comment should have same text", list.get(len - 1).getText().equals(text));
		assertTrue("list comment should have same username", list.get(len-1).getUsername().equals(username));
	}
	
	/* Test for use case 14 */
	public void testShareComment () {
		int size1 = TopLevelCommentList.getInstance(activity.getApplicationContext()).getList().size();
		initializeComment();
		int size2 = TopLevelCommentList.getInstance(activity.getApplicationContext()).getList().size();
		size1++;
        assertEquals("Size of top level comment list should be increased by 1 when new comment is added", size1, size2);
    }
	
	/* test to see if user is being pushed to server after update */
	public void testPushUser() {
		int size1 = User.getUser().getMyCommentIds().size();
		initializeComment();
		User.login(username, activity.getApplicationContext());
		int size2 = User.getUser().getMyCommentIds().size();
		size1++;
		assertEquals("Number of comments by user pulled from the server after adding comment should be increased by 1",size1, size2);
	}

}
