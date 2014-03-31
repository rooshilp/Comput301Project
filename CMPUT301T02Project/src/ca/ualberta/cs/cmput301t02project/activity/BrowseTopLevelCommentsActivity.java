package ca.ualberta.cs.cmput301t02project.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.ualberta.cs.cmput301t02project.R;
import ca.ualberta.cs.cmput301t02project.model.CommentListModel;
import ca.ualberta.cs.cmput301t02project.model.CommentModel;
import ca.ualberta.cs.cmput301t02project.view.CommentListAdapter;
import ca.ualberta.cs.cmput301t02project.view.CommentListAdapterAbstraction;

/**
 * Displays comments top level comments (aka. comments with no parents, comments that are not replies to anything). 
 * A list of all top level comments is stored in ProjectApplication.getInstance().
 */

public class BrowseTopLevelCommentsActivity extends BrowseCommentsActivityAbstraction {
	
	// TODO: Refactor code to use new classes
	
	private ArrayList<CommentModel> topLevelCommentList;
	private ListView topLevelCommentListView;
	//private CommentListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_level_list);
		topLevelCommentListView = (ListView) findViewById(R.id.commentListView);

		// Create the sortBy menu and set up the adapter, inherited from BrowseCommentsActivity -SB
		setupPage();
		
		topLevelCommentListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				// Refactor into MVC?	
				CommentModel nestedComment = (CommentModel) adapter.getItem(position);
				ProjectApplication.getInstance().setCurrentComment(nestedComment, getApplicationContext());
				
				//CommentListModel nestedCommentList = nestedComment.getReplies();
				//ProjectApplication.getInstance().setCurrentCommentList(nestedCommentList);
				
				Intent goToReplyListActivity = new Intent(getApplicationContext(),BrowseReplyCommentsActivity.class);
				startActivity(goToReplyListActivity);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		initializeAdapter();
		adapter.sortList();
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Creates an adapter for displaying the top level comments.
	 * <p>
	 * Called from onCreate().
	 * Replies to a comment can be viewed by clicking that comment. 
	 * <p>
	 * @return 
	 */
	public CommentListAdapterAbstraction initializeAdapter(){
		
		// Call the method in the CommentListModel class that will 
		// pull comments from the server - TH
		topLevelCommentList = new ArrayList<CommentModel>();
		topLevelCommentList = CommentListModel.getTopLevelComments(getApplicationContext()).getCommentList();
		

		// Add comments to adapter
		adapter = new CommentListAdapter(this, R.layout.list_item, topLevelCommentList);
		adapter.setModel(topLevelCommentList);

		// Display comments in adapter
		topLevelCommentListView.setAdapter(adapter);
		
		return adapter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.top_level_list, menu);
		return true;
	}
}
