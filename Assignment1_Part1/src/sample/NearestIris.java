package sample;

/**
 * Created by danielwalker on 30/03/18.
 */
public class NearestIris implements Comparable<NearestIris>{

    private IrisProperties iris;
    private double dist;

    public NearestIris(IrisProperties iris, double dist){
        this.iris = iris;
        this.dist = dist;
    }

    public String getClassType() {
        return this.iris.getName();
    }


    public int compareTo(NearestIris o) {return this.dist > o.dist?1:(this.dist < o.dist?-1:0);}
}
