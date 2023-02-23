package com.haiprj.converttomp3.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.haiprj.android_app_lib.ui.BaseDialog;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.DialogDeleteBinding;

public class DeleteDialog extends BaseDialog<DialogDeleteBinding> {

    public final static String DELETE = "delete";

    public DeleteDialog(@NonNull Context context, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        super(context, activity, onActionDialogCallback);
    }

    public DeleteDialog(@NonNull Context context, int themeResId, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        super(context, themeResId, activity, onActionDialogCallback);
    }

    public DeleteDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        super(context, cancelable, cancelListener, activity, onActionDialogCallback);
    }

    @Override
    protected void addEvent() {
        binding.delete.setOnClickListener(v -> {
            onActionDialogCallback.callback(DELETE);
            dismiss();
        });
        binding.cancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    protected void initView() {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_delete;
    }
}
