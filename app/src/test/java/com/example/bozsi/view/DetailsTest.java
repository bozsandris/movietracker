package com.example.bozsi.view;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import model.Films;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DetailsTest {

    @Test
    public void getindex() throws Exception {
        Details details = new Details();
        int i=0;
        List<Films> filmek = new ArrayList<>();
        filmek.add(new Films("asd","asd","asd","asd",4.5));
        details.getIndex(i,filmek);
        assertTrue(details.index == i && details.films.equals(filmek));
    }
}