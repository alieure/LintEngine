
import java.util.ArrayList;

/**
 *

 */
public class Block {
	protected String BlockName;
	protected String BlockType;
	protected ArrayList<Variable> vars;
	protected ArrayList<Block> subBlocks;
	protected ArrayList<AssignmentStatement> assignments;
	protected Block parent;
	protected int LineNumber;

	Block() {
		BlockType = "";
		BlockName = "";
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		LineNumber = Parser.currentLineNumber;

	}

	Block(String type, Block comesFrom) {
		BlockType = type;
		BlockName = "";
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		LineNumber = Parser.currentLineNumber;
	}

	Block(String type, Block comesFrom, String name) {
		BlockType = type;
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		LineNumber = Parser.currentLineNumber;
	}

	public void addVariable(Variable newVariable) {
		vars.add(newVariable);
		// sortVariables();
	}

	public void addSubBlock(Block newSubBlock) {
		subBlocks.add(newSubBlock);
	}

	public void addAssignment(AssignmentStatement newAssignment) {
		assignments.add(newAssignment);
	}

	public void sortVariables() {
		int i = 0, j = 0;
		Variable temp;
		for (i = 0; i < vars.size(); i++) {
			for (j = 0; j < vars.size() - (i + 1); j++) {
				if (vars.get(j).compareVariable(vars.get(j + 1)) == 1) {
					temp = vars.get(j);
					vars.set(j, vars.get(j + 1));
					vars.set(j + 1, temp);
				}
			}
		}
	}

	protected Variable findVariableInParentBlockHierarchy(String varToFind) {
		int i = 0;
		Block checker = this;
		do {
			for (i = 0; i < checker.vars.size(); i++) {
				if (checker.vars.get(i).getName().equals(varToFind))
					if (checker.vars.get(i).getClass() != TaskCall.class
							&& checker.vars.get(i).getClass() != FunctionCall.class) {
						return checker.vars.get(i);
					}
			}
			checker = checker.parent;
		} while (checker != null);
		return null;
	}

	protected ArrayList<Variable> findVectorNameInParentBlockHierarchy(String varToFind) {
		int i = 0;
		Block checker = this;
		ArrayList<Variable> vectorVars = new ArrayList();
		do {
			for (i = 0; i < checker.vars.size(); i++) {
				if (checker.vars.get(i).getVectorName().equals(varToFind)) {
					vectorVars.add(checker.vars.get(i));
				}
			}
			checker = checker.parent;
		} while (checker != null);
		return vectorVars;
	}

	public ArrayList<Variable> getAllVariables() {
		ArrayList<Variable> collection = new ArrayList();
		collection.addAll(vars);
		for (int i = 0; i < subBlocks.size(); i++) {
			collection.addAll(subBlocks.get(i).getAllVariables());
		}

		ArrayList<Variable> cleanCollection = new ArrayList();
		for (int i = 0; i < collection.size(); i++) {
			if (collection.get(i).getClass() != TaskCall.class && collection.get(i).getClass() != FunctionCall.class) {
				cleanCollection.add(collection.get(i));
			}
		}
		return cleanCollection;
	}

	public ArrayList<Block> getAllBlocks() {
		ArrayList<Block> collection = new ArrayList();
		collection.addAll(subBlocks);
		for (int i = 0; i < subBlocks.size(); i++) {
			collection.addAll(subBlocks.get(i).getAllBlocks());
		}
		return collection;
	}

	public ArrayList<AssignmentStatement> getAllAssignmentStatements() {
		ArrayList<AssignmentStatement> collection = new ArrayList();
		collection.addAll(assignments);
		for (int i = 0; i < subBlocks.size(); i++) {
			collection.addAll(subBlocks.get(i).getAllAssignmentStatements());
		}
		return collection;
	}

	public ArrayList<AssignmentStatement> getBlockAssignmentStatements() {
		return assignments;
	}

	public int getBlockLineNumber() {
		return LineNumber;
	}

	/*
	@Override
	public String toString() {
		int i = 0;
		String retVar = BlockType + " " + BlockName + " ";

		for (i = 0; i < vars.size(); i++) {
		}
		for (i = 0; i < subBlocks.size(); i++) {
			retVar += subBlocks.get(i).toString();
		}
		for (i = 0; i < assignments.size(); i++) {
		}

		return retVar;
	}  */
}


////////////////////////////


	