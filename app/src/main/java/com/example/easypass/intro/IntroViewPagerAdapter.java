package com.example.easypass.intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.easypass.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    List<IntroScreen> mScreenList;
    Context mContext;

    public IntroViewPagerAdapter(List<IntroScreen> mScreenList, Context mContext) {
        this.mScreenList = mScreenList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mScreenList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_intro, null);

        TextView desc = layoutScreen.findViewById(R.id.introDescText);
        ImageView img = layoutScreen.findViewById(R.id.imageIntro);

        desc.setText(mScreenList.get(position).getIntroDescription());
        img.setImageResource(mScreenList.get(position).getIntroImage());

        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
