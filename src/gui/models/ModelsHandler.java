package gui.models;

public class ModelsHandler {
    private static ModelsHandler modelsHandler;


    private ModelsHandler(){

    }

    public static ModelsHandler getInstance() {
        if (modelsHandler == null) modelsHandler = new ModelsHandler();

        return modelsHandler;
    }
}
