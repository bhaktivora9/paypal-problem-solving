package com.paypal.assessment.q4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

	/**
	 * Constraints: 
	 * 2 ≤ server_nodes ≤ 1000,
	 * 1 ≤ server_from[i], server_to[i] ≤ n,
	 * 1 ≤ server_weight[i] ≤ 106,
	 * 1 ≤ signal_speed ≤ 1000
	 */

	private static Map<Integer, List<int[]>> createGraph(int server_nodes, int[] server_from, int[] server_to,
			int[] server_weight) {
		Map<Integer, List<int[]>> graph = new HashMap<>();

		for (int i = 0; i < server_from.length; i++) {
			int from = server_from[i];
			int to = server_to[i];
			int weight = server_weight[i];

			graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new int[] { to, weight });
			graph.computeIfAbsent(to, k -> new ArrayList<>()).add(new int[] { from, weight });
		}

		return graph;
	}

	public static int[] getNumPairs(int server_nodes, int[] server_from, int[] server_to, int[] server_weight,
			int signal_speed) {
		Map<Integer, List<int[]>> graph = createGraph(server_nodes, server_from, server_to, server_weight);
		int[] pairsPerServer = new int[server_nodes];

		for (int server = 1; server <= server_nodes; server++) {
			pairsPerServer[server - 1] = countPairsThroughServer(graph, server, signal_speed, server_nodes);
		}

		return pairsPerServer;
	}

	private static int countPairsThroughServer(Map<Integer, List<int[]>> graph, int hub, int speed, int totalServers) {
		List<List<Integer>> subPaths = new ArrayList<>();

		for (int[] neighbor : graph.getOrDefault(hub, Collections.emptyList())) {
			int nextServer = neighbor[0];
			int weight = neighbor[1];
			List<Integer> reachable = dfsReachableServer(graph, nextServer, hub, weight, speed, totalServers);
			subPaths.add(reachable);
		}

		int totalPairs = 0;

		if (subPaths.size() >= 2) {
			for (int i = 0; i < subPaths.size(); i++) {
				for (int j = i + 1; j < subPaths.size(); j++) {
					totalPairs += subPaths.get(i).size() * subPaths.get(j).size() * 2;
				}
			}
		} else if (subPaths.size() == 1) {
			int count = subPaths.get(0).size();
			totalPairs = count * (count - 1);
		}

		return totalPairs;
	}

	private static List<Integer> dfsReachableServer(Map<Integer, List<int[]>> graph, int current, int parent,
			int distance, int speed, int totalServers) {
		boolean[] visited = new boolean[totalServers + 1];
		visited[parent] = true;
		return dfs(graph, current, distance, speed, visited);
	}

	private static List<Integer> dfs(Map<Integer, List<int[]>> graph, int node, int distance, int speed,
			boolean[] visited) {
		List<Integer> reachable = new ArrayList<>();
		visited[node] = true;

		if (distance % speed == 0) {
			reachable.add(node);
		}

		for (int[] edge : graph.getOrDefault(node, Collections.emptyList())) {
			int neighbor = edge[0];
			int weight = edge[1];

			if (!visited[neighbor]) {
				reachable.addAll(dfs(graph, neighbor, distance + weight, speed, visited));
			}
		}

		return reachable;
	}

	public static void main(String[] args) {
		System.out.println("Q4: Server Communication Pairs ===\n");

		testCase1();

		testCase2();

		testCase3();
	}

	private static void testCase1() {
		System.out.println("TEST CASE 1");
		int server_nodes = 4;
		int[] server_from = { 1, 1, 2 };
		int[] server_to = { 2, 3, 4 };
		int[] server_weight = { 2, 5, 3 };
		int signal_speed = 5;

		int[] result = getNumPairs(server_nodes, server_from, server_to, server_weight, signal_speed);
		System.out.println("Result:   " + Arrays.toString(result));
		System.out.println();
	}

	private static void testCase2() {
		System.out.println("TEST CASE 2");
		int server_nodes = 5;
		int[] server_from = { 1, 2, 3, 4 };
		int[] server_to = { 2, 3, 4, 5 };
		int[] server_weight = { 10, 20, 15, 25 };
		int signal_speed = 10;

		int[] result = getNumPairs(server_nodes, server_from, server_to, server_weight, signal_speed);

		System.out.println("Result: " + Arrays.toString(result));

		System.out.println();
	}

	private static void testCase3() {
		System.out.println("TEST CASE 3");
		int server_nodes = 2;
		int[] server_from = { 1 };
		int[] server_to = { 2 };
		int[] server_weight = { 10 };
		int signal_speed = 5;

		int[] result = getNumPairs(server_nodes, server_from, server_to, server_weight, signal_speed);
		System.out.println("Result:   " + Arrays.toString(result));
		System.out.println();
	}
}