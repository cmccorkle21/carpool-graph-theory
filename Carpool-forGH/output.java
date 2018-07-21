import java.io.*;
public class output{

	public static void main(String[] args){

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("filename.txt"), "utf-8"))) {
   		writer.write("Did I actually just figure this out that fast");
		}catch (IOException ex) {
    		System.out.println("oh no. something went wrong.");
    	}
	}
}