module mult_add(a, b, c, d, z, clk, rst);
input clk, rst;
input [1:0] a,b,c,d;
output reg [3:0] z;
reg [1:0] a_ff, b_ff, c_ff, d_ff;
reg [3:0] m1, m2, m1_ff, m2_ff;
always @(*)
begin
m1 <= a_ff * b_ff;
m2 <= c_ff * d_ff;
z <= m1_ff + m2_ff;
end
always @(posedge clk or posedge rst)
begin
if(rst)
begin
a_ff <= 0;
b_ff <= 0;
c_ff <= 0;
d_ff <= 0;
m1_ff <= 0;
m2_ff <= 0;
end
else begin
a_ff <= a;
b_ff <= b;
c_ff <= c;
d_ff <= d;
m1_ff <= m1;
m2_ff <= m2;
end
end
endmodule