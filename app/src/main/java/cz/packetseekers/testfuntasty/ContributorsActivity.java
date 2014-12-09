package cz.packetseekers.testfuntasty;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class ContributorsActivity extends ListActivity {

    private ProgressDialog pDialog;

    private static final String url = "https://api.github.com/repos/torvalds/linux/contributors";

    // JSON node names
    public static final String TAG_LOGIN = "login";
    public static final String TAG_ID = "id";
    public static final String TAG_AVATAR_URL = "avatar_url";
    public static final String TAG_URL = "html_url";

    JSONArray contributors = null;

    ArrayList<HashMap<String, Object>> contributorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);

        contributorsList = new ArrayList<HashMap<String, Object>>();

        ListView lv = getListView();

        new GetContributors().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contributors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task for getting JSON file and parsing
     */
    private class GetContributors extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ContributorsActivity.this);
            pDialog.setMessage(getString(R.string.getting_contributors));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall(url, HttpHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null)
            {
                try
                {
                    contributors = new JSONArray(jsonStr.toString());

                    for(int i = 0; i < contributors.length(); i++)
                    {
                        JSONObject contributorJSONObject = contributors.getJSONObject(i);

                        String id = contributorJSONObject.getString(TAG_ID);
                        String login = contributorJSONObject.getString(TAG_LOGIN);
                        String avatarUrl = contributorJSONObject.getString(TAG_AVATAR_URL);
                        String url = contributorJSONObject.getString(TAG_URL);

                        HashMap<String, Object> contributor = new HashMap<String, Object>();

                        contributor.put(TAG_ID, id);
                        contributor.put(TAG_LOGIN, login);
                        contributor.put(TAG_URL, url);

                        //load image from url
                        InputStream in = new URL(avatarUrl).openStream();
                        Bitmap avatar = BitmapFactory.decodeStream(in);

                        contributor.put(TAG_AVATAR_URL, avatar);


                        contributorsList.add(contributor);
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                catch(MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
                Log.e("HttpHandler", "No data found on url");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(pDialog.isShowing())
            {
                pDialog.dismiss();
            }

            // show data in listView
            ListAdapter adapter = new ContributorsAdapter(
                    ContributorsActivity.this, contributorsList
            );

            setListAdapter(adapter);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        HashMap<String,Object> item = (HashMap<String,Object>)this.getListAdapter().getItem(position);

        Uri uri = Uri.parse(item.get(TAG_URL).toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
