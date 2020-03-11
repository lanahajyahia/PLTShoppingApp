package com.example.plt.Models;

import java.util.Comparator;

public class Sortbyprice implements Comparator<Item>
{
    // Used for sorting in ascending order of
    // price
    public int compareHighToLow(Item a, Item b)
    {
        return a.getPrice().compareTo(b.getPrice());
    }
    public int compareLowToHigh(Item a, Item b)
    {
        return b.getPrice().compareTo(a.getPrice());
    }

    @Override
    public int compare(Item o1, Item o2) {
        return 0;
    }
}
