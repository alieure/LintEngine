/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author cloud9
 */

public class ModuleInstantiation extends Variable {
	private String moduleText;
	private ArrayList<Variable> externalPortVariables;
	private ArrayList<Variable> internalPortVariables;

	ModuleInstantiation() {
		name = "";
		variableType = "";
		moduleText = "";
		externalPortVariables = new ArrayList();
		internalPortVariables = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	ModuleInstantiation(String moduleTextIn, ArrayList<String> subModule, int line) {
		name = "";
		variableType = "";
		moduleText = "";
		externalPortVariables = new ArrayList();
		internalPortVariables = new ArrayList();
		moduleText = moduleTextIn;
		LineNumber = line;
		parseSubModule(subModule);
	}

	private void parseSubModule(ArrayList<String> pieces) {
	}

	@Override
	public String toString() {
		return moduleText + " LINE: " + LineNumber + "\n";
	}

}
