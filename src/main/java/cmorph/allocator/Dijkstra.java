package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import cmorph.entities.Link;
import cmorph.simulator.Simulator;

public class Dijkstra {

    public static ArrayList<Integer> calculate(int startNodeId, int endNodeId) {

        ArrayList<Node> graph = new ArrayList<>();

        ArrayList<Link> links = Simulator.getSimulatedLinks();

        int n = DATA_CENTER_NUM + MICRO_DATA_CENTER_NUM; // ノードの数
        int m = links.size(); // エッジの数
        for (int i = 0; i < n; i++)
            graph.add(new Node(i, Integer.MAX_VALUE)); // 各ノードを生成する
        for (int i = 0; i < m; i++) { // エッジの定義を読み込む
            int s = links.get(i).getConnectNode1().getNodeId(); // 始点
            int t = links.get(i).getConnectNode2().getNodeId(); // 終点
            double w = links.get(i).getCost(); // 重み
            graph.get(s).addEdge(t, w); // ノードsからtにエッジを張る
            graph.get(t).addEdge(s, w); // ノードtからsにエッジを張る
        }
        Node src = graph.get(startNodeId); // スタートノード
        Node dst = graph.get(endNodeId); // 目的地ノード
        double d = dijkstra(graph, src, dst);
        if (d == Double.MAX_VALUE) {
            return null;
        } else {
            return getRoute(src, dst);
        }
    }

    public static double dijkstra(ArrayList<Node> graph, Node src, Node dst) {
        HashSet<Node> fixed = new HashSet<>(); // 最短距離が確定したノード
        src.d = 0; // 始点ノードの距離を0とおく
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        queue.add(src);
        while (queue.size() > 0) {
            Node x = queue.poll(); // キューからの中で最小のノードを取り出す
            assert !fixed.contains(x);
            if (x == dst)
                return x.d; // 解を発見した
            fixed.add(x); // x を確定ノードとする
            for (Map.Entry<Integer, Double> entry : x.edges.entrySet()) { // xから出ている各エッジに対して
                Node y = graph.get(entry.getKey()); // エッジの終端ノード
                double w = entry.getValue(); // エッジの重み
                double d2 = x.d + w; // xを経由したyまでの距離
                if (fixed.contains(y) || d2 >= y.d)
                    continue; // 「すでに確定済み」or「x経由は遠い」場合何もしない
                if (queue.contains(y))
                    queue.remove(y); // yがキューに含まれる場合は一旦取り出す
                y.d = d2; // yまでの距離を更新して
                y.prev = x; // 直前の経由ノードを記録して
                y.via = getLinkId(x.id, y.id); // yの経由地はx
                queue.add(y); // yをキューに追加する
            }
        }
        return Double.MAX_VALUE; // 到達不可能(非連結グラフ)
    }

    static int getLinkId(int src, int dst) {
        ArrayList<Link> links = Simulator.getSimulatedLinks();
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getConnectNode1().getNodeId() == src
                    && links.get(i).getConnectNode2().getNodeId() == dst) {
                return i;
            }
            if (links.get(i).getConnectNode1().getNodeId() == dst
                    && links.get(i).getConnectNode2().getNodeId() == src) {
                return i;
            }
        }
        return -1;
    }

    static ArrayList<Integer> getRoute(Node src, Node dst) { // src->dst の最短経路
        ArrayList<Integer> r = new ArrayList<>();
        Node x = dst;
        while (x != src) { // src まで逆順にたどる
            r.add(x.via);
            x = x.prev;
        }
        // Collections.reverse(r);
        return r;
    }

    static class Node implements Comparable<Node> { // ノードの定義 (比較可能)
        int id; // ノード番号
        double d; // 距離
        Node prev; // 直前の経由ノード
        int via; // 直前の経由エッジ
        HashMap<Integer, Double> edges; // エッジ: toノード, 重み

        public Node(int id, int d) {
            this.id = id;
            this.d = d;
            edges = new HashMap<>();
        }

        void addEdge(int t, double w) {
            edges.put(t, w);
        }

        public int compareTo(Node x) {
            return (int) (d - x.d);
        }
    }
}
