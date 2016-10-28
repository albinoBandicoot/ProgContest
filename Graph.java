import java.util.*;
import static java.lang.Math.*;
public class Graph {

	/* Most of this code is untested in its current form but is derived
	 * from code that was tested and working */

	public List<Node> nodes;

	public Graph () {
		nodes = new ArrayList<Node>();
	}

	public static boolean ok (int x, int y, int xs, int ys, boolean wx, boolean wy) {
		int a = wx ? 1 : 0;
		int b = wy ? 1 : 0;
		return x >= -a && x < xs+a && y >= -b && y < ys+b;
	}

	// grid graph
	public Graph (int x, int y, boolean diags, boolean wrap_x, boolean wrap_y) {
		this();
		for (int i=0; i < x; i++) {
			for (int j=0; j < y; j++) {
				new Node(i + "," + j);	// or whatever object you want for data
			}
		}
		for (int i=0; i < x; i++) {
			for (int j=0; j < y; j++) {
				for (int a = i-1; a <= i+1; a++) {
					for (int b = j-1; b <= i+1; b++) {
						int q = abs(i-a)+abs(j-b);
						if (q != 0 && q <= (diags ? 2 : 1) && ok(a,b,x,y,wrap_x,wrap_y)) {
							// add weights as appropriate
							nodes.get(i*y+j).addEdge (nodes.get(Euclid.mod(a,x)*y+Euclid.mod(b,y)), false);
						}
					}
				}
			}
		}
	}

	class Node implements Comparable<Node> {
		public List<Edge> adj;
		public Object val;
		public double flag;

		// NOTICE! This constructor adds 'this' to the graph! Be careful
		// not to double-add!
		public Node (Object v) {
			val = v;
			adj = new ArrayList<Edge>();
			nodes.add(this);	// auto-add to the graph
		}

		public void addEdge (Node n, double w, boolean directed) {
			System.out.println("Adding " + this + (directed ? " --> " : "<-->")  + n);
			adj.add(new Edge(this, n, w));
			if (!directed) n.adj.add(new Edge(n, this, w));
		}

		public void addEdge (Node n, boolean directed) {
			addEdge (n, 1, directed);
		}

		// only needed for flow
		public void addEdgeFF (Node b, long c) {
			Edge e = new Edge (this,b,c,true);
			Edge f = new Edge (b,this,c,false);
			e.mate = f;
			f.mate = e;
			adj.add(e);
			b.adj.add(f);
		}

		public int compareTo (Node n) {
			return (int) signum (flag - n.flag);
		}

		// only needed for connectedComponents()
		public void flagConnected () {
			for (Edge e : adj) {
				if (e.e.flag == -1) {
					e.e.flag = flag;
					e.e.flagConnected();
				}
			}
		}

		public String toString () {
			return val.toString();
		}
	}

	class Edge implements Comparable<Edge>{
		public Node s,e;
		public double w;

		public Edge (Node s, Node e, double w) {
			this.s = s;
			this.e = e;
			this.w = w;
		}

		/* This section is only needed for flow */
		public Edge mate;
		public long cap=1, flow=0;
		public boolean fw=true;

		public Edge (Node s, Node e, long c, boolean f) {
			this(s,e,0);
			cap = c;
			fw = f;
		}

		public long room () {
			return fw ? cap - flow : flow;
		}
		/* end flow section */

		public int compareTo (Edge e) {
			return (int) signum (w - e.w);
		}

		public String toString () {
			return "(" + s + " --> " + e + ")";
		}
	}

	class Path {

		public Node head;
		public Edge edge;
		public Path tail;

		public Path (Node n, Edge e, Path p) {
			head = n;
			edge = e;
			tail = p;
		}

		/* End of mandatory Path methods */
		public int length () {
			if (tail == null) return 1;
			return 1 + tail.length();
		}

		public double weight (){ 
			if (tail == null) return 0;
			return edge.w + tail.weight();
		}

		public String toString () {
			if (tail == null) return head.toString();
			return head + " " + tail;
		}

		/* Flow */
		public long bottleneck () {
			if (tail == null) return Long.MAX_VALUE;
			return Math.min (edge.room(), tail.bottleneck());
		}

		public void augment (long b) {
			if (tail == null) return;
			tail.augment(b);
			if (!edge.fw) b = -b;
			edge.flow += b;
			edge.mate.flow += b;
		}
		/* end flow */
	}

	public void setFlags (double k) {
		for (Node n : nodes){
			n.flag = k;
		}
	}

	// only required for bfs and dijkstra.
	// Note: after running bfs or dijkstra, this can be called multiple
	// times with different targets 
	public Path extractPath (Node target, Map<Node,Edge> parents) {
		if (target.flag == -1) return null;
		Path p = new Path(target, null, null);
		Edge e = parents.get(p.head);
		while (e != null) {
			p = new Path(e.s, e, p);
			e = parents.get(p.head);
		}
		return p;
	}

	/* End of required Graph methods */

	public Path bfs (Node start, Node target) {
		Map<Node,Edge> par = bfs(start);
		return extractPath (target, par);
	}

	public Map<Node,Edge>  bfs (Node start) {
		setFlags(-1);
		Queue<Node> q = new LinkedList<Node>();
		Map<Node,Edge> parents = new HashMap<Node,Edge>();
		start.flag = 0;
		q.add(start);
		while (!q.isEmpty()) {
			Node n = q.remove();
			for (Edge e : n.adj) {
				if (e.e.flag == -1 && e.room() > 0) {	// can get rid of the e.room condition if not doing flow
					parents.put(e.e, e);
					e.e.flag = n.flag+1;
					q.add(e.e);
				}
			}
		}
		return parents;
	}

	public Path dijkstra (Node start, Node target) {
		Map<Node,Edge> par = dijkstra(start);
		return extractPath (target, par);
	}

	public Map<Node,Edge> dijkstra (Node start) {
		setFlags(-1);
		PriorityQueue<Node> q = new PriorityQueue<Node>();
		HashMap<Node,Edge> parents = new HashMap<Node,Edge>();
		start.flag = 0;
		q.add(start);
		while (!q.isEmpty()) {
			Node n = q.poll();
			for (Edge e : n.adj) {
				if (e.e.flag == -1 || e.e.flag > n.flag + e.w) {
					parents.put(e.e, e);
					e.e.flag = n.flag + e.w;
					q.add(e.e);
				}
			}
		}
		return parents;
	}

	// sets flags according to component; returns # of components
	public int flagConnectedComponents () {
		setFlags(-1);
		int f = 0;
		for (Node n : nodes) {
			if (n.flag == -1) {
				n.flag = f++;
				n.flagConnected();
			}
		}
		return f;
	}

	public Graph[] getConnectedComponents () {
		int nc = flagConnectedComponents();
		Graph[] comps = new Graph[nc];
		for (int i=0; i < nc; i++) {
			comps[i] = new Graph();
		}
		for (Node n : nodes) {
			comps[(int) n.flag].nodes.add(n);
		}
		return comps;
	}

	// Prim's algorithm 
	public Graph minSpanningTree (Node root) {
		setFlags(0);
		Graph g = new Graph();
		g.new Node(root.val);
		PriorityQueue<Edge> q = new PriorityQueue<Edge>();
		q.addAll (root.adj);
		while (g.nodes.size() < nodes.size()) {	// not all nodes are in the tree
			Edge e = q.poll();	// edges will have at most 1 endpoint not in the tree.
			Node n = null;
			if (e.s.flag == 0) {	// start is not in tree
				n = e.s;
			} else if (e.e.flag == 0) {
				n = e.e;
			}
			if (n != null) {
				n.flag = 1;
				Node nn = g.new Node(n.val);
				nn.addEdge (n == e.s ? e.e : e.s, e.w, false);
				q.addAll(n.adj);
			}
		}
		return g;
	}

	public List<Node> topologicalSort () {
		List<Node> res = new ArrayList<Node>();
		Queue<Node> q = new LinkedList<Node>();
		Map<Node,Integer> inc = new HashMap<Node,Integer>();
		for (Node n : nodes) {
			inc.put(n,0);
		}
		for (Node n : nodes) {
			for (Edge e : n.adj) {
				inc.put(e.e, inc.get(e.e)+1);
			}
		}
		for (Node n : nodes) {
			if (inc.get(n) == 0) q.add(n);
		}
		while (!q.isEmpty()) {
			Node n = q.remove();
			res.add(n);
			for (Edge e : n.adj) {
				inc.put(e.e, inc.get(e.e)-1);
				if (inc.get(e.e) == 0) q.add(e.e);
			}
		}
		for (Node n : nodes) {
			if (inc.get(n) != 0) return null;	// cyclic graph
		}
		return res;
	}

	public long fordFulkerson (Node s, Node t) {
		Path p = bfs(s, t);
		while (p != null) {
			p.augment(p.bottleneck());
			p = bfs(s, t);
		}
		long flow = 0;
		for (Edge e : s.adj) {
			flow += e.flow;
		}
		return flow;
	}

	// parameter is number of nodes on the source side
	public List<Edge> matching (int na) {
		Node s = new Node("source");
		Node t = new Node("sink");
		for (int i=0; i < na; i++) {
			s.addEdgeFF (nodes.get(i),1);
		}
		for (int i=na; i < nodes.size()-2; i++) {
			nodes.get(i).addEdgeFF (t, 1);
		}
		long msize = fordFulkerson (s,t);
		if (msize != na) {
			// no perfect matching
		}
		List<Edge> M = new ArrayList<Edge>();
		nodes.remove(s);
		nodes.remove(t);
		for (int i=0; i < na; i++) {
			Node n = nodes.get(i);
			boolean matched = false;
			for (Edge e : n.adj) {
				if (e.flow == 1 && e.fw && e.e != t) {
					matched = true;
					M.add(e);
				}
			}
			if (!matched) {
				// no perfect matching, and n is unmatched. 
			}
		}
		return M;
	}
}
