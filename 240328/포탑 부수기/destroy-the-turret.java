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

    static class Turret {
        int attack, t, r, c;

        public Turret(int attack, int t, int r, int c) {
            this.attack = attack;
            this.t = t;
            this.r = r;
            this.c = c;
        }
    }

    static int N, M, K;
    static Turret[][] map;
    static ArrayList<Turret> attackTurret;
    static int[] dr = {0, 1, 0, -1}; // 우 하 좌 상
    static int[] dc = {1, 0, -1, 0};
    static int[] ddr = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] ddc = {0, 1, 1, 1, 0, -1, -1, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new Turret[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = new Turret(Integer.parseInt(st.nextToken()), 0, i, j);
            }
        }

        int time = 1;
        while (K-- > 0) {

            //print(map);

            if (findNotBrokenTurret() == 1) break;

            // 1. 공격자 선정
            Turret attacker = findAttacker();

            // 1. 공격력 증가
            attacker.attack += N + M;

            // 1. 공격시점 저장
            attacker.t = time++;

            // 2. 공격자의 공격
            Turret attackee = findAttackee(attacker);

            attackTurret = new ArrayList<>();

            Node start = new Node(attacker.r, attacker.c);
            Node destination = new Node(attackee.r, attackee.c);

            if (!lazer(start, destination, attacker.attack)) bullet(start, destination, attacker.attack);

            attackTurret.add(attacker);

            // 4. 포탑 정비
            fixTurret();

        }

        int answer = Integer.MIN_VALUE;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                answer = Math.max(answer, map[i][j].attack);
            }
        }

        System.out.println(answer);


    }

    private static void fixTurret() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (!attackTurret.contains(map[i][j]) && map[i][j].attack != 0) {
                    map[i][j].attack += 1;
                }
            }
        }
    }

    private static void bullet(Node start, Node destination, int attack) {
        attackTurret.add(map[destination.r][destination.c]);
        for (int d = 0; d < 8; d++) {
            int nr = (destination.r + ddr[d] + N) % N;
            int nc = (destination.c + ddc[d] + M) % M;
            if (nr == start.r && nc == start.c) continue;
            if (map[nr][nc].attack != 0) {
                attackTurret.add(map[nr][nc]);
            }
        }

        // 공격
        attack(destination.r, destination.c, attack);
    }

    private static boolean lazer(Node start, Node destination, int attack) {
        Queue<Node> q = new LinkedList<>();
        q.add(start);
        boolean[][] v = new boolean[N][M];
        v[start.r][start.c] = true;
        Node[][] shortRoute = new Node[N][M];

        boolean isShortPath = false;

        while (!q.isEmpty()) {
            Node cur = q.poll();

            if (cur.r == destination.r && cur.c == destination.c) {
                isShortPath = true;
                break;
            }

            for (int d = 0; d < 4; d++) {
                int nr = (cur.r + dr[d] + N) % N;
                int nc = (cur.c + dc[d] + M) % M;
                if (!v[nr][nc] && map[nr][nc].attack != 0) {
                    v[nr][nc] = true;
                    shortRoute[nr][nc] = cur;
                    q.add(new Node(nr, nc));
                }
            }
        }

        if (!isShortPath) {
            return false;
        }

        int r = destination.r;
        int c = destination.c;
        while (r != start.r || c != start.c) {
            attackTurret.add(map[r][c]);

            Node cur = shortRoute[r][c];
            r = cur.r;
            c = cur.c;
        }
        // 공격
        attack(destination.r, destination.c, attack);
        return true;
    }

    private static void attack(int r, int c, int attack) {
        for (Turret turret : attackTurret) {
            if (turret.r == r && turret.c == c) {
                turret.attack = Math.max(0, turret.attack - attack);
            } else {
                turret.attack = Math.max(0, turret.attack - attack / 2);
            }
        }
    }

    private static Turret findAttackee(Turret attacker) {
        PriorityQueue<Turret> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1.attack == o2.attack) {
                if (o1.t == o2.t) {
                    if (o1.r + o1.c == o2.r + o2.c) {
                        return o1.c - o2.c;
                    } else return (o1.r + o1.c) - (o2.r + o2.c);
                } else return o1.t - o2.t;
            } else return o2.attack - o1.attack;
        });
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (i == attacker.r && j == attacker.c) continue;
                if (map[i][j].attack != 0) {
                    pq.add(map[i][j]);
                }
            }
        }
        return pq.poll();
    }

    private static Turret findAttacker() {
        PriorityQueue<Turret> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1.attack == o2.attack) {
                if (o1.t == o2.t) {
                    if (o1.r + o1.c == o2.r + o2.c) {
                        return o2.c - o1.c;
                    } else return (o2.r + o2.c) - (o1.r + o1.c);
                } else return o2.t - o1.t;
            } else return o1.attack - o2.attack;
        });
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j].attack != 0) {
                    pq.add(map[i][j]);
                }
            }
        }
        return pq.poll();
    }

    private static int findNotBrokenTurret() {
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j].attack != 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    private static void print(Turret[][] map) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(map[i][j].attack + " ");
            }
            System.out.println();
        }
    }
}