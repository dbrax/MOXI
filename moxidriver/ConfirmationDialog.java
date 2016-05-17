package tz.co.delis.www.moxidriver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class ConfirmationDialog extends DialogFragment {


    private String Dtitle;
    private String Dmessage;
    private String PBtnTxt;
    private String  NBtnTxt;

    private String NeutralTxt;







    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();


        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(Dtitle)
                .setMessage(Dmessage)
                .setPositiveButton(PBtnTxt, null)
                .setNegativeButton(NBtnTxt,null)
                .setNeutralButton(NeutralTxt,null);




        AlertDialog dialog =builder.create();
        return dialog;


    }


    public void ConfirmationDialogBuilder(String title,String message,String
            positiveBtnText,String
            negativeBtnText,String neutralBtnText){


        this.Dtitle=title;
        this.Dmessage=message;
        this.PBtnTxt=positiveBtnText;
        this.NBtnTxt=negativeBtnText;
        this.NeutralTxt=neutralBtnText;



    }




}
