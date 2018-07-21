import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class dataAnalyzer {
	
	private ArrayList<Student> students;

	// Constructor
	public dataAnalyzer(String fn) throws IOException{ //takes in the file you want to be read

		students = new ArrayList<Student>();
		Student toAdd;

		Scanner fscan = new Scanner(new File(fn)); //initiates file scanner
		//fscan.nextLine();//skips the first line
		while(fscan.hasNextLine()){ //while there are still lines left in the file
			
			String line = fscan.nextLine(); //read one in
			String[] bits = line.split("\t");
				String name = (bits[1]+" "+bits[2]);
				String address = bits[4];//need to deal with that some lines don't have addresses
				String zipCode = bits[7];
			students.add(new Student(name, "7:45", "3:15", 5, (address+", "+zipCode)));
		}
		
		//This only needed to happen once
			giveStudentsPlace_IDs();
			//buildPID();
		
			
	}

	/**
	*Returns a student with the name that is given
	*@param name First and last name of the student to be found
	*@return the student whos name matches 'name'
	**/
	public Student findStudent(String name){
		for(Student s: students){
			if (s.getName().equals(name)){
				return s;
			}
		}
		return students.get(0);
		//can i return an error or something?
	}

	/**
	*Prints the list of students of the dataAnalyzer
	*@return the list of students of the dataAnalyzer
	**/
	public void printStudents(){
		String toReturn = "";
		for (Student s: students){
			toReturn+=(s+"\n");
		}
		System.out.println(toReturn);

	}

	/**
	*Returns the list of students of the dataAnalyzer
	*@return the list of students of the dataAnalyzer
	**/
	public ArrayList<Student> getStudents(){
		return students;
	}

	/**
	*Returns the URL that will request the Geocoding information of a given students home address
	*@param s the student whos home address will be geocoded
	*@return the Google API address that geocodes the students address
	**/
	public String geoURL(Student s){//https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
		String address = s.getAddress();
		String frontURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String API = "&key=AIzaSyB7TaYb8qpe8TLAB658Lwm2h8rFwlWlpgc";
	//now gotta separate the words in the address by pluses to work with the url request
		String formattedAddress = "";
		for (int i=0; i<address.length(); i++){
			if (address.charAt(i) == ' '){
				formattedAddress+="+";
			//}else if (address.charAt(i) == ','){
				//formattedAddress+="+";
			}else{
				formattedAddress+=(address.charAt(i));
			}
		}
		//System.out.println("TESTING: FORMATTED ADDRESS: "+formattedAddress);
		return frontURL+formattedAddress+API;
	}

	/**
	*Returns a String of the datapage that is found when requesting a give web address
	*@param URL the url to be requested
	*@return the String of the webpage that is found
	**/
	public String dataPage(String URL){//takes in a student and returns the latitude/longitude of their home address
		String toReturn = "";

		try {
			URLConnection connection = new URL(URL).openConnection();			//connection.setRequestProperty("Accept-Charset", charset);
			InputStream response = connection.getInputStream();

			
			Scanner scanner = new Scanner(response);
		    //String responseBody = scanner.useDelimiter("\\A").next();
		    //System.out.println(responseBody);
				while (scanner.hasNext()){
					String line = scanner.next();
					toReturn+=(line);
					toReturn +="\n";
				}
		} catch (MalformedURLException e){
			System.out.println("ERROR: Malformed URL Exception");
		} catch (IOException e2){
			System.out.println("ERROR: IO Exception");
		}
		return toReturn;// first I need to actually grab the lat and long -- probably want to return a string array for this
	}

	/**
	*Returns the place_id of a given students home address
	*@param s the student whose place_id will be found
	*@return the place_id of the students home address
	**/
	public String getPlace_ID(Student s){//root of the issue - so some palce ID's are way longer, so instead of +40, gotta go until you find a quotation mark
		String DP = dataPage(geoURL(s));
		int trace = DP.indexOf("place_id");
		return DP.substring(trace+13, DP.indexOf("\"",trace+13));
	}

	public String PID(Student s){
		
		String toReturn = "";

		Scanner fscan = null;
		try {
			fscan = new Scanner(new File("dsaPID.txt")); //initiates file scanner
		} catch (IOException e){
			System.out.println("dsaPID.txt ERROR");
		}
		//fscan.nextLine();//skips the first line

		while(fscan.hasNextLine()){ //while there are still lines left in the file
			String line = fscan.nextLine(); //read one in
			if (line.contains(s.getName())){
				int start = (line.indexOf("|")+2); //we start reading after the pipe
				int end = (line.indexOf(";"));
				toReturn = line.substring(start, end);
			}
		}

		return toReturn;
	}

	/**
	*Returns the URL of the DataPage that contains the distance between two students
	*@param s1 origin student
	*@param s2 destination student
	*@return URL of DataPaged
	**/
	public String matrixURL(Student s1, Student s2){
		//Example URL for distance from my house to pace -- https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=place_id:ChIJcwi6ZesF9YgR192bCJmBxKA&destinations=place_id:ChIJDawNlYoP9YgRvdT7cBPERq0&key=AIzaSyBe7kTXcUXNoll_Q29vrUkFcCLs-ZmiG5Y
		String place_id1 = PID(s1);
		String place_id2 = PID(s2);

		String front = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&";
		String bit1 = "origins=place_id:"+place_id1+"&";
		String bit2 = "destinations=place_id:"+place_id2+"&key=";
		String MatrixAPIKey = "AIzaSyBe7kTXcUXNoll_Q29vrUkFcCLs-ZmiG5Y"; 
		return front+bit1+bit2+MatrixAPIKey;
	}

	/**
	*Returns the distance between two students home addresses
	*@param s1 origin student
	*@param s2 destination student
	*@return The distance between two students, in feet
	**/
	public int distanceFeet(Student s1, Student s2){
		String matrix = dataPage(matrixURL(s1, s2));
		String str = "";
		int start = matrix.indexOf("distance");
		String chopped = matrix.substring(start, start+55);
		int choppedStart = (chopped.indexOf("value"))+9;

		int choppedEnd = 0;
		for (int i = choppedStart; Character.isDigit(chopped.charAt(i)); i++){
			choppedEnd = i;
		}
		choppedEnd++;

		String toReturn = chopped.substring(choppedStart, choppedEnd);//this needs to be a loop so that it stops once it's not a number
		int toActuallyReturn = Integer.parseInt(toReturn);
		return (int) (toActuallyReturn*3.28084);
	}

	/**
	*Returns the distance in minutes between two students home addresses
	*@param s1 origin student
	*@param s2 destination student
	*@return the distance between two students, in minutes
	**/
	public double distanceMinutes(Student s1, Student s2){
		String matrix = dataPage(matrixURL(s1, s2));
		//System.out.println("MATRIX: "+matrixURL(s1, s2)); //for use if we want to print out the matrix url too

		int start = ((matrix.indexOf("duration"))+24);
		int end = ((matrix.indexOf("min", start+1)-1));

		String stringReturn = matrix.substring(start, end);
		//System.out.println("STRING RETURN: "+stringReturn);
		double toReturn = (double) (Integer.parseInt(stringReturn));

		return toReturn;
	}

	/**
	*Returns the time between two students arrivals at work/school in the morning
	*@param s1 the first student to be compared
	*@param s2 the second student to be compared
	*@return The time in minutes between the two student's morning arrivals
	**/
	public double minutesbetweenArrivals(Student s1, Student s2){
		String today = Instant.now().toString().substring(8,10);//'today' will always = the two number representation of the day of the month
		String thisMonth = Instant.now().toString().substring(5,7);//'thisMonth' will always = the two number representation of the month of the year
		String thisYear = Instant.now().toString().substring(0,4);//2018
		Instant base = Instant.parse(thisYear+"-"+thisMonth+"-"+today+"T10:00:00Z");//'base' will always be 6:00AM this morning 

		String toChop = s1.getArrivalTime();
		String Arrival8601 = (thisYear+"-"+thisMonth+"-"+today+"T"+toChop+":00"+"Z");

		if (Arrival8601.length() == 19){//adds a zero if needed
			Arrival8601 = (thisYear+"-"+thisMonth+"-"+today+"T"+"0"+toChop+":00"+"Z");
		}

		//Arrival8601 is now the arrival time "today," in the ISO8601 format.
		Instant arrivalInstant = Instant.parse(Arrival8601);
		//-----------------------Just doing it again to the second student-----------------------
		String toChop2 = s2.getArrivalTime();
		String Arrival86012 = (thisYear+"-"+thisMonth+"-"+today+"T"+toChop2+":00"+"Z");

		if (Arrival86012.length() == 19){//adds a zero if needed
			Arrival86012 = (thisYear+"-"+thisMonth+"-"+today+"T"+"0"+toChop2+":00"+"Z");
		}

		//Arrival8601 is now the arrival time "today," in the ISO8601 format.
		Instant arrivalInstant2 = Instant.parse(Arrival86012);

		//arrivalInstant2 is now the correct time. It is an 'instant'
		double between = Duration.between(arrivalInstant, arrivalInstant2).toMinutes();
		return between;
	}

	/**
	*Returns the time between two students departures at work/school in the afternoon
	*@param s1 the first student to be compared
	*@param s2 the second student to be compared
	*@return The time in minutes between the two student's morning departures
	**/
	public double minutesbetweenDepartures(Student s1, Student s2){
		String today = Instant.now().toString().substring(8,10);//'today' will always = the two number representation of the day of the month
		String thisMonth = Instant.now().toString().substring(5,7);//'thisMonth' will always = the two number representation of the month of the year
		String thisYear = Instant.now().toString().substring(0,4);//2018
		Instant base = Instant.parse(thisYear+"-"+thisMonth+"-"+today+"T10:00:00Z");//'base' will always be 6:00AM this morning 

		String toChop = s1.getDepartureTime();
		String Arrival8601 = (thisYear+"-"+thisMonth+"-"+today+"T"+toChop+":00"+"Z");

		if (Arrival8601.length() == 19){//adds a zero if needed
			Arrival8601 = (thisYear+"-"+thisMonth+"-"+today+"T"+"0"+toChop+":00"+"Z");
		}

		//Arrival8601 is now the arrival time "today," in the ISO8601 format.
		Instant arrivalInstant = Instant.parse(Arrival8601);
		//-----------------------Just doing it again to the second student-----------------------
		String toChop2 = s2.getDepartureTime();
		String Arrival86012 = (thisYear+"-"+thisMonth+"-"+today+"T"+toChop2+":00"+"Z");

		if (Arrival86012.length() == 19){//adds a zero if needed
			Arrival86012 = (thisYear+"-"+thisMonth+"-"+today+"T"+"0"+toChop2+":00"+"Z");
		}

		//Arrival8601 is now the arrival time "today," in the ISO8601 format.
		Instant arrivalInstant2 = Instant.parse(Arrival86012);

		//arrivalInstant2 is now the correct time. It is an 'instant'
		double between = Duration.between(arrivalInstant, arrivalInstant2).toMinutes();
		return between;
	}

	/**
	*Returns the 'compatibility' score between two students, lower is better
	*@param s1 the first student to be compared
	*@param s2 the second student to be compared
	*@return the compatibility score between the two students
	**/
	public double calculateCompatibility(Student s1, Student s2){//we are four hours back from GMT (What the unix timestamp uses)
		//calculate time btwn arrivals at pace
			double arrivalDisparity = minutesbetweenArrivals(s1, s2)*1.1111111111;
		//calculate time btwn departures from pace
			double departureDisparity = minutesbetweenDepartures(s1, s2)*1.1111111111;
		//calculate time between addresses
			double distanceDisparity = distanceMinutes(s1, s2);
		//this will then create a weighting that will connect two students with an edge.
			return arrivalDisparity+departureDisparity+distanceDisparity;
	}

	/**
	*Returns the congressperson of a given student
	*@param name the name of the student who's congressperson will be returned
	*@return The congressperson of the student with the name given
	**/
	public String wmc(String name){
		Student s = findStudent(name);
		String address = s.getAddress();
		address = address.replace(",", "");
	
		String[] bits = address.split(" ");

		String formattedAddress = (bits[0]+"%20"+bits[1]+"%20"+bits[2]+"%20Atlanta%20GA");
		
		String front = "https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyB7TaYb8qpe8TLAB658Lwm2h8rFwlWlpgc&address=";
		String back = "&electionId=2000";
		String URL = front+formattedAddress+back;

		String DP = dataPage(URL);
		int root = DP.indexOf("ocd-division/country:us/state:ga/cd:");
		int location = root+36;
		int end=0;
		
		for (int i = location; Character.isDigit(DP.charAt(i)); i++){
			end = i;
		}
		end++;
		
		int district = Integer.parseInt(DP.substring(location, end));
		String congressperson = "";

		if (district == 1){
			congressperson = "Buddy Carter";
		}else if (district == 2){
			congressperson = "Sanford Bishop";
		}else if (district == 3){
			congressperson = "Drew Ferguson";
		}else if (district == 4){
			congressperson = "Hank Johnson";
		}else if (district == 5){
			congressperson = "John Lewis";
		}else if (district == 6){
			return (name+": You live in district "+district+", you're congresswoman is Karen Handel");
		}else if (district == 7){
			congressperson = "Rob Woodall";
		}else if (district == 8){
			congressperson = "Austin Scott";
		}else if (district == 9){
			congressperson = "Doug Collins";
		}else if (district == 10){
			congressperson = "Jody Hice";
		}else if (district == 11){
			congressperson = "Barry Loudermilk";
		}else if (district == 12){
			congressperson = "Rick Allen";
		}else if (district == 13){
			congressperson = "David Scott";
		}else if (district == 14){
			congressperson = "Tom Graves";
		}

		return (name+": You live in district "+district+", you're congressman is "+congressperson);
		
	}
	
	/**
	*Compares adjacent students, returning the furthest distance between adjacent students in the list -- solely proof on concept, do not use
	*@return the highest distance between adjacent students in the students list
	**/
	public String maxDistanceStudents (){
		double max = 0.0;
		String s1 = null;
		String s2 = null;

		for (int i = 0; i<students.size()-1; i++){
			System.out.println (i+" Comparing "+(students.get(i).getName())+" with "+(students.get(i+1).getName()));
			double val = (distanceMinutes(students.get(i),students.get(i+1))) ;
			if ( val > max){//chase austin and troy baker are having issues
				max = val;
				System.out.println(i);
				s1 = students.get(i).getName();
				s2 = students.get(i+1).getName();
			}
		}

		return "The maximum distance is between "+s1+" and "+s2+". the distance is "+max+"miles.";
	}

	/**
	*Uses PID to find the Place_IDs of students, then sets their instance variable to that
	**/
	public void giveStudentsPlace_IDs(){
		System.out.println("***GIVING STUDENTS PLACE_IDS***");
		for (Student s: students){
			//System.out.println("Finding "+s.getName()+" Place_id");
			String pi = PID(s);
			s.setPlace_ID(pi);
		}
		System.out.println("***DONE***");
	}

	/**
	*Finds the students Place_IDs with Google API and sticks them all in a file
	**/
	public void buildPID (){
		//Writer writer = null;
		String keyValue = "";

		for (Student s: students){
			keyValue+=s.getName();
			keyValue+=" | ";
			keyValue+=s.getPlace_ID()+";"+"\n";
			//System.out.println(keyValue);
		}
		draw(keyValue);
		//System.out.println(keyValue);

	}

	/**
	*Adds a new line to dsaPID.txt. Solely for use with buildPID
	*@param str the String to be added to dsaPID.txt
	**/
	public void draw(String str){
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("dsaPID.txt"), "utf-8"))) {
   			writer.write(str);
		}catch (IOException ex) {
    		System.out.println("oh no. something went wrong.");
    	}
	}

	/**
	*Calculates the compatibility between students, lower is better
	*@param s1 the first student to be compared
	*@param s2 the second student to be compared
	*@return the double value of how compatible the students are, lower is bett2er
	**/
	public double disparity (Student s1, Student s2){
		double mbA = minutesbetweenArrivals(s1, s2);
		double mbD = minutesbetweenDepartures(s1, s2);
		double dM = Math.abs(distanceMinutes(s1, s2));

		double scaledMBA = mbA*(1/3);
		double scaledMBD = mbD*(1/3);
		double scaledDM = dM;

		return (scaledDM+scaledMBD+scaledMBA);
	}

	/**
	*Calculates students compatibility and then makes edges between them
	**/
	public void makeAGraph (){
		for (Student s: students){
			for (Student p: students){
				if (s != p){
					System.out.println("Connecting "+s.getName()+" and "+p.getName());
					double compat = disparity(s,p);
					s.connect(p);
				}
			}
		}
	}

	/**
	*Prints all the edge lists of all the students
	**/
	public void printGraph(){
		for (Student s: students){
			System.out.println(s.edgeList());
		}
	}

	public ArrayList<Edge> allEdges(){
		ArrayList<Edge> toReturn = new ArrayList<Edge>(students.size());
		for (Student s: students){
			for (Edge e: s.getEdges()){
				toReturn.add(e);
			}
		}
		return toReturn;
	/*ArrayList<Edge> toReturn = new ArrayList<Edge>(students.size());
		Edge toAdd = null;
		for(Edge e: toNotReturn){
			double min = Integer.MAX_VALUE;
			while (toReturn.size() != toNotReturn.size()){
				 min = Integer.MAX_VALUE;
				if (e.getWeight()<min){
					min = e.getWeight();
					toAdd = e;
				}
				toReturn.add(e);
			}
		}*/

	}


    /*public ArrayList<Edge> selectionSort(ArrayList<Edge> edges){
 		ArrayList<Edge> toReturn = new ArrayList<Edge>();

 		//incrementing down unsorted list
 		for(int p = 0; p<edges.size(); p++){

 			//find the minimum element
 			double minWeight = 1.0* Integer.MAX_VALUE;
 			Edge minEdge = null;
 			int minIndex = -1;
 			for(int i = 0; i<edges.size(); i++){
 				if (edges.get(i).getWeight() < minWeight){
 					minWeight = edges.get(i).getWeight();
 					minEdge = edges.get(i);
 					minIndex = i;
 				}
 			}

 			//Swap minimum edge with the first edge
 			Edge temp = edges.get(p);
 			edges.set(p, minEdge);
 			edges.set(minIndex, temp);


 		}
 		return edges;
   	}*/

   	public ArrayList<Edge> bubbleSort(ArrayList<Edge> edges){
   		Edge temp = null;
   		for (int j = 0; j<edges.size()-1; j++){
	   		for (int i=0; i<edges.size()-1; i++){
	   			if (edges.get(i).getWeight() > edges.get(i+1).getWeight()){//if first one is bigger than second one
	   				//swap them
	   				System.out.println("SWAPPIN WEAPONS!");
	   				temp = edges.get(i);
	   				edges.set(i, edges.get(i+1));
	   				edges.set(i+1, temp);
	   			}
	   		}
	   	}
   		return edges;
   	}


	/*public ArrayList<ArrayList<Edge>> makeGroups(ArrayList<Edge> edges){
		int numGroups = students.size()/2;//MUST CHANGE TO 5 FOR FULL DATA SET
		int sizeGroups = students.size()/numGroups;
		//Create ArrayList of ArrayLists, each inner list is a group of carpoolers
			ArrayList<ArrayList<Edge>> list= new ArrayList<ArrayList<Edge>>(numGroups);

		for (int q=0; q<edges.size(); q++){
			//pick edge with smallest edge weight (always the first one)
				Edge toDo = edges.get(0);
				Student s1 = toDo.getStudent1();
				Student s2 = toDo.getStudent2();

				int p = 0;
				//p = 0, if list at p isn't full, add toDo to it, if full, p++.
				while (p<numGroups){
					
					if (list.get(p).size() < 5){
						list.get(p).add(toDo);
					}else{
						p++;
					}
				}
				
			//delete all instances of s1 and s2
				for (int i = 0; i<edges.size(); i++){
					if (edges.get(i).getStudent1() == s1 || edges.get(i).getStudent2() == s2){
						edges.remove(i);
					}
				}
		}
		return list;
	}*/

	public ArrayList<ArrayList<Edge>> gimmeGroups(ArrayList<Edge> sortedEdges){

		//declare that you want n groups of size s
			int sizeGroups = 5;
			int numGroups = 3;//students.size()/sizeGroups;


		//declare carpool groups
			ArrayList<ArrayList<Edge>> carpoolGroups= new ArrayList<ArrayList<Edge>>(numGroups);
				//add some  arraylists you MORON
					for (int y=0; y<numGroups; y++){
						ArrayList<Edge> emptyList = new ArrayList<Edge>(sizeGroups);
						carpoolGroups.add(emptyList);
					}
		//3 times
			for (int i=0; i<3; i++){
				//find smallest edge (first in list)
					if (sortedEdges.size() == 0){
						return carpoolGroups;
					}
					Edge smallestEdge = sortedEdges.remove(0);
					removeMirror(sortedEdges, smallestEdge);
					//add these two students to carpool group i
						Student s1 = smallestEdge.getStudent1();
						Student s2 = smallestEdge.getStudent2();
						carpoolGroups.get(i).add(smallestEdge);


				//find smallest edge that contains either s1 or s2 from first edge
						Edge newSmallestEdge = null;
						boolean found = false;
						for (int j=0; j<sortedEdges.size(); j++){
							if (!found && (sortedEdges.get(j).getStudent1().getName().equals(s1.getName()) || sortedEdges.get(j).getStudent2().getName().equals(s2.getName()))){
								newSmallestEdge = sortedEdges.remove(j);
								removeMirror(sortedEdges, newSmallestEdge);
								found = true;
							}
						}
					//add the new student (s3) to carpool group i+1
						carpoolGroups.get(i).add(newSmallestEdge);
					//delete all instances of s1 or s2 fromt first edge
						/*ArrayList<Edge> toRemove = new ArrayList();
						for (Edge e: sortedEdges){
							if (e.getStudent1().getName().equals(s1.getName()) || e.getStudent2().getName().equals(s2.getName())){
								toRemove.add(e);
							}
						}
						for (Edge g: toRemove){
							sortedEdges.remove(g);
						}
*/
				//loop through remaining edges, each time, deleting all instances of lastEdge(s3 first)
					//Edge lastEdge = newSmallestEdge;
					for (int q=0; q<sizeGroups-3; q++){
						//find smallest edge that contains lastEdge
							Edge anotherSmallEdge = null;
							boolean foundedge = false;
							for (int x=0; x<sortedEdges.size() && !foundedge; x++){
								for (Edge lastEdge: carpoolGroups.get(i)){
									if (!foundedge && (sortedEdges.get(x).getStudent1().getName().equals(lastEdge.getStudent1().getName()) || sortedEdges.get(x).getStudent1().getName().equals(lastEdge.getStudent2().getName()) || sortedEdges.get(x).getStudent2().getName().equals(lastEdge.getStudent1().getName()) || sortedEdges.get(x).getStudent2().getName().equals(lastEdge.getStudent2().getName()))){
										anotherSmallEdge = sortedEdges.remove(x);
										removeMirror(sortedEdges, anotherSmallEdge);
										foundedge = true;
									}
								}
							}
							if (anotherSmallEdge != null){
							//add it to carpoolGroup i+1
								carpoolGroups.get(i).add(anotherSmallEdge);
						//delete all instances of lastEdge.getStudent1or2
							Student student1 = anotherSmallEdge.getStudent1();
							Student student2 = anotherSmallEdge.getStudent2();

							/*ArrayList<Edge> toRemove1 = new ArrayList();
							for (Edge e: sortedEdges){
								if (e.getStudent1() == student1 || e.getStudent1() == student2 || e.getStudent2() == student1 || e.getStudent2() == student2){
									toRemove1.add(e);
								}
							}
							for (Edge g: toRemove1){
								sortedEdges.remove(g);
							}*/
						} else {
							System.out.println("Another small edge is NULL");
							System.out.println("Remaining edges:" + sortedEdges);
						}
					}
				for (Edge e: carpoolGroups.get(i)){
					for (int x = sortedEdges.size()-1; x>=0; x--){
						if (sortedEdges.get(x).getStudent1().getName().equals(e.getStudent1().getName()) || 
							 sortedEdges.get(x).getStudent1().getName().equals(e.getStudent2().getName()) || 
							 sortedEdges.get(x).getStudent2().getName().equals(e.getStudent1().getName()) || 
							 sortedEdges.get(x).getStudent2().getName().equals(e.getStudent2().getName())){
								sortedEdges.remove(x);
						}
					}
				}
			}
		return carpoolGroups;
	}

	public void removeMirror(ArrayList<Edge> sorted, Edge e){
		String s1 = e.getStudent1().getName();
		String s2 = e.getStudent2().getName();
		for (int i = sorted.size() - 1; i >= 0; i--){
			if (sorted.get(i).getStudent2().getName().equals(s1) && sorted.get(i).getStudent1().getName().equals(s2)){
				sorted.remove(i);
				return;
			}
		}
	}

	public ArrayList<ArrayList<Student>> groups(ArrayList<Edge> sortedEdges){
		ArrayList<Student> templateList = students;
		ArrayList<ArrayList<Student>> toReturn = new ArrayList<ArrayList<Student>>(3);
		ArrayList<Student> group1 = new ArrayList<Student>(5);
		ArrayList<Student> group2 = new ArrayList<Student>(5);
		ArrayList<Student> group3 = new ArrayList<Student>(5);


		Edge edge1 = sortedEdges.get(0);
		group1.add(edge1.getStudent1());
		group1.add(edge1.getStudent2());
		Edge lastEdge = edge1;

		while (group1.size() < 5){
			for (Edge e: sortedEdges){
				if (e.getStudent1() == lastEdge.getStudent1() || e.getStudent1() == lastEdge.getStudent2()){
					group1.add(e.getStudent2());
					templateList.remove(e.getStudent2());
					lastEdge = e;
				}else if (e.getStudent2() == lastEdge.getStudent1() || e.getStudent2() == lastEdge.getStudent2()){
					group1.add(e.getStudent1());
					templateList.remove(e.getStudent1());
					lastEdge = e;
				}
			}
			sortedEdges.remove(lastEdge);
		}
		for(Student s:group1){
			System.out.println(s.getName());
		}

		toReturn.add(group1);
		toReturn.add(group2);
		toReturn.add(group3);
		return toReturn;
	}
}







