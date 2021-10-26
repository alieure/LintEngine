
import java.util.ArrayList;

// ================== This Class checks for Port Mismatch Rule ================ //

public class Rule1_PortMismatch {

	public static ArrayList<Display> getDisplayMessage(Parser parser) {
		
	    ArrayList<Display> Msg_List = new ArrayList();
		ArrayList<AssignmentStatement> Statement_List;
		ArrayList<Variable> left_var, right_var;
		String msg = "All Good!";
		Block Blk;
		Variable in_var, out_var;
		int found = 0;
		
		if (!parser.getBlockList().isEmpty()) {
			
			Blk = parser.getBlockList().get(0);
			
			Statement_List = Blk.getAllAssignmentStatements();
			
			int m = 0;
			while (m < Statement_List.size()){
				found = 0;
				left_var = Statement_List.get(m).getLHSvars();
			//	System.out.println(m+"\t"+left_var+"\t");
				int n = 0;
				while(n < left_var.size()) {
					
					in_var = left_var.get(n);
					
					if (in_var.variableAttribute.equals("input") && found == 0) {
						LintEngine.count++;
						msg = LintEngine.count + ") WARNING!\nThere is Port Mismatch on line number -> "
								 +Statement_List.get(m).LineNumber+"\nInput signal \'"+left_var.get(n).name
								 + "\' is used on the L.H.S of the assignment.\n";


					//	Display warning = new Display();
					//	warning.setDisplayMsg(msg);
					//	warning.addLineNumber(Statement_List.get(m).LineNumber);
					//	Msg_List.add(warning);
						System.out.println(msg);
						
						found = 1;
					}
					
					n++;
				}

				found = 0;
				right_var = Statement_List.get(m).getRHSvars();
				
				int l =0;
				while ( l < right_var.size()) {
					
					out_var = right_var.get(l);
					
					if (out_var.variableAttribute.equals("output") && found == 0
							&& out_var.getClass() != Reg.class) {
						LintEngine.count++;
						msg = LintEngine.count + ") WARNING!\nThere is Port Mismatch on line number -> "
							 +Statement_List.get(m).LineNumber+"\nOuput signal \'"+right_var.get(l).name
							 + "\' is used on the R.H.S of the assignment.\n";

					//	Display warning = new Display();
					//	warning.setDisplayMsg(msg);
					//	warning.addLineNumber(Statement_List.get(m).LineNumber);
					//	Msg_List.add(warning);
						System.out.println(msg);

						found = 1;
					}
					
					l++;
				}
				
				m++;
			}

			if (msg.equals("All Good!")) {
				System.out.println("No Port Mismatch!\n");
				Display Warning = new Display("No Port Mismatch!");
				Msg_List.add(Warning);
			} 
		}
		return Msg_List;
	}
}

