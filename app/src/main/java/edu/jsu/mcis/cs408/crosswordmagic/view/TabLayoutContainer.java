package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.jsu.mcis.cs408.crosswordmagic.R;

public class TabLayoutContainer extends Fragment{

    public TabLayoutContainer(){
        super(R.layout.tab_container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 pager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        pager.setAdapter(new FragmentStateAdapter(this){
            @NonNull
            @Override
            public Fragment createFragment(int position){
                if (position == 0) return new PuzzleFragment();
                else return new ClueFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy(){
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0){
                    tab.setText(R.string.puzzle_tab);
                }else{
                    tab.setText(R.string.clue_tab);
                }
            }
        }).attach();
    }
}
