import java.util.*;

import Algorithm.Trees.LCA.*;

public class Main {
    public static void main(String[] args) {
        int N = 50; // number of nodes
        List<Integer> tree[] = new ArrayList[N];
        for (int i = 0; i < N; i++)
            tree[i] = new ArrayList<>();

        int[][] edges = {
                // { 0, 1 }, { 0, 2 }, { 1, 3 }, { 1, 4 }, { 2, 5 },
                // { 2, 6 }, { 3, 7 }, { 3, 8 }, { 4, 9 }, { 4, 10 },
                // { 5, 11 }, { 5, 12 }, { 6, 13 }, { 6, 14 }, { 7, 15 },
                // { 8, 16 }, { 9, 17 }, { 10, 18 }, { 11, 19 }, { 12, 20 },
                // { 13, 21 }, { 14, 22 }, { 15, 23 }, { 16, 24 }, { 17, 25 },
                // { 18, 26 }, { 19, 27 }, { 20, 28 }, { 21, 29 }, { 22, 30 },
                // { 23, 31 }, { 24, 32 }, { 25, 33 }, { 26, 34 }, { 27, 35 },
                // { 28, 36 }, { 29, 37 }, { 30, 38 }, { 31, 39 }

                { 0, 1 }, { 1, 2 }, { 1, 3 }, { 0, 4 }, { 4, 5 },
                { 1, 6 }, { 2, 7 }, { 3, 8 }, { 0, 9 }, { 6, 10 },
                { 5, 11 }, { 7, 12 }, { 7, 13 }, { 0, 14 }, { 9, 15 },
                { 14, 16 }, { 10, 17 }, { 17, 18 }, { 6, 19 }, { 5, 20 },
                { 2, 21 }, { 21, 22 }, { 3, 23 }, { 20, 24 }, { 6, 25 },
                { 0, 26 }, { 15, 27 }, { 14, 28 }, { 25, 29 }, { 29, 30 },
                { 30, 31 }, { 27, 32 }, { 27, 33 }, { 0, 34 }, { 11, 35 },
                { 1, 36 }, { 20, 37 }, { 28, 38 }, { 2, 39 }, { 39, 40 },
                { 36, 41 }, { 41, 42 }, { 23, 43 }, /* Removed {44, 30} */ { 26, 44 },
                { 34, 45 }, { 22, 46 }, { 46, 47 }, { 47, 48 }, { 45, 49 }
        };

        for (int[] e : edges) {
            tree[e[0]].add(e[1]);
            tree[e[1]].add(e[0]);
        }
        SqrRootDecomposition lca = new SqrRootDecomposition(tree, N);

        int[][] lcaQueries = {
                // { 15, 16 }, // both under node 3
                // { 23, 24 }, // under node 7 and 8
                // { 31, 39 }, // deep children of 15 and 23
                // { 19, 20 }, // children of 11 and 12 under 5
                // { 33, 34 }, // under 17 and 18 → under node 9 and 10
                // { 37, 38 }, // under 29 and 30
                // { 35, 36 }, // under 27 and 28
                // { 6, 14 }, // direct child and leaf
                // { 12, 14 }, // both under node 2
                // { 27, 36 }, // subtrees of 19 and 20
                // { 5, 14 }, // under node 2
                // { 32, 33 }, // under 24 and 25
                // { 7, 13 }, // under node 3 and 6
                // { 29, 38 }, // children of 13 and 14
                // { 1, 6 } // direct child of root and grandchild

                { 12, 13 },
                { 18, 19 },
                { 31, 33 },
                { 42, 48 },
                { 30, 44 } // Crucial query to test the impact of the removed edge
        };

        int[] expectedLCA = {
                // { 3 }, // LCA(15,16) = 3
                // { 3 }, // LCA(23,24) = 3
                // { 15 }, // LCA(31,39) = 15
                // { 5 }, // LCA(19,20) = 5
                // { 4 }, // LCA(33,34) = 4
                // { 6 }, // LCA(37,38) = 6
                // { 5 }, // LCA(35,36) = 5
                // { 6 }, // LCA(6,14) = 6
                // { 2 }, // LCA(12,14) = 2
                // { 5 }, // LCA(27,36) = 5
                // { 2 }, // LCA(5,14) = 2
                // { 2 }, // LCA(32,33) = 2
                // { 0 }, // LCA(7,13) = 0
                // { 2 }, // LCA(29,38) = 2
                // { 0 } // LCA(1,6) = 0

                7, // LCA(12, 13)
                6, // LCA(18, 19)
                0, // LCA(31, 33)
                1, // LCA(42, 48)
                0 // LCA(30, 44) - They are now on different main branches from root 0
        };

        long st = System.currentTimeMillis();

        for (int i = 0; i < lcaQueries.length; i++) {
        int query[] = lcaQueries[i];
        int res = lca.findLCA(query[0], query[1]);
        if (res == expectedLCA[i]) {
        System.out.println("Passed");
        } else {
        System.out.println(res + " : " + expectedLCA[i]);
        System.out.println("Failed");
        }
        }
        long et = System.currentTimeMillis();
        System.out.println(et - st);

        BinaryLifiting lca2 = new BinaryLifiting(tree, N);
        st = System.currentTimeMillis();
        for (int i = 0; i < lcaQueries.length; i++) {
            int query[] = lcaQueries[i];
            int res = lca2.lcaQuery(query[0], query[1]);
            if (res == expectedLCA[i]) {
                System.out.println("Passed");
            } else {
                System.out.println(res + " : " + expectedLCA[i]);
                System.out.println("Failed");
            }
        }

        et = System.currentTimeMillis();
        System.out.println(et - st);
    }
}