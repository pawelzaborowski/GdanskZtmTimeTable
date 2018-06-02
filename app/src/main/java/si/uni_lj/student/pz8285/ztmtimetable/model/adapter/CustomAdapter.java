package si.uni_lj.student.pz8285.ztmtimetable.model.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import si.uni_lj.student.pz8285.ztmtimetable.R;
import si.uni_lj.student.pz8285.ztmtimetable.activities.MainActivity;

/**
 * Created by Pawel on 17.05.2018.
 */

public class CustomAdapter  extends BaseAdapter implements Filterable {

    private ArrayList<HashMap<String, String>> mOriginalValues; // Original Values
    private ArrayList<HashMap<String, String>> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        LinearLayout llContainer;
        TextView stopDesc;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.stops_items, null);
            holder.llContainer = (LinearLayout) convertView.findViewById(R.id.ll);
            holder.stopDesc = (TextView) convertView.findViewById(R.id.stopDesc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.stopDesc.setText(mDisplayedValues.get(position).get("stopDesc"));


        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = ( ArrayList<HashMap<String, String>>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<HashMap<String, String>> FilteredArrList = new  ArrayList<HashMap<String, String>>();

                if (mOriginalValues == null) {
                    mOriginalValues = new  ArrayList<HashMap<String, String>>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).get("stopDesc");
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new HashMap<String, String>(Integer.parseInt(mOriginalValues.get(i).get("stopDesc"))));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }


}
