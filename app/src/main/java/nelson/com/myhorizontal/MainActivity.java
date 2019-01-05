package nelson.com.myhorizontal;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nelson.com.horizontallibrary.HorizontalWithIndicator;

public class MainActivity extends AppCompatActivity {
    List<String> data = new ArrayList<>();
    private List<View> imageViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = LayoutInflater.from(this).inflate(R.layout.test,null);
        imageViews.add(view);
        View view1 = LayoutInflater.from(this).inflate(R.layout.test1,null);
        imageViews.add(view1);
        View view2 = LayoutInflater.from(this).inflate(R.layout.test2,null);
        imageViews.add(view2);
        Log.i("nelson","CustomHorizontalScroll");
        List<String> list = new ArrayList<>();
         list.add("Item-1");
        list.add("Item-2");
        list.add("Item-3");
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new A());
        HorizontalWithIndicator pHorizontalView = findViewById(R.id.scro);
        pHorizontalView.bindTitles(list);
        pHorizontalView.bindVp(viewPager);
        pHorizontalView.setOnPageChangeListener(new HorizontalWithIndicator.OnPageChangeListener() {

            @Override
            public void onSelected(int position) {

            }
        });

    }

    class A extends PagerAdapter {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(imageViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = imageViews.get(position);
            container.addView(view);
            return view;
        }
    }
}
