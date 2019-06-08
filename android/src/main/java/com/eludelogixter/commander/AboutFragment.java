package com.birsan.commander;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class AboutFragment extends Fragment {

    public static final String TAG = "About_Fragment.TAG";

    // Factory method Pairing_Fragment
    // No constructor overload allowed
    public static AboutFragment newInstance() {

        // Create a new instance of this very object

        AboutFragment frag = new AboutFragment();
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _saveInstanceState) {

        // Create and return a view for this fragment
        View view = _inflater.inflate(R.layout.about_fragment, _container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // Forces the orientation in portrait mode
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        // Display and set a textview
//        TextView tx = (TextView)getView().findViewById(R.id.text);
//        tx.setText("Lorem ipsum dolor sit amet, vel in paulo nihil. " +
//                "Sed ea nostro mollis intellegebat, mea voluptua deseruisse ea, " +
//                "at has detracto consulatu. Sanctus minimum vix ne, aeque audiam nam at, " +
//                "ut dicam maiestatis pro. Eu duo sale assum dissentiunt, " +
//                "nominati accommodare te nec. Tale affert sea ex, aliquip inimicus sea ad, " +
//                "duo populo expetenda principes cu.");
    }




}
