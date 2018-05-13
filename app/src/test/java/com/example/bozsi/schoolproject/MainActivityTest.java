package com.example.bozsi.schoolproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;
import static org.powermock.api.support.membermodification.MemberModifier.method;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class MainActivityTest {

    @Test
    public void setUp() throws Exception {
        suppress(method(MainActivity.class, "setContentView", int.class));
    }

    @Test
    public void filmsclear() throws Exception {
        MainActivity mainActivity = new MainActivity();
        mainActivity.films.Add("test","test","test","test",4.5);
        mainActivity.films.Add("test","test","test","test",4.5);
        mainActivity.clearfilms();
        assertTrue(mainActivity.films.title.size()==0);
    }
}