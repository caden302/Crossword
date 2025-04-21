package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import java.beans.PropertyChangeEvent;

public interface AbstractView {

    public abstract void modelPropertyChange(final PropertyChangeEvent evt);


}