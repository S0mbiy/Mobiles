package mx.tec.sep23;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        // View Holder - class in charge of dealing with a view
        // corresponding to a single element (row)

        public TextView text1, text2;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.textView3);
            text2 = itemView.findViewById(R.id.textView4);
        }
    }

    // constructor
    // we ALWAYS need a data source

    private JSONArray friends;
    private View.OnClickListener listener;

    public FriendAdapter(String friends, View.OnClickListener listener){
        try {
            this.friends = new JSONArray(friends);
            Log.wtf("Datos", this.friends.toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // parse / translate from XML to Java Object
        View v = (View) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        v.setOnClickListener(listener);

        FriendViewHolder fvh = new FriendViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        try {
            JSONObject friend = friends.getJSONObject(position);
            holder.text1.setText(friend.getString("nombre"));
            holder.text2.setText(friend.getString("hobby"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.friends.length();
    }


}
