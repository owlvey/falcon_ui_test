package com.falcon.selenium.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class Resources {

    public static String getProperty(String resourceFileName, String parameter) {
        String value = "";
        try{
            Properties properties = new Properties();
            InputStream input = new FileInputStream(getResourcesPath()+resourceFileName+".properties");
            properties.load(input);

            Enumeration enumeration = properties.keys();
            while(enumeration.hasMoreElements()){
                String key = (String) enumeration.nextElement();
                if(!parameter.equals(key))
                    continue;

                value = properties.getProperty(key);
            }

            input.close();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        finally {
            return value;
        }
    }

    private static String getResourcesPath() {
        return System.getProperty("user.dir") + "//src//test//resources//";
    }
}