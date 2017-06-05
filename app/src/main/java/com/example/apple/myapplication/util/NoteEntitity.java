package com.example.apple.myapplication.util;

public class NoteEntitity {
	
	//private variables
	int _id;
	String title;
	String text;
	String imageUrl;
	String time;


	// Empty constructor
	public NoteEntitity(){
		
	}
	// constructor
	public NoteEntitity( String title, String text, String imageUrl , String time ){
		//this._id = id;
		this.title = title;
		this.text = text ;
		this.imageUrl = imageUrl ;
		this.time = time ;

	}
	public NoteEntitity( int id ,String title, String text, String imageUrl , String time ){
		this._id = id;
		this.title = title;
		this.text = text ;
		this.imageUrl = imageUrl ;
		this.time = time ;

	}


	public String getTime() {
		return time;
	}

	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	// getting phone number


	public void setTime(String time) {
		this.time = time;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getText() {
		return text;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setText(String text) {
		this.text = text;
	}
}
