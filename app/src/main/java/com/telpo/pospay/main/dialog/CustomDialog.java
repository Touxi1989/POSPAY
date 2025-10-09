package com.telpo.pospay.main.dialog;
import com.telpo.pospay.R;

/**
 * 定制专用Dialog
 */
public class CustomDialog extends android.app.Dialog {
    // private Context context;
    private android.widget.TextView title;// 标题
    private android.widget.TextView message;// 提示信息
    private android.widget.TextView confirm;// 确认
    private android.widget.TextView cancleTv;//取消
    private android.widget.TextView info;//终端信息

    public android.widget.TextView getTitle() {
        return title;
    }

    public void setTitle(android.widget.TextView title) {
        this.title = title;
    }

    public CustomDialog(android.content.Context context) {
        super(context, R.style.DialogTheme);
        // this.context=context;
        init();
    }


    /**
     * 初始化方法，该方法在CustomDialog对象new出来的时候执行
     */
    private void init() {
        setContentView(R.layout.dialog_custom);
        title = (android.widget.TextView) findViewById(R.id.title);
        message = (android.widget.TextView) findViewById(R.id.msg);
        confirm = (android.widget.TextView) findViewById(R.id.confirm);
        cancleTv = (android.widget.TextView) findViewById(R.id.cancle);
        info = (android.widget.TextView) findViewById(R.id.info);
    }

    /**
     * 设置提示信息
     *
     * @param title 提示信息
     */
    @Override
    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    /**
     * 设置提示信息
     *
     * @param titleId 提示信息在R文件总的ID
     */
    @Override
    public void setTitle(int titleId) {
        this.title.setText(titleId);
    }


    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setInfo(String info) {
        this.info.setText(info);
    }

    public void setOnYesClick(String text, final android.content.DialogInterface.OnClickListener listener) {
        if (android.text.TextUtils.isEmpty(text)) {
            confirm.setVisibility(android.view.View.GONE);
        }
        confirm.setText(text);
        confirm.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                listener.onClick(com.telpo.pospay.main.dialog.CustomDialog.this, confirm.getId());
            }
        });
    }

    public void setOnNoClick(String text, final android.content.DialogInterface.OnClickListener listener) {
        if (android.text.TextUtils.isEmpty(text)) {
            cancleTv.setVisibility(android.view.View.GONE);
        }
        cancleTv.setText(text);
        cancleTv.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                listener.onClick(com.telpo.pospay.main.dialog.CustomDialog.this, cancleTv.getId());
            }
        });
    }

    public interface CustomDialogListener {

        void onYesClick();

        void onNoClick();

        void timeOut();

    }
}
