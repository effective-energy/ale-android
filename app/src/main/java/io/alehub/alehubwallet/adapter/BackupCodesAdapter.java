package io.alehub.alehubwallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.model.BackupCode;

/**
 * Created by dima on 1/25/18.
 */

public class BackupCodesAdapter extends RecyclerView.Adapter<BackupCodesAdapter.BackupCodeHolder> {


    private Context context;
    private List<BackupCode> elements = new ArrayList<>();

    public BackupCodesAdapter(Context context) {
        this.context = context;
    }

    public void addElement(BackupCode code) {
        if (code != null) {
            elements.add(code);
            notifyItemInserted(elements.size() - 1);
        }
    }

    public void setElement(int pos, BackupCode code) {
        if (pos > -1 && pos < elements.size()) {
            elements.set(pos, code);
            notifyItemChanged(pos);
        }
    }

    public void clear() {
        int size = elements.size();
        if (size > 0) {
            elements.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public BackupCodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.code_element, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new BackupCodeHolder(v);
    }

    @Override
    public void onBindViewHolder(BackupCodeHolder holder, int position) {
        BackupCode bc = elements.get(position);
        holder.code.setText(bc.getCode());
        if (bc.isUsed()) {
            holder.code.setTextColor(context.getResources().getColor(R.color.text_light));
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.code.setTextColor(context.getResources().getColor(R.color.text_dark));
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }


    public static class BackupCodeHolder extends RecyclerView.ViewHolder {
        TextView code;
        View line;

        public BackupCodeHolder(View itemView) {
            super(itemView);
            code = (TextView) itemView.findViewById(R.id.tv_code_element_code);
            line = (View) itemView.findViewById(R.id.v_code_element_line);
        }
    }
}
