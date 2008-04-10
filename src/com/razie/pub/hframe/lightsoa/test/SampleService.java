package com.razie.pub.hframe.lightsoa.test;

import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.lightsoa.SoaMethod;
import com.razie.pub.hframe.lightsoa.SoaResponse;
import com.razie.pub.hframe.lightsoa.SoaStreamable;

/**
 * a sample lightsoa service
 * 
 * @author razvanc99
 * 
 */
public class SampleService {
    @SoaMethod (descr="concatenate two values", args={"parm1","parm2"})
    public SoaResponse concatenate(String parm1, String parm2) {
        // that's how it's done in UPnP
        return new SoaResponse("Result", parm1 + parm2);
    }
    
    @SoaMethod (descr="does nothing", args={"parm1","parm2"})
    public void doNothing(String parm1, String parm2) {
    }
    
    /** note that it cant have the same name as the other one */
    @SoaMethod (descr="concatenate two values", args={"parm1","parm2"})
    @SoaStreamable
    public void concatenateStream(DrawStream out, String parm1, String parm2) {
        out.write(parm1 + parm2);
    }
    
}
