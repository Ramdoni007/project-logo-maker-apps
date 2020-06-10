package com.coolapps.logomaker.utilities;

//public class BubbleInputDialog extends Dialog {
//    private static final int MAX_COUNT = 33;
//    private BubbleTextView bubbleTextView;
//    private final String defaultStr = "Vo Thanh Tai";
//    private EditText et_bubble_input;
//    private CompleteCallBack mCompleteCallBack;
//    private Context mContext;
//    private TextView tv_action_done;
//    private TextView tv_show_count;
//
//    public interface CompleteCallBack {
//        void onComplete(View view, String str);
//    }
//
//    class C07431 implements TextWatcher {
//        C07431() {
//        }
//
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            long textLength = CommonUtils.calculateLength(s);
//            BubbleInputDialog.this.tv_show_count.setText(String.valueOf(33 - textLength));
//            if (textLength > 33) {
//                BubbleInputDialog.this.tv_show_count.setTextColor(BubbleInputDialog.this.mContext.getResources().getColor(R.color.red_e73a3d));
//            } else {
//                BubbleInputDialog.this.tv_show_count.setTextColor(BubbleInputDialog.this.mContext.getResources().getColor(R.color.grey_8b8b8b));
//            }
//        }
//
//        public void afterTextChanged(Editable s) {
//        }
//    }
//
//    class C07442 implements OnEditorActionListener {
//        C07442() {
//        }
//
//        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            if (actionId != 6) {
//                return false;
//            }
//            BubbleInputDialog.this.done();
//            return true;
//        }
//    }
//
//    class C07453 implements OnClickListener, View.OnClickListener {
//        C07453() {
//        }
//
//        public void onClick(View v) {
//            BubbleInputDialog.this.done();
//        }
//
//        public void onClick(DialogInterface dialogInterface, int i) {
//            BubbleInputDialog.this.done();
//        }
//    }
//
//    class C07464 implements Runnable {
//        C07464() {
//        }
//
//        public void run() {
//            ((InputMethodManager) BubbleInputDialog.this.et_bubble_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, 2);
//        }
//    }
//
////    public BubbleInputDialog(Context context) {
////        super(context, 16973840);
////        this.mContext = context;
////        initView();
////    }
////
////    public BubbleInputDialog(Context context, BubbleTextView view) {
////        super(context, 16973840);
////        this.mContext = context;
////        this.bubbleTextView = view;
////        initView();
////    }
//
//    public void setBubbleTextView(BubbleTextView bubbleTextView) {
//        this.bubbleTextView = bubbleTextView;
//        getClass();
//        if ("Vo Thanh Tai".equals(bubbleTextView.getmStr())) {
//            this.et_bubble_input.setText(BuildConfig.FLAVOR);
//            return;
//        }
//        this.et_bubble_input.setText(bubbleTextView.getmStr());
//        this.et_bubble_input.setSelection(bubbleTextView.getmStr().length());
//    }
//
//    private void initView() {
//        setContentView(R.layout.view_input_dialog);
//        this.tv_action_done = (TextView) findViewById(R.id.tv_action_done);
//        this.et_bubble_input = (EditText) findViewById(R.id.et_bubble_input);
//        this.tv_show_count = (TextView) findViewById(R.id.tv_show_count);
//        this.et_bubble_input.addTextChangedListener(new C07431());
//        this.et_bubble_input.setOnEditorActionListener(new C07442());
//        this.tv_action_done.setOnClickListener(new C07453());
//    }
//
//    public void show() {
//        super.show();
//        new Handler().postDelayed(new C07464(), 500);
//    }
//
//    public void dismiss() {
//        super.dismiss();
//        ((InputMethodManager) this.et_bubble_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.et_bubble_input.getWindowToken(), 0);
//    }
//
//    public void setCompleteCallBack(CompleteCallBack completeCallBack) {
//        this.mCompleteCallBack = completeCallBack;
//    }
//
//    private void done() {
//        if (Integer.valueOf(this.tv_show_count.getText().toString()).intValue() < 0) {
//            Toast.makeText(this.mContext, this.mContext.getString(R.string.over_text_limit), Toast.LENGTH_LONG).show();
//            return;
//        }
//        dismiss();
//        if (this.mCompleteCallBack != null) {
//            String str;
//            if (TextUtils.isEmpty(this.et_bubble_input.getText())) {
//                str = BuildConfig.FLAVOR;
//            } else {
//                str = this.et_bubble_input.getText().toString();
//            }
//            this.mCompleteCallBack.onComplete(this.bubbleTextView, str);
//        }
//    }
//}
