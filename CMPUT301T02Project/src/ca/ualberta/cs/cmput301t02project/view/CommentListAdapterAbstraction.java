package ca.ualberta.cs.cmput301t02project.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ca.ualberta.cs.cmput301t02project.ProjectApplication;
import ca.ualberta.cs.cmput301t02project.model.CommentListModel;
import ca.ualberta.cs.cmput301t02project.model.CommentModel;
import android.content.Context;
import android.location.Location;
import android.widget.ArrayAdapter;

/**
 * CommentListAdapterAbstraction is a subclass of ArrayAdapter that is extended by
 * MyCommentsAdapter, FavoritesAdapter and CommentListAdapter. It is considered a
 * view under MVC. CommentListAdapterAbstraction implements sorting functionality
 * and is called on by either an activity, to change sorting method, or by its model,
 * to update the view and/or resort the list due to changes to the model.
 */
public abstract class CommentListAdapterAbstraction extends ArrayAdapter<CommentModel> {

	private CommentListModel model = null;
	private String sortMethod = "Default";
	private Comparator<CommentModel> sortByDate;
	private Comparator<CommentModel> sortByLocation;
	private Location myLocation = null;
	

	/**
	 * Constructs a new instance of ArrayAdapter<CommentModel>
	 * <p>
	 * The constructor is adapted from the original ArrayAdapter<> constructor, using the
	 * same inputs and calling super() with those inputs. The changes are made with the 
	 * instantiation of the comparators for sortByDate and sortByLocation. These comparators
	 * are used to sort the ArrayList<CommentModel> that is within the model this adapter displays.
	 * <p>
	 * @param context The current context of the application
	 * @param resource The resource ID for the layout file containing the ListView to use
	 * @param model Objects to represent in the ListView
	 */
	public CommentListAdapterAbstraction(Context context, int resource, ArrayList<CommentModel> list) {
		super(context, resource, list);
		
		sortByDate = new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				if (a.getDate().before(b.getDate())) {
					return 1;
				} 
				else if (a.getDate().after(b.getDate())) {
					return -1;
				} 
				else {
					return 0;
				}
			}
		};
		
		sortByLocation = new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				String loc = "Location Initialization";
				myLocation = new Location(loc);
				myLocation = ProjectApplication.getCurrentLocation();
				Float dist1 = a.getLocation().distanceTo(myLocation);
				Float dist2 = b.getLocation().distanceTo(myLocation);
				if (dist1 < dist2) {
					return -1;
				} 
				else if (dist1 > dist2) {
					return 1;
				} 
				else {
					return 0;
				}
			}
		};
	}
	
	
	/**
	 * Returns the current sorting method.
	 * <p>
	 * @return current sort method
	 */
	public String getMethod() {
		return sortMethod;
	}
	
	/**
	 * Sets the model that this adapter is displaying.
	 * <p>
	 * This allows the current list within the model to be accessed by the 
	 * adapter for sorting purposes.
	 * <p>
	 * @param model Sets model for adapter instance
	 */
	public void setModel(CommentListModel model) {
		this.model = model;
	}
	
	/**
	 * Returns the current model that the adapter is displaying.
	 * <p>
	 * @return Model set for adapter instance
	 */
	public CommentListModel getModel() {
		return model;
	}
	
	/**
	 * Sorts the ArrayList<CommentModel> within model using the default method.
	 * <p>
	 * The default method of sorting uses a combination of date sorting and location sorting.
	 * This is done by sorting comments into distance categories based on how far away the user
	 * is from the distance category, and then sorting these categories by date.
	 * <p>
	 * The current list is obtained from the model. It is then subjected to serveral for loops
	 * which determine which comments are in which distance category. These comments are added
	 * to list2, which is sorted by date after each for loop, added to finalList,
	 * and then cleared before the next for loop. Once the method has been through all the
	 * for loops, it clears the comments currently in list and
	 * adds the sorted list of comments contained in finalList.
	 */
	public void sortByDefaultMethod() {
		String loc = "Location Initialization";
		myLocation = new Location(loc);
		myLocation = ProjectApplication.getCurrentLocation();
		
		
		// will hold the remaining unsorted CommentModels
		ArrayList<CommentModel> list = (ArrayList<CommentModel>) model.getCommentList();
		// holds the sorted CommentModels to be passed to sortByLocation
		ArrayList<CommentModel> list2 = new ArrayList<CommentModel>();
		// contains the final list
		ArrayList<CommentModel> finalList = new ArrayList<CommentModel>();
		Integer i;
		int l = list.size();
		
		for (i = 0; i<l; i++) {
			if ((list.get(i).getLocation().distanceTo(myLocation) < 5000) && (!finalList.contains(list.get(i)))) {
				list2.add(list.get(i));
			}
		}
		
		Collections.sort(list2, sortByDate);
		finalList.addAll(list2);
		list2 = new ArrayList<CommentModel>();
			
		for (i = 0; i<l; i++) {
			if ((list.get(i).getLocation().distanceTo(myLocation) < 100000) && (!finalList.contains(list.get(i)))) {
				list2.add(list.get(i));
			}
		}
		
		Collections.sort(list2, sortByDate);
		finalList.addAll(list2);
		list2 = new ArrayList<CommentModel>();
		
		for (i = 0; i<l; i++) {
			if ((list.get(i).getLocation().distanceTo(myLocation) < 5000000) && (!finalList.contains(list.get(i)))) {
				list2.add(list.get(i));
			}
		}

		Collections.sort(list2, sortByDate);
		finalList.addAll(list2);
		list2 = new ArrayList<CommentModel>();
		
		// rest of list
		for (i = 0; i<l; i++) {
			if (!finalList.contains(list.get(i))) {
				list2.add(list.get(i));
			}
		}

		list.clear();
		Collections.sort(list2, sortByDate);
		finalList.addAll(list2);
		list.addAll(finalList);

		
	}
	
	/**
	 *  Sets the sortMethod string to Default.
	 *  <p>
	 *  This allows sortList to know that the list should be sorted by Default
	 *  when called.
	 */
	public void sortByDefault() {
		sortMethod = "Default";
		sortList();
	}
	
	/**
	 *  Sets the sortMethod string to Date.
	 *  <p>
	 *  This allows sortList to know that the list should be sorted by Date
	 *  when called.
	 */
	public void sortByDate() {
		sortMethod = "Date";
		sortList();
	}
	
	/**
	 *  Sets the sortMethod string to Picture.
	 *  <p>
	 *  This allows sortList to know that the list should be sorted by Picture
	 *  when called.
	 */
	public void sortByPicture() {
		sortMethod = "Picture";
		sortList();
	}
	
	/**
	 *  Sets the sortMethod string to Location.
	 *  <p>
	 *  This allows sortList to know that the list should be sorted by Location
	 *  when called.
	 */
	public void sortByLocation() {
		sortMethod = "Location";
		sortList();
	}
	
	/**
	 *  Sets the sortMethod string to Other Location.
	 *  <p>
	 *  This allows sortList to know that the list should be sorted by Other Location
	 *  when called.
	 */
	public void sortByOtherLocation() {
		sortMethod = "Other Location";
		sortList();
	}
	
	/**
	 *  Sets the sortMethod string to Ranking.
	 *  <p>
	 *  This allows sortList to know that the list should be sorted by Ranking
	 *  when called.
	 */
	public void sortByRanking() {
		sortMethod = "Ranking";
		sortList();
	}
	
	/**
	 * Sorts the list by the method currently set in sortMethod.
	 * <p>
	 * The method checks to see if a model is currently associated
	 * with the adapter and then checks to see which sortMethod is currently
	 * selected. After determining how to sort, it either uses the comparator for
	 * that sorting type to sort the list using the Collections.sort() method
	 * or calls the method responsible for that sort type. After the sort is complete,
	 * it calls the notifyDataSetChanged() method on the adapter in order to update
	 * the view.
	 */
	public void sortList() {
		if (model != null) {
			ArrayList<CommentModel> list = model.getCommentList();
			if (sortMethod.equals("Default")) {
				sortByDefaultMethod();
				notifyDataSetChanged();
			} else if (sortMethod.equals("Date")) {
				Collections.sort(list, sortByDate);
				notifyDataSetChanged();
			} else if (sortMethod.equals("Picture")) {

			} else if (sortMethod.equals("Location")) {
				Collections.sort(list, sortByLocation);
				notifyDataSetChanged();
			} else if (sortMethod.equals("Other Location")) {
			
			} else if (sortMethod.equals("Ranking")) {
			
			}
		}

	}
}
