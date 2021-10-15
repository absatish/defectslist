package com.defectlist.inwarranty.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ListUtils {

    public static <T> List<List<T>> partition(final List<T> listToBePartitioned, final int numberOfItemsInList) {
        final List<List<T>> partitionedList = new ArrayList<>();
        final int reminder = listToBePartitioned.size() % numberOfItemsInList == 0 ? 0 : 1;
        Stream.iterate(0, index -> index + numberOfItemsInList)
                .limit(listToBePartitioned.size() / numberOfItemsInList + reminder)
                .map(index -> listToBePartitioned.subList(index, Math.min(listToBePartitioned.size(), index + numberOfItemsInList)))
                .forEach(partitionedList::add);
        return partitionedList;

    }
}
