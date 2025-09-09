package jonah;

import java.util.ArrayList;

public class Function {
	private ArrayList<Object> equation;
	public Function(ArrayList<Object> function) {
		equation = function;
	}
	public double evaluate(double x, ArrayList<Object> formula)  {
		String tempval;
		for(int i = 0; i < formula.size(); i++) {
			if((formula.get(i) + "").equals("x")) {
				formula.set(i, (x));
			}
			if((formula.get(i) + "").equals("pi")) {
				formula.set(i, Math.PI);
			}
			if((formula.get(i) + "").equals("e")) {
				formula.set(i, Math.E);
			}
			tempval = "" + formula.get(i);
			char c = tempval.charAt(0);
			if(c == 's' | c == 'c' | c == 't' | c == 'a') {
				double temporaryResult = Operations(tempval, 0, evaluate(x, new ArrayList<Object>((ArrayList<Object>)formula.get(i + 1))));
				formula.set(i, temporaryResult);
				formula.remove(i + 1);
			}
			if(tempval.length() > 1 && c == '[') {
				double tempres = evaluate(x, new ArrayList<Object>((ArrayList<Object>)formula.get(i)));
				formula.set(i, tempres);
			}
		}
		for(int i = 0; i < formula.size(); i++) {
			tempval = "" + formula.get(i);
			char c = tempval.charAt(0);
			if(c == '-') {
				try {
					double isNumberTest = (double)formula.get(i);
				} catch(ClassCastException e) {
					boolean isNegative = false;
					if(i == 0) {
						isNegative = true;
					} else {
						try {
							double isNumberTest = (double)formula.get(i - 1);
						} catch (ClassCastException f) {
							isNegative = true;
						}
					}
					if(isNegative && formula.size() > 1) {
						formula.set(i, -(double)formula.get(i + 1));
						formula.remove(i + 1);
					}
				}
			}
		}
		int operationOrder = 0;
		while(formula.size() > 1) {
			for(int i = 1; i < formula.size(); i += 2) {
				tempval = "" + formula.get(i);
				char c = tempval.charAt(0);
				double tempresult = 0;
				boolean changeArray = false;
				if(operationOrder == 0 && c == '^') {
					tempresult = Operations(tempval, (double)formula.get(i - 1), (double)formula.get(i + 1));
					changeArray = true;
				}
				if(operationOrder == 1 && (c == '/' | c == '*')) {
					tempresult = Operations(tempval, (double)formula.get(i - 1), (double)formula.get(i + 1));
					changeArray = true;
				}
				if(operationOrder == 2 && (c == '+' | c == '-')) {
					tempresult = Operations(tempval, (double)formula.get(i - 1), (double)formula.get(i + 1));
					changeArray = true;
				}
				if(changeArray) {
					formula.set(i - 1, tempresult);
					formula.remove(i);
					formula.remove(i);
					i -= 2;
				}
			}
			operationOrder++;
		}
		return (double)formula.get(0);
	}
	
	public double[] findYValues(double lowerX, double higherX) {
		double[] YValues = new double[601];
		double range = higherX - lowerX;
		double xVal = lowerX;
		for(int x = 0; x <= 600; x += 1) {
			YValues[x] = evaluate(xVal, new ArrayList<Object>(equation));
			xVal += range/600;
		}
		return YValues;
	}
	
	private double Operations(String operator, double result, double operand) {
		double res = result;
		
		if(operator.equals("+")) {
			res += operand;
		} else if(operator.equals("-")) {
			res -= operand;
		} else if(operator.equals("*")) {
			res *= operand;
		} else if(operator.equals("/")) {
			res /= operand;
		} else if(operator.equals("^")) {
			res = Math.pow(res, operand);
		} else if(operator.equals("sin")) {
			res = Math.sin(operand);
		} else if(operator.equals("cos")) {
			res = Math.cos(operand);
		} else if(operator.equals("tan")) {
			res = Math.tan(operand);
		} else if(operator.equals("arcsin")) {
			res = Math.asin(operand);
		} else if(operator.equals("arccos")) {
			res = Math.acos(operand);
		} else if(operator.equals("arctan")) {
			res = Math.atan(operand);
		} else if(operator.equals("arcsec")) {
			res = Math.acos(1/operand);
		} else if(operator.equals("arccsc")) {
			res = Math.asin(1/operand);
		} else if(operator.equals("arccot")) {
			res = Math.atan(1/operand);
		} else if(operator.equals("csc")) {
			res = 1/Math.sin(operand);
		} else if(operator.equals("sec")) {
			res = 1/Math.cos(operand);
		} else if(operator.equals("cot")) {
			res = 1/Math.tan(operand);
		} else if(operator.equals("abs")) {
			res = Math.abs(operand);
		}
		
		return res;
	}
}
