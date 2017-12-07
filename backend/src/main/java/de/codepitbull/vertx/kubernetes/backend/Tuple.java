package de.codepitbull.vertx.kubernetes.backend;

public class Tuple<X,Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public static<XX,YY> Tuple<XX,YY> tuple(XX x, YY y) {
        return new Tuple(x,y);
    }
}
