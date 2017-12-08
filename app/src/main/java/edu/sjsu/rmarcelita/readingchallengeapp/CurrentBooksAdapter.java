package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by riri on 12/7/17.
 */

public class CurrentBooksAdapter extends RecyclerView.Adapter<CurrentBooksAdapter.BooksViewHolder>{

    ArrayList<Books> books;
    Context context;
    SQLiteHelper db;

    CurrentBooksAdapter(ArrayList<Books> books, Context appContext) {
        this.books = books;
        context = appContext;
        db = new SQLiteHelper(context);
    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView author;
        TextView genre;
        TextView pages;
        ImageView cover;
        RatingBar stars;
        Button readBtn;
        Button deleteBtn;

        BooksViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            title = itemView.findViewById(R.id.currentBookTitleTextView);
            author = itemView.findViewById(R.id.currentBookAuthorTextView);
            genre = itemView.findViewById(R.id.currentGenreTextView);
            pages = itemView.findViewById(R.id.currentPagesTextView);
            cover = itemView.findViewById(R.id.currentBookImage);
            stars = itemView.findViewById(R.id.currentBookRatingBar);
            readBtn = itemView.findViewById(R.id.readButton);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
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
    public void onBindViewHolder(final BooksViewHolder booksViewHolder, int i) {
        final String title = books.get(i).getTitle();
        final Books curBook = books.get(i);
        booksViewHolder.title.setText(books.get(i).getTitle());
        booksViewHolder.author.setText(books.get(i).getAuthor());
        booksViewHolder.genre.setText(books.get(i).getGenre());
        booksViewHolder.pages.setText(books.get(i).getPages() + " pages");
        booksViewHolder.stars.setRating((float) books.get(i).getStars());
        Picasso.with(context)
                .load(books.get(i).getCover())
                .resize(200,300)
                .into(booksViewHolder.cover);
        booksViewHolder.readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteFromCurrentBookTable(title);
                db.insertReadBooksTable(title, curBook.getAuthor(), curBook.getGenre(),
                        curBook.getSynopsis(), curBook.getPages(), curBook.getCover(),
                        curBook.getStars(), db.getUserInfo("username").substring(1));
                Toast.makeText(context, "You have finished reading " + title,
                        Toast.LENGTH_SHORT).show();
            }
        });
        booksViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteFromCurrentBookTable(title);
                Toast.makeText(context, title + " successfully deleted from "
                        + "your currently reading list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
