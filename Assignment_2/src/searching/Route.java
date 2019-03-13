package searching;

public class Route implements Comparable<Route>{
	/*
	 * An edge.
	 */
	int source_position, destination_position, weight;
	
	public Route(int source_position, int destination_position, int weight) {
		this.destination_position = destination_position;
		this.source_position = source_position;
		this.weight = weight;
	}
	
	// Override compareTo so we can use PriorityQueues
	@Override
	public int compareTo(Route other) {
		if(this.weight < other.get_weight())
			return -1;
		else if(this.weight > other.get_weight())
			return 1;
		else
			return 0;
	}
	
	public void set_weight(int w) {
		this.weight = w;
	}
	
	public int get_weight() {
		return weight;
	}
	
	public int get_destination() {
		return destination_position;
	}
	
	public int get_source() {
		return source_position;
	}

}
