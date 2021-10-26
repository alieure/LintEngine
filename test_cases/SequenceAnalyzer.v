module Sequence_Analyzer(input wire serialInput, clk, reset, output wire
out);
 reg [1:0] current;
 reg [1:0] next;

 always@(serialInput or current)begin
 case(current)
 2'b00:begin
 if(serialInput)
 next=2'b01;
 else
 next=2'b00;
 end
 2'b01:begin
 if(serialInput)
 next=2'b01;
 else
 next=2'b10;
 end
 2'b10:begin
 if(serialInput)
 next=2'b01;
 else
 next=2'b11;
 end
 2'b11:begin
 if(serialInput)
 next=2'b01;
 else
 next=2'b00;
 end
 default:next=2'b00;
 endcase
 end

 always@(posedge clk or posedge reset)begin
 if(reset)
 current<=2'b00;
 else
 current<=next;
 end

 assign out = (current==2'b11);
endmodule