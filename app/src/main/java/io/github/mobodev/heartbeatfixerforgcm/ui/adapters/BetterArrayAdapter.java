package io.github.mobodev.heartbeatfixerforgcm.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Artem Chepurnoy on 27.12.2014.
 */
public abstract class BetterArrayAdapter<T> extends ArrayAdapter<T> {

    @NonNull
    protected final LayoutInflater mInflater;

    @NonNull
    protected final Context mContext;

    @LayoutRes
    private final int mResource;

    public abstract static class ViewHolder {

        @NonNull
        public final View view;

        public ViewHolder(@NonNull View view) {
            this.view = view;
        }

    }

    protected BetterArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final ViewHolder vh;
        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
            vh = onCreateViewHolder(view);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        onBindViewHolder(vh, position);
        return view;
    }

    @NonNull
    protected abstract ViewHolder onCreateViewHolder(@NonNull View view);

    protected abstract void onBindViewHolder(@NonNull ViewHolder viewHolder, int i);

}
