package com.jeannot;

import com.jeannot.exceptions.IdNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GwtIds {

    private static Map<String,String> ids = new HashMap<String,String>();

    static {
        Properties props = new Properties();
        InputStream in = Class.class.getResourceAsStream("/gwtids.properties");
        try {
            props.load(in);
            in.close();
        }
        catch (IOException ioe) {
            System.out.println("Unable to load GWT IDs");
            System.exit(-1);
        }

        Set keys = props.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = props.getProperty(key);
            ids.put(key,value);
        }
    }

    public static String getGwtId(String text) throws IdNotFoundException {
        String id = ids.get(text);
        if (id==null) {
            throw new IdNotFoundException("Unable to find a GWT ID for \"" + text + "\"");
        }
        return id;
    }
}
