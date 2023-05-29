package be.Enum;

public enum ClientType {
    Consumer("B2C"),
    Business("B2B"),
    Government("B2G");

    private final String type;
    ClientType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ClientType getClientType(String type) {
        if (type.equalsIgnoreCase(Consumer.getType())) return Consumer;

        if (type.equalsIgnoreCase(Business.getType())) return Business;

        if(type.equalsIgnoreCase(Government.getType())) return Government;

        return null;
    }
}
