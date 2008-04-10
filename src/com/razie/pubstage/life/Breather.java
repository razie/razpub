package com.razie.pubstage.life;


/**
 * beings live in the environment. They breathe at the mercy of the Lifegiver.
 * 
 * @author razvanc99
 * 
 */
public interface Breather extends Being {
    /** beings take life one breath at a time, whenever the lifegiver wants */
    public void breathe();
}
