package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class Main extends Application {


    int numCategories;
    int numAtts;
    List<String> categoryNames;
    List<String> attNames;
    List<Instance> allInstances;
    List<String> data;
    String testData = "/Users/danielwalker/Desktop/COMP307/Assignment 1/src/ass1-data/part1/iris-test.txt";
    String trainingData = "/Users/danielwalker/Desktop/COMP307/Assignment 1/src/ass1-data/part1/iris-training.txt";

    private List<IrisProperties> trainingIris;
    private List<IrisProperties> testIris;

    //numbers required for determining euclidean distance (divide by range)
    private double rangeOfSepalLength;
    private double rangeOfSepalWidth;
    private double rangeOfPetalLength;
    private double rangeOfPetalWidth;
    private List<NearestIris> irisDistances;
    private int k;


    @Override
    public void start(Stage primaryStage) throws Exception{
    }


    private List<Instance> readInstances(Scanner din){
    //instance = class name
        List<Instance> instances = new ArrayList<Instance>();
        while (din.hasNext()){
            Scanner line = new Scanner(din.nextLine());
            instances.add(new Instance(categoryNames.indexOf(line.next()),line));
        }
        System.out.println("Read " + instances.size()+" instances");
        return instances;
    }

    private class Instance {

        private int category;
        private List<Boolean> vals;

        public Instance(int cat, Scanner s){
            category = cat;
            vals = new ArrayList<Boolean>();
            while (s.hasNextBoolean()) vals.add(s.nextBoolean());
        }

        public String toString(){
            StringBuilder ans = new StringBuilder(categoryNames.get(category));
            ans.append(" ");
            for (Boolean val : vals)
                ans.append(val?"true  ":"false ");
            return ans.toString();
        }
    }

    public Main(String fileOne, String fileTwo, int k){
        this.k = k;
        File trainingFile = new File(fileOne);
        File testingFile = new File(fileTwo);
        trainingIris = readIrisData(trainingFile);
        testIris = readIrisData(testingFile);
        findRange(testIris);
        int correct = 0;
        for (IrisProperties iris: testIris){
            String typeOfPlant = classifyPlant(iris);
            System.out.println("Given instance: " + iris.getName() + " ==> Predicted instance: " + typeOfPlant);
            if (typeOfPlant.equals(iris.getName())){
                correct++;
            }
        }
        //calculating correct guesses
        double percentage = ((double)correct/(double)testIris.size())*100;
        System.out.println("KNN Success rate: " + percentage + " ==> " + correct + "/" + testIris.size() + " With a K value = " + this.k);
    }

    public List<IrisProperties> readIrisData (File file) {
        System.out.println("Reading data from file");
        Scanner scanner = null;
        List<IrisProperties> irisData = new ArrayList<IrisProperties>();

        try {
            scanner = new Scanner(file);
            categoryNames = new ArrayList<String>();
            data = new ArrayList<String>();

            for (Scanner s = new Scanner(scanner.nextLine()); s.hasNext(); categoryNames.add(s.next()));
            numCategories=categoryNames.size();
            System.out.println(numCategories+ " categories: " + categoryNames);

            while (scanner.hasNext()){
                for (Scanner s = new Scanner(scanner.nextLine()); s.hasNext(); data.add(s.next()));
            }
            //System.out.print(data+ " ");
            allInstances = readInstances(scanner);
            scanner.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Data File caused IO exception");
        }

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(file)));

            while (scanner.hasNextLine()) {

                double sepalLength = scanner.nextDouble();
                double sepalWidth = scanner.nextDouble();
                double petalLength = scanner.nextDouble();
                double petalWidth = scanner.nextDouble();
                String classFound = scanner.next();
                IrisProperties newIris = Classification.classifyInstance(classFound, sepalLength, sepalWidth, petalLength, petalWidth);
                irisData.add(newIris);
                scanner.nextLine();
            }
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();


        } finally {
            scanner.close(); return irisData;
        }
    }


    public static void main(String[] args) {
        if (args.length > 2){
            String trainingSetFile = args[0];
            String testSetFile = args[1];
            int k = Integer.parseInt(args[2]);
            new Main(trainingSetFile, testSetFile,k);
        } else {
            System.out.println("Invalid Argument Length");
        }
    }

    /**
     * To find the range within the testData Arraylist we find the minimum and max value for each attribute
     * The range is the max-min
     * @param closestIris
     */
    public void findRange(List<IrisProperties> closestIris){
        //determine min and max values
        double minSepalLength = 100;
        double maxSepalLength = 0;

        double minSepalWidth = 100;
        double maxSepalWidth = 0;

        double minPetalLength = 100;
        double maxPetalLength = 0;

        double minPetalWidth = 100;
        double maxPetalWidth = 0;

        //find the min and max for each attr
        for (IrisProperties i: closestIris
             ) { if (i.getSepalLength()<minSepalLength){
                 minSepalLength = i.getSepalLength();
                 }
                 if (i.getSepalLength()>maxSepalLength){
                 maxSepalLength = i.getSepalLength();
                 }

                 if (i.getPetalLength()<minPetalLength){
                     minPetalLength = i.getPetalLength();
                 }
                 if (i.getPetalLength()>maxPetalLength){
                     maxPetalLength = i.getPetalLength();
                 }

                 if (i.getSepalWidth()<minSepalWidth){
                     minSepalWidth = i.getSepalWidth();
                 }
                 if (i.getSepalWidth()>maxSepalWidth){
                     maxSepalWidth = i.getSepalWidth();
                 }

                 if (i.getPetalWidth()<minPetalWidth){
                     minPetalWidth = i.getPetalWidth();
                 }
                 if (i.getPetalWidth()>maxPetalWidth){
                     maxPetalWidth = i.getPetalWidth();
                 }
        }

        //update range
        this.rangeOfSepalLength = maxSepalLength-minSepalLength;
        this.rangeOfPetalLength = maxPetalLength-minPetalLength;
        this.rangeOfSepalWidth = maxSepalWidth-minSepalWidth;
        this.rangeOfPetalWidth = maxSepalWidth-minSepalWidth;

    }

    //uses Euclidean distance alg
    public double eucDistanceMesure(IrisProperties trainingIris, IrisProperties testIris){
        double sLen = Math.pow(trainingIris.getSepalLength() - testIris.getSepalLength(), 2)/rangeOfSepalLength;
        double sWid = Math.pow(trainingIris.getSepalWidth() - testIris.getSepalWidth(), 2)/rangeOfSepalWidth;
        double pLen = Math.pow(trainingIris.getPetalLength() - testIris.getPetalLength(), 2)/rangeOfPetalLength;
        double pWid = Math.pow(trainingIris.getPetalWidth() - testIris.getPetalWidth(), 2)/rangeOfPetalWidth;

        return Math.sqrt(sLen+sWid+pLen+pWid);
    }

    public IrisProperties calculateNearestNeighbor(IrisProperties iris, ArrayList<IrisProperties> list){
         double min=100000;
         IrisProperties fl=null;
        for(IrisProperties i:list){
            double dist=eucDistanceMesure(iris,i);
            if(min>dist){
                min=dist;
                fl=i;
            }
        }
        return fl;
    }
    public String classifyPlant(IrisProperties testIris){
        ArrayList<IrisProperties> clone = new ArrayList(trainingIris);

        int irisSetosa = 0;
        int irisVersicolor = 0;
        int irisVirginica = 0;

        for (int i = 0; i < k; i++) {
            IrisProperties fl = calculateNearestNeighbor(testIris, clone);
            if (fl == null)
                break;
            clone.remove(fl); // wont do this on the same flower over and over
            if (fl.getName().equals("Iris-setosa")) {
                irisSetosa++;
            } else if (fl.getName().equals("Iris-versicolor"))
                irisVersicolor++;
            else if (fl.getName().equals("Iris-virginica"))
                irisVirginica++; //assumes it must be this then
        }

        //which iris has the highest count? return this one
        if (irisSetosa > irisVersicolor && irisSetosa > irisVirginica) {
            return "Iris-setosa";
        } else if (irisVersicolor > irisSetosa && irisVersicolor > irisVirginica) {
            return "Iris-versicolor";
        } else {
            return "Iris-virginica";
        }
    }
}
