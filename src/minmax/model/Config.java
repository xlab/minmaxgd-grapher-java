package minmax.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import minmax.Settings;

/**
 *
 * @author Kouprianov Maxim <me@kc.vc> @ SE HSE
 */
public class Config {

    private final ZUinfPoint[] vertex;
    private boolean star;

    public Config(float k, float t) {
        vertex = new ZUinfPoint[]{new ZUinfPoint(k, t)};
    }

    private Config(ArrayList<ZUinfPoint> list) {
        vertex = list.toArray(new ZUinfPoint[]{});
    }

    private Config(ZUinfPoint[] set) {
        vertex = set;
    }

    public Config() {
        vertex = new ZUinfPoint[]{new ZUinfPoint(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY)};
    }

    public Iterable<ZUinfPoint> getVertex() {
        return new Iterable<ZUinfPoint>() {

            @Override
            public Iterator<ZUinfPoint> iterator() {
                return new Iterator<ZUinfPoint>() {

                    int state = 0;

                    @Override
                    public boolean hasNext() {
                        if (state < vertex.length) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public ZUinfPoint next() {
                        return vertex[state++];
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        };
    }

    public ZUinfPoint getVertex(int i)
            throws IllegalArgumentException {
        if (i < vertex.length) {
            return vertex[i];
        } else {
            throw new IllegalArgumentException("Cannot acces to " + i
                    + "th element of array (size: " + vertex.length + ")");
        }
    }

    public int getVertexCount() {
        return vertex.length;
    }

    @Override
    public String toString() {
        String string = "";
        for (ZUinfPoint point : this.getVertex()) {
            string += point.toString() + "\n";
        }

        return string;
    }

    public Config plus(Config b) {
        if (this.getVertexCount() < 1) {
            return b;
        }

        if (b.getVertexCount() < 2) {
            if (b.getVertexCount() < 1) {
                return this;
            }

            ArrayList<ZUinfPoint> set = new ArrayList();
            ZUinfPoint taken = b.getVertex(0);

            for (ZUinfPoint my : this.getVertex()) {
                if (taken.getX() >= my.getX() && taken.getY() <= my.getY()) {
                    taken = null; //useless
                    break;
                }
            }

            for (ZUinfPoint my : this.getVertex()) {
                if (taken != null) {
                    set.add(taken);
                    if (!(taken.getX() < my.getX() && taken.getY() > my.getY())) {
                        my = markUseless(set, my);

                        if (my != null) {
                            set.add(my);
                        }
                    }
                    taken = null;
                } else {
                    my = markUseless(set, my);

                    if (my != null) {
                        set.add(my);
                    }
                }
            }

            Collections.sort(set);
            return new Config(set);
        } else {
            Config c = this;
            for (ZUinfPoint taken : b.getVertex()) {
                c = c.plus(new Config(taken.getX(), taken.getY()));
            }

            return c;
        }
    }

    private ZUinfPoint markUseless(ArrayList<ZUinfPoint> set, ZUinfPoint my) {
        for (ZUinfPoint p : set) {
            if (my.getX() >= p.getX() && my.getY() <= p.getY()) {
                my = null; //useless
                break;
            }
        }
        return my;
    }

    public Config times(Config b) {
        ArrayList<ZUinfPoint> set = new ArrayList();
        for (ZUinfPoint my : this.getVertex()) {
            for (ZUinfPoint taken : b.getVertex()) {
                set.add(new ZUinfPoint(my.getX() + taken.getX(), my.getY() + taken.getY()));
            }
        }
        return new Config(set).plus(new Config());
    }

    public Config power(int power) {
        return new Config(this.powerSet(power));
    }

    public ZUinfPoint[] powerSet(int power) {
        ZUinfPoint[] set = new ZUinfPoint[this.getVertexCount()];

        ZUinfPoint tmp = this.getVertex(0);
        for (int i = 0; i < this.getVertexCount(); tmp = this.getVertex(i), ++i) {
            set[i] = (new ZUinfPoint(tmp.getX() * power, tmp.getY() * power));
        }

        return set;
    }

    public Config star() {
        if (this.isStar()) {
            return this;
        }

        ZUinfPoint[] set = new ZUinfPoint[this.getVertexCount() * Settings.defaultPrecision];
        for (int i = 0, k= 0; i < Settings.defaultPrecision; ++i) {
            final ZUinfPoint[] subset = this.powerSet(i);
            for(int j = 0; j < subset.length; ++j)
            {
                set[k++] = subset[j];
            }
        }
        
        Config e = new Config(set);
        e.setStar(true);
        return e;
    }

    @Override
    protected Object clone() {
        ZUinfPoint[] set = new ZUinfPoint[this.getVertexCount()];
        int i = 0;
        for (ZUinfPoint v : this.getVertex()) {
            set[i++] = (new ZUinfPoint(v.getX(), v.getY()));
        }

        return new Config(set);
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }
}
