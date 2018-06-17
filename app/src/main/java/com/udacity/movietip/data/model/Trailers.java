package com.udacity.movietip.data.model;

// @Generated("com.robohorse.robopojogenerator")
public class Trailers {

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
}