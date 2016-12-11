
public class Dijkstra {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private DirectedEdge[] edgeTo;
	private double[] distTo;
	private IndexMinPQ<Double> pq;

	public Dijkstra(EdgeWeightedDigraph G, int s) {
		edgeTo = new DirectedEdge[G.V()];
		distTo = new double[G.V()];
		pq = new IndexMinPQ<Double>(G.V());
		for (int v = 0; v < G.V(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
		}
		distTo[s] = 0.0;

		pq.insert(s, 0.0);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (DirectedEdge e : G.adj(v)) {
				relax(e);
			}
		}
	}

	private void relax(DirectedEdge e) {
	int v = e.from(), w = e.to();
	if (distTo[w] > distTo[v] + e.weight()) 
	{
		distTo[w] = distTo[v] + e.weight(); 
		edgeTo[w] = e;
		if (pq.contains(w)){
			pq.decreaseKey(w, distTo[w]);  //updates PQ points
		}
		else
		{
			pq.insert (w, distTo[w]);
		}
	}
}
