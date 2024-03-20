import java.io.*;
import java.util.*;

public class Main {
    static class Node {
        int r, c, dist;

        public Node(int r, int c, int dist) {
            this.r = r;
            this.c = c;
            this.dist = dist;
        }
    }

    static int N, M, K;
    static int[][] map;
    static ArrayList<Node> list;
    static int[] dr = {-1, 1, 0, 0}; // 상하우좌
    static int[] dc = {0, 0, 1, -1};
    static int[] tr = {1, 0, -1, 0};
    static int[] tc = {0, 1, 0, -1};
    static int outR, outC;
    static int turnR, turnC;
    static int answer;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        list = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int humanR = Integer.parseInt(st.nextToken()) - 1;
            int humanC = Integer.parseInt(st.nextToken()) - 1;
            map[humanR][humanC] = -1;
        }

        st = new StringTokenizer(br.readLine());
        outR = Integer.parseInt(st.nextToken()) - 1;
        outC = Integer.parseInt(st.nextToken()) - 1;
        map[outR][outC] = -2;

        // 거리계산 후 리스트에 사람 넣기
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] == -1) {
                    int distCal = Math.abs(i - outR) + Math.abs(j - outC);
                    list.add(new Node(i, j, distCal));
                }
            }
        }
//        print(map);
//        System.out.println("===========");

        while (K-- > 0) {
            move();

//            System.out.println("=====move map=====");
//            print(map);
//            System.out.println("===================");

            int minDist = Integer.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (minDist > list.get(i).dist) {
                    minDist = list.get(i).dist;
                    index = i;
                }
            }

//            System.out.println( " 움직이고 민디스트 " + minDist);

//            System.out.println();
//            print(map);
//            System.out.println();
//
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println(list.get(i).r + " " + list.get(i).c + " " + list.get(i).dist);
//            }

            if (list.size() == 0) break;

            findSquare(index, minDist);

//            System.out.println("턴 " + turnR + " " + turnC);

            turn(minDist);


            list.clear();

            // 돌리고 나면 사람 좌표랑 출구 좌표 다시 해야함

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (map[i][j] == -2) {
                        outR = i;
                        outC = j;
                    }
                }
            }

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (map[i][j] == -1) {
                        int newDist = Math.abs(i - outR) + Math.abs(j - outC);
                        list.add(new Node(i, j, newDist));
                    }
                }
            }

//            System.out.println("======turn map======");
//            print(map);
//            System.out.println("===================");
//            System.out.println();
//            System.out.println();

        }

        sb.append(answer).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] == -2) {
                    sb.append((i + 1) + " " + (j + 1));
                }
            }
        }

        System.out.println(sb);
    }

    private static void turn(int dist) {
        //System.out.println("turn " + turnR + " " + turnC);
        int stdR = Math.min(outR, turnR);
        int stdC = Math.min(outC, turnC);
//        System.out.println(stdR + " " + stdC);

        //int dist = Math.max(Math.abs(outR - turnR), Math.abs(outC - turnC));

        int[][] arr = new int[dist + 1][dist + 1];

        for (int i = stdR; i <= dist + stdR; i++) {
            for (int j = stdC; j <= dist + stdC; j++) {
                // dist = 1 , stdr = 1, stdc = 0
                //  i == 1 j == 0
                // 0 0 -> map 1 0 ( 2 0 )
                // i == 1 j == 1
                // 0 1 -> map 0 0 ( 1 0 )
                // i == 2 j == 0
                // 1 0 -> map 1 1 ( 2 1 )
                // i == 2 j == 1
                // 1 1 -> map 0 1 ( 1 1 )
                arr[i - stdR][j - stdC] = map[dist + stdC - j + stdR][i - stdR + stdC];
            }
        }

//        System.out.println("-------------");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] > 0) arr[i][j]--;
//                System.out.print(arr[i][j] + " ");
            }
//            System.out.println();
        }
//        System.out.println("------------");

        for (int i = 0; i <= dist; i++) {
            for (int j = 0; j <= dist; j++) {
                map[stdR + i][stdC + j] = arr[i][j];
            }
        }

//        for (int t = 0; t < dist / 2; t++) {
//            int r = stdR + t;
//            int c = stdC + t;
//
//            int temp = map[r][c];
//
//            if (temp > 0) temp--;
//
//            int idx = 0;
//            while (idx < 4) {
//                int nr = r + tr[idx];
//                int nc = c + tc[idx];
//
//                if (nr >= stdR + t && nr < stdR + dist - t + 1 && nc >= stdC + t && nc < stdC + dist - t + 1) {
//                    if (map[r][c] > 0) map[r][c]--;
//                    map[r][c] = map[nr][nc];
//                    r = nr;
//                    c = nc;
//                } else idx++;
//            }
//
//            map[stdR + t + 1][stdC + t] = temp;
//
//        }

    }

    private static void findSquare(int index, int minDist) {
        int[] sr = {-minDist, -minDist, minDist, minDist};
        int[] sc = {-minDist, minDist, -minDist, minDist};


        L:
        for (int i = 0; i < N - minDist; i++) {
            for (int j = 0; j < N - minDist; j++) {
                int outCnt = 0;
                int humanCnt = 0;
                for (int k = i; k <= minDist + i; k++) {
                    for (int l = j; l <= minDist + j; l++) {
                        if (map[k][l] == -2) outCnt++;
                        else if (map[k][l] == -1) humanCnt++;
                        //System.out.println("사각형 좌표 "  + k + " " + l + " " + outCnt + " " + humanCnt );
                        if (outCnt > 0 && humanCnt > 0) {
                            turnR = i;
                            turnC = j;
                            //  System.out.println("사각형" + turnR + " " + turnC + " " + minDist);
                            break L;
                        }
                    }
                }
            }
        }
//
//        for (int d = 0; d < 4; d++) {
//            int nr = outR + sr[d];
//            int nc = outC + sc[d];
//
//            if (!isRange(nr, nc)) continue;
//            if (map[nr][outC] == -1 || map[outR][nc] == -1) {
//                turnR = nr;
//                turnC = nc;
//                break;
//            }
//        }


    }

    private static void print(int[][] map) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void move() {
        for (int i = 0; i < list.size(); i++) {
            Node cur = list.get(i);
//            System.out.println(cur.r + " " + cur.c + " " + cur.dist);
            for (int d = 0; d < 4; d++) {
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];

                // 범위밖 x
                if (!isRange(nr, nc)) continue;
                // 벽 x
                if (map[nr][nc] > 0) continue;

                if (map[nr][nc] == -2) {
                    answer++;
                    list.remove(i);
                    map[cur.r][cur.c] = 0;
                    i--;
                    break;
                }

                int distTemp = Math.abs(nr - outR) + Math.abs(nc - outC);

                if (distTemp < cur.dist) {
                    list.set(i, new Node(nr, nc, distTemp));
                    map[cur.r][cur.c] = 0;
                    if (map[nr][nc] != -2) {
                        map[nr][nc] = -1;
                    }
                    answer++;
                    break;
                }

            }
        }
    }

    private static boolean isRange(int nr, int nc) {
        if (nr >= 0 && nr < N && nc >= 0 && nc < N) return true;
        return false;
    }
}