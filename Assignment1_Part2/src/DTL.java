import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DTL {

    private int numCategories;
    private int numAttributes;
    private List<String> categoryNames;
    private List<String> attributeNames;
    private List<Instances> nodeInstances;
    private List<Node> nodes;
    private Node category0;
    private Node category1;
    private Node root;
    private Node baseLine;
    int count = 0;



    public static void main(String[] args) {
            String trainingSetFile = args[0];
            new DTL(trainingSetFile);
    }

    public DTL(String fileOne) {
        readDataFile(fileOne);
        category0 = new Node(categoryNames.get(0), categoryNames.get(0), categoryNames.get(1));
        category1 = new Node(categoryNames.get(1), categoryNames.get(0), categoryNames.get(1));
        baseLine = getBaseLine();
        nodes = new ArrayList<>();
        printNodeOutput(); //displays node calculations such as cat1/cat2 ratio, Impurity etc
        Collections.sort(nodes);
        System.out.println( "\n Display Node Impurity Levels \n");
        for (Node n : nodes) {
            System.out.println( "=> " + n.getName() + " \n Average Weighted Impurity =  " + n.calculateAverageWeightedImpurity() + " | ImpurityBSide: " + n.calculateFalseImpurity() + " | ImpurityLSide: " + n.calculateTrueImpurity());
        }
        root = buildDT(root);
       // root = buildBaseLineDT(root);

        System.out.println("\nDecision Tree: \n");
        displayLeftNonLeafNodes(" ", root); // displays the tree
    }


    /**
     * Recursively outputs nodes in tree format
     * @param indent
     * @param n
     */
    public void displayLeftNonLeafNodes(String indent, Node n) {

        if (n.getLeftNode() != null) {

            System.out.format("%s%s = True:\n", indent, n.getName());
            displayLeftNonLeafNodes(indent + " ", n.getLeftNode());
        }
        if (n.getRightNode() != null) {
            System.out.format("%s%s = False:\n", indent, n.getName());
            displayLeftNonLeafNodes(indent + " ", n.getRightNode());
        }
        if (n.getLeftNode() == null && n.getRightNode() == null){
            displayLeaves(indent, n);
        }
    }


    // printing a leaf node of a tree
    public void displayLeaves(String indent, Node n) {
        System.out.format("%sClass %s\n", indent, n.getName() + "Prob: " + n.getProbability());
    }


    private void printNodeOutput() {
        for (int i = 0; i < attributeNames.size(); i++) {
            String attributename = attributeNames.get(i);
            Node node = new Node(attributename, categoryNames.get(0), categoryNames.get(1));
            for (int j = 0; j < nodeInstances.size(); j++) {
                node.add(nodeInstances.get(j).instancesList.get(i), categoryNames.get(nodeInstances.get(j).category));
            }
            nodes.add(node);
        }
        //displays node a/b ratios
        for (Node n : nodes) {
            System.out.println(
                    "Attribute: " + n.getName()+ " => " + "True(a) " + categoryNames.get(0) + "/" + categoryNames.get(1) +
                            " Ratio = " + n.catagoryTwoSideOnTrue() + "/" + n.catagoryOneSideOnTrue() + " | False(b) " +
                            categoryNames.get(0) + "/" + categoryNames.get(1) + " Ratio : " + n.catagoryTwoSideOnFalse()
                            + "/" + n.catagoryOneSideOnFalse());
        }
    }

    private Node buildDT(Node node) {
        if (node==null){
            node = nodes.remove(0);
        }
        node.setLeftNode(ATreeNode(node));
        node.setRightNode(BTreeNode(node));
        return node;
    }

    private Node buildBaseLineDT(Node node){
        if (node==null){
            node = nodes.remove(0);
        }
        return getBaseLine();
    }

    //Right Side tree node following algorithm in handout
    private Node BTreeNode(Node n){
        if (n.catagoryOneSideOnFalse() + n.catagoryTwoSideOnFalse() == 0) { //step one
           return getBaseLine(); //return Baseline predictor
        } else if (n.calculateFalseImpurity() == 0) { //step two
            return getFalseMajority(n);
            //return a leaf node containing the name of the class of the instances
            //in the node and probability 1
        } else if (nodes.isEmpty()){ //step four
            //returning majority class of the instances
            return getFalseMajority(n); //step 5
        } else return buildDT(nodes.remove(0));
    }


    //Left Side tree node
    private Node ATreeNode(Node n){
        if (n.catagoryOneSideOnTrue() + n.catagoryTwoSideOnTrue() == 0) {
           return getBaseLine();
        } else if (n.calculateTrueImpurity() == 0){
            return getTrueMajority(n);
            //return a leaf node containing the name of the class of the instances
            //in the node and probability 1
        } else if (nodes.isEmpty()){
            return getTrueMajority(n);
        } else {
            return buildDT(nodes.remove(0));
        }
    }

    private Node getBaseLine(){
        int catagoryZero = 0;
        int catagoryOne = 0;
        for (Instances i: nodeInstances){
            if (i.getCategory()==0){
                catagoryZero+=1;
            } else {
                catagoryOne+=1;
            }
        }
        if (catagoryZero>catagoryOne){
            Node n = new Node(categoryNames.get(0), categoryNames.get(0), categoryNames.get(1));
            n.setProbability(catagoryZero / (catagoryZero+catagoryOne));
            return n;
        } else{
            Node n = new Node(categoryNames.get(1), categoryNames.get(0), categoryNames.get(1));
            n.setProbability(catagoryOne/(catagoryZero+catagoryOne));
            return n;
        }
    }


    private Node getTrueMajority(Node n) {
        if (n.getTrueMajority().equals("left")) {
            Node node = new Node(categoryNames.get(0), categoryNames.get(0), categoryNames.get(1));
            node.setProbability(n.catagoryTwoSideOnTrue() / (n.catagoryOneSideOnTrue()+n.catagoryTwoSideOnTrue()));
            return node;
        } else {
            Node node = new Node(categoryNames.get(1), categoryNames.get(0), categoryNames.get(1));
            node.setProbability(n.catagoryTwoSideOnTrue() / (n.catagoryOneSideOnTrue()+n.catagoryTwoSideOnTrue()));
            return node;
        }
    }


    private Node getFalseMajority(Node n) {
        if (n.getFalseMajority().equals("right")) {
            Node node = new Node(categoryNames.get(0),  categoryNames.get(0), categoryNames.get(1));
            node.setProbability(n.catagoryTwoSideOnFalse() / (n.catagoryOneSideOnFalse() + n.catagoryTwoSideOnFalse()));
            return node;
        } else {
            Node node = new Node(categoryNames.get(1), categoryNames.get(0), categoryNames.get(1));
            node.setProbability(n.catagoryOneSideOnFalse() / (n.catagoryOneSideOnFalse() + n.catagoryTwoSideOnFalse()));
            return node;
        }
    }


    private List<Instances> readInstances(Scanner scan) {
        List<Instances> instances = new ArrayList<Instances>();
        while (scan.hasNext()) {
            Scanner line = new Scanner(scan.nextLine());
            instances.add(new Instances(line, categoryNames.indexOf(line.next())));
        }
        System.out.println(instances.size() + " instances found \n");
        return instances;
    }


    private void readDataFile(String file) {
        System.out.println("Read in File: " + file );//"\nRead in test File: " + testFile);
        try {
            Scanner scan = new Scanner(new File(file));
            categoryNames = new ArrayList<String>();
            for (Scanner scan2 = new Scanner(scan.nextLine()); scan2.hasNext();)
                categoryNames.add(scan2.next());
            numCategories = categoryNames.size();
            System.out.println("Categories = " + numCategories);
            attributeNames = new ArrayList<String>();
            for (Scanner scan3 = new Scanner(scan.nextLine()); scan3.hasNext();)
                attributeNames.add(scan3.next());
            numAttributes = attributeNames.size();
            System.out.println("Attributes = " + numAttributes);
            nodeInstances = readInstances(scan);
            scan.close();
        } catch (IOException e) {
            throw new RuntimeException("Data File caused IO exception");
        }
    }


    private class Instances {
        private List<Boolean> instancesList;
        private int category;

        public Instances(Scanner s, int catagory) {
            this.category = catagory;
            instancesList = new ArrayList<Boolean>();
            while (s.hasNextBoolean())
                instancesList.add(s.nextBoolean());
        }


        public int getCategory() {
            return category;
        }


        public String toString() {
            StringBuilder ans = new StringBuilder(categoryNames.get(category));
            ans.append(" ");
            for (Boolean tOrF : instancesList)
                ans.append(tOrF ? "true  " : "false ");
            return ans.toString();
        }
    }

    public String getPredictedCategory(Instance instance) {
        Node n = root;
        while (!(n.getLeftNode() == null & n.getRightNode() == null)) {
            String s = n.getName();
            int i = 0;
            for (i = 0; i < nodeInstances.size(); i++) {
                if (attributeNames.get(i).equals(s))
                    break;
            }
            if (instance.getAtt(i) == true)
                n = n.getLeftNode();
            else if (instance.getAtt(i) == false)
                n = n.getRightNode();
        }
        return n.getName();
    }
}