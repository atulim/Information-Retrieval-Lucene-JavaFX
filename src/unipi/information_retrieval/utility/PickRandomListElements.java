package unipi.information_retrieval.utility;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PickRandomListElements {
    public static <E> List<E> pickNRandomElements(List<E> list, int n, Random r) {
        /*
         * https://stackoverflow.com/a/35278327
         * I found this procedure on Stack OverFlow it uses the Durstenfeld's algorithm in order to efficiently
         * get the n random elements out of the ArrayList without needing to do a full shuffle. It swaps the N
         * last elements with N random elements inside the List. The end result is that the last N elements of the
         * List are all random.
         */
        int length = list.size();

        if (length < n) return null;

        //We don't need to shuffle the whole list
        for (int i = length - 1; i >= length - n; --i)
        {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        return pickNRandomElements(list, n, ThreadLocalRandom.current());
    }
}
