/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;

/**
 * This class is to be used for conditional statements used in "if" and
 * "else if",
 */

public class ConditionStatement extends AssignmentStatement {
	ArrayList<Variable> conditionVars;

	ConditionStatement(String rawText, Block blockSource, Parser parser) {
		assignmentText = rawText;
		conditionVars = new ArrayList();
		LineNumber = Parser.currentLineNumber;
		parent = blockSource;

		identifyConditionVariables(parser);
	}

	private void identifyConditionVariables(Parser parser) {
		String preserve = assignmentText;
		String temp = "";
		Variable var;
		for (temp = super.getNextPiece(); !temp.equals("##END_OF_STATEMENT"); temp = super.getNextPiece()) {
			if (temp.equals("$#")) {
				temp = getNextPiece();
				parser.setLineNumber(Integer.parseInt(temp));
			} else if (parent.findVariableInParentBlockHierarchy(temp) != null
					|| !parent.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
				// conditionVars.add(parent.findVariableInParentBlockHierarchy(temp));
				temp = Variable.safelyParseVariableForAssignmentStatement(parser, parent, this, temp, conditionVars,
						conditionVars);
			} else {
				// conditionVars.addAll(parent.findVectorNameInParentBlockHierarchy(temp));
				// temp = Variable.safelyParseVariableForAssignmentStatement(
				// parser, parent, this,temp, conditionVars,conditionVars);
			}
		}
		assignmentText = preserve;
	}

	@Override
	public String toString() {
		return assignmentText + "";
	}

}
