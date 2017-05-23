package in.technodroid.util;

/**
 * Created by IBM_ADMIN on 9/15/2015.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



import in.technodroid.db.DBHelper;
import in.technodroid.model.AsyncResult;
import in.technodroid.model.QAModel;
import in.technodroid.model.UpdatesModel;
import in.technodroid.swap.HomeScreen;
import in.technodroid.swap.R;
import in.technodroid.swap.Updates;

public class DownloadWebpageTask extends AsyncTask<String, Void, List<?>> {


    AsyncResult callback;
    String fileName;
    Context context;
    public DBHelper dbHelper;
    private ArrayList<QAModel> arQA;
    private ProgressDialog dialog;
    public DownloadWebpageTask(AsyncResult callback,Context context) {
        this.callback = callback;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context,R.style.MyTheme);
        dialog.setCancelable(false);
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.show();
    }

    @Override
    protected List<?> doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        fileName=urls[1];
        try {
           return  downloadUrl(urls[0]);
        } catch (IOException e) {
           return null;
        }
    }
    // onPostExecute displays the results of the AsyncTask.

    protected void onPostExecute(List<?> result) {
         dialog.hide();
         callback.onResult(result);
    }
    private List<?> downloadUrl(String urlString) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.connect();

            is = conn.getInputStream();
            String response = Util.convertStreamToString(is);

            //Filter data from JSON
            int start = response.indexOf("{", response.indexOf("{") + 1);
            int end = response.lastIndexOf("}");
            String jsonResponse = response.substring(start, end);
            try {
                JSONObject table = new JSONObject(jsonResponse);
                if(fileName.equalsIgnoreCase(AppConstants.QA_FILENAME))
             return    processJson(table);
                else if (fileName.equalsIgnoreCase(AppConstants.UPDATES_FILENAME)) {
                return processJsonTips(table);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

           return null;
        }
        catch(Exception ex){
            Log.e("ex","-->"+ex.getMessage());
            return null;
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
    List<UpdatesModel> arUpdates;
    private List<?> processJsonTips(JSONObject jsonTips) {
        dbHelper = DBHelper.getInstance(context);
       // dbHelper.deleteUpdates();
        try {
            JSONArray rows = jsonTips.getJSONArray("rows");
             arUpdates = new ArrayList<UpdatesModel>();
            List<UpdatesModel> arUpdates=  dbHelper.getAllUpdates();
            int newUpdates=rows.length()-arUpdates.size();

            for (int r = 1; r < newUpdates; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String title = (columns.get(0).equals(null))?null:columns.getJSONObject(0).getString("v");
                String desc = (columns.get(1).equals(null))?null:columns.getJSONObject(1).getString("v");
                String imgPath = (columns.get(2).equals(null))?null:columns.getJSONObject(2).getString("v");
                String category = (columns.get(3).equals(null))?null:columns.getJSONObject(3).getString("v");
                String tag = (columns.get(4).equals(null))?null:columns.getJSONObject(4).getString("v");
                UpdatesModel update = new UpdatesModel(category,title, desc, imgPath,tag);
                arUpdates.add(update);

            }
            dbHelper.insertUpdates(arUpdates);
            return arUpdates;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<?> processJson(JSONObject jsonQA) {

        try {
            JSONArray rows = jsonQA.getJSONArray("rows");
            arQA = new ArrayList<QAModel>();
            dbHelper = DBHelper.getInstance(context);
            List<QAModel> qaModel=  dbHelper.getAllQA();
            //dbHelper.deleteQA();
            // New Questions : 1 to rows.length-
            int newQAAddedCount=rows.length()-qaModel.size();
            for (int r = 1; r <newQAAddedCount ; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String question = (columns.get(0).equals(null))?null:columns.getJSONObject(0).getString("v");
                String answer = (columns.get(1).equals(null))?null:columns.getJSONObject(1).getString("v");
                String topic = (columns.get(2).equals(null))?null:columns.getJSONObject(2).getString("v");
                String subtopic = (columns.get(3).equals(null))?null:columns.getJSONObject(3).getString("v");
                // int level = (columns.get(4).equals(null))?null:columns.getJSONObject(4).getInt("v");
                String askedIn = (columns.get(5).equals(null))?null:columns.getJSONObject(5).getString("v");
                String imageUrl = (columns.get(6).equals(null))?null:columns.getJSONObject(6).getString("v");
                String by = (columns.get(7).equals(null))?null:columns.getJSONObject(7).getString("v");
                //int rating = (columns.get(4).equals(null))?null:columns.getJSONObject(4).getInt("v");

                QAModel qa = new QAModel(question, answer, imageUrl, by);
                //qa.set_Level(level);
                qa.set_id(r);
                qa.setIsFav(false);
                qa.set_Topic(topic);
                qa.set_AskedIn(askedIn);
                qa.set_SubTopic(subtopic);
                //dbHelper.insertQA(qa);
                arQA.add(qa);
            }
            dbHelper.insertQA(arQA);
           return dbHelper.getAllQA();

        } catch (JSONException e) {
            e.printStackTrace();
            return dbHelper.getAllQA();
        }
    }


}