public class StringUtils {
    public static boolean isNotNullOrEmptyOrOneChar (String str) {
        return str != null && !str.isEmpty() && str.length() > 1;
    }
}
