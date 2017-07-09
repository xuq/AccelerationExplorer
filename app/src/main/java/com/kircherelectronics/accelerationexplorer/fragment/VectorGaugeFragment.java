package com.kircherelectronics.accelerationexplorer.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kircherelectronics.accelerationexplorer.R;
import com.kircherelectronics.accelerationexplorer.gauge.GaugeAcceleration;
import com.kircherelectronics.accelerationexplorer.gauge.GaugeVector;
import com.kircherelectronics.accelerationexplorer.viewmodel.AccelerationViewModel;

/**
 * Created by kaleb on 7/9/17.
 */

public class VectorGaugeFragment extends LifecycleFragment {

    private GaugeVector gaugeVector;
    private Handler handler;
    private Runnable runnable;

    private float[] acceleration;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AccelerationViewModel model = ViewModelProviders.of(getActivity()).get(AccelerationViewModel.class);

        model.getAccelerationListener().observe(this, new Observer<float[]>() {
            @Override
            public void onChanged(@Nullable float[] floats) {
                acceleration = floats;
            }
        });

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                updateAccelerationGauge();
                handler.postDelayed(this, 20);
            }
        };

        acceleration = new float[4];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vector_gauge, container, false);

        gaugeVector = (GaugeVector) view.findViewById(R.id.gauge_vector);

        return view;
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    private void updateAccelerationGauge() {
        gaugeVector.updatePoint(acceleration[0], acceleration[1]);
    }
}
