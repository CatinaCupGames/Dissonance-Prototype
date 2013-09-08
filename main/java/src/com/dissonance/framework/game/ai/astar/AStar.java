package com.dissonance.framework.game.ai.astar;

import com.dissonance.framework.game.ai.Position;

import java.util.List;
import java.util.Map;

public final class AStar {

    public static List<Position> findPath(NodeMap map, Position start, Position goal) {
        return map.findPath(start, goal);
    }

    public static List<Position> findPath(int width, int height, Position start, Position goal) {
        return new NodeMap(width, height).findPath(start, goal);
    }

    public static List<Position> findPath(NodeMap map,
                                          Position start, Position goal, List<Position> illegal,
                                          Map<Position, Integer> extraCost) {
        for (Position position : illegal) {
            Node node = map.getNode(position);

            if (node == null) {
                continue;
            }

            node.setReachable(false);
        }

        for (Position position : extraCost.keySet()) {
            Node node = map.getNode(position);

            if (node == null) {
                continue;
            }

            node.setExtraCost(extraCost.get(position));
        }

        return map.findPath(start, goal);
    }

    public static List<Position> findPath(int width, int height,
                                          Position start, Position goal,
                                          List<Position> illegal, Map<Position, Integer> extraCost) {
        return findPath(new NodeMap(width, height), start, goal, illegal, extraCost);
    }
}
