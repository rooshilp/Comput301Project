package ca.ualberta.cs.cmput301t02project.model;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ca.ualberta.cs.cmput301t02project.model.Server.ObjectWrapper;

public class CommentServer {
	private JestClient client;
	private Context context;

	public JestClient getClient() {
		return client;
	}

	public void setClient(JestClient client) {
		this.client = client;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ArrayList<CommentModel> pullChildrenOf(String parentId, Server server) {
		if (parentId != null) {
			CommentModel parent = this.pull(parentId, server);
			return this.pull(parent.getChildrenIds());
		} else {
			return this.pullTopLevel();
		}
	}

	//http://stackoverflow.com/a/4239019 April 2 2014
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void post(final CommentModel comment) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Index index = new Index.Builder(comment).index("cmput301w14t02").type("comments")
							.id(comment.getId()).build();
					JestResult result = client.execute(index);
					String id = result.getJsonObject().get("_id").getAsString();
					comment.setId(id);
					index = new Index.Builder(comment).index("cmput301w14t02").type("comments").id(comment.getId())
							.build();
					result = client.execute(index);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public CommentModel pull(final String id, Server server) {
		final Cache cache = Cache.getInstance(context);
		final ObjectWrapper wrapper = server.new ObjectWrapper();
		Thread thread = new Thread() {
			@Override
			public void run() {
				Get get = new Get.Builder("cmput301w14t02", id).type("comments").build();
				CommentModel comment;
				try {
					JestResult result = client.execute(get);
					comment = result.getSourceAsObject(CommentModel.class);
					cache.put(id, comment);
				} catch (Exception e) {
					comment = cache.getIfPresent(id);
				}
				wrapper.object = comment;
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (CommentModel) wrapper.object;
	}

	public ArrayList<CommentModel> pull(final ArrayList<String> idList) {
		final Cache cache = Cache.getInstance(context);
		final ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		Thread thread = new Thread() {
			@Override
			public void run() {
				for (String Id : idList) {
					Get get = new Get.Builder("cmput301w14t02", Id).type("comments").build();
					try {
						JestResult result = client.execute(get);
						CommentModel comment = result.getSourceAsObject(CommentModel.class);
						commentList.add(comment);
						cache.put(Id, comment);
					} catch (Exception e) {
						CommentModel comment = cache.getIfPresent(Id);
						if (comment != null) {
							commentList.add(comment);
						}
					}
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return commentList;
	}

	public ArrayList<CommentModel> pullTopLevel() {
		final Cache cache = Cache.getInstance(context);
		final ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		Thread thread = new Thread() {
			@Override
			public void run() {
				String query = "{\"size\": 1000, \"query\": {\"term\": {\"topLevelComment\": \"true\"}}}";
				Search search = new Search.Builder(query).addIndex("cmput301w14t02").addType("comments").build();
				try {
					JestResult result = client.execute(search);
					commentList.addAll((ArrayList<CommentModel>) result.getSourceAsObjectList(CommentModel.class));
					for (CommentModel c : commentList) {
						cache.put(c.getId(), c);
					}
				} catch (Exception e) {
					commentList.addAll(cache.getAllTopLevelPresent());
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return commentList;
	}

	public ArrayList<CommentModel> pullFollowedUserComments(final String username) {
		final Cache cache = Cache.getInstance(context);
		final ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		Thread thread = new Thread() {
			@Override
			public void run() {
				String query = String.format("{\"size\": 1000, \"query\": {\"term\": {\"username\": \"%s\"}}}",
						username);
				Search search = new Search.Builder(query).addIndex("cmput301w14t02").addType("comments").build();
				try {
					JestResult result = client.execute(search);
					commentList.addAll((ArrayList<CommentModel>) result.getSourceAsObjectList(CommentModel.class));
					for (CommentModel c : commentList) {
						cache.put(c.getId(), c);
					}
				} catch (Exception e) {
					commentList.addAll(cache.getAllFollowedInCache());
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return commentList;
	}

	public void addChildren(String parentId, String childId, Server server) {
		if (parentId == null) {
			throw new IllegalArgumentException("Parent Id can't be null");
		}
		CommentModel comment = this.pull(parentId, server);
		comment.addChildId(childId);
		this.post(comment);
	}
	
	
}