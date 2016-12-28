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
    static final String[] fortunes={"오늘은 수업이 휴강할지도 모르겠네요. 메일과 공지 잘 확인해보세요!",
            "당신의 친구분이 세미클론을 그리스어 ;로 바꾸는 짓궂은 장난을 할 거 같네요. 디버깅을 꼭 하시길 바랍니다! 친구분 딱밤 한 대 정도는 때려주세요~",
    "오늘 컴퓨터가 갑자기 꺼질 것 같은 날이네요~. 오늘만큼은 ctrl+s를 습관화하세요!",
    "얼근 플메에게 용기를 주세요. 지금 (수강취소/휴학)서를 내러 창의관으로 가고 있는지도 몰라요.",
    "실수로 프로젝트가 klms에 저장이 안되었네요. 조교님께 한번 빌어보세요. 씨알도 안먹히겠지만요.",
    "하루종일 에러가 가득하던 코드가 갑자기 왠지 모르게 돌아갈 것 같은 날이네요!",
    "오늘은 무엇을 해도 다 이루어질 것으로 보입니다. 당장 컴퓨터 앞으로 뛰어가 안되던 코딩을 시작해보는건 어떤가요?",
    "어라? 오늘 조모임을 가면 플메가 코딩을 다 끝내놨다고 할 것 같네요. 이쁜 플메한테 밥이라도 사주는게 어떤가요?"};



    static int i;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        i = getIntent().getIntExtra("fortune", 0);
        setContentView(R.layout.fortune);


        TextView tt= (TextView)findViewById(R.id.textView);

        tt.setText(fortunes[i%fortunes.length]);
    }


    public static class gifView extends Fragment {
        String[] gifs={"happyryan.gif", "sadryan.gif", "computerryan.gif","cheerupryan.gif", "smhryan.gif", "thumbupryan.gif",
                "goodryan.gif", "jumpryan.gif"};

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            InputStream stream=null;
            try{
                stream=getActivity().getAssets().open(gifs[i%gifs.length]);
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
                mMovie.draw(canvas,canvas.getWidth()/2-50,0);
                this.invalidate();
            }


        }

    }

}