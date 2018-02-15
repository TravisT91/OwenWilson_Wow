package com.example.travistkachuk.giftester;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends Activity {
    int[] wows = {R.raw.deep_wow,R.raw.gentle_wow,R.raw.loud_wow,R.raw.uplifting_wow,R.raw.whisper_wow};
    private Disposable disposable;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView gifContainer = findViewById(R.id.imageView);
        RequestOptions options = new RequestOptions()
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(this)
                .load(R.drawable.wow_img)
                .apply(options)
                .into(gifContainer);

        disposable = RxView.clicks(gifContainer)
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        playRandomAudio();
                    }
                });

    }

    private void playRandomAudio() {
        try {
            final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),getRandomWow());
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {mp.release();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    int getRandomWow(){
        return wows[random.nextInt(wows.length + 1)];
    }
}
