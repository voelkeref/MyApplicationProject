package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.widget.ArrayAdapter;

public class AnimalSlider extends FragmentActivity
{
    private static final int[] IMAGES = { R.drawable.i1, R.drawable.i2,R.drawable.i3 };
    private int mPosition = 0;
    private ImageSwitcher mImageSwitcher;
    FragmentActivity g = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            AnimFragment fv = new AnimFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fv);
            ft.commit();
        }
    }

    public void onSwitch(View view,int m) {
        mPosition = (mPosition + m) % IMAGES.length;
        if(mPosition < 0)
        {
            mPosition = 2;
        }
        mImageSwitcher.setImageResource(IMAGES[mPosition]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
    }

    public class AnimFragment extends Fragment
    {
        private Button btnClick;
        private Button btnCa;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_animslider, container, false);
            mImageSwitcher = (ImageSwitcher) rootView.findViewById(R.id.imageSwitcher1);
            mImageSwitcher.setFactory(new ViewFactory() {
                @Override
                public View makeView() {
                    ImageView imageView = new ImageView(AnimalSlider.this);
                    return imageView;
                }
            });
            mImageSwitcher.setInAnimation(g, android.R.anim.fade_in);
            mImageSwitcher.setOutAnimation(g, android.R.anim.fade_out);
            mImageSwitcher.setOnTouchListener(new OnSwipeTouchListener(g)
            {
                public void onSwipeRight() {
                    onSwitch(null,1);
                }

                public void onSwipeLeft() {
                    onSwitch(null,1);
                }
            });

            btnClick = (Button) rootView.findViewById(R.id.logoutb1);
            btnClick.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(AnimalSlider.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            btnCa = (Button) rootView.findViewById(R.id.buttonca);
            btnCa.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onSwitch(null,1);
                }
            });

            Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerc);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(g,R.array.animals_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            onSwitch(null,0);
            return rootView;
        }
    }

    public class OnSwipeTouchListener implements OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        public boolean onTouch(final View v, final MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeBottom();
                            } else {
                                onSwipeTop();
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }
}
