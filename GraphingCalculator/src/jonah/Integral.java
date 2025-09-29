package jonah;
import java.util.ArrayList;

public class Integral extends Formula {
    
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
        double originalValue = findIntegral(lowerBound, higherBound);
        return integrate(lowerBound, higherBound, originalValue, 1);
    }
    
    public double integrate(double lower, double higher, double originalValue, int depth) {
        double midpoint = (lower + higher)/2;
        double s1 = findIntegral(lower, midpoint);
        double s2 = findIntegral(midpoint, higher);
        if(Math.abs(s1 + s2 - originalValue) < 0.001 | depth > 15) {
            return s1 + s2;
        } else {
            return integrate(lower, midpoint, s1, depth + 1) + integrate(midpoint, higher, s2, depth + 1);
        } 
    }

    private double findIntegral(double lower, double higher) {
        double deltaX = (higher - lower)/6;
        double midpoint = (higher + lower)/2;
        double fa = equation.evaluate(lower, new ArrayList<Object>(function), 0);
        double fmidpoint = 4 * equation.evaluate(midpoint, new ArrayList<Object>(function), 0);
        double fb = equation.evaluate(higher, new ArrayList<Object>(function), 0);
        return deltaX * (fa + fmidpoint + fb);
    }
}
