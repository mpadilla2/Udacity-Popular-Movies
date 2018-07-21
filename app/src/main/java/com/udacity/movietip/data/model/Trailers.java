package com.udacity.movietip.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailers implements Parcelable {

	private static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

	//https://coderwall.com/p/nihgwq/get-a-thumbnail-from-a-youtube-video
	private static final String TRAILER_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
	private static final String TRAILER_THUMBNAIL_END_PATH = "/default.jpg";

	private final String site;
	private final int size;
	private final String iso31661;
	private final String name;
	private final String id;
	private final String type;
	private final String iso6391;
	private final String key;

	public String getName(){
		return name;
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

	private Trailers(Parcel in) {
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