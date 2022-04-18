package com.inmersoft.unityfragmenttest;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.unity3d.player.UnityPlayer;
 // Other imports available in full code linked to below
 
 public class UnityPlayerNativeFragment extends Fragment
 {
     // Changes in this class:
     // 1- 'this' references changed to "getActivity()"
     // 2- onCreate -> onCreateView
     // 3- protected -> public in function names
     // 4- @Override added before function calls
     // 5- newInstance and onAttach added
     protected UnityPlayer mUnityPlayer;        // don't change the name of this variable; referenced from native code
     private static final String ARG_SECTION_NUMBER = "section_number";
 
     public static UnityPlayerNativeFragment newInstance(int sectionNumber) {
         UnityPlayerNativeFragment fragment = new UnityPlayerNativeFragment();
         Bundle args = new Bundle();
         args.putInt(ARG_SECTION_NUMBER, sectionNumber);
         fragment.setArguments(args);
         return fragment;
     }
 
 
     @Override
     public void onAttach(Activity activity) {
         super.onAttach(activity);
         ((MainActivity) activity).onSectionAttached(
                 getArguments().getInt(ARG_SECTION_NUMBER));
     }
 
     // UnityPlayer.init() should be called before attaching the view to a layout - it will load the native code.
     // UnityPlayer.quit() should be the last thing called - it will unload the native code.
     
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
     {
         //below line removed because it was causing errors
         //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
 
         getActivity().getWindow().takeSurface(null);
         getActivity().setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
         getActivity().getWindow().setFormat(PixelFormat.RGB_565);
 
         mUnityPlayer = new UnityPlayer(getActivity());
         if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
             getActivity().getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
 
         int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
         boolean trueColor8888 = false;
         mUnityPlayer.init(glesMode, trueColor8888);
 
         View playerView = mUnityPlayer.getView();
         return playerView;
     }
 
     @Override
     public void onDestroy ()
     {
         mUnityPlayer.quit();
         super.onDestroy();
     }
 
     // onPause()/onResume() must be sent to UnityPlayer to enable pause and resource recreation on resume.
     @Override
     public void onPause()
     {
         super.onPause();
         mUnityPlayer.pause();
     }
 
     @Override
     public void onResume()
     {
         super.onResume();
         mUnityPlayer.resume();
     }
 
     @Override
     public void onConfigurationChanged(Configuration newConfig)
     {
         super.onConfigurationChanged(newConfig);
         mUnityPlayer.configurationChanged(newConfig);
     }
 }