/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author cloud9
 */
public class IntegerVariable extends Variable {
	protected int startValue;
	protected String startValueString;

	IntegerVariable(String name_in) {
		name = name_in;
		variableType = "IntegerVariable";
		variableAttribute = "";
		edgeSensitivity = "";
		vectorParentName = "";
		LineNumber = Parser.currentLineNumber;
		variableSign = "";
		arraySize = 0;
		startValue = 0;
		startValueString = "";
	}

	IntegerVariable(ArrayList<String> startVals) {
		name = "";
		variableType = "IntegerVariable";
		variableAttribute = "";
		edgeSensitivity = "";
		vectorParentName = "";
		LineNumber = Parser.currentLineNumber;
		variableSign = "";
		arraySize = 0;
		startValueString = "";

		parseIntegerVariable(startVals);
	}

	private void parseIntegerVariable(ArrayList<String> pieces) {
		int equalsFlag, i;
		for (i = 0, equalsFlag = 0; i < pieces.size(); i++) {
			if (pieces.get(i).equals("signed") || pieces.get(i).equals("unsigned")) {
				variableSign = pieces.get(i);
			} else if (pieces.get(i).equals("=")) {
				equalsFlag = 1;
			} else if (equalsFlag == 0 && Module.checkVariableName(pieces.get(i))) {
				name = pieces.get(i);
			} else if (Parser.isANumber(pieces.get(i)) == 1 && equalsFlag == 1) {
				startValue = Integer.parseInt(Parser.parseNumberFromExpression(pieces.get(i) + " "));
				startValueString = Integer.toString(startValue);
			} else {
				startValue = 0;
				startValueString = pieces.get(i);
			}
		}

	}

	@Override
	public String toString() {
		return variableAttribute + " " + variableType + " " + variableSign + " " + name + " = " + startValueString
				+ "; LINE: " + LineNumber;
	}
}
