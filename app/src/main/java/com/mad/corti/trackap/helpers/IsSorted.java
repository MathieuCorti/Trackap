package com.mad.corti.trackap.helpers;

/*
 * Trackap
 * Created by Mathieu Corti on 9/3/17.
 * mathieucorti@gmail.com
 */

import java.util.Comparator;
import java.util.List;

public class IsSorted {

    public static <T> boolean ascending(List<T> list, Comparator<? super T> c) {

        boolean sorted = true;

        for (int i = 1; i < list.size(); i++) {

            if (c.compare(list.get(i - 1), list.get(i)) > 0) {
                sorted = false;
            }
        }

        return sorted;
    }

    public static <T> boolean descending(List<T> list, Comparator<? super T> c) {

        boolean sorted = true;

        for (int i = 1; i < list.size(); i++) {

            if (c.compare(list.get(i - 1), list.get(i)) > 0) {
                sorted = false;
            }
        }

        return sorted;
    }

}
