import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.TextView;

public class BrowseCommentsActivityTest extends ActivityInstrumentationTestCase2<BrowseCommentsActivity> {
    
    public BrowseCommentsActivityTest() {
	super(BrowseCommentsActivity.class);
    }
    /* Code for Use Case 4 tests start here
       Test to see if text is being displayed */
    public void testDisplayTopLevelComment() {
	Intent intent = new Intent();
	String text = "NewTopLevelComment";
	intent.putExtra(BrowseCommentsActivity.TEXT_KEY, text);
	setActivityIntent(intent);
	BrowseCommentsActivity activity = getActivity();
	TextView textView = (TextView) activity
		.findViewById(ca.ualberta.cs.project301.R.id.comment_item);
	assertEquals("text should be displayed", text, textView.getText()
		.toString());
    }

    // Test to see if default message is displayed when there are no top-level
    // comments
    public void testDefaultMessage() {
	String default_text = "Default";
	Intent intent = new Intent();
	setActivityIntent(intent);
	BrowseCommentsActivity activity = getActivity();
	TextView textView = (TextView) activity
		.findViewById(ca.ualberta.cs.project301.R.id.comment_item);
	assertEquals("text should not be displayed", default_text, textView
		.getText().toString());
    }

    // Test to see if top-level comments are displayed on the screen
    public void testVisibleTopLevelComment() {
	Intent intent = new Intent();
	String text = "top-level comment";
	intent.putExtra(BrowseCommentsActivity.TEXT_KEY, text);
	setActivityIntent(intent);
	BrowseCommentsActivity activity = getActivity();
	TextView textView = (TextView) activity
		.findViewById(ca.ualberta.cs.project301.R.id.comment_item);
	ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),
		textView);
    }
    
    // Tests for Use Case 4 end here

}