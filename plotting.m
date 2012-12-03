clc;
clear all;
close all;

recall = [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100];

bm25 = [ 74.37,  73.37,  71.68,  58.10,  57.42,  57.55,  56.63,  60.48,  57.00,  57.58,  56.35 ];
boolean = [ 18.22,  20.88,  23.65,  25.17,  21.00,  23.91,  39.17,  51.71,  51.71,  37.48,  37.86 ];
vsm  = [ 76.47,  72.54,  69.73,  57.99,  54.65,  54.72,  58.12,  67.44,  64.68,  50.69,  48.85 ];

figure;
plot(recall, boolean, ...
     recall, vsm, ...
     recall, bm25);
 grid on;
 axis([0 100 0 100]);
 xlabel('Recall');
 ylabel('Presicion');
 title('Precision-Recall diagram for Boolean-Vector Space-OKAPI BM25 Models');
 legend('Boolean Model', 'Vector Space Model', 'OKAPI - BM25');
