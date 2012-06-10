package edu.nouri.photoReX.Picture;

import java.util.ArrayList;

public class FiveHundredPXPhotoCollection {
	public ArrayList<FiveHundredPXPhoto> pics; 
	
	
	public FiveHundredPXPhotoCollection()
	{
		pics = new ArrayList<FiveHundredPXPhoto>(); 
	}
	
	
	public ArrayList<PictureInfo> pictureInfoCollection()
	{
		ArrayList<PictureInfo> result = new ArrayList<PictureInfo>(); 
		
		
		for(int i=0; i<pics.size(); i++)
			result.add(pics.get(i).pictureInfo()); 
		
		return result; 
	}
	
}
