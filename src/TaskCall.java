/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author cloud9
 */
public class TaskCall extends Variable {
	String taskCallText;
	ArrayList<String> taskCallElements;
	ArrayList<Variable> parameterVars;

	TaskCall() {
		taskCallText = "";
		taskCallElements = new ArrayList();
		parameterVars = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	TaskCall(String text, ArrayList<String> elements) {
		taskCallText = text;
		taskCallElements = elements;
		parameterVars = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	@Override
	public String toString() {
		return taskCallText + "; //Task Call LINE: " + LineNumber + "\n";
	}
}
