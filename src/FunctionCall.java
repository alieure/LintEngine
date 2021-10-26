/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author cloud9
 */
public class FunctionCall extends Variable {
	String functionCallText;
	ArrayList<String> functionCallElements;
	ArrayList<Variable> parameterVars;

	FunctionCall() {
		functionCallText = "";
		functionCallElements = new ArrayList();
		parameterVars = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	FunctionCall(String text, ArrayList<String> elements) {
		functionCallText = text;
		functionCallElements = elements;
		parameterVars = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	@Override
	public String toString() {
		return functionCallText + " //Function Call LINE: " + LineNumber + "\n";
	}
}
