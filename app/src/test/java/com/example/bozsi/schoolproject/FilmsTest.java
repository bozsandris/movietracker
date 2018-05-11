package com.example.bozsi.schoolproject;

import android.content.Intent;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FilmsTest {
    @Test
    public void filmsadd() throws Exception {
        Films filmek = new Films();
        filmek.Add("test","test","test","test",4.5);
        assertTrue(filmek.title.get(0).contentEquals("test")&&filmek.genreids.get(0).contentEquals("test")
                &&filmek.overview.get(0).contentEquals("test")&&filmek.releasedate.get(0).contentEquals("test")
                &&filmek.voteavg.get(0)==4.5);
    }
    @Test
    public void filmsremove() throws Exception {
        Films filmek = new Films();
        filmek.Add("test","test","test","test",4.5);
        filmek.Remove(0);
        assertTrue(filmek.title.size()==0);
    }
}