import java.util.ArrayList;
import java.util.Scanner;

public class Vertex {
	
	private String name;
	private boolean visited;
	private ArrayList<Neighbor>neighbors;

	public Vertex(String name){
		this.name = name;
		this.visited = false;
		this.neighbors = new ArrayList<Neighbor>();
	}

	public void connect(Vertex vertex, int weight){
		Neighbor newNeighbor = new Neighbor(vertex, weight);
		neighbors.add(newNeighbor);

		Neighbor secondNeighbor = new Neighbor(this, weight);
		vertex.neighbors.add(secondNeighbor);
	}

	public Integer getWeight(Vertex other){
		for (Neighbor pair: neighbors){
			if (pair.getVertex().equals(other)){
				return pair.getWeight();
			}
		}
		return null;
	}

	public ArrayList<Neighbor> getNeighbors(){
		return neighbors;
	}

	public String toString(){
		String toReturn = name+": ";
		for (Neighbor pair: neighbors){//this is going out of bounds
			toReturn+=(pair.toString());
		}
		return toReturn;
	}

	public String getName(){
		return name;
	}

	public boolean getVisited(){
		return visited;
	}

	public void setName(String newName){
		name = newName;
	}

	public void setVisited(boolean fax){
		visited = fax;
	}

	public class Neighbor{

		private Vertex vertex;
		private int weight;

		public Neighbor(Vertex vertex, int weight){
			this.vertex = vertex;
			this.weight = weight;
		}

		public Vertex getVertex(){
			return vertex;
		}

		public int getWeight(){
			return weight;
		}

		public String toString(){
			return ("<"+vertex.getName()+", "eight+">");
		}

		public Neighbor getNeighbor(Vertex v){//this might not make any sense
			if (this.getVertex().equals(v)){
				return this;
			}
			return null;
		}

		//public boolean equals(){

		//}

	}
	
}