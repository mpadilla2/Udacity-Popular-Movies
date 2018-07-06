package com.udacity.movietip.data.model;

import android.os.Parcel;

public class Reviews implements android.os.Parcelable {

	private String author;
	private String id;
	private String content;
	private String url;

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	// Parcelable boilerplate code implemented with Android Parcelable Code Generator by Michal Charmas
	// Reference: https://developer.android.com/guide/components/activities/parcelables-and-bundles
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.author);
		dest.writeString(this.id);
		dest.writeString(this.content);
		dest.writeString(this.url);
	}

	public Reviews() {
	}

	protected Reviews(Parcel in) {
		this.author = in.readString();
		this.id = in.readString();
		this.content = in.readString();
		this.url = in.readString();
	}

	public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
		@Override
		public Reviews createFromParcel(Parcel source) {
			return new Reviews(source);
		}

		@Override
		public Reviews[] newArray(int size) {
			return new Reviews[size];
		}
	};
}