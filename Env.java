import java.util.Hashtable;
import java.util.Vector;

public class Env {
    public static Hashtable<String, String> variables;
    public static Hashtable<String, Integer> params;
    public static Hashtable<String, Vector<String>> body;

    public Env() {
        variables = new Hashtable<>();
        params = new Hashtable<>();
        body = new Hashtable<>();

        // Built-in sqrt function
        params.put("SQRT", 1);

        // Built-in sin function
        params.put("SIN", 1);

        // Built-in cos function
        params.put("COS", 1);

        // Built-in tan function
        params.put("TAN", 1);
    }
}
