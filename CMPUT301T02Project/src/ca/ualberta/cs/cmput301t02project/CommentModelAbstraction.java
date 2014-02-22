package ca.ualberta.cs.cmput301t02project;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Picture;

public abstract class CommentModelAbstraction {
    private Date date;
    private LocationModel location;
    private String text;
    private Picture image;
    private int rating;
    private ArrayList<ReplyCommentModel> replies;
    private String username;

    //Post with picture
    public CommentModelAbstraction(String text, Picture image,
	LocationModel location, String username) {
	this.text = text;
	this.image = image;
	this.location = location;
	this.username = username;
	this.rating = 0;
	this.replies = new ArrayList<ReplyCommentModel>();
	this.date = new Date();
    }
    
    //Post without picture
    public CommentModelAbstraction(String text,
	LocationModel location, String username) {
	this.text = text;
	this.image = null;
	this.location = location;
	this.username = username;
	this.rating = 0;
	this.replies = new ArrayList<ReplyCommentModel>();
	this.date = new Date();
    }

    public Date getDate() {
	return date;
    }

    public LocationModel getLocation() {
	return location;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public Picture getImage() {
	return image;
    }

    public void setImage(Picture image) {
	this.image = image;
    }

    public int getRating() {
	return rating;
    }

    public void setRating(int rating) {

    }

    public ArrayList<ReplyCommentModel> getReplies() {
	return replies;
    }

    public void setReplies(ArrayList<ReplyCommentModel> replies) {
	this.replies = replies;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    @Override
    public String toString() {
	return text;
    }

}
