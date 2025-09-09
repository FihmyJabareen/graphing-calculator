package jonah;

import java.util.ArrayList;

public class Parser {
	private String function;
	public Parser(String formula) {
		function = formula;
	}
	public ArrayList<Object> ParseFunction(String function) {
		String State = "ST";
		ArrayList<Object> operation = new ArrayList<Object>();
		
		int pointer = 0;
		
		while(pointer < function.length()) {
			char tempval = function.charAt(pointer);
			if ((tempval > 47 && tempval < 58) | tempval == 120) {
				operation.add("" + tempval);
			}
			if (tempval == 43) {
				operation.add("" + tempval);
			}
			if (tempval == '(') {
				operation.add(ParseFunction(function.substring(pointer + 1)));
			}
			pointer++;
		}
		
		System.out.println(operation);
		return operation;
	}
}
