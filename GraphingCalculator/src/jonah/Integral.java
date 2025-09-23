package jonah;
import java.util.ArrayList;

public class Integal extends Formula {
    
    private ArrayList<Object> function;
    private Function equation;
    private double xVal;
    private double lowerBound;
    private double higherBound;

    public Integral(double xVal, ArrayList<Object> function, double lowerBound, double higherBound) {
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
        return sum;
    }

