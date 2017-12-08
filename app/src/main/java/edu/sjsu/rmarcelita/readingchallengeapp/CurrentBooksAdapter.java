package edu.sjsu.rmarcelita.readingchallengeapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by riri on 12/7/17.
 */

public class CurrentBooksAdapter extends RecyclerView.Adapter<CurrentBooksAdapter.BooksViewHolder>{

    ArrayList<Books> books;

    CurrentBooksAdapter(ArrayList<Books> books) {
        this.books = books;
    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView author;
        TextView genre;
        TextView pages;
        ImageView cover;
        RatingBar stars;

        BooksViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            title = itemView.findViewById(R.id.currentBookTitleTextView);
            author = itemView.findViewById(R.id.currentBookAuthorTextView);
            genre = itemView.findViewById(R.id.currentGenreTextView);
            pages = itemView.findViewById(R.id.currentPagesTextView);
            //cover = itemView.findViewById(R.id.currentBookImage);
            stars = itemView.findViewById(R.id.currentBookRatingBar);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_cardview, viewGroup, false);
        BooksViewHolder pvh = new BooksViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BooksViewHolder booksViewHolder, int i) {
        booksViewHolder.title.setText(books.get(i).getTitle());
        booksViewHolder.author.setText(books.get(i).getAuthor());
        booksViewHolder.genre.setText(books.get(i).getGenre());
        booksViewHolder.pages.setText(books.get(i).getPages() + " pages");
        booksViewHolder.stars.setRating((float) books.get(i).getStars());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
