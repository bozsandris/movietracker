package model;

import java.util.ArrayList;
import java.util.List;

/***
 * Class which stores film details.
 * */
public class Films {

    public String title,genreids,overview,releasedate;
    public Double popularity;
    public Films(String title,String genreids,String overview,String releasedate,Double popularity){
        this.title = title;
        this.genreids = genreids;
        this.overview = overview;
        this.releasedate = releasedate;
        this.popularity = popularity;
    }
    public String gettitle(){
        return this.title;
    }
}
