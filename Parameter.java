/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author cloud9
 */
public class Parameter extends Variable {

	private String parameterValue;
	private String nonNumericalValue;

	Parameter(String name_in, String type, String assignment) {
		name = name_in;
		variableType = type;
		variableAttribute = assignment;
		edgeSensitivity = "";
		LineNumber = Parser.currentLineNumber;

		determineParameterValue();
	}

	private void determineParameterValue() {
		parameterValue = Parser.parseNumberFromExpression(variableAttribute);
	}

	public String getParameterValue() {
		return parameterValue;
	}

	@Override
	public String toString() {
		return variableType + " " + name + " = " + parameterValue + "; \\\\LINE:" + LineNumber + "\n";
	}
}
