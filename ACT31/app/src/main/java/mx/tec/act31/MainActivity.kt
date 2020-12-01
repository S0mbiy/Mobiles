package mx.tec.act31

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: FoodAdapter
    val food:String = "[\n" +
            "{'name':'burger', 'price':15, 'description':'a juicy burger!'},\n" +
            "{'name':'hotdog', 'price':20, 'description':'an ok hot dog'},\n" +
            "{'name':'tacos', 'price':12, 'description':'some pretty good tacos'},\n" +
            "{'name':'torta', 'price':22, 'description':'nice torta'},\n" +
            "{'name':'carne asada', 'price':50, 'description':'a great carne asada'}\n" +
            "]"
    val foodJson: JSONArray = JSONArray(food)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        RECYCLER.layoutManager = linearLayoutManager


        adapter = FoodAdapter(food, this)
        RECYCLER.adapter = adapter
    }

    override fun onClick(v: View?) {
        val pos: Int? = v?.let { RECYCLER.getChildLayoutPosition(it) }
        val meal: JSONObject? = pos?.let { foodJson.getJSONObject(it) }
        Toast.makeText(applicationContext, meal?.getString("description"),Toast.LENGTH_SHORT).show()
    }
}