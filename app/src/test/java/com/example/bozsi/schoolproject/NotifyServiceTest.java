package com.example.bozsi.schoolproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NotifyServiceTest {

    private NotifyService notifyService = new NotifyService();

    @Test
    public void addtitle() throws Exception {
        String title = "Hello";
        notifyService.addtitle(title);
        assertTrue(notifyService.Titles.get(0).contentEquals("Hello"));
    }

    @Test
    public void onstartcommand() throws Exception {
        Intent intent = new Intent();
        assertThat(notifyService.onStartCommand(intent,0,0), instanceOf(int.class));
    }
}