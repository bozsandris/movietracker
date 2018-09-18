package com.example.bozsi.view;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Films;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FilmsTest {
    @Test
    public void filmsadd() throws Exception {
        List<Films> filmek = new ArrayList<>();
        filmek.add(new Films("test","test","test","test",4.5));
        assertTrue(filmek.get(0).title.contentEquals("test")&&filmek.get(0).genreids.contentEquals("test")
                &&filmek.get(0).overview.contentEquals("test")&&filmek.get(0).releasedate.contentEquals("test")
                &&filmek.get(0).popularity==4.5);
    }
    @Test
    public void filmsremove() throws Exception {
        List<Films> filmek = new ArrayList<>();
        filmek.add(new Films("test","test","test","test",4.5));
        filmek.remove(0);
        assertTrue(filmek.size()==0);
    }
}