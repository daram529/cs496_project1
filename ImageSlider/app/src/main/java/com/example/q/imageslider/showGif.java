package com.example.q.imageslider;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by q on 2016-12-28.
 */

public class showGif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fortune);


        TextView tt= (TextView)findViewById(R.id.textView);
                tt.setText(getIntent().getStringExtra("fortune"));

    }
    public static class gifView extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            InputStream stream=null;
            try{
                stream=getActivity().getAssets().open("loading.gif");
            }catch(IOException o){
                o.printStackTrace();
            }
            GifMovieView view=new GifMovieView(getActivity(), stream);
            return view;
        }



        public class GifMovieView extends View {
            private Movie mMovie;
            private long mMoviestart;

            public GifMovieView(Context context, InputStream stream){
                super(context);
                mMovie=Movie.decodeStream(stream);
            }
            @Override
            protected void onDraw(Canvas canvas){
                canvas.drawColor(Color.TRANSPARENT);
                super.onDraw(canvas);
                final long now= SystemClock.uptimeMillis();
                if(mMoviestart ==0){
                    mMoviestart=now;
                }
                final int relTime = (int)((now-mMoviestart)%mMovie.duration());
                mMovie.setTime(relTime);
                mMovie.draw(canvas,0,0);
                this.invalidate();
            }


        }

    }

}