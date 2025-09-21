package jonah;
import java.util.ArrayList;

public class Product extends Formula {
    
    private ArrayList<Object> function;
    private Function equation;
    private double xVal;
    private double lowerBound;
    private double higherBound;

    public Product(double xVal, ArrayList<Object> function, double lowerBound, double higherBound) {
        this.function = function;
        this.xVal = xVal;
        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
        equation = new Function(function);
    }

    public double evaluate(){
        int lower = (int)(lowerBound);
        int higher = (int)(higherBound);
        double product = 1;
        if((higherBound < lowerBound)) {
            return Double.NaN;
        }
        for(int i = lower; i <= higher; i++) {
            product *= equation.evaluate(xVal, new ArrayList<Object>((ArrayList<Object>)function), i);
        }

        return product;
    }
}
