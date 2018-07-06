public class Functions {
    public static String add(String a, String b) {
        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
    }

    public static String subtract(String a, String b) {
        return String.valueOf(Integer.parseInt(a) - Integer.parseInt(b));
    }

    public static String multiply(String a, String b) {
        return String.valueOf(Integer.parseInt(a) * Integer.parseInt(b));
    }

    public static String divide(String a, String b) {
        return String.valueOf(Integer.parseInt(a) / Integer.parseInt(b));
    }
}
