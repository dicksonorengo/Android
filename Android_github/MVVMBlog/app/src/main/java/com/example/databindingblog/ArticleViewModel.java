package com.example.databindingblog;

import com.example.databindingblog.BR;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class ArticleViewModel extends BaseObservable {

    private Article mArticle;
    private Context mContext;

    public ArticleViewModel(Article mArticle, Context mContext) {
        this.mArticle = mArticle;
        this.mContext = mContext;
    }

    @Bindable
    public String getTitle() {
        return mArticle.getTitle();
    }

    public void setTitle(String title) {
        mArticle.setTitle(title);
        notifyPropertyChanged(BR.title);
    }

    public int getCardBackgroundColor() {
        return mArticle.isHighlight() ?
                ContextCompat.getColor(mContext, R.color.highlight) :
                Color.parseColor("#ffffffff");
    }

    public int getCommentsButtonVisibility() {
        return mArticle.getCommentsNumber() == 0 ?
                View.GONE : View.VISIBLE;
    }

    public int getCommentsNumber() {
        return mArticle.getCommentsNumber();
    }

    public String getExcerpt() {
        return mArticle.getExcerpt();
    }

    public String getImageUrl() {
        return mArticle.getImageUrl();
    }

    @BindingAdapter({"image"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).centerCrop().into(view);
    }

    public void setRead(boolean read) {
        // change title of already read article:
        if (read && !mArticle.isRead()) {
            setTitle("READ: " + getTitle());
        }

        mArticle.setRead(read);
    }

    public View.OnClickListener onReadMoreClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opens article detail", Toast.LENGTH_SHORT).show();
                setRead(true);
            }
        };
    }

    public View.OnClickListener onCommentsClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opens comments detail", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
