package mx.tec.sergioalvaradoparcial2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PerritoAdapter extends RecyclerView.Adapter<PerritoAdapter.PerritoViewHolder> {

    public static class PerritoViewHolder extends RecyclerView.ViewHolder {

        public TextView text1, text2, text3;

        public PerritoViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.textView);
            text2 = itemView.findViewById(R.id.textView2);
            text3 = itemView.findViewById(R.id.textView3);
        }
    }

    private JSONArray dogs;
    private View.OnClickListener listener;

    public PerritoAdapter(JSONArray dogs, View.OnClickListener listener){
        this.dogs = dogs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PerritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.perrito_row, parent, false);

        v.setOnClickListener(listener);

        PerritoViewHolder pvh = new PerritoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PerritoViewHolder holder, int position) {
        try {
            JSONObject dog = dogs.getJSONObject(position);
            holder.text1.setText(dog.getString("nombre"));
            holder.text2.setText(dog.getString("raza"));
            holder.text3.setText(dog.getString("edad"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.dogs.length();
    }
}