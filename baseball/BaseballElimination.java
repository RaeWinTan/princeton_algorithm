import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int noOfTeams;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    private final SET<String> e;
    private final int[] stillIn;
    private final HashMap<String, Integer> teamId;
    private final HashMap<Integer, String> idTeam;

    public BaseballElimination(String filename) {
        In file = new In(filename);
        this.noOfTeams = Integer.parseInt(file.readLine());
        this.teamId = new HashMap<String, Integer>();
        this.idTeam = new HashMap<Integer, String>();
        this.w = new int[this.noOfTeams];
        this.l = new int[this.noOfTeams];
        this.r = new int[this.noOfTeams];
        this.e = new SET<String>();
        this.g = new int[this.noOfTeams][];
        for (int j = 0; j < this.noOfTeams; j++) {
            if (j == 0) {
                this.g[0] = null;
            }
            else this.g[j] = new int[j + 1];
        }
        int i = 0;
        while (file.hasNextLine()) {
            String s = file.readLine().trim();
            String[] ss = s.split("\\s+");
            this.w[i] = Integer.parseInt(ss[1]);
            this.l[i] = Integer.parseInt(ss[2]);
            this.r[i] = Integer.parseInt(ss[3]);
            for (int j = 4; j < i + 4; j++) this.g[i][j - 4] = Integer.parseInt(ss[j]);
            this.idTeam.put(i, ss[0]);
            this.teamId.put(ss[0], i++);
        }
        int mostWin = 0;
        for (int wins : this.w) {
            if (mostWin < wins) {
                mostWin = wins;
            }
        }
        for (int j = 0; j < this.noOfTeams; j++) {
            if (w[j] + r[j] < mostWin) this.e.add(this.idTeam.get(j));
        }
        this.stillIn = new int[this.noOfTeams - this.e.size()];
        int c = 0;
        for (int j = 0; j < this.noOfTeams; j++) {
            if (!this.e.contains(this.idTeam.get(j))) {
                this.stillIn[c++] = j;
            }
        }

    }

    private int getAgainst(int curr, int other) {
        if (other == curr) return 0;
        if (curr < other) return this.getAgainst(other, curr);
        else return this.g[curr][other];
    }

    public int numberOfTeams() {
        return this.noOfTeams;
    }                        // number of teams

    public Iterable<String> teams() {
        return this.teamId.keySet();
    } // all teams

    public int wins(String team) {
        if (!this.teamId.containsKey(team)) throw new IllegalArgumentException("no such team");
        return this.w[this.teamId.get(team)];
    }                      // number of wins for given team

    public int losses(String team) {
        if (!this.teamId.containsKey(team)) throw new IllegalArgumentException("no such team");
        return this.l[this.teamId.get(team)];
    }                    // number of losses for given team

    public int remaining(String team) {
        if (!this.teamId.containsKey(team)) throw new IllegalArgumentException("no such team");
        return this.r[this.teamId.get(team)];
    }                 // number of remaining games for given team

    public int against(String team1, String team2) {
        if (!this.teamId.containsKey(team1) || !this.teamId.containsKey(team2))
            throw new IllegalArgumentException("no such team");
        return this.getAgainst(this.teamId.get(team1), this.teamId.get(team2));
    }    // number of remaining games between team1 and team2

    private FlowNetwork createNetwork(String team) {
        if (!this.teamId.containsKey(team)) throw new IllegalArgumentException("no such team");
        int n = this.noOfTeams - this.e.size() - 1;//number of teams still in
        int size = ((n * (n + 1)) / 2) + this.noOfTeams + 2;
        int source = this.noOfTeams + ((n * (n + 1)) / 2);
        int sink = this.noOfTeams + ((n * (n + 1)) / 2) + 1;
        FlowNetwork graph = new FlowNetwork(size);
        int v = this.noOfTeams;
        for (int i = 0; i < this.stillIn.length; i++) {
            if (this.stillIn[i] == this.teamId.get(team)) continue;
            for (int j = i + 1; j < this.stillIn.length; j++) {
                if (this.stillIn[j] == this.teamId.get(team)) continue;
                graph.addEdge(new FlowEdge(source, v,
                                           this.getAgainst(this.stillIn[i], this.stillIn[j])));
                graph.addEdge(new FlowEdge(v, this.stillIn[i], Integer.MAX_VALUE));
                graph.addEdge(new FlowEdge(v, this.stillIn[j], Integer.MAX_VALUE));
                v++;
            }
        }
        for (int t : this.stillIn) {
            graph.addEdge(new FlowEdge(t, sink, this.wins(team) + this.remaining(team) - this
                    .wins(this.idTeam.get(t))));
        }
        return graph;
    }

    public boolean isEliminated(String team) {
        if (!this.teamId.containsKey(team)) throw new IllegalArgumentException("no such team");
        if (this.e.contains(team)) return true;
        int n = this.noOfTeams - this.e.size() - 1;
        int source = this.noOfTeams + ((n * (n + 1)) / 2);
        int sink = this.noOfTeams + ((n * (n + 1)) / 2) + 1;
        FlowNetwork graph = this.createNetwork(team);
        new FordFulkerson(graph, source, sink);
        for (FlowEdge fe : graph.adj(source)) {
            if (fe.capacity() != fe.flow()) return true;
        }
        return false;
    }


    public Iterable<String> certificateOfElimination(String team) {
        if (!this.teamId.containsKey(team)) throw new IllegalArgumentException("no such team");
        if (this.e.contains(team)) {
            SET<String> minst = new SET<String>();
            for (int i = 0; i < this.noOfTeams; i++) {
                if (this.w[this.teamId.get(team)] + this.r[this.teamId.get(team)] < this.w[i])
                    minst.add(this.idTeam.get(i));
            }
            if (minst.isEmpty()) return null;
            return minst;
        }
        else {
            int n = this.noOfTeams - this.e.size() - 1;
            int source = this.noOfTeams + ((n * (n + 1)) / 2);
            int sink = this.noOfTeams + ((n * (n + 1)) / 2) + 1;
            FlowNetwork graph = this.createNetwork(team);
            FordFulkerson ff = new FordFulkerson(graph, source, sink);
            SET<String> minst = new SET<String>();
            for (int t : this.stillIn) if (ff.inCut(t)) minst.add(this.idTeam.get(t));
            if (minst.isEmpty()) return null;
            return minst;
        }

    }  // subset R of teams that eliminates given team; null if not eliminated

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
