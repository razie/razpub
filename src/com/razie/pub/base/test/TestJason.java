package com.razie.pub.base.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.razie.pub.base.data.Pair;

public class TestJason extends TestCase {

    public void setUp() {
    }

    public void testPair() throws JSONException {
        List<Pair> list = new ArrayList<Pair>();
        list.add(new Pair("a", "a"));
        list.add(new Pair("b", "b"));

        String json = Pair.toJson(list, null).toString();

        JSONObject read = new JSONObject(json);
        list = Pair.fromJson(new ArrayList<Pair>(), read);
        
        assertTrue(list.size()==2);
        assertTrue(list.get(0).b.equals("a") || list.get(0).b.equals("b"));
    }
}
