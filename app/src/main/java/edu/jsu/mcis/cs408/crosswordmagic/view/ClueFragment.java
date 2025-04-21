package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;
import java.util.List;
import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;

public class ClueFragment extends Fragment implements AbstractView{
    private CrosswordMagicController controller;

    public ClueFragment() {
        super(R.layout.fragment_clue);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_clue, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.controller = ((MainActivity)getContext()).getController();
        controller.addView(this);
        controller.getClues();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt){
        View view = getView();
        if (view == null) return;

        if (controller.CLUES_ACROSS.equals(evt.getPropertyName())) {
            List<String> acrossClues = (List<String>) evt.getNewValue();
            updateClueColumn(view.findViewById(R.id.aContainer), acrossClues);
        }
        else if (controller.CLUES_DOWN.equals(evt.getPropertyName())) {
            List<String> downClues = (List<String>) evt.getNewValue();
            updateClueColumn(view.findViewById(R.id.dContainer), downClues);
        }
    }

    private void updateClueColumn(TextView container, List<String> clues){
        StringBuilder stringBuilder = new StringBuilder();
        for (String clue : clues) {
            stringBuilder.append(clue).append("\n");
        }
        container.setText(stringBuilder.toString());
    }
}