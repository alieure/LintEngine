
module Vending_Machine(input wire clk, reset, N, D, output wire OK);

	reg [2:0] current; reg [2:0] next;
	
	always@(N or D or current)
	begin
		case(current)
		3'd0:begin
			if(N)
				next=3'd1;
			else if(D)
				next=3'd2;
			else
				next=current;
		end	
		3'd1:begin
			if(N)
				next=3'd2;
			else if(D)
				next=3'd3;
			else
				next=current;
		end
		3'd2:begin
			if(N)
				next=3'd3;
			else if(D)
				next=3'd4;
			else
				next=current;
		end
		3'd3:begin
			if(N)
				next=3'd4;
			else if(D)
				next=3'd0;
			else
				next=current;
		end
		3'd4:begin
			if(N)
				next=3'd0;
			else if(D)
				next=3'd1;
		end
		endcase
	end

	always@(posedge clk or posedge reset)
	begin
		if(reset)
			current=3'd0;
		else 
			current<=next;
		end
	end
	assign OK =
	((current==3'd3)&&D)||((current==3'd4)&&N)||((current==3'd4)&&D);

endmodule