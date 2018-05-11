package com.example.bozsi.schoolproject;

import java.util.ArrayList;
import java.util.List;

public class Films {
    List<String> title = new ArrayList<>();
    List<String> genreids = new ArrayList<>();
    List<String> overview = new ArrayList<>();
    List<String> releasedate = new ArrayList<>();
    List<Double> voteavg = new ArrayList<>();
    /***
     * Store a new film's details in the lists
     * Title,genre,overview,releasedate and average vote are being stored.
     * */
    public void Add(String original_title,String genreid,String over_view,String release_date,Double vote_avg) {
            this.title.add(original_title);
            this.genreids.add(genreid);
            this.overview.add(over_view);
            this.releasedate.add(release_date);
            this.voteavg.add(vote_avg);
    }
    /***
     * Remove a film from the lists by id.
     * */
    public void Remove (int id){
        this.title.remove(id);
        this.genreids.remove(id);
        this.overview.remove(id);
        this.releasedate.remove(id);
        this.voteavg.remove(id);
    }
}
