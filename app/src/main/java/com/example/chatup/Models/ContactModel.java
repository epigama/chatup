package com.example.chatup.Models;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.TextView;

import com.example.chatup.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactModel extends AbstractItem<ContactModel, ContactModel.ViewHolder> {
        String name;

        public String getPhotoUri() {
            return photoUri;
        }

        public void setPhotoUri(String photoUri) {
            this.photoUri = photoUri;
        }

        String photoUri;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        String email;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        String number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public ContactModel(String message, String number, String email, String photoUri){
            this.name = message;
            this.number = number;
            this.email = email;
            this.photoUri = photoUri;
        }

        public ContactModel(){}

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
            return R.layout.activity_contact_model;
        }


        protected static class ViewHolder extends FastAdapter.ViewHolder<ContactModel> {
            @BindView(R.id.text_name)
            TextView name;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            @Override
            public void bindView(ContactModel item, List<Object> payloads) {
                StringHolder.applyTo(new StringHolder(item.name), name);
            }

            @Override
            public void unbindView(ContactModel item) {
                name.setText(null);
            }
        }
    }

