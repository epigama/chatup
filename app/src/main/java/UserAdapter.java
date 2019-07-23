import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.DisplayUsers;
import com.example.chatup.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    private Context context;
    private List<DisplayUsers>  users;

    public UserAdapter(Context context,List<DisplayUsers> users){
        this.context=context;
        this.users=users;

    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public TextView user_name;
        public ImageView profile_image;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            user_name=itemView.findViewById(R.id.UserName);
            profile_image=itemView.findViewById(R.id.profile_image);

        }
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
      DisplayUsers user=users.get(position);
      holder.user_name.setText((CharSequence) user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
