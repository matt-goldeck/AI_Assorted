package searching;
import java.io.*;
import java.util.*;

public class Salesman {
	/*
	 *  The salesman needs to visit all the cities in the shortest possible time. 
	 *  We're going to try to use a Greedy Best First search and a Branch and Bound search
	 */
	
	private int targets; // The number of cities to visit
	private int distance_travelled; // The distance we'e traveled
	private ArrayList<City> city_list; // LinkedList of City objects; our Adjacency List
	
	// Constructor - Build a graph from a list of city names
	Salesman(String[] cities) {
		targets = cities.length;
		distance_travelled = 0;
		city_list = new ArrayList<City>();
		
		// Initialize city_list
		for(int i=0; i<targets; i++) {
			City run_city = new City(cities[i],i);
			city_list.add(run_city);
		}
	}
	
	void add_connection(int y, int x, int weight) {
		// Add connection to each position's lists
		
		City city_x = city_list.get(x);
		City city_y = city_list.get(y);
		
		city_x.add_route(y, weight);
		city_y.add_route(x, weight);
		
		city_list.set(x, city_x);
		city_list.set(y, city_y);
	}
	
	boolean check_goal(boolean[] visited) {
		/* 
		 * When we reach the end of a path Check to see if all cities have been visited
		 * If they have, pass back that we're done; otherwise signal that we're going backwards to find more
		 */
		for(int x = 0; x < visited.length; x++) {
			if(!visited[x])
				return false;
		}
		return true;
	}
	
	void greedy_visit(int pos, boolean visited[], int cost) {
		visited[pos] = true;
		distance_travelled += cost; // increment distance
		System.out.println("Visited: " + city_list.get(pos).get_name() + " | Distance so far: " + distance_travelled);
		
		// Recur for each adjacent city in order of closest
		ArrayList<Route> nearby_routes = city_list.get(pos).get_greedy_routes();
		Iterator<Route> r_itr = nearby_routes.iterator();
		
		while(r_itr.hasNext()) {
			Route run_route = r_itr.next();
			// If we haven't visited it yet, do so
			if(!visited[run_route.get_destination()])
				greedy_visit(run_route.get_destination(), visited, run_route.get_weight());
		}
		// Check to see if we've finished; otherwise need to go backwards (add distance again)
		if(check_goal(visited) == false) {
			System.out.println("Dead end... Going backwards");
			distance_travelled += cost;
		}
			
	}

	
	void greedy_traversal(int start_pos) {
		// Boolean array to keep track of where we've been
		boolean visited[] = new boolean[city_list.size()];
		
		// Visit the first city
		greedy_visit(start_pos, visited, 0);
	}

	public static void main(String[] args) {
		String[] cities = {"Montclair", "Newark", "Princeton", "Camden", "Atlantic City", "Vineland", "Cape May"};
		Salesman elon = new Salesman(cities);
		
		// Expand graph of weighted edges 
		elon.add_connection(0,1,10); // Montclair <> Newark - 10 
		elon.add_connection(0,2, 50); // Montclair <> Princeton - 50
		elon.add_connection(2, 3, 50); // Princeton <> Camden - 50
		elon.add_connection(3, 5, 80); // Princeton <> Vineland - 80
		elon.add_connection(3, 5, 30); // Camden <> Vineland - 30
		elon.add_connection(3, 4, 50); // Camden <> AC - 50
		elon.add_connection(5, 4, 40); // Vineland <> AC - 30
		elon.add_connection(5, 6, 50); // Vineland <> Cape May - 50
		elon.add_connection(4, 6, 50); // AC <> Cape May - 50
	
	
		elon.greedy_traversal(0);
			
	}
		

}


