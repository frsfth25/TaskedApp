package com.example.farisfathurrahman25.taskedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    Context myContext;
    private ArrayList<Book> bookSet;
    private LayoutInflater vi;
    private int Resource;
    private int lastPosition = -1;

    public BookAdapter(ArrayList<Book> book, Context context) {
        super(context, R.layout.book_item, book);
        this.bookSet = book;
        this.myContext = context;
    }

    BookAdapter(Context context, int resource, ArrayList<Book> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        this.bookSet = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Book bookModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.book_item, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.textView);
            viewHolder.imgView = convertView.findViewById(R.id.thumbnailView);
            viewHolder.txtAuthor = convertView.findViewById(R.id.textView2);
            viewHolder.rateBar = convertView.findViewById(R.id.ratingBar);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtTitle.setText(bookModel.getTitle());
        //viewHolder.imgView.setImageBitmap();
        Picasso.get().load(bookModel.getImageURL()).into(viewHolder.imgView);
        viewHolder.txtAuthor.setText(bookModel.getAuthor());
        viewHolder.rateBar.setRating(bookModel.getRating());

/*        StringBuilder stb = new StringBuilder();
        for (int i=1; i<=dataModel.getLevel(); i++)
            stb.append("*");
        viewHolder.txtLevel.setText(stb);

        viewHolder.txtStatus.setText(dataModel.getStatus()); */

        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        ImageView imgView;
        TextView txtAuthor;
        RatingBar rateBar;

    }
}
