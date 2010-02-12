package com.razie.pubstage.life;

import razie.base.ActionItem;

/**
 * beings live in the environment. They live  at the mercy of the Lifegiver.
 * 
 * @author razvanc99
 * 
 */
public interface Being {
    /** beings should be nice and answer what they're doing right now (status in nerdsspeak) */
    public ActionItem whatAreYouDoing();

    /** beings should be nice and tell who they are */
    public ActionItem whoAreYou();
}
