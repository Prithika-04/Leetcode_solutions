import java.util.*;

class Solution {
    public int minMoves(String[] classroom, int energy) {

        int m = classroom.length;
        int n = classroom[0].length();

        int sr = 0, sc = 0;
        int litterCount = 0;

        // Find S and count L
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                char ch = classroom[i].charAt(j);

                if (ch == 'S') {
                    sr = i;
                    sc = j;
                }

                if (ch == 'L') {
                    litterCount++;
                }
            }
        }

        if (litterCount == 0)
            return 0;

        // Give every litter a bit number
        int[][] id = new int[m][n];

        for (int[] row : id)
            Arrays.fill(row, -1);

        int k = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (classroom[i].charAt(j) == 'L') {
                    id[i][j] = k++;
                }
            }
        }

        int totalMasks = 1 << litterCount;

        /*
         * visited[r][c][energy][mask]
         *
         * Instead of HashSet<String>,
         * use boolean array.
         */
        boolean[][][][] visited =
            new boolean[m][n][energy + 1][totalMasks];

        Queue<int[]> q = new ArrayDeque<>();

        int fullMask = totalMasks - 1;

        q.offer(new int[]{
            sr, sc, energy, fullMask, 0
        });

        visited[sr][sc][energy][fullMask] = true;

        int[][] dir = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
        };

        while (!q.isEmpty()) {

            int[] cur = q.poll();

            int r = cur[0];
            int c = cur[1];
            int e = cur[2];
            int mask = cur[3];
            int moves = cur[4];

            // All litter collected
            if (mask == 0) {
                return moves;
            }

            // Cannot move
            if (e == 0) {
                continue;
            }

            for (int[] d : dir) {

                int nr = r + d[0];
                int nc = c + d[1];

                // Outside
                if (nr < 0 || nr >= m ||
                    nc < 0 || nc >= n) {
                    continue;
                }

                // Wall
                if (classroom[nr].charAt(nc) == 'X') {
                    continue;
                }

                int newEnergy = e - 1;
                int newMask = mask;

                char cell = classroom[nr].charAt(nc);

                // Recharge
                if (cell == 'R') {
                    newEnergy = energy;
                }

                // Collect litter
                if (cell == 'L') {

                    int bit = id[nr][nc];

                    newMask = mask & ~(1 << bit);
                }

                // Already visited?
                if (!visited[nr][nc][newEnergy][newMask]) {

                    visited[nr][nc][newEnergy][newMask] = true;

                    q.offer(new int[]{
                        nr,
                        nc,
                        newEnergy,
                        newMask,
                        moves + 1
                    });
                }
            }
        }

        return -1;
    }
}