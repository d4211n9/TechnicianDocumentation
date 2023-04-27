package gui.util;

import be.Enum.SystemRole;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonAccessLevel {

    private List<Button> buttons;

    private Map<Button, List<SystemRole>> accessLevel;

    public ButtonAccessLevel() {
        buttons = new ArrayList<>();
        accessLevel = new HashMap<>();
    }

    public void addButtonAccessLevel(Button button, List<SystemRole> accessLevel) {
        buttons.add(button);

        this.accessLevel.put(button, accessLevel);
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public List<SystemRole> getAccessLevelsForButton(Button button) {
        return accessLevel.get(button);
    }
}
