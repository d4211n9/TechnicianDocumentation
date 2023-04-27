package gui.util;

import be.Enum.SystemRole;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonAccessLevel {

    private List<Button> buttons;

    private Map<Button, List<SystemRole>> accessLevel;

    public ButtonAccessLevel(List<Button> buttons, List<List<SystemRole>> accessLevel) {
        this.buttons = buttons;
        this.accessLevel = new HashMap<>();

        for (int i = 0; i < buttons.size(); i++) {
            for (SystemRole systemRole: accessLevel.get(i)) {
                this.accessLevel.put(buttons.get(i), accessLevel.get(i));
            }
        }
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public List<SystemRole> getAccessLevelsForButton(Button button) {
        return accessLevel.get(button);
    }
}
