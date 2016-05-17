package tz.co.delis.www.moxidriver.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import tz.co.delis.www.moxidriver.R;

/**
 * Created by apple on 5/12/16.
 */
public class Profile extends Fragment {


    @Bind(R.id.Appstatus)
    TextView appStatusText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //cooommmeennnttt


        return inflater.inflate(R.layout.profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //appStatusText.setText("");
    }
}
