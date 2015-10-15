package nz.co.doltech.databind.util;

public class ModifierBuilder {
    StringBuilder sb = new StringBuilder();
    boolean empty = true;

    public void append(String s) {
        if (!empty) {
            sb.append(" & ");
        }
        empty = false;
        sb.append(s);
    }

    @Override
    public String toString() {
        String res = sb.toString();
        if (res.isEmpty()) {
            return "0";
        }
        return res;
    }
}