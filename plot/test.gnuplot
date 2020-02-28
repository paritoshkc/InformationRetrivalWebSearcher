set term png
set output "pr_graph.png"
set title "P/R from trec_eval test Analyzer=Standard"
set ylabel "Precision"
set xlabel "Recall"
set xrange [0:1]
set yrange [0:1]
set xtics 0,.2,1
set ytics 0,.2,1

plot 'sb.dat' title "Similarity=BM25" with lines, 'sl.dat' title "Similarity=LMDirichlet" with lines, 'sc.dat' title "Similarity=Classic" with lines
