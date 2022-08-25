package com.bigshen.learningDemo.design.strategy.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author BYJ
 * @Date 2022/8/14 20:36
 * @Describe
 */
public class Sorter {
    
    private static final long GB = 1000 * 1000 * 1000;
    private static final List<AlgRange> ALGS = new ArrayList<>();

    static {
        ALGS.add(new AlgRange(0, 6 * GB, SortAlgFactory.getSortAlg("QuickSort")));
        ALGS.add(new AlgRange(6 * GB, 10 * GB, SortAlgFactory.getSortAlg("ExternalSort")));
        ALGS.add(new AlgRange(10 * GB, 100 * GB, SortAlgFactory.getSortAlg("ConcurrentExternalSort")));
        ALGS.add(new AlgRange(100 * GB, Long.MAX_VALUE, SortAlgFactory.getSortAlg("MapReduceSort")));
    }

    public void sortFile(String filePath) {
        // 省略校验逻辑
        File file = new File(filePath);
        long fileSize = file.length();
        ISortAlg sortAlg = null;
        for (AlgRange algRange : ALGS) {
            if (algRange.inRange(fileSize)) {
                sortAlg = algRange.getAlg();
                break;
            }
        }
        sortAlg.sort(filePath);
    }

    private static class AlgRange {
        private final long start;
        private final long end;
        private final ISortAlg alg;

        public AlgRange(long start, long end, ISortAlg alg) {
            this.start = start;
            this.end = end;
            this.alg = alg;
        }

        public ISortAlg getAlg() {
            return alg;
        }

        public boolean inRange(long size) {
            return size >= start && size < end;
        }
    }
}