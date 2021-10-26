//This Mealy FSM design removes 1 form
//every string of 1's on input
module FSM_Mealy(clk, reset, in, out);
input clk, reset, in;
output out;
reg out;
reg state;
reg next_state;
always @(posedge clk)
begin
if (reset) begin
 state = 0;
 end
else begin
 state = next_state;
 end
end
always @(in or state)
begin
out = 0;
next_state = 0;
case (state)
0: begin
if (in) begin
 next_state = 1;
 end
 end
1: begin
if (in) begin
 next_state = 1;
 out = 1;
end
 end
endcase
end
endmodule