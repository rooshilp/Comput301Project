package ca.ualberta.cs.cmput301t02project.view;

import java.util.ArrayList;

import android.content.Context;
import ca.ualberta.cs.cmput301t02project.model.CommentModel;

/**
 * View for lists of comments favorited by the current user.
 */
public class FavoritesAdapter extends CommentListAdapterAbstraction {

	public FavoritesAdapter(Context context, int resource, ArrayList<CommentModel> model) {
		super(context, resource, model);
		// TODO Auto-generated constructor stub
	}

}
