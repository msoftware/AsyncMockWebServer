package com.mosn.asyncmockwebserver.example.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosn.asyncmockwebserver.example.R;
import com.mosn.asyncmockwebserver.example.domain.model.Content;
import com.mosn.asyncmockwebserver.example.domain.model.Contents;
import com.mosn.asyncmockwebserver.example.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchContentsAdapter extends ArrayAdapter<Content> {

    private final Context context;
    private final LayoutInflater inflater;

    public final String searchKeyword;

    public SearchContentsAdapter(Context context, @NonNull Contents contents) {
        super(context, 0, contents.contents);
        this.context = context;
        inflater = LayoutInflater.from(context);
        searchKeyword = contents.searchKeyword;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Content content = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_contents_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(content.artworkUrl100).transform(PicassoUtils.circleTransformation()).into(holder.icon);
        holder.collection.setText(content.collectionName);
        holder.track.setText(content.trackName);

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.search_contents_list_imageview_icon)
        public ImageView icon;
        @Bind(R.id.search_contents_list_textview_collection)
        public TextView collection;
        @Bind(R.id.search_contents_list_textview_track)
        public TextView track;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
