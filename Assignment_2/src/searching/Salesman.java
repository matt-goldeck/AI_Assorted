package searching;
import java.io.*;
import java.util.*;

public class Salesman {
	/*
	 *  The salesman needs to visit all the cities in the shortest possible time. 
	 *  We're going to try to use a Greedy Best First search and a Branch and Bound search
	 */
	
	private int graph_size; // The number of cities to visit
	private int distance_traveled; // The distance we'e traveled
	private ArrayList<City> city_list; // LinkedList of City objects; our Adjacency List
	
	// Constructor - Build a graph from a list of city names
	Salesman(String[] cities) {
		graph_size = cities.length;
		city_list = new ArrayList<City>();
		
		// Initialize city_list
		for(int i=0; i<graph_size; i++) {
			City run_city = new City(cities[i],i);
			city_list.add(run_city);
		}
	}
	
	void add_connection(int x, int y, int weight) {
		// Add connection to each position's lists
		
		City city_x = city_list.get(x);
		
		city_x.add_route(y, weight);
		
		city_list.set(x, city_x);
	}
	
	boolean check_goal(boolean[] visited, String goal_name) {
		/* 
		 * Check to see if our goal city has been visited
		 * If it has, pass back that we're done; otherwise signal that we're going backwards to find more
		 */
		for(int x = 0; x < visited.length; x++) {
			if(city_list.get(x).get_name() == goal_name && visited[x])
				return true;
		}
		return false;
	}

	void ucs_traversal(String start_city, String goal_name, boolean use_map) {
		int start_pos = match_name_to_pos(start_city); // Find our start position
		
		for(int x = 0; x < graph_size; x++) {
			City run_city = city_list.get(x);
			if(x==start_pos)
				run_city.set_shortest_path(0);		// Init our start city to be the closest (0 distance)
			else
				run_city.set_shortest_path(999999);	// Init every other city to some large number
			city_list.set(x, run_city);
		}
		
		PriorityQueue<Route> route_queue = new PriorityQueue<Route>();	// Create Route Queue to hold all discovered routes
		route_queue = add_child_routes(city_list.get(start_pos).get_routes(), route_queue); // Prime route queue with starting city's routes
		
		while(!route_queue.isEmpty()) {
			Route run_route = route_queue.poll();	// Pull the front of the queue (the cheapest path)
			
			City source_city = city_list.get(run_route.get_source());
			City dest_city = city_list.get(run_route.get_destination());
			
			// Functionality to simulate a person arriving at his/her goal, not looking for optimal.
			if(source_city.get_name() == goal_name && !use_map)
			{
				System.out.println("Arrived at goal!");
				return;
			}
			System.out.println("Exploring path from " + source_city.get_name() + " to " + dest_city.get_name());
			
			int new_dist = source_city.get_shortest_path() + run_route.get_weight(); // Weight of edge + current distance to source city
			// If the discovered path is shorter than the existing path, update the city node, replace it in city_list, add children to queue.
			if(new_dist < dest_city.get_shortest_path()) {
				System.out.println("New fastest route found!");
				dest_city.set_shortest_path(new_dist); // Update shortest cost to the destination city
				dest_city.set_parent(source_city); // Update the parent node of the city to leave a trail of the shortest path
				city_list.set(dest_city.get_pos(), dest_city); // Replace the city data in the city_list
				route_queue = add_child_routes(dest_city.get_routes(), route_queue); // Add all child routes to the route queue.
			}
		}
		
	}
	
	PriorityQueue<Route> add_child_routes(ArrayList<Route> child_routes, PriorityQueue<Route> route_queue) {
		// Add routes from a given city to the queue if they are more optimal than what we already have
		for(int x = 0; x < child_routes.size(); x++)
			route_queue.add(child_routes.get(x));
		
		return route_queue;
	}
	
	int match_name_to_pos(String name) {
		for(int x = 0; x < city_list.size(); x++) {
			if(city_list.get(x).get_name() == name) {
				return city_list.get(x).get_pos();
			}
		}
		return 0; // Default to first position if we can't find the name
	}
	
	void print_optimal_route(String goal) {
	// Driver function to print out the generated tree
		City goal_city = city_list.get(match_name_to_pos(goal));
		System.out.println("\n===== Results ====");
		System.out.println("Distance to goal: " + goal_city.get_shortest_path());
		System.out.print("Path Found: ");
		print_parent_recursive(goal_city);
		System.out.println("\n==================");
		
	}
	void print_parent_recursive(City child_city)
	// Recursive print out the tree generated
	{
		if(child_city.get_parent() != null) // We found the end
		{
			print_parent_recursive(child_city.get_parent());
			System.out.print(" ---> " + child_city.get_name());
		}
		else
			System.out.print(child_city.get_name());
			
	}
	
	void greedy_visit(int pos, boolean visited[], int cost, String goal_name) {
		visited[pos] = true;
		distance_traveled += cost; // increment distance
		System.out.println("Visited: " + city_list.get(pos).get_name() + " | Distance so far: " + distance_traveled);
		
		// Recur for each adjacent city in order of closest
		ArrayList<Route> nearby_routes = city_list.get(pos).get_greedy_routes();
		Iterator<Route> r_itr = nearby_routes.iterator();
		
		// While there are still more cities connected and we haven't yet reached our goal, visit them.
		while(r_itr.hasNext() && !check_goal(visited, goal_name)) {
			Route run_route = r_itr.next();
			// If we haven't visited it yet, do so
			if(!visited[run_route.get_destination()])
				greedy_visit(run_route.get_destination(), visited, run_route.get_weight(), goal_name);
		}
		// Check to see if we've finished; otherwise need to go backwards (add distance again)
		if(check_goal(visited, goal_name) == false) {
			System.out.println("Dead end... Going backwards");
			distance_traveled += cost;
		}
			
	}

	
	void greedy_traversal(String start_city, String goal_name) {
		int start_pos = match_name_to_pos(start_city); // Find our start point
		distance_traveled = 0; // Refresh/Init distance traveled
		boolean visited[] = new boolean[city_list.size()]; 	// Boolean array to keep track of where we've been
		
		greedy_visit(start_pos, visited, 0, goal_name); 	// Visit the first city
	}

	public static void main(String[] args) {
		String[] cities = {"Montclair", "Newark", "Princeton", "Camden", "Hammonton", "Atlantic City", "Vineland", "Maurice River", "Cape May"};
		Salesman elon = new Salesman(cities);
		
		// Expand graph of weighted edges 
		elon.add_connection(0,1,10); // Montclair <> Newark - 10 
		elon.add_connection(0,2, 20); // Montclair <> Princeton - 20
		elon.add_connection(2, 3, 20); // Princeton <> Camden - 20
		elon.add_connection(3, 4, 20); // Camden <> Hammonton - 20
		elon.add_connection(1, 4, 30); // Newark <> Hammonton - 30
		elon.add_connection(4, 5, 5); // Hammonton <> AC - 5
		elon.add_connection(5, 8, 80); // AC <> CM - 80;
		elon.add_connection(4, 6, 20); // Hammonton <> Vineland - 20
		elon.add_connection(6, 7, 20); // Vineland <> Maurice River - 20
		elon.add_connection(7, 8, 5); // MR <> CM - 5
		
		// Init our source and goal
		String source_city = "Montclair";
		System.out.println("Source: " + source_city);
		String goal = "Cape May";
		System.out.println("Goal: " + goal);
		
		System.out.println("====Begin Greedy====");
		elon.greedy_traversal(source_city, goal);
		System.out.println("====End Greedy====");
		
		System.out.println("====Begin UCS====");
		elon.ucs_traversal(source_city, goal, false);
		elon.print_optimal_route(goal);
		System.out.println("====End UCS====");
			
	}
		

}


