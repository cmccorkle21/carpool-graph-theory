
public class java {
	

	public static void main(String[] args){
		int s = 500;
		int sum = 0;

		for (int i = 500; i>0; i--){
			sum +=(i+(i--));
		}

		System.out.println(sum);
	}
	
}