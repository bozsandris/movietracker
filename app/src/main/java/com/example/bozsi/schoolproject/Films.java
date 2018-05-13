package com.example.bozsi.schoolproject;

import java.util.ArrayList;
import java.util.List;

/***
 * Class which stores film details.
 * */
public class Films {
    /**Title of the film.*/
    List<String> title = new ArrayList<>();
    /**Genre of the film.*/
    List<String> genreids = new ArrayList<>();
    /**Overview of the film.*/
    List<String> overview = new ArrayList<>();
    /**Release date of the film.*/
    List<String> releasedate = new ArrayList<>();
    /**Average vote of the film.*/
    List<Double> voteavg = new ArrayList<>();
    /***
     * Store a new film's details in the lists.
     * @param original_title title of the film
     * @param genreid genre of the film
     * @param over_view overview of the film
     * @param release_date release date of the films
     * @param vote_avg average vote of the film
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
     * @param id is the ID of the film.
     * */
    public void Remove (int id){
        this.title.remove(id);
        this.genreids.remove(id);
        this.overview.remove(id);
        this.releasedate.remove(id);
        this.voteavg.remove(id);
    }
}
