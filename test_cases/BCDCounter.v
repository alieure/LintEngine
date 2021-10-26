module BCD_Up_counter(
 input Clock, input Clear, input up,
 output [3:0] out );
reg [3:0] BCD;
always @(posedge Clock) begin
 if (Clear) begin
 BCD <= 0;
 end
end
always @(posedge up)
begin
 if (BCD == 4'b1001) begin
 BCD <= 0;
 else
 BCD <= BCD + 1;
 end
end
assign out = BCD;
endmodule
