package cz.packetseekers.testfuntasty;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Michal Konecny on 9.12.14.
 * Custom adapter for Contributors listView
 */
public class ContributorsAdapter extends BaseAdapter{

    private Activity activity;
    ArrayList<HashMap<String, Object>> contributorsList;
    private static LayoutInflater inflater=null;

    public ContributorsAdapter(Activity activity,
                               ArrayList<HashMap<String, Object>> contributorsList)
    {
        super();
        this.activity = activity;
        this.contributorsList = contributorsList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return contributorsList.size();
    }

    @Override
    public Object getItem(int i) {
        return contributorsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if(view1 == null)
        {
            view1 = inflater.inflate(R.layout.list_item, null);
        }
        TextView login = (TextView) view1.findViewById(R.id.login);
        login.setText(contributorsList.get(i).get(ContributorsActivity.TAG_LOGIN).toString());

        ImageView image = (ImageView) view1.findViewById(R.id.image);
        image.setImageBitmap((Bitmap)
                contributorsList.get(i).get(ContributorsActivity.TAG_AVATAR_URL));

        return view1;
    }
}
