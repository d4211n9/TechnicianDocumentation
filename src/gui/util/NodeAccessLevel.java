package gui.util;

import be.Enum.SystemRole;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeAccessLevel {

    private List<Node> nodes;

    private Map<Node, List<SystemRole>> accessLevel;

    public NodeAccessLevel() {
        nodes = new ArrayList<>();
        accessLevel = new HashMap<>();
    }

    public void addButtonAccessLevel(Node node, List<SystemRole> accessLevel) {
        nodes.add(node);

        this.accessLevel.put(node, accessLevel);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<SystemRole> getAccessLevelsForNode(Node node) {
        return accessLevel.get(node);
    }
}
