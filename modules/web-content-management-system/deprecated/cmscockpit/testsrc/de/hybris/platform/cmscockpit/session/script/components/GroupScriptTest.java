/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmscockpit.session.script.components;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Test;

/**
 * Created by i840081 on 2014-12-17.
 */
@UnitTest
public class GroupScriptTest {

    @Test(expected = IllegalArgumentException.class)
    public void null_setGroup_paramater_throws_exception()
    {
        GroupScript groupScript = new GroupScript();
        groupScript.setGroup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zero_length_setGroup_paramater_throws_exception()
    {
        GroupScript groupScript = new GroupScript();
        groupScript.setGroup("");
    }
}
