package phoenix.iitdcomplaints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gautam on 2/20/2016.
 */
// for comments section of threads
    // whenever new comments are posted they get queued in the list view
    // then it shows if any comments have been ther epreviously or not
    //date and time for posting of threads
public class CustomListAdapterNo extends BaseAdapter {
    private ArrayList<Row2Item> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapterNo(Context aContext, ArrayList<Row2Item> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout_no, null);
            holder = new ViewHolder();
            holder.TitleView=(TextView)convertView.findViewById(R.id.title);
            holder.TimeView=(TextView)convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.TitleView.setText(listData.get(position).description);
        holder.TimeView.setText("Created at "+listData.get(position).created);
        return convertView;
    }

    static class ViewHolder {
        TextView TitleView;
        TextView TimeView;
    }
}
