package ca.ualberta.cs.cmput301t02project.test;

import ca.ualberta.cs.cmput301t02project.ProjectApplication;
import ca.ualberta.cs.cmput301t02project.R;
import ca.ualberta.cs.cmput301t02project.activity.BrowseFavoritesActivity;
import ca.ualberta.cs.cmput301t02project.model.CommentListModel;
import ca.ualberta.cs.cmput301t02project.model.CommentModel;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.ListView;

public class BrowseFavoritesActivityTest extends ActivityInstrumentationTestCase2<BrowseFavoritesActivity> {

	public BrowseFavoritesActivityTest() {
		super(BrowseFavoritesActivity.class);
	}

	public CommentModel initializeComment() {
		String loc = "Location Intialization";
		Location currentLocation;
		Location myLocation;
		currentLocation = new Location(loc);
		myLocation = new Location(loc);

		CommentModel comment = new CommentModel("comment", currentLocation,"username");

		ProjectApplication.setCurrentLocation(myLocation);

		return comment;
	}

	public void testDisplayFavorites() {

		CommentModel comment = initializeComment();
		CommentListModel favoriteComments = new CommentListModel();
		favoriteComments = ProjectApplication.getUser().getFavorites();
		favoriteComments.add(comment);

		ListView view = (ListView) getActivity().findViewById(R.id.commentListView);
		assertEquals("text should be displayed", comment.toString(), view.getAdapter().getItem(0).toString());

	}

	public void testVisibleListView() {

		// Throwing an error, not failure -KW
		BrowseFavoritesActivity activity = getActivity();
		ListView view = (ListView) activity.findViewById(R.id.commentListView);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), view);
	}

	/* Test for use case 21 */
	public void testDisplayUsername() {
		CommentModel comment = initializeComment();
		ListView view = (ListView) getActivity().findViewById(R.id.commentListView);
		assertTrue("username should be displayed", view.getAdapter().getItem(0).toString().contains(comment.getUsername()));

	}


	/* test for use case 12 */
	public void testReadFavorites() {
		CommentModel comment = initializeComment();
		CommentListModel favoriteComments = new CommentListModel();
		favoriteComments = ProjectApplication.getUser().getFavorites();
		favoriteComments.add(comment);
		
		assertEquals("Comments should be the same",comment,ProjectApplication.getUser().getFavorites().getCommentList().get(0));
	}

}
