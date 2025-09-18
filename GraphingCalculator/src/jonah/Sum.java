package jonah;
import java.util.ArrayList;

public class Sum extends Formula {
    
    private ArrayList<Object> function;
    private Function equation;
    private double xVal;
    private double lowerBound;
    private double higherBound;

    public Sum(double xVal, ArrayList<Object> function, double lowerBound, double higherBound) {
        this.function = function;
        this.xVal = xVal;
        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
        equation = new Function(function);
    }

    public double evaluate(){
        int lower = (int)(lowerBound);
        int higher = (int)(higherBound);
        double sum = 0;
        if((higherBound < lowerBound)) {
            return Double.NaN;
        }
        for(int i = lower; i <= higher; i++) {
            sum += equation.evaluate(xVal, new ArrayList<Object>((ArrayList<Object>)function), i);
        }

        return sum;
    }
}
