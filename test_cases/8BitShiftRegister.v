// 8-bit shift register can clear, load, and left shift input data
module shiftReg (CLK, clr, shift, ld, Din, SI, Dout);
input CLK, clr, shift, ld;
input [7:0] Din;
input SI;
output reg [7:0] Dout;
always @(posedge CLK) begin
if (clr) Dout <= 0;
else if (ld) Dout <= Din;
else if (shift) Dout <= { Dout[6:0], SI };
end
endmodule
