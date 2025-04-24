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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.MenuActivityBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.DAOFactory;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.WebServiceDAO;
import edu.jsu.mcis.cs408.crosswordmagic.R;

public class MenuActivity extends AppCompatActivity implements AbstractView {

    private final String TAG = "MenuActivity";
    private CrosswordMagicController controller;
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
                Log.d("NameofPuzzle",jsonObject.getString("id") + jsonObject.getString("name"));
            }
            adapter = new PuzzleListAdapter(this, puzzleList);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("Error getting list", e.toString());
        }
        binding.downloadAndPlayBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int originalPuzzleId = adapter.getPuzzleIDFromTextview();

                controller = new CrosswordMagicController(MenuActivity.this);
                int newId = controller.callDownload(originalPuzzleId) + 1;

                Log.d("PuzzleId", "Puzzleid when pressing downloadandplay: " + newId);

                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("puzzleid", newId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(controller.PUZZLE_DOWNLOADED_PROPERTY)){
            int puzzleid = (Integer) evt.getNewValue();
        }
    }
}
