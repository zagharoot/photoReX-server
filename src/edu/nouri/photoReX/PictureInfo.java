package edu.nouri.photoReX;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;

public abstract class PictureInfo {

	public static MessageDigest sha1; 
	public String website;
	
	PictureInfo(String w)
	{
		if (sha1==null)
		{
			try
			{
				sha1 = MessageDigest.getInstance("SHA1"); 
			}catch(NoSuchAlgorithmException e)
			{
				
			}
		}
		
		
		website = w; 
	}
	
	
	public String toHash()
	{
		try {
			String encode = this.toString(); 
			byte[] digest = sha1.digest(encode.getBytes("UTF-8"));
			
			StringBuffer result = new StringBuffer(); 
			for(int i=0; i< digest.length; i++)
			{
				result.append(Integer.toHexString(0xFF & digest[i])); 
			}
			return result.toString(); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ""; 
		} 
	}

	public String toJson()
	{
		Gson gson = new Gson(); 
		String result = gson.toJson(this); 
		return result; 
	}

}
