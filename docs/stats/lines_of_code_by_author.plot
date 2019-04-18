set terminal png transparent size 640,240
set size 1.0,1.0

set terminal png transparent size 640,480
set output 'lines_of_code_by_author.png'
set key left top
set yrange [0:]
set xdata time
set timefmt "%s"
set format x "%Y-%m-%d"
set grid y
set ylabel "Lines"
set xtics rotate
set bmargin 6
plot 'lines_of_code_by_author.dat' using 1:2 title "Charles de Freitas" w lines, 'lines_of_code_by_author.dat' using 1:3 title "Archie Edwards" w lines, 'lines_of_code_by_author.dat' using 1:4 title "Dylan" w lines, 'lines_of_code_by_author.dat' using 1:5 title "Claire" w lines, 'lines_of_code_by_author.dat' using 1:6 title "Ben Crocker" w lines, 'lines_of_code_by_author.dat' using 1:7 title "Ewan Bains" w lines, 'lines_of_code_by_author.dat' using 1:8 title "cxf717" w lines, 'lines_of_code_by_author.dat' using 1:9 title "bjc756" w lines, 'lines_of_code_by_author.dat' using 1:10 title "ben crocker" w lines, 'lines_of_code_by_author.dat' using 1:11 title "Dylan McQuitty" w lines, 'lines_of_code_by_author.dat' using 1:12 title "Claire Fletcher" w lines
