package jonah;
import java.util.ArrayList;

public class Factorial extends Formula {
    
    private ArrayList<Object> function;
    private Function equation;
    private double xVal;

    public Factorial(ArrayList<Object> function, double xVal) {
        this.function = function;
        this.xVal = xVal;
        equation = new Function(function);
    }

    public double evaluate(){
        double equationValue = (equation.evaluate(xVal, new ArrayList<Object>((ArrayList<Object>)function), 0));
        double product = 1;
        if((equationValue < 0)) {
            return Double.NaN;
        }
        int higherBound = (int)equationValue;
        for(int i = 0; i < higherBound; i++) {
            product *= higherBound - i;
        }

        return product;
    }
}
