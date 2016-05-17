package tz.co.delis.www.moxidriver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by apple on 3/29/16.
 */
public class InformationDialog extends DialogFragment {

   private String Dtitle;
   private String Dmessage;
   private String PBtnTxt;
   private String status;

    // when status is con re try connecton ....



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(status=="con") {

                    builder.setTitle(Dtitle)
                    .setMessage(Dmessage)
                    .setPositiveButton(PBtnTxt, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        }
        else if(status=="loc"){

            builder.setTitle(Dtitle)
                    .setMessage(Dmessage)
                    .setPositiveButton(PBtnTxt, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {




                        }
                    });
        }


            AlertDialog dialog = builder.create();

            return dialog;




    }

    public void InformationDialogBuilder(String title,String message,String
            positiveBtnText,String status){


        this.Dtitle=title;
        this.Dmessage=message;
        this.PBtnTxt=positiveBtnText;
        this.status=status;




    }






}
