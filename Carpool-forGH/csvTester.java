import java.io.*;
import java.util.ArrayList;

public class csvTester {//Google Maps Geocoding API Key: AIzaSyB7TaYb8qpe8TLAB658Lwm2h8rFwlWlpgc

	public static void main(String[] args){
		
		dataAnalyzer dA = null;
		try {
			dA = new dataAnalyzer("dsaStudents.txt");
		} catch (IOException e){
			System.out.println("Something went wrong");	
		}

		//dA.printStudents();
		Student cole = new Student("Cole","7:30","3:00",5,"391 Pine Tree Dr. NE, Atlanta GA, 30305");
		Student richard = new Student("Richard", "8:00", "3:30", 5, "790 Stovall Blvd. NE, Atlanta GA, 30342");

		//System.out.println(dA.makeURL(cole)); //just checking to make sure the URL is accurate
		//System.out.println(dA.dataPage(cole));
		//System.out.println(dA.getPlace_ID(cole));//works perfect
		//System.out.println(dA.matrixURL(cole, richard));//works perfect
		//System.out.println(dA.matrixURL(dA.getStudents().get(1), dA.getStudents().get(2)));
		//dA.geoURL(cole);
		//System.out.println(dA.distance(richard, cole));
		//dA.printStudents();
		//System.out.println(dA.matrixURL(dA.getStudents().get(1), dA.getStudents().get(2)));
		

		//System.out.println(dA.matrixURL(dA.getStudents().get(1), dA.getStudents().get(2)));
		//System.out.println(dA.distanceFeet(dA.getStudents().get(1), dA.getStudents().get(2)));
		//System.out.println(dA.distanceMinutes(dA.getStudents().get(1), dA.getStudents().get(2)));
		//System.out.println(dA.findStudent("Cole McCorkle"));
		//System.out.println(dA.geoURL(cole));
		//System.out.println(dA.calculateCompatibility(cole, richard));
		//System.out.println(dA.minutesbetweenArrivals(cole, richard));
		//System.out.println(dA.minutesbetweenDepartures(cole, richard));
		//System.out.println(dA.calculateCompatibility(cole, richard));

		//System.out.println(dA.maxDistanceStudents());
		//issue btwn chase austin and troy baker testing
		//Student test1 = new Student ("Chase Austin", "7:45", "3:15", 5, "1093 Columbus Drive	Lake Spivey	GA	30236");
		//Student test2 = new Student ("Troy Baker", "7:30", "3:00", 5, "783 King Sword Court, SE	Mableton	GA	30126");
		
		/*
		System.out.println("----------PLACE ID ISSUE TESTING----------");
		System.out.println(dA.geoURL(dA.getStudents().get(37)));
		Student test1 = dA.getStudents().get(37);
		Student another1 = dA.getStudents().get(5);
		System.out.println("Distance matrix URL: "+dA.matrixURL(test1, another1));
		System.out.println(dA.distanceMinutes(test1, another1));
		*/


		//System.out.println("MATRIX URL: "+dA.matrixURL(dA.getStudents().get(36), dA.getStudents().get(37)));
		//System.out.println("DISTANCE MINUTES: "+dA.distanceMinutes(dA.getStudents().get(36), dA.getStudents().get(37)));
	//	System.out.println("DISTANCE MINUTES: "+dA.distanceMinutes(dA.getStudents().get(3), dA.getStudents().get(4)));
		

		//System.out.println(dA.dataPage("https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyB7TaYb8qpe8TLAB658Lwm2h8rFwlWlpgc&address=391%20PineTree%20Dr.%20Atlanta%20GA&electionId=2000"));//need to use electionQuery to figure out Election ID
		
		//----------------_FOR USE WITH WALKOUT_-----------------------
		//System.out.println(dA.wmc("Jordan Shoulberg"));



		/*
		System.out.println("Rewriting distanceMinutes");
		Student s1 = dA.getStudents().get(10);
		Student s2 = dA.getStudents().get(11);
		System.out.println("distanceMinutes: "+(dA.distanceMinutes(s1, s2)));
		System.out.println("Matrix URL: " + (dA.matrixURL(s1, s2)));
		*/

//System.out.println(dA.maxDistanceStudents());

		//Fixing some shit
/*
		Student p1 = dA.findStudent("Justice Johnson");
		Student p2 = dA.findStudent("Camryn Jones");
		//System.out.println("distanceMinutes: "+(dA.distanceMinutes(p1, p2)));
		System.out.println(dA.geoURL(p2));
		System.out.println(dA.getPlace_ID(p2));*/



		//Testing stuff with dsaStudents
		//dA.printStudents();
		//System.out.println(dA.maxDistanceStudents());
		//dA.giveStudentsPlace_IDs();
		//dA.printStudents();
		//dA.buildPID();

		//Student s = dA.findStudent("Ethan Much");

	//	System.out.println(dA.geoURL(s));
	//	System.out.println(dA.distanceMinutes(s, dA.findStudent("Nicholas McCorkle")));
		//Student me = dA.findStudent("Nicholas McCorkle");
		//this is breaking things System.out.println(dA.disparity(s,me));
		//System.out.println(dA.getPlace_ID(me));
		//System.out.println(dA.PID(me));
		//System.out.println("Actual PLACE_ID: "+me.getPlace_ID());

		Student p1 = dA.findStudent("Richard Ellis");
		Student p2 = dA.findStudent("Zeke Diamond");
		//System.out.println(dA.disparity(p1, p2));
		//dA.makeAGraph();
//testing PID
		/*for (Student s: dA.getStudents()){
			System.out.println(s.getName()+": "+dA.PID(s));
		}*/






		dA.makeAGraph();
		for (Student s: dA.getStudents()){
			System.out.println(s.getName()+":");
			System.out.println(s.edgeList());
		}

		ArrayList<Edge> allEdges = null;
		allEdges = dA.allEdges();
		ArrayList<Edge> sortedEdges = null;
		sortedEdges = dA.bubbleSort(allEdges);


		System.out.println("***SORTED EDGES ARRAY LIST***");
		for(Edge e: sortedEdges){
			System.out.println(e);
		}

		
		ArrayList<ArrayList<Edge>> listing = null;
		listing = dA.gimmeGroups(sortedEdges);
		for(ArrayList<Edge> list: listing){
			System.out.println("Group X");
			for(Edge e: list){
				System.out.println(e);
			}
		}

		/*ArrayList<ArrayList<Student>> finalTest = null;
		finalTest = dA.groups(sortedEdges);
		for(ArrayList<Student> list: holyFuck){
			System.out.println("Group X");
			for(Student s: list){
				System.out.println(s.getName());
			}
		}*/

	

		//p1.connect(p2);
		//dA.distanceMinutes(p1, p2);

	}
	
}