package com.example.groupproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class FavoriteFragment_WPG extends Fragment {

        ArrayList<MovieData> data = new ArrayList<>();
        MyMovieAdapter adapter= new MyMovieAdapter();

        MyOpenHelper_movie opener;
        SQLiteDatabase db;

        Bitmap image;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View movieLayout = inflater.inflate(R.layout.saved_list_movie,container, false);

            RecyclerView movieList = movieLayout.findViewById(R.id.recycler_movie);

            //adapter
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            movieList.setLayoutManager(layoutManager);
            adapter = new MyMovieAdapter();
            movieList.setAdapter(adapter);

            opener = new MyOpenHelper_movie(getContext());
            db = opener.getWritableDatabase();

            Cursor results = db.rawQuery("Select * from " + MyOpenHelper_movie.TABLE_NAME + ";", null);
            int idCol = results.getColumnIndex("id_Col");
            int titleCol = results.getColumnIndex(MyOpenHelper_movie.TITLE_COL);
            int yearCol = results.getColumnIndex(MyOpenHelper_movie.YEAR_COL);
            int ratingCol = results.getColumnIndex(MyOpenHelper_movie.RATING_COL);
            int runtimeCol = results.getColumnIndex(MyOpenHelper_movie.RUNTIME_COL);
            int actorsCol = results.getColumnIndex(MyOpenHelper_movie.ACTOR_COL);
            int plotCol = results.getColumnIndex(MyOpenHelper_movie.PLOT_COL);
            int imageCol = results.getColumnIndex(MyOpenHelper_movie.IMAGE_COL);

            while(results.moveToNext()){
                long id = results.getInt(idCol);
                String title = results.getString(titleCol);
                String year = results.getString(yearCol);
                String rating = results.getString(ratingCol);
                String runtime = results.getString(runtimeCol);
                String actors = results.getString(actorsCol);
                String plot = results.getString(plotCol);
                String imageURL = results.getString(imageCol);
                data.add(new MovieData(title, year, rating, runtime, actors, plot, imageURL, id));
            }
            Collections.sort(data,(MovieData data1, MovieData data2)->{return data1.getTitle().compareTo(data2.getTitle());});

            return movieLayout;
        }

        private class MyRowViews extends RecyclerView.ViewHolder{
            TextView titleInfo;
            TextView timeInfo;
             int position = -1;
            ImageView imageURL;
//            TextView titleText;
//            TextView yearText;
//            ImageView imageURLText;
         //   int position = -1;

            public MyRowViews(View itemView) {
                super(itemView);
                titleInfo =  itemView.findViewById(R.id.movieName);
                timeInfo = itemView.findViewById(R.id.time);
                imageURL = itemView.findViewById(R.id.movieImage);

                itemView.setOnClickListener( click ->{
                    int chosenPosition = getAbsoluteAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
                    builder.setMessage(R.string.view_detail_movie )
                            .setTitle(R.string.choice_movie )
                            .setNegativeButton(R.string.delete_movie, (dialog, cl) ->{
                                MovieData removedData = data.get(chosenPosition);
                                data.remove(chosenPosition);
                                adapter.notifyItemRemoved(chosenPosition);

                                db.delete(MyOpenHelper_movie.TABLE_NAME, "_id=?", new String[]{Long.toString(removedData.getId())});

                                Snackbar.make(imageURL, "You deleted movie: " + removedData.getTitle(), Snackbar.LENGTH_LONG)
                                        .setAction(R.string.undo_movie, clk ->{
                                            data.add(chosenPosition, removedData);
                                            adapter.notifyItemInserted(chosenPosition);
                                            ContentValues values=new ContentValues();
                                            values.put(MyOpenHelper_movie.ID_COL,removedData.getId());
                                            values.put(MyOpenHelper_movie.TITLE_COL,removedData.getTitle());
                                            values.put(MyOpenHelper_movie.YEAR_COL,removedData.getYear());
                                            values.put(MyOpenHelper_movie.RATING_COL,removedData.getRating());
                                            values.put(MyOpenHelper_movie.RUNTIME_COL,removedData.getRuntime());
                                            values.put(MyOpenHelper_movie.ACTOR_COL,removedData.getActor());
                                            values.put(MyOpenHelper_movie.PLOT_COL,removedData.getPlot());
                                            values.put(MyOpenHelper_movie.IMAGE_COL,removedData.getImageURL());

                                            db.insert(MyOpenHelper_movie.TABLE_NAME,null,values);
                                        })
                                        .show();
                            })
                            .setPositiveButton(R.string.detail_movie, (dialog, cl) ->{
                                MovieData searchResult = data.get(chosenPosition);
                                int type = 2;
                                image = BitmapFactory.decodeFile(getContext().getFilesDir() + "/" + data.get(chosenPosition).getTitle() + ".png");
                                MovieInfo parentActivity = (MovieInfo) getContext();
                                parentActivity.userClickedMessage(searchResult,image, type);
                            })
                            .create().show();
                });
            }
            public void setPosition(int p) {
                position = p;
            }
        }

        private class MyMovieAdapter extends RecyclerView.Adapter<MyRowViews>{
            @Override
            public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = getLayoutInflater();
                int layoutID;
                layoutID=  R.layout.item_view_movie;

                View loadedRow = inflater.inflate(layoutID, parent, false);
                MyRowViews initRow = new MyRowViews(loadedRow);
                return initRow;
            }

            @Override
            public void onBindViewHolder(MyRowViews holder, int position) {
                holder.titleInfo.setText(data.get(position).getTitle());
                holder.timeInfo.setText(data.get(position).getYear());

                image = BitmapFactory.decodeFile(getContext().getFilesDir() + "/" + data.get(position).getTitle() + ".png");
                holder.imageURL.setImageBitmap(image);
                holder.setPosition(position);
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        }
    }


