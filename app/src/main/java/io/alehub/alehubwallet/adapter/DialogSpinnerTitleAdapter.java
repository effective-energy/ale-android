package io.alehub.alehubwallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.view.TextViewFont;

/**
 * Created by dima on 1/30/18.
 */

public class DialogSpinnerTitleAdapter extends BaseAdapter {

    private Context context;
    private List<String> elements = new ArrayList<>();

    public DialogSpinnerTitleAdapter(Context context) {
        this.context = context;
    }

    public DialogSpinnerTitleAdapter(Context context, List<String> elements) {
        this.context = context;
        this.elements = elements;
    }

    public void clear() {
        elements.clear();
        notifyDataSetChanged();
    }

    public void addElement(String str) {
        if (str != null) {
            elements.add(str);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return elements.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_title_spiner_element, null);
        TextView tvTitle = (TextViewFont) v.findViewById(R.id.tv_dialog_title_spinner_element_title);
        tvTitle.setText(elements.get(position));
      //  v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return v;
    }
}
