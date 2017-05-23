package in.technodroid.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by IBM_ADMIN on 9/16/2015.
 */
public class Util {

    public static final String  appCompanyName = "TechnoDroid";
    public static File createDirIfNotExists(String filename) {

        File file = new File(Environment.getExternalStorageDirectory()+"/"+appCompanyName);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("SWAP :: ", "Error creating Image folder");
                return new File(file.toString(), filename);
            }
        }
            return  new File( file.toString(), filename);

    }

    public static void writeToFile(InputStream is ,File file) {
        try {
            OutputStream output = new FileOutputStream(file);
            try {
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = is.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace(); // handle exception, define IOException and others
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static String readFromFile(File file) {
        FileInputStream fis =null;
        try{
         fis = new FileInputStream(file);
            return  convertStreamToString(fis)  ;
        }catch (FileNotFoundException fe){
          return null;
        }
        finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static JSONObject filterFile(String strFileContetnt){
        int start = strFileContetnt.indexOf("{", strFileContetnt.indexOf("{") + 1);
        int end = strFileContetnt.lastIndexOf("}");
        String jsonResponse = strFileContetnt.substring(start, end);
        try {
            JSONObject table = new JSONObject(jsonResponse);
            return table;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
