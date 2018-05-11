package com.example.bozsi.schoolproject;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

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
        Films filmek = new Films();
        filmek.Add("asd","asd","asd","asd",4.5);
        details.getIndex(i,filmek);
        assertTrue(details.index == i && details.films.equals(filmek));
    }
}