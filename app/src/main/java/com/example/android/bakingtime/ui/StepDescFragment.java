package com.example.android.bakingtime.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Fragment class to handle the recipe step descriptions.
 */
public class StepDescFragment extends Fragment {

    @BindView(R.id.step_desc_tv)
    TextView mDescTV;
    String desc;

    public StepDescFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_desc, container, false);
        ButterKnife.bind(this, rootView);
          if(savedInstanceState==null){
              if(!desc.isEmpty()){
                  mDescTV.setText(desc);
              }
          }
        return rootView;
    }



    public void setStepDescData(String desc) {
        this.desc = desc;
        if(mDescTV!=null){
            mDescTV.setText(desc);
        }

    }
}
