public class Edge{
	private Student s1;
	private double weight;
	private Student s2;


	public Edge(Student s1, Student s2, double weight){
		this.s1 = s1;
		this.s2 = s2;
		this.weight = weight;
	}

	public double getWeight(){
		return weight;
	} 

	public Student getStudent1(){
		return s1;
	}

	public Student getStudent2(){
		return s2;
	}

	public String toString(){
		String toReturn = (s1.getName()+" is connected to "+s2.getName()+" with a weight of "+weight);
		return toReturn;
	}
}