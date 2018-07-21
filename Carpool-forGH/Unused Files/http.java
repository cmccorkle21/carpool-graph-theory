import java.net.*;
import java.util.Scanner;
import java.io.*;

public class http {

	public static void main(String[] args){//Google Maps Geocoding API Key: AIzaSyB7TaYb8qpe8TLAB658Lwm2h8rFwlWlpgc
		
		try {
		URLConnection connection = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=place_id:ChIJcwi6ZesF9YgR192bCJmBxKA|place_id:ChIJDawNlYoP9YgRvdT7cBPERq0").openConnection();
		//connection.setRequestProperty("Accept-Charset", charset);
		InputStream response = connection.getInputStream();

		
			Scanner scanner = new Scanner(response);
	    //String responseBody = scanner.useDelimiter("\\A").next();
	    //System.out.println(responseBody);
			while (scanner.hasNext()){
				String line = scanner.next();
				System.out.println(line);
			}
		} catch (MalformedURLException e){
			System.out.println("something bad happened 1");
		} catch (IOException e2){
			System.out.println("something bad happened 2");
		}

	}
	
}