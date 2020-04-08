package sample.snake.game;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import static org.junit.Assert.*;

public class SnakeBrainTest {
    @Test
    public void TestHash() {
        PriorityQueue<SnakeBrain.Node> set = new PriorityQueue<>(Comparator.comparingDouble(node -> node.getX()));
        set.add(new SnakeBrain.Node(10, 10));
        Assert.assertFalse(set.contains(new SnakeBrain.Node(11, 10)));
        Assert.assertTrue(set.contains(new SnakeBrain.Node(10, 10)));
    }
}