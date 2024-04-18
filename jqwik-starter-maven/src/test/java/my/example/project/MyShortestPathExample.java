package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

class MyShortestPathExample {

    @Example
    void bfsExampleTest() {
        MyShortestPath msp = new MyShortestPath();
        int[][] adj = {
                {0, 1, 1, 0},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {0, 1, 1, 0}
        };
        List<Integer> expected = Arrays.asList(0, 3);
        List<Integer> actual = msp.bfs(4, adj, 0, 2);
        assertThat(actual).isEqualTo(expected);
    }
}