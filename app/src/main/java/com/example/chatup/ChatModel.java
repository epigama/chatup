package com.example.chatup;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatModel extends AbstractItem<ChatModel, ChatModel.ViewHolder> {

    public ChatModel(String name, String message, String time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String name;
    String message;
    String time;

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.framelayoutparent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.chat_item;
    }


    protected static class ViewHolder extends FastAdapter.ViewHolder<ChatModel> {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.message)
        TextView message;

        @BindView(R.id.time)
        TextView time;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(ChatModel item, List<Object> payloads) {
            StringHolder.applyTo(new StringHolder(item.name), name);
            StringHolder.applyTo(new StringHolder(item.message), message);
            StringHolder.applyTo(new StringHolder(item.time), time);

        }

        @Override
        public void unbindView(ChatModel item) {
            name.setText(null);
            message.setText(null);
            time.setText(null);
        }
    }
}
