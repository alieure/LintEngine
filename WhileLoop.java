
import java.util.ArrayList;

public class WhileLoop extends Block {
	private ArrayList<String> whileLoopBody;
	private ArrayList<String> whileLoopHead;

	WhileLoop(String type, Block comesFrom, String name) {
		BlockType = type;
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		whileLoopBody = new ArrayList();
		whileLoopHead = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	public static void parseWhileLoop(Block current, Parser parser) {
		String temp = parser.getNextPiece(); // Should be equal to "("
		String lastType = "DEFAULT_NET_TYPE";
		String lastAttribute = "";
		WhileLoop whileLoop = new WhileLoop("whileLoop", current, temp);
		current.addSubBlock(whileLoop);
		for (; !temp.equals(")") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			whileLoop.whileLoopHead.add(temp);
		}
		whileLoop.whileLoopHead.add(temp);
		temp = parser.getNextPiece();
		whileLoop.whileLoopBody.add(temp);
		if (temp.equals("begin")) {
			temp = parser.getNextPiece();
			for (int endCount = 0; (!temp.equals("end") || endCount != 0)
					&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				whileLoop.whileLoopBody.add(temp);
				if (temp.equals("begin")) {
					endCount++;
				} else if (temp.equals("end")) {
					endCount--;
				}
			}
			whileLoop.whileLoopBody.add(temp);
		} else {
			temp = parser.getNextPiece();
			// if the while loop is not wrapped in begin/end it is assumed the
			// loop contains
			// only a single assignment statement, and not sub-blocks, this will
			// need to
			// be changed in the future if for loops are going to be
			// legitimately used
			for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				whileLoop.whileLoopBody.add(temp);
			}
			whileLoop.whileLoopBody.add(";");
		}
	}

	@Override
	public String toString() {
		ArrayList<String> whileLoopText = whileLoopHead;
		whileLoopText.addAll(whileLoopBody);
		String exit = "while ";
		for (int i = 0; i < whileLoopText.size(); i++) {
			exit += whileLoopText.get(i) + " ";
			if (whileLoopText.get(i).equals("begin") || whileLoopText.get(i).equals("end")
					|| whileLoopText.get(i).equals(";")) {
				exit += "\n";
			}
		}
		return exit + "\n";
	}

}
