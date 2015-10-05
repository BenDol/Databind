package nz.co.doltech.databinder.util;

public interface Func2<P1, P2, R> {
    R exec(P1 param1, P2 param2);
}
