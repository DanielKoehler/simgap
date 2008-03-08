function [x, y] = plotgraph (file, option) 

% This is a function that will take data from a file and plot the values onto 
% a graph. 
% The file must be in the comma separated value (.csv) format. 
% The file can only contain two colums, the first must be x values, 
% the second column should be the y values 
% 
% Options 
% * = Plot with *'s 
% . = Plot with .'s 
% l = Plot with lines 

        data = load(file); 
        x=data(:,8); y=data(:,9); 

if (option == "*") 

        plot (x,y, "*") 

elseif (option == ".") 

        plot (x,y,".") 

elseif (option == "l") 
        plot (x,y) 

endif 

endfunction 
