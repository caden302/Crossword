package edu.jsu.mcis.cs408.crosswordmagic.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.model.CrosswordMagicModel;

public class MainActivity extends AppCompatActivity {
    private CrosswordMagicController controller;
    private CrosswordMagicModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Integer puzzleid = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            puzzleid = extras.getInt("puzzleid");
        }

        CrosswordMagicModel model = new CrosswordMagicModel(this, puzzleid);

        controller = new CrosswordMagicController(this);
    }

    public CrosswordMagicController getController() {
        return controller;
    }
}