package com.example.chatup.Adapters;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;

public class User extends AbstractItem<User, User.ViewHolder> {

    String userName;

    @NonNull
    @Override
    public User.ViewHolder getViewHolder(View v) {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    class ViewHolder extends FastAdapter.ViewHolder<User>{
        @BindView(R.id.username)
        TextView userName;

        @BindView(R.id.profile_image)
        ImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(User item, List<Object> payloads) {
            
        }

        @Override
        public void unbindView(User item) {

        }
    }
}
