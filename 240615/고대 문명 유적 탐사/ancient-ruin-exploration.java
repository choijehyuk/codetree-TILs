import java.io.*;
import java.util.*;

public class Main {
    static class Node {
        int r, c;

        public Node(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static int K, M;
    static int[][] map;
    static int[][] copyMap;
    static boolean[][] v;
    static int[] relics; // 유물
    static int relicsIdx;
    static int maxRelics;
    static int minDir;
    static int minrow;
    static int mincol;
    static boolean flag;
    static int end;
    static int answer;
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine());
        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[5][5];
        copyMap = new int[5][5];
        relics = new int[M];

        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            relics[i] = Integer.parseInt(st.nextToken());
        }

        while (K-- > 0) {
            // [1] 탐사 진행
            maxRelics = 0;
            minDir = 0;
            minrow = 0;
            mincol = 0;
            answer = 0;
            for (int i = 1; i < 4; i++) {
                for (int j = 1; j < 4; j++) {
                    // 선택된거 돌리기
                    copy();

                    for (int k = 0; k < 3; k++) {
                        turnMap(i, j);
                        findRelics(k, i, j);
                    }

//                    System.out.println(i + " " + j);
//                    printMap();
                }
            }

            if (maxRelics == 0) break;

            turnOriginalMap(minrow, mincol);
            v = new boolean[5][5];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (!v[i][j]) removeMap(i, j);
                }
            }
            fillMap();

//            for (int i = 0; i < 5; i++) {
//                for (int j = 0; j < 5; j++) {
//                    System.out.print(map[i][j] + " ");
//                }
//                System.out.println();
//            }

            flag = true;
            while (flag) {
                v = new boolean[5][5];
                end = 0;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (!v[i][j]) findRelics(i, j);
                    }
                }

//                System.out.println("---");
//                for (int i = 0; i < 5; i++) {
//                    for (int j = 0; j < 5; j++) {
//                        System.out.print(map[i][j] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println("---");

                fillMap();

                if (end == 0) flag = false;
            }

//            System.out.println();
//
//            for (int i = 0; i < 5; i++) {
//                for (int j = 0; j < 5; j++) {
//                    System.out.print(map[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
            System.out.print(answer + " ");
            //System.out.println(maxRelics + " " + minDir + " " + minrow + " " + mincol);
        }
    }

    private static void findRelics(int r, int c) {
        Queue<Node> q = new LinkedList<>();
        q.add(new Node(r, c));
        v[r][c] = true;
        ArrayList<Node> list = new ArrayList<>();
        int cnt = 1;
        int num = map[r][c];
        list.add(new Node(r, c));

        while (!q.isEmpty()) {
            Node cur = q.poll();

            for (int d = 0; d < 4; d++) {
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];

                if (!isRange(nr, nc)) continue;
                if (v[nr][nc]) continue;
                if (map[nr][nc] != num) continue;

                list.add(new Node(nr, nc));
                cnt++;
                q.add(new Node(nr, nc));
                v[nr][nc] = true;
            }
        }


        if (cnt >= 3) {
            end++;
            for (Node node : list) {
                map[node.r][node.c] = 0;
            }
        }
    }


    private static void turnOriginalMap(int r, int c) {
        int startR = r - 1;
        int startC = c - 1;

        int[][] temp = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[i][j] = map[startR + i][startC + j];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                map[startR + j][startC + 2 - i] = temp[i][j];
            }
        }
    }

    private static void fillMap() {
        for (int i = 0; i < 5; i++) {
            for (int j = 4; j >= 0; j--) {
                if (map[j][i] == 0) {
                    answer++;
                    map[j][i] = relics[relicsIdx];
                    relicsIdx++;
                    relicsIdx %= M;
                }
            }
        }
    }

    private static void removeMap(int r, int c) {
        Queue<Node> q = new LinkedList<>();
        q.add(new Node(r, c));
        v[r][c] = true;
        ArrayList<Node> list = new ArrayList<>();
        list.add(new Node(r, c));
        int num = map[r][c];
        int cnt = 1;

        while (!q.isEmpty()) {
            Node cur = q.poll();


            for (int d = 0; d < 4; d++) {
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];

                if (!isRange(nr, nc)) continue;
                if (v[nr][nc]) continue;
                if (map[nr][nc] != num) continue;

                cnt++;
                q.add(new Node(nr, nc));
                list.add(new Node(nr, nc));
                v[nr][nc] = true;
            }
        }

        if (cnt >= 3) {
            for (Node node : list) {
                map[node.r][node.c] = 0;
            }
        }
    }


    private static void findRelics(int dir, int r, int c) {
        v = new boolean[5][5];
        int relics = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!v[i][j]) {
                    relics += bfs(i, j);
                }
            }
        }

        if (relics > maxRelics) {
            maxRelics = relics;
            minDir = dir;
            minrow = r;
            mincol = c;
        } else if (relics == maxRelics) {
            if (dir < minDir) {
                minDir = dir;
                minrow = r;
                mincol = c;
            } else if (dir == minDir) {
                if (c < mincol) {
                    minrow = r;
                    mincol = c;
                } else if (c == mincol) {
                    if (r < minrow) {
                        minrow = r;
                    }
                }
            }
        }
    }

    private static int bfs(int r, int c) {
        Queue<Node> q = new LinkedList<>();
        v[r][c] = true;
        q.add(new Node(r, c));
        int num = copyMap[r][c];
        int cnt = 1;

        while (!q.isEmpty()) {
            Node cur = q.poll();

            for (int d = 0; d < 4; d++) {
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];

                if (!isRange(nr, nc)) continue;
                if (v[nr][nc]) continue;
                if (copyMap[nr][nc] != num) continue;

                cnt++;
                q.add(new Node(nr, nc));
                v[nr][nc] = true;
            }
        }

        if (cnt >= 3) return cnt;
        return 0;
    }

    private static boolean isRange(int nr, int nc) {
        if (nr >= 0 && nr < 5 && nc >= 0 && nc < 5) return true;
        return false;
    }

    private static void printMap() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(copyMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void turnMap(int r, int c) {

        int startR = r - 1;
        int startC = c - 1;

        int[][] temp = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[i][j] = copyMap[startR + i][startC + j];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copyMap[startR + j][startC + 2 - i] = temp[i][j];
            }
        }

    }

    private static void copy() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                copyMap[i][j] = map[i][j];
            }
        }
    }
}