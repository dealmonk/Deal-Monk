package com.deal.monk;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deal.monk.fragments.Check_in_Fragment;
import com.deal.monk.fragments.Check_in_fragment2;
import com.deal.monk.fragments.Right_here_Fragment;
import com.deal.monk.fragments.Search_Fragment;
import com.deal.monk.fragments.Search_fragment1;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.R;


public class Deals extends android.support.v4.app.Fragment {
	
public Deals(){}

    com.deal.monk.utility.GPSTracker gps;
    FrameLayout frame_search ;
    FrameLayout frame_righthere ;
    FrameLayout frame_CheckIn ;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.deals, container, false);
        
        frame_search = (FrameLayout)rootView.findViewById(R.id.My_Container_1_ID);
		frame_righthere = (FrameLayout)rootView.findViewById(R.id.My_Container_2_ID);
		frame_CheckIn = (FrameLayout)rootView.findViewById(R.id.My_Container_3_ID);
		
		android.support.v4.app.Fragment frg =new Search_Fragment();//create the fragment instance for the top fragment
        android.support.v4.app.Fragment frg1=new Right_here_Fragment();//create the fragment instance for the middle fragment
        android.support.v4.app.Fragment frg2=new Check_in_Fragment();//create the fragment instance for the bottom fragment
        	
        
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
        fragmentTransaction.add(R.id.My_Container_1_ID, frg, "Frag_Top_tag");        
        fragmentTransaction.add(R.id.My_Container_2_ID, frg1, "Frag_Middle_tag");
        fragmentTransaction.add(R.id.My_Container_3_ID, frg2, "Frag_Bottom_tag");       
        fragmentTransaction.commit();
        
        
          frame_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 android.support.v4.app.Fragment s1  = new Search_fragment1();
			     android.support.v4.app.FragmentManager fm = getFragmentManager();
			     android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
			     ft.replace(R.id.My_Container_1_ID, s1);
			     ft.commit();
				
			}
		});
          
          frame_righthere.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent select = new Intent (getActivity(),Select.class);	
//				Bundle bundle = new Bundle();
//				bundle.putBoolean("IsFromRightHereRightnow", true);
				select.putExtra("IsFromRightHereRightnow", true);
				startActivity(select);
				
			}
		});
          
          
          frame_CheckIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(getActivity());
				String Main_USER_ID = dealMonkPreferences.getString("MAIN_USER_ID");
				
				if (Main_USER_ID == null) {
                    
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        switch (which){
					        case DialogInterface.BUTTON_POSITIVE:
					            //Yes button clicked
					        	Intent login =new Intent (getActivity(),Login.class);
					        	startActivity(login);					        	
					            break;

					        case DialogInterface.BUTTON_NEGATIVE:
					            //No button clicked
					            break;
					        }
					    }
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Please login first to check-in").
					setIcon(R.drawable.ic_launcher).setTitle("Please Login").setPositiveButton("OK", dialogClickListener)
					    .setNegativeButton("Cancel", dialogClickListener).show();
					
				} else {
					
					 android.support.v4.app.Fragment s1  = new Check_in_fragment2();
				     android.support.v4.app.FragmentManager fm = getFragmentManager();
				     android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
				     ft.replace(R.id.My_Container_3_ID, s1);
				     ft.commit();				
				}
					
			}
		});
       
 
         
        return rootView;
    }

}
