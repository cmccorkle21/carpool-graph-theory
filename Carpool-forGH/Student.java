import java.io.*;
import java.util.ArrayList;
public class Student {
	
	private String name;
	private String arrivalTime;
	private String departureTime;
	int carCapacity;
	private String address;
	private String place_id;//now need a way to throw all place ID's into a file
	private ArrayList <Edge> edges;

	// Constructor
	public Student(String name, String arrivalTime, String departureTime, int carCapacity, String address){
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.carCapacity = carCapacity;
		this.address = address;
		this.edges = new ArrayList<Edge>(10);

	}

	public String toString(){
		String toReturn = ("Name: "+name+", Arrival Time: "+arrivalTime+", Departure Time: "+departureTime+", Car Capacity: "+carCapacity+", Address: "+address+"PLACE_ID: "+place_id);
		return toReturn;
	}

	public String getName(){
		return name;
	}

	public String getArrivalTime(){
		return arrivalTime;
	}

	public String getDepartureTime(){
		return departureTime;
	}

	public int getCarCapacity(){
		return carCapacity;
	}
	
	public String getAddress(){
		return address;
	}

	public void setPlace_ID(String pi){
		place_id = pi;
	}

	public String getPlace_ID(){
		return place_id;
	}

	public ArrayList<Edge> getEdges(){
		return edges;
	}

	public String edgeList(){
		String toReturn = "";
		for (Edge e: edges){
			toReturn+=e+"\n";
		}
		return toReturn;
	}

	public void connect(Student s){
		dataAnalyzer dA = null;
		try {
			dA = new dataAnalyzer("dsaStudents.txt");
		} catch (IOException e){
			System.out.println("Something went wrong");	
		}

		Edge test = new Edge(this, s, dA.disparity(this, s));
		System.out.println(this.getName()+" and "+s.getName()+" successfully connected.");
		edges.add(test);
	}

	/*class Edge{
		Student me;
		double weight;
		Student student;


		public Edge(Student student, double weight){
			this.me = this;
			this.student = student;
			this.weight = weight;
		}

		public double getWeight(){
			return weight;
		} 

		public Student getStudent(){
			return student;
		}

		public String toString(){
			String toReturn = (" is connected to "+student.getName()+" with a weight of "+weight);
			return toReturn;
		}
	}*/
}