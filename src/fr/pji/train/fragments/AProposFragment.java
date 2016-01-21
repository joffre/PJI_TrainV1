package fr.pji.train.fragments;

import fr.pji.train.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AProposFragment extends Fragment {
	
	private View view;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_apropos, null);

        return view;
    }
	
	public static AProposFragment newInstance() {
        return new AProposFragment();
    }

}
