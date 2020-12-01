package mx.tec.sergioalvaradoparcial2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;


public class PerritoFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM = "data";

    private RecyclerView recyclerView;
    private JSONArray data;

    public PerritoFragment() {
        // Required empty public constructor
    }

    public static PerritoFragment newInstance(String data) {
        PerritoFragment pf = new PerritoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, data);
        pf.setArguments(args);
        return pf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam = getArguments().getString(ARG_PARAM);
            try {
                this.data = new JSONArray(mParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perrito, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        PerritoAdapter pa = new PerritoAdapter(data, this);
        recyclerView.setAdapter(pa);
        return v;
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        try {
            Toast.makeText(getActivity(), data.getJSONObject(pos).getString("nombre"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}