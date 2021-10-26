/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;

/**
 *

 */
public class AssignmentStatement {
	// LHSvars needs to be an ArrayList in case of the presence of vectors
	protected ArrayList<Variable> LHSvars;
	protected ArrayList<Variable> RHSvars;
	protected String assignmentText;
	Block parent;
	protected int LineNumber;

	AssignmentStatement() {
		assignmentText = "";
		LHSvars = new ArrayList();
		RHSvars = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	AssignmentStatement(String rawText, Parser parser) {
		assignmentText = rawText;
		LHSvars = new ArrayList();
		RHSvars = new ArrayList();
		LineNumber = Parser.currentLineNumber;

		identifyLHSvariables(parser);
	}

	AssignmentStatement(String rawText, Block blockSource, Parser parser) {
		assignmentText = rawText;
		LHSvars = new ArrayList();
		RHSvars = new ArrayList();
		parent = blockSource;
		LineNumber = Parser.currentLineNumber;

		//System.out.println(LineNumber);

		identifyLHSvariables(parser);
		identifyRHSvariables(parser);
	}

	protected void identifyLHSvariables(Parser parser) {
		String preserve = assignmentText;
		String temp = getNextPiece();
		Variable tempVar = null;
		for (; !temp.equals("=") && !temp.equals("<") && !temp.equals("##END_OF_STATEMENT");) {
			if (temp.equals("$#")) {
				temp = getNextPiece();
				parser.setLineNumber(Integer.parseInt(temp));
			} else {
				tempVar = parent.findVariableInParentBlockHierarchy(temp);
				if (tempVar != null) {
					// LHSvars.add(tempVar);
					temp = Variable.safelyParseVariableForAssignmentStatement(parser, parent, this, temp, LHSvars,
							RHSvars);
				} else if (parent.findVectorNameInParentBlockHierarchy(temp).size() > 0) {
					// LHSvars.addAll(parent.findVectorNameInParentBlockHierarchy(temp));
					temp = Variable.safelyParseVariableForAssignmentStatement(parser, parent, this, temp, LHSvars,
							RHSvars);

				} else {

				}
			}
			if (!temp.equals("=")) {
				temp = getNextPiece();
			}
		}
		assignmentText = preserve;
	}

	protected void identifyRHSvariables(Parser parser) {
		String preserve = assignmentText;
		assignmentText = assignmentText.substring(assignmentText.indexOf("="));
		String temp = getNextPiece();
		Variable tempVar = null;
		for (temp = getNextPiece(); !temp.equals("##END_OF_STATEMENT");) {
			if (temp.equals("$#")) {
				temp = getNextPiece();
				parser.setLineNumber(Integer.parseInt(temp));
			} else {
				tempVar = parent.findVariableInParentBlockHierarchy(temp);
				if (tempVar != null) {
					// LHSvars.add(tempVar);
					Variable.safelyParseVariableForAssignmentStatement(parser, parent, this, temp, RHSvars, RHSvars);
				} else if (parent.findVectorNameInParentBlockHierarchy(temp).size() > 0) {
					// LHSvars.addAll(parent.findVectorNameInParentBlockHierarchy(temp));
					Variable.safelyParseVariableForAssignmentStatement(parser, parent, this, temp, RHSvars, RHSvars);

				} else {

				}
			}
			if (!temp.equals("$#")) {
				temp = getNextPiece();
			}
		}
		assignmentText = preserve;
	}

	protected String getNextPiece() {
		String[] piece = assignmentText.split(" ", 2);
		if (piece.length != 1)
			assignmentText = piece[1];
		while (piece[0].equals("")) {
			piece = assignmentText.split(" ", 2);
			if (piece.length != 1) {
				assignmentText = piece[1];
			} else {
				piece[0] = "##END_OF_STATEMENT";
			}

		}
		return piece[0];
	}

	public ArrayList<Variable> getLHSvars() {
		return LHSvars;
	}

	public ArrayList<Variable> getRHSvars() {
		return RHSvars;
	}

	public static boolean expressionPiece(String piece) {
		if (piece.equals("=")) {
			return true;
		} else if (piece.equals("!")) {
			return true;
		} else if (piece.equals("=")) {
			return true;
		} else if (piece.equals("~")) {
			return true;
		} else if (piece.equals("&")) {
			return true;
		} else if (piece.equals("|")) {
			return true;
		} else if (piece.equals("^")) {
			return true;
		} else if (piece.equals("%")) {
			return true;
		} else if (piece.equals("*")) {
			return true;
		} else if (piece.equals("+")) {
			return true;
		} else if (piece.equals("-")) {
			return true;
		} else if (piece.equals("/")) {
			return true;
		} else if (piece.equals("(")) {
			return true;
		} else if (piece.equals(")")) {
			return true;
		} else if (piece.equals("[")) {
			return true;
		} else if (piece.equals("]")) {
			return true;
		} else if (piece.equals("<")) {
			return true;
		} else if (piece.equals(">")) {
			return true;
		} else {
			return false;
		}
	}

	public int getAssignmentStatementLineNumber() {
		return LineNumber;
	}

	@Override
	public String toString() {
		String text = "";
		text += assignmentText + ";\\\\ ";
		text += "LHSvars: " + LHSvars.toString();
		text += ",   RHSvars: " + RHSvars.toString() + " LINE: " + LineNumber + "\n";
		return text;
	}
}
