package com.lib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.lib.R;

public class DialogUtil {

    // NOTES: Use showOneButtonDialog() in AppBaseFragment
    // NOTES: Use showOneButtonDialog() in AppBaseFragment
    // NOTES: Use showOneButtonDialog() in AppBaseFragment

//    public static AlertDialog createOneButtonDialog(Context context, String title, String message,
//                                                    String positiveButton,
//                                                    DialogInterface.OnClickListener positiveOnClickListener) {
//        return new AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(positiveButton, positiveOnClickListener)
//                .create();
//    }

    // NOTES: Use showTwoButtonsDialog() in AppBaseFragment
    // NOTES: Use showTwoButtonsDialog() in AppBaseFragment
    // NOTES: Use showTwoButtonsDialog() in AppBaseFragment

//    public static AlertDialog createTwoButtonsDialog(Context context, String title, String message,
//                                                     String positiveButton, String negativeButton,
//                                                     DialogInterface.OnClickListener positiveOnClickListener,
//                                                     DialogInterface.OnClickListener negativeOnClickListener) {
//        return new AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(positiveButton, positiveOnClickListener)
//                .setNegativeButton(negativeButton, negativeOnClickListener)
//                .create();
//    }

    public static AlertDialog createItemListDialog(Context context, String title, CharSequence[] items,
                                                   DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, onClickListener)
                .create();
    }

    public static AlertDialog createCustomItemListDialog(Context context, View title, CharSequence[] items,
                                                         DialogInterface.OnClickListener onItemClickListener) {
        return new AlertDialog.Builder(context)
                .setCustomTitle(title)
                .setItems(items, onItemClickListener)
                .create();
    }

    public static AlertDialog createCustomTwoButtonsDialog(Context context, View title,
                                                           String positiveButton, String negativeButton,
                                                           DialogInterface.OnClickListener positiveOnClickListener,
                                                           DialogInterface.OnClickListener negativeOnClickListener) {
        return new AlertDialog.Builder(context)
                .setCustomTitle(title)
                .setPositiveButton(positiveButton, positiveOnClickListener)
                .setNegativeButton(negativeButton, negativeOnClickListener)
                .create();
    }

    public static AlertDialog createCustomSingleChoiceDialogWithCancel(Context context, View title,
                                                                       CharSequence[] items, int checkedItem,
                                                                       DialogInterface.OnClickListener onItemClickListener,
                                                                       DialogInterface.OnClickListener negativeOnClickListener) {
        return new AlertDialog.Builder(context)
                .setCustomTitle(title)
                .setSingleChoiceItems(items, checkedItem, onItemClickListener)
                .setNegativeButton(context.getString(R.string.__t_global_text_cancel), negativeOnClickListener)
                .create();
    }

}
