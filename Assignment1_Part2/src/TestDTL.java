import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestDTL {

    private int numCategories;
    private int numAtts;
    private List<String> categoryNames;
    private List<String> attNames;
    private List<Instance> allInstances;

    public TestDTL(String testing, String training) {
        readDataFile(testing);
        test(training);
    }

    private void test(String training) {
        DTL d = new DTL(training);
        double correct = 0;
        double incorrect = 0;

        for (int i = 0; i < allInstances.size(); i++) {
            String prediction = d.getPredictedCategory(allInstances.get(i));
            String actualCategory = categoryNames.get(allInstances.get(i).getCategory());
            if (prediction.equals(actualCategory)) {
                correct+=1;
                System.out.println("\n" + "Correctly Predicted category: " + prediction + ". Actual category: " + actualCategory);
            } else{
                incorrect+=1;
                System.out.println("\n" + "Incorrectly Predicted category: " + prediction + ". Actual category: " + actualCategory);

            }

        }
        int total = (int)correct+(int)incorrect;
        System.out.println("\n" + "Success rate: "+100*(correct/allInstances.size()) + " (" + (int)correct + "/" + total + ")");
    }

    private void readDataFile(String fname) {
        System.out.println("Reading data from file " + fname);
        try {
            Scanner din = new Scanner(new File(fname));
            categoryNames = new ArrayList<String>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext();)
                categoryNames.add(s.next());
            numCategories = categoryNames.size();
            System.out.println(numCategories + " categories");

            attNames = new ArrayList<String>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext();)
                attNames.add(s.next());
            numAtts = attNames.size();
            System.out.println(numAtts + " attributes");

            allInstances = readInstances(din);
            din.close();
        } catch (IOException e) {
            throw new RuntimeException("Data File caused IO exception");
        }
    }

    private List<Instance> readInstances(Scanner din) {
        List<Instance> instances = new ArrayList<Instance>();
        String ln;
        while (din.hasNext()) {
            Scanner line = new Scanner(din.nextLine());
            instances.add(new Instance(categoryNames.indexOf(line.next()), line, categoryNames));
        }
        System.out.println("Read " + instances.size() + " instances");
        return instances;
    }

    public static void main(String[] args) {
        new TestDTL(args[0],args[1]);
    }

}
