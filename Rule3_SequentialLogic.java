import java.util.ArrayList;

public class Rule3_SequentialLogic {
	public static ArrayList<Display> getDisplayMessage(Parser parser) {
		
		ArrayList<Display> Msg_List = new ArrayList();
		ArrayList<AssignmentStatement> Statement_List;
		ArrayList<Block> blocks = parser.getBlockList();		
		String msg = "All Good!";
		Block Blk;
		String Statement;
		String Sensitivity;		
		
		for (int m = 0; m < blocks.size(); m++) {
			// get the current block
			Blk = blocks.get(m);
		
		if (Blk.getClass() == Always.class) {
			
			Sensitivity = ((Always) Blk).BlockType;
			Statement_List = Blk.getAllAssignmentStatements();

			// check for blocking in a flip flop always
			for (int n = 0; n < Statement_List.size(); n++) {
				Statement = Statement_List.get(n).assignmentText;
				if (!(Statement.contains("<"))) {
					if (Sensitivity.equals("edgeSensitive")) {
						LintEngine.count++;
						// create the error message for the line
						msg = LintEngine.count + ") WARNING!\nBlocking Satement \" " + Statement + 
							  "\" found in Sequential Block on line no. -> " + Statement_List.get(n).LineNumber + "\n";
						System.out.println(msg);
					}
				}
			}
		}
	}
		
		if (msg.equals("All Good!")) {
			System.out.println("No Blocking assignments in Sequential block!\n");
			Display Warning = new Display("No Blocking assignments in Sequential block!\n");
			Msg_List.add(Warning);
		} 
		return Msg_List;
	}
}
