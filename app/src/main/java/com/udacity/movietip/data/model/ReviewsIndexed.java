package com.udacity.movietip.data.model;

import java.util.List;

// Generated by robopojogenerator
public class ReviewsIndexed{

	private int id;
	private int page;
	private int totalPages;
	private List<Reviews> results;
	private int totalResults;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<Reviews> results){
		this.results = results;
	}

	public List<Reviews> getResults(){
		return results;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

}