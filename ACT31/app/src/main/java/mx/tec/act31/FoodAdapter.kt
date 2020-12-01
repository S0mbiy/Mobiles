package mx.tec.act31;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray

import org.json.JSONObject;

class FoodAdapter(food: String, private val  listener: View.OnClickListener) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val price: TextView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.NAME)
            price = view.findViewById(R.id.PRICE)
        }
    }

    private val foodJson: JSONArray = JSONArray(food);

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FoodViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row, viewGroup, false)
        view.setOnClickListener(listener);
        return FoodViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: FoodViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val meal:JSONObject = foodJson.getJSONObject(position)
        viewHolder.name.text = meal.getString("name")
        viewHolder.price.text = meal.getString("price")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = foodJson.length()

}


