package com.deal.monk.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager.BadTokenException;

import com.deal.monk.R;

public class DealmonkProgressDialog {
	
	 public static ProgressDialog createProgressDialog(Context mContext) {
         ProgressDialog dialog = new ProgressDialog(mContext);
         try {
                 dialog.show();
         } catch (BadTokenException e) {

         }
                
         dialog.setCancelable(false);
         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
         dialog.setContentView(R.layout.dialog_progress);
     
         // dialog.setMessage(Message);
         return dialog;
 }

}
