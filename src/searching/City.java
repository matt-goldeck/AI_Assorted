package searching;
import java.util.*;

public class City {
	private String name;
	private int array_pos; // Position in ArrayList
	private ArrayList<Route> routes; // ArrayList of connected edges
	
	private City parent; // The city prior in the shortest path (UCS/BnB)
	private int shortest_path; // Shortest distance yet found to this node
	
	public City (String name, int array_pos) {
		this.name = name;
		this.array_pos = array_pos;
		
		this.parent = null;
		this.shortest_path = 999999;
		routes = new ArrayList<Route>();
	}
	
	public void add_route(int dest_pos, int weight) {
		Route run_route = new Route(this.array_pos,dest_pos, weight);
		routes.add(run_route);
	}
	public void set_parent(City c) {
		this.parent = c;
	}
	public void set_shortest_path(int s) {
		this.shortest_path = s;
	}
	public ArrayList<Route> get_greedy_routes() {
		// Used in Greedy Search - returns shortest connection that hasn't been visited yet
		ArrayList<Route> greedy_routes = routes;
		
		// Override comparator to sort array by weight
		Collections.sort(greedy_routes, new Comparator<Route>() {
	        @Override
	        public int compare(Route route1, Route route2)
	        {
	        	if(route1.get_weight() > route2.get_weight())
	        		return 1;
	        	else if(route1.get_weight() == route2.get_weight())
	        		return 0;
	        	else
	        		return -1;
	        }
	    });
		return greedy_routes;
	}
	public int get_shortest_path() {
		return shortest_path;
	}
	public ArrayList<Route> get_routes() {
		return routes;
	}
	
	public int get_pos() {
		return array_pos;
	}
	public String get_name() {
		return name;
	}
	public City get_parent() {
		return parent;
	}
}

