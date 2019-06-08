package com.birsan.commander;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ControlFragment extends Fragment {
    public interface CatchIntent {
        public ConnectThread catchMyThread();

        public void backHome();
    }

    public static final String TAG = "Control_Fragment.TAG";
    protected static Boolean soundFlag = true;
    private boolean pressed = false;
    private CatchIntent catchIntent;
    private ConnectedThread dataControl;
    private Vibrator vibrator;
    private MediaPlayer mplayer;
    private SharedPreferences sharedPref;


    // Factory method Pairing_Fragment
    // No constructor overload allowed
    public static ControlFragment newInstance() {

        // Create a new instance of this very object

        ControlFragment frag = new ControlFragment();
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _saveInstanceState) {

        // Create and return a view for this fragment
        View view = _inflater.inflate(R.layout.control_fragment, _container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // Instantiate the local interface so it allows calling internal methods
        catchIntent = (CatchIntent) getActivity();

        // Set the flagSound to the value extracted from shared preferences
        sharedPref = getActivity().getSharedPreferences("sound_on", 0);
        readPreferences();

        //Log.i("readPreferences = ", readPreferences());


        // Instantiate the vibrator
        vibrator = (Vibrator) getActivity().getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);

        // Instantiate a MediaPlayer object
        mplayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.click_btn);

        // Wait for ConnectedThread to initialize
        //while(PairingActivity.connectThread.getManageConnection() == null);
        dataControl = PairingActivity.connectThread.getManageConnection();

        // Target and implement the seekBar (speed_widget)
        SeekBar speedWidget = (SeekBar) getView().findViewById(R.id.speedWidget);

        // Create a new object of type SeekBarControl and reuse it througout
        //final SeekBarControl seekControl = new SeekBarControl();

        // Create an event listener for the seek bar
        speedWidget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                //Log.i("Value i", String.valueOf(i));

                //if(dataControl.getMmSocket().isConnected()) {
                if (i % 25 == 0) {
                    vibrator.vibrate(30);

                }

//                i = ((int)Math.round(i/stepSize))*stepSize;
//                seekBar.setProgress(i);
//                Log.i("i after the Math ", String.valueOf(i));

                if (i <= 5) {

                    dataControl.write("0".getBytes());
                }
                if (i >= 20 && i <= 25) {

                    //seekBar.setProgress(25);

                    // Vibrate

                    dataControl.write("2".getBytes());

                }


                if (i >= 45 && i <= 55) {

                    //seekBar.setProgress(50);

                    // Vibrate
                    //vibrator.vibrate(50);
                    dataControl.write("5".getBytes());

                }

                //*******************DONT FORGET TO COMMENT OUT FOR ARDUINO*************************

                 if (i >= 65 && i <= 75) {

                 //seekBar.setProgress(50);

                 // Vibrate
                 //vibrator.vibrate(50);
                 dataControl.write("7".getBytes());

                 }

                 if (i > 90) {

                 //seekBar.setProgress(50);

                 // Vibrate
                 //vibrator.vibrate(50);
                 dataControl.write("9".getBytes());

                 }

                 //********************************************************************************/

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                dataControl.write("x".getBytes());

            }
        });

        //final ButtonControl btnControl = new ButtonControl();

        // Target and implement powerBtn
        ImageButton powerBtn = (ImageButton) getView().findViewById(R.id.power_btn);

        //dataControl = PairingActivity.connectThread.getManageConnection();
        powerBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Call the vibrator
                    vibrator.vibrate(200);

                    // Call the mplayer

                    if (soundFlag) {

                        mplayer.start();
                    }


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Release the media-player object on power-button
                    mplayer.release();
                    mplayer = null;
                    catchIntent.backHome();
                }
                return true;
            }
        });

        // Target and implement leftButton
        final ImageButton leftBtn = (ImageButton) getView().findViewById(R.id.l_button);

        leftBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (soundFlag) {

                        mplayer.start();
                    }


                    // Call the vibrator
                    vibrator.vibrate(50);

                    dataControl.write("l".getBytes());


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    dataControl.write("s".getBytes());

                }

                return true;
            }
        });

        // Target and implement rightButton
        ImageButton rightBtn = (ImageButton) getView().findViewById(R.id.r_button);

        //dataControl = PairingActivity.connectThread.getManageConnection();
        rightBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    // Call the vibrator
                    vibrator.vibrate(50);


                    if (soundFlag) {

                        mplayer.start();
                    }


                    dataControl.write("r".getBytes());


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    dataControl.write("s".getBytes());

                }

                return true;
            }
        });

        // Target and implement forwardBtn
        ImageButton forwardBtn = (ImageButton) getView().findViewById(R.id.f_button);

        //dataControl = PairingActivity.connectThread.getManageConnection();
        forwardBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    if (soundFlag) {

                        mplayer.start();
                    }

                    // Call the vibrator
                    vibrator.vibrate(50);

                    // Send data to the arduino
                    dataControl.write("f".getBytes());


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    dataControl.write("s".getBytes());

                }


                return true;
            }
        });

        // Target and implement forwardBtn
        ImageButton backBtn = (ImageButton) getView().findViewById(R.id.b_button);

        //dataControl = PairingActivity.connectThread.getManageConnection();
        backBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    // Call the vibrator
                    vibrator.vibrate(50);

                    if (soundFlag) {

                        mplayer.start();
                    }


                    //btnControl.execute("b");
                    dataControl.write("b".getBytes());


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    dataControl.write("s".getBytes());

                }

                return true;
            }
        });

    }

    protected boolean readPreferences() {

        try {

            // Set the soundFlag static property to the value that was saved in sharedPref
            soundFlag = sharedPref.getBoolean("sound_on", Boolean.parseBoolean(null));

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            Log.i("readPreferences error", "can't read from sharedPref file");

            return false;
        }
    }

}
