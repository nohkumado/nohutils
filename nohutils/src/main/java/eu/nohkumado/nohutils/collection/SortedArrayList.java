package eu.nohkumado.nohutils.collection;

import java.util.*;
/**
 from https://stackoverflow.com/questions/4031572/sorted-array-list-in-java
 answer from https://stackoverflow.com/users/276052/aioobe
*/

public class SortedArrayList<T> extends ArrayList<T>
{

    @SuppressWarnings("unchecked")
    public void insertSorted(T value)
	{
        add(value);
        Comparable<T> cmp = (Comparable<T>) value;
        for (int i = size() - 1; i > 0 && cmp.compareTo(get(i - 1)) < 0; i--)
            Collections.swap(this, i, i - 1);
    }
}//class SortedArrayList<T> extends ArrayList<T>

