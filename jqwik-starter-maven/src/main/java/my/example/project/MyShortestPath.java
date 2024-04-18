package my.example.project;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class MyShortestPath {
    public List<Integer> bfs(int no, int[][] adj, int id1, int id2) {
        boolean[] visited = new boolean[no];
        List<Integer> path = new ArrayList<>(Collections.nCopies(no, -1));
        Queue<Integer> q = new LinkedList<>();
        q.add(id1);

        for (int i = 0; i < no; i++) {
            visited[i] = false;
        }
        visited[id1] = true;

        while (!q.isEmpty()) {
            int temp = q.poll();
            visited[temp] = true;

            for (int i = 0; i < no; i++) {
                if (adj[temp][i] == 1 && !visited[i]) {
                    q.add(i);
                    path.set(i, temp);
                    //visited[i] = true;
                    if (id2 == i) {
                        List<Integer> result = new ArrayList<>();
                        for (int at = id2; at != -1; at = path.get(at)) {
                            result.add(at);
                        }
                        Collections.reverse(result);
                        return result;
                    }
                }
            }
        }
        return new ArrayList<>(); // return an empty list if there is no path
    }
}