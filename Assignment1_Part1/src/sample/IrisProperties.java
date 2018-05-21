package sample;

/**
 * Created by danielwalker on 28/03/18.
 */
public abstract class IrisProperties {

    private double petalLength;
    private double petalWidth;
    private double sepalLength;
    private double sepalWidth;
    private String name;

    public IrisProperties(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String name){
        this.name = name;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
    }

    public double getPetalLength() {return this.petalLength;}
    public double getPetalWidth() {return this.sepalWidth;}
    public double getSepalLength() {return this.sepalLength;}
    public double getSepalWidth() {return this.sepalWidth;}
    public String getName() {return this.name;}
}
