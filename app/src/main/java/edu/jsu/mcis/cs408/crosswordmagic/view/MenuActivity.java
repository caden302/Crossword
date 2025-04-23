package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.jsu.mcis.cs408.crosswordmagic.databinding.MenuActivityBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.DAOFactory;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.WebServiceDAO;
import edu.jsu.mcis.cs408.crosswordmagic.R;

public class MenuActivity extends AppCompatActivity {

    private final String TAG = "MenuActivity";
    private MenuActivityBinding binding;
    private RecyclerView recyclerView;
    private PuzzleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MenuActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recycler_view_puzzles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            DAOFactory daoFactory = new DAOFactory(this);
            WebServiceDAO dao = daoFactory.getWebServiceDAO();
            JSONArray json = dao.list();
            ArrayList<PuzzleListItem> puzzleList = new ArrayList<>();
            puzzleList.clear();
            for (int i = 0; i < json.length(); i++) {
                String jsonString = json.getString(i);
                JSONObject jsonObject = new JSONObject(jsonString);
                puzzleList.add(new PuzzleListItem(Integer.parseInt(jsonObject.getString("id")), jsonObject.getString("name")));
            }
            adapter = new PuzzleListAdapter(this, puzzleList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching puzzle list", e);
        }
        binding.downloadAndPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("puzzleid", 1);
                startActivity(intent);
            }
        });
    }
}
