package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.R;

public class PuzzleFragment extends Fragment implements AbstractView{

    private CrosswordMagicController controller;

    public PuzzleFragment() {
        super(R.layout.fragment_puzzle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_puzzle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.controller = ((MainActivity)getContext()).getController();
        controller.addView(this);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt){
        //I didn't end up using but it got mad if I didn't have.
    }

}
