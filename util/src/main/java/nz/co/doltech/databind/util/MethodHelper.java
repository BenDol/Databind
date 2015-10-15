package nz.co.doltech.databind.util;

public class MethodHelper {

    public static final String[] GETTER_PREFIXES = new String[]{
        "get", "is"
    };

    public static final String[] SETTER_PREFIXES = new String[]{
        "set"
    };

    public static boolean isGetter(String methodName) {
        return hasPrefix(methodName, GETTER_PREFIXES);
    }

    public static boolean isSetter(String methodName) {
        return hasPrefix(methodName, SETTER_PREFIXES);
    }

    public static boolean isGetterOrSetter(String methodName) {
        return isGetter(methodName) || isSetter(methodName);
    }

    public static String stripSetterOrGetterPrefix(String methodName) {
        for (String prefix : MethodHelper.GETTER_PREFIXES) {
            if (methodName.length() > prefix.length()) {
                if(methodName.startsWith(prefix) && Character.isUpperCase(methodName.charAt(prefix.length()))) {
                    return StringUtils.lowerFirstLetter(methodName.substring(prefix.length()));
                }
            }
        }

        for (String prefix : MethodHelper.SETTER_PREFIXES) {
            if (methodName.length() > prefix.length()) {
                if(methodName.startsWith(prefix) && Character.isUpperCase(methodName.charAt(prefix.length()))) {
                    return StringUtils.lowerFirstLetter(methodName.substring(prefix.length()));
                }
            }
        }
        return methodName;
    }

    public static boolean hasPrefix(String str, String[] prefixes) {
        for (String prefix : prefixes) {
            if (str.length() > prefix.length()) {
                if(str.startsWith(prefix) && Character.isUpperCase(str.charAt(prefix.length()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
