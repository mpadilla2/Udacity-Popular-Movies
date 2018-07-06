package com.udacity.movietip.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailers implements Parcelable {

	private static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

	//https://coderwall.com/p/nihgwq/get-a-thumbnail-from-a-youtube-video
	private static final String TRAILER_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
	private static final String TRAILER_THUMBNAIL_END_PATH = "/default.jpg";

	private String site;
	private int size;
	private String iso31661;
	private String name;
	private String id;
	private String type;
	private String iso6391;
	private String key;

	/*
	  sample output from tmdb api call for videos

	  Of note
	  key is the id for retrieving from youtube
	  Name is the name of the trailer
	  Type can show Trailer, Clip, Teaser, Featurette

	  "id": "5a200baa925141033608f5f0",
      "iso_639_1": "en",
      "iso_3166_1": "US",
      "key": "6ZfuNTqbHE8",
      "name": "Official Trailer",
      "site": "YouTube",
      "size": 1080,
      "type": "Trailer"
	 */


	public void setSite(String site){
		this.site = site;
	}

	public String getSite(){
		return site;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getSize(){
		return size;
	}

	public void setIso31661(String iso31661){
		this.iso31661 = iso31661;
	}

	public String getIso31661(){
		return iso31661;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setIso6391(String iso6391){
		this.iso6391 = iso6391;
	}

	public String getIso6391(){
		return iso6391;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	public String getTrailerUrl(){
		return TRAILER_BASE_URL + key;
	}

	public String getTrailerThumbnailUrl(){
		return TRAILER_THUMBNAIL_BASE_URL + key + TRAILER_THUMBNAIL_END_PATH;
	}

	@Override
 	public String toString(){
		return 
			"Trailers{" +
			"site = '" + site + '\'' + 
			",size = '" + size + '\'' + 
			",iso_3166_1 = '" + iso31661 + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			",iso_639_1 = '" + iso6391 + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}

	// Parcelable boilerplate code implemented with Android Parcelable Code Generator by Michal Charmas
	// Reference: https://developer.android.com/guide/components/activities/parcelables-and-bundles
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.site);
		dest.writeInt(this.size);
		dest.writeString(this.iso31661);
		dest.writeString(this.name);
		dest.writeString(this.id);
		dest.writeString(this.type);
		dest.writeString(this.iso6391);
		dest.writeString(this.key);
	}

	public Trailers() {
	}

	protected Trailers(Parcel in) {
		this.site = in.readString();
		this.size = in.readInt();
		this.iso31661 = in.readString();
		this.name = in.readString();
		this.id = in.readString();
		this.type = in.readString();
		this.iso6391 = in.readString();
		this.key = in.readString();
	}

	public static final Creator<Trailers> CREATOR = new Creator<Trailers>() {
		@Override
		public Trailers createFromParcel(Parcel source) {
			return new Trailers(source);
		}

		@Override
		public Trailers[] newArray(int size) {
			return new Trailers[size];
		}
	};
}