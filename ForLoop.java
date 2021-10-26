/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author cloud9
 */
public class ForLoop extends Block {
	private ArrayList<String> forLoopBody;
	private ArrayList<String> whileLoopHead;

	ForLoop(String type, Block comesFrom, String name) {
		BlockType = type;
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		forLoopBody = new ArrayList();
		whileLoopHead = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	public static void parseForLoop(Block current, Parser parser) {
		String temp = parser.getNextPiece(); // Should be equal to "("
		String lastType = "DEFAULT_NET_TYPE";
		String lastAttribute = "";
		ForLoop forLoop = new ForLoop("forLoop", current, temp);
		current.addSubBlock(forLoop);
		for (; !temp.equals(")") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			forLoop.whileLoopHead.add(temp);
		}
		forLoop.whileLoopHead.add(temp);
		temp = parser.getNextPiece();
		forLoop.forLoopBody.add(temp);
		if (temp.equals("begin")) {
			temp = parser.getNextPiece();
			for (int endCount = 0; (!temp.equals("end") || endCount != 0)
					&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				forLoop.forLoopBody.add(temp);
				if (temp.equals("begin")) {
					endCount++;
				} else if (temp.equals("end")) {
					endCount--;
				}
			}
			forLoop.forLoopBody.add(temp);
		} else {
			temp = parser.getNextPiece();
			// if the for loop is not wrapped in begin/end it is assumed the
			// loop contains
			// only a single assignment statement, and not sub-blocks, this will
			// need to
			// be changed in the future if for loops are going to be
			// legitimately used
			for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				forLoop.forLoopBody.add(temp);
			}
			forLoop.forLoopBody.add(";");
		}
	}

	@Override
	public String toString() {
		ArrayList<String> forLoopText = whileLoopHead;
		forLoopText.addAll(forLoopBody);
		String exit = "for( ";
		for (int i = 0; i < forLoopText.size(); i++) {
			exit += forLoopText.get(i) + " ";
			if (forLoopText.get(i).equals("begin") || forLoopText.get(i).equals("end")
					|| forLoopText.get(i).equals(";")) {
				exit += "\n";
			}
		}
		return exit + "\n";
	}

}
