package searching;

public class Route {
	/*
	 * An edge.
	 */
	int destination_position, weight;
	
	public Route(int destination_position, int weight) {
		this.destination_position = destination_position;
		this.weight = weight;
	}
	
	public int get_weight() {
		return weight;
	}
	
	public int get_destination() {
		return destination_position;
	}

}
