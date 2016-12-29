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
public class CustomListAdapterNo2 extends BaseAdapter {
    private ArrayList<RowItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapterNo2(Context aContext, ArrayList<RowItem> listData) {
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
            convertView = layoutInflater.inflate(R.layout.list_row_layout_no2, null);
            holder = new ViewHolder();
            holder.TitleView=(TextView)convertView.findViewById(R.id.title);
            holder.TimeView=(TextView)convertView.findViewById(R.id.time);
            holder.StatusView=(TextView)convertView.findViewById(R.id.status);
            holder.TagsView=(TextView)convertView.findViewById(R.id.tags);
            holder.LevelView=(TextView)convertView.findViewById(R.id.level);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.TitleView.setText(listData.get(position).description);
        holder.TagsView.setText("Tags:-"+listData.get(position).tags);
        holder.StatusView.setText("Status:-"+listData.get(position).status);

        holder.LevelView.setText("Complaint Level:-"+listData.get(position).level);

        holder.TimeView.setText("Created at "+listData.get(position).created);
        return convertView;
    }

    static class ViewHolder {
        TextView TitleView;
        TextView StatusView;
        TextView LevelView;
        TextView TagsView;

        TextView TimeView;
    }
}
