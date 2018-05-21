import java.util.HashMap;
import java.util.Map;



public class Node implements Comparable<Node>{

    private String name;
    private Node left;
    private Node right;
    private String catagory0;
    private String catagory1;
    private Map<Boolean, Integer> category0Count = new HashMap<Boolean, Integer>();
    private Map<Boolean, Integer> category1Count = new HashMap<Boolean, Integer>();
    private double probability=0;


    public Node(String name, String catagory0, String catagory1) {
        category0Count.put(true, 0);
        category0Count.put(false, 0);
        category1Count.put(true, 0);
        category1Count.put(false, 0);
        this.catagory0 = catagory0;
        this.catagory1 = catagory1;
        this.name = name;
    }

    public void setLeftNode(Node n) {
        this.left = n;
    }

    public void setRightNode(Node n) {
        this.right = n;
    }

    public Node getLeftNode() {return left;}

    public Node getRightNode() {return right;}

    public double calculateAverageWeightedImpurity() {
        double sumLeft = category0Count.get(true)+ category1Count.get(true);
        double sumRight = category0Count.get(false)+ category1Count.get(false);
        double aLeft = category0Count.get(true)/sumLeft;
        double bLeft = category1Count.get(true)/sumLeft;
        double aRight = category0Count.get(false)/sumRight;
        double bRight = category1Count.get(false)/sumRight;
        double max=sumLeft+sumRight;
        double probA = sumLeft/max;
        double probB= sumRight/max;
        double aImpurity = 2*aLeft*bLeft;
        double bImpurity = 2*aRight*bRight;

        return (probA*aImpurity)+(bImpurity*probB);
    }


    public double calculateTrueImpurity() {
        double a = category0Count.get(true)+ category1Count.get(true);
        return 2*(category0Count.get(true)/a)*(category1Count.get(true)/a);
    }


    public double calculateFalseImpurity() {
        double b = category0Count.get(false)+ category1Count.get(false);
        return  2*(category0Count.get(false)/b)*(category1Count.get(false)/b);
    }


    public void add(boolean b, String s) {
        if ((b == true || b == false) && s.equals(catagory0)) {
            category1Count.put(b, category1Count.get(b) + 1);
        } else if ((b == true || b == false) && s.equals(catagory1))
            category0Count.put(b, category0Count.get(b) + 1);
        else {
            System.out.println("Unknown catagory");
        }
    }


    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Node arg0) {
        Node a=  arg0;
        if(a.calculateAverageWeightedImpurity()>this.calculateAverageWeightedImpurity())
            return -1;
        else if(a.calculateAverageWeightedImpurity()<this.calculateAverageWeightedImpurity())
            return 1;
        else
            return 0;
    }


    public String getFalseMajority() {
        if(category0Count.get(false)< category1Count.get(false))
            return "right";
        else
            return "left";

    }


    public String getTrueMajority() {
        if(category0Count.get(true)< category1Count.get(true))
            return "right";
        else
            return "left";
    }

    public double catagoryOneSideOnFalse() {
        return category0Count.get(false);
    }
    public double catagoryTwoSideOnFalse() {
        return category1Count.get(false);
    }
    public double catagoryOneSideOnTrue() {
        return category0Count.get(true);
    }
    public double catagoryTwoSideOnTrue() {
        return category1Count.get(true);
    }

    public void setProbability(double d) {
        this.probability=d;
    }

    public double getProbability(){
        return probability;
    }
}