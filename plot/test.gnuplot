set term png
set output "pr_graph.png"
set title "P/R from trec_eval test Analyzer=Classic"
set ylabel "Precision"
set xlabel "Recall"
set xrange [0:1]
set yrange [0:1]
set xtics 0,.2,1
set ytics 0,.2,1

plot 'cb.dat' title "Similarity=BM25" with lines, 'cl.dat' title "Similarity=LMDirichlet" with lines, 'cc.dat' title "Similarity=Classic" with lines
