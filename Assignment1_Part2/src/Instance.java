import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Instance {



    private List<String> categoryNames;
    public List<Boolean> vals;
    public int category;

    public Instance(int catagory, Scanner scan, List<String> catagoryNames) {
        this.categoryNames=catagoryNames;
        category = catagory;
        vals = new ArrayList<Boolean>();
        while (scan.hasNextBoolean())
            vals.add(scan.nextBoolean());
    }

    public int getCategory() {
        return category;
    }

    public boolean getAtt(int index) {
        return vals.get(index);
    }

    public String toString() {
        StringBuilder ans = new StringBuilder(categoryNames.get(category));
        ans.append(" ");
        for (Boolean val : vals)
            ans.append(val ? "true  " : "false ");
        return ans.toString();
    }
}
