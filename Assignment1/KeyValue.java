import java.util.StringTokenizer;

public class KeyValue {
    private String key;
    private String value;

    public KeyValue(String line) {
        StringTokenizer st = new StringTokenizer(line, "=");
        key = st.nextToken();
        value = st.nextToken();
    }
    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    String getKey() { return key; }
    String getValue() { return value; }
}