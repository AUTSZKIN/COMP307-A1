package sample;

/**
 * Created by danielwalker on 4/04/18.
 */
public class Classification {

    private Classification(){};

    public static IrisProperties classifyInstance(String instance, double sepalLength, double sepalWidth, double petalLength, double petalWidth){
        switch (instance) {
            case "Iris-versicolor":
                return new IrisVersicolor(sepalLength, sepalWidth, petalLength, petalWidth, instance);
            case "Iris-setosa":
                return new IrisSetosa(sepalLength, sepalWidth, petalLength, petalWidth, instance);
            case "Iris-virginica":
                return new IrisViginica(sepalLength, sepalWidth, petalLength, petalWidth, instance);
            default:
                System.out.println("Can not find named instance");
                return null;
        }
    }

}
