package edu.nouri.photoReX.picture;

public class FiveHundredPXPictureInfo extends PictureInfo {

	private String id; 
	private String url; 
	
	// because 500px api provides these fields by default, we pass them along so client doesn't have to make another call
	public String userFullName; 
	public String userid; 	
	public String description; 
	public int timesViewed; 
	
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
		
		userFullName = p.userFullName; 
		userid = p.userid; 
		description = p.name; 
		timesViewed = p.timesViewed; 
		
		hash = toHash(); 
	}
	
	
	public String toString()
	{
		return "500px:" + id + ":" + url ; 
	}
	
}
