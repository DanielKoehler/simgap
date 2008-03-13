clear

load file3.dat
load file4.dat
Xsx=file3(:,2).-1000;
Xdx=file4(:,2).-1000;
Ysx=file3(:,3);
Ydx=file4(:,4);

%%%__gnuplot_set__ term postscript enhanced color;

minVec=[min(Ysx), min(Ydx)];
maxVec=[max(Ysx), max(Ydx)];

maxY=max(maxVec);
minY=min(minVec);

%%%__gnuplot_set__ out "out.eps"

[ax, h1, h2]=plotyy(Xsx,Ysx,Xdx,Ydx);
xlabel ("X");
ylabel (ax(1), "Axis 1");
ylabel (ax(2), "Axis 2");

set(h1,'linewidth',2);
set(h1,'Color', 'r');
set(h2,'linewidth',2);
set(h2,'Color', 'b');

print("out.eps","-color","-deps");
