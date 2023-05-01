package bll.util;

import util.Searchable;

import java.util.ArrayList;
import java.util.List;

public class Search<T> {

    public List<Searchable<T>> searchForString(List<Searchable<T>> searchables, String query){
        List<Searchable<T>> matchingSearchables = new ArrayList<>();
        for (Searchable<T> searchable : searchables) {

            if (searchable.search(query) != null) matchingSearchables.add(searchable);
        }
        return matchingSearchables;
    }

}
