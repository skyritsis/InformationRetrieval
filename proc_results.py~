#!/usr/bin/python


import io

class QueryRelation:

    def __init__(self, filename = None):
        self.relations = {}
        if filename is not None:
            self.read_file(filename)

    def read_file(self, filename):

        doc_list = []
        qry_num = 1

        with io.open(filename) as f:
            for line in f:
                line_parted = line.split()
                if qry_num < int(line_parted[0]):
                    # new query set, store query with its answer set 
                    # and continue.
                    self.relations[qry_num] = doc_list
                    qry_num = int(line_parted[0])
                    doc_list = []
                doc_list.append(int(line_parted[1]))


class Evaluator:

    def __init__(self):
        self.precision_recall = []

    def evaluate(self, relevant_doc_list, answer_set_list):
        relevant_len = len(relevant_doc_list)
        recall_lvl = 0.0
        answers = 0.0
        for i in answer_set_list:
            answers += 1
            # can be done using filter
            if i in relevant_doc_list:
                recall_lvl += 1
                recall = recall_lvl * 100 / relevant_len
                precision = recall_lvl * 100 / answers
                self.precision_recall.append([recall, precision])

def computePrecision(precision_recall_list):
    # standard recall levels
    r = [0.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0]
    min_lvl = 0.0
    max_lvl = 10.0

    max_precision = 0.0
    avg_precision = []
    interpolated_precision_recall = []

    for recall, precision in precision_recall_list:
        if recall > max_lvl:
            interpolated_precision_recall.append([min_lvl, max_precision])
            interpolated_precision_recall.append([max_lvl, max_precision])
            min_lvl += 10.0
            max_lvl += 10.0
            max_precision = 0.0

        if recall == min_lvl or recall == max_lvl:
            interpolated_precision_recall.append([min_lvl, precision])
            interpolated_precision_recall.append([max_lvl, precision])
        if precision > max_precision: max_precision = precision
        interpolated_precision_recall.sort()

    for lvl in r:
        sum = 0.0
        count = 0.0
        for i in interpolated_precision_recall:
            if i[0] == lvl:
                sum += i[1]
                count += 1
        if count != 0: sum /= count
        avg_precision.append([lvl, sum])

    return avg_precision



###########################################################
if __name__ == '__main__':
    import sys
    if len(sys.argv) == 1:
        print 'Usage: ', sys.argv[0], 'relevant_query-doc query_answer_set'
        exit()

    cranqrel = sys.argv[1]
    results = sys.argv[2]

    qrel = QueryRelation(cranqrel)
    e = Evaluator()
    answers = []
    qry_num = 1
    with io.open(results) as f:
        for line in f:
            line_parted = line.split()
            if qry_num < int(line_parted[0]):
                e.evaluate(qrel.relations[qry_num], answers)
                answers = []
                qry_num = int(line_parted[0])
            try:
                answers.append(int(line_parted[1]))
            except IndexError:
                answers.append(0)

    e.precision_recall.sort()
    result = computePrecision(e.precision_recall)

    print '%',
    print '%s %s %s' % ('proc_results.py', cranqrel, results)
    print '[',
    for recall, precision in result:
        if recall == 100.0:
            print '%0.2f ]' % (precision),
        else: print '%0.2f, ' % (precision),

