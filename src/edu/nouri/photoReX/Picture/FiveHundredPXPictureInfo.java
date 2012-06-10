package edu.nouri.photoReX.Picture;

public class FiveHundredPXPictureInfo extends PictureInfo {

	private String id; 
	private String url; 
	
	public FiveHundredPXPictureInfo()
	{
		super("500px");
	}

	public FiveHundredPXPictureInfo(String _id) 
	{
		this(); 
		id = _id; 		
		hash = toHash(); 
	}
	
	
	public FiveHundredPXPictureInfo(FiveHundredPXPhoto p)
	{
		this(); 
		
		id = p.id; 
		url = p.url; 
		hash = toHash(); 
	}
	
	
	public String toString()
	{
		return "500px:" + id + ":" + url ; 
	}
	
}
