package bll.util;

import util.Searchable;

import java.util.ArrayList;
import java.util.List;

public class Search<T extends Searchable<T>> {

    public List<T> searchForString(List<T> searchables, String query){
        List<T> matchingSearchables = new ArrayList<>();
        for (T searchable : searchables) {
            if (searchable.search(query) != null) matchingSearchables.add(searchable);
        }
        return matchingSearchables;
    }

}
