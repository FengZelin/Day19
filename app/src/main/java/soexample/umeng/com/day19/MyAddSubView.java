package soexample.umeng.com.day19;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class MyAddSubView extends LinearLayout implements View.OnClickListener{
    TextView tv_sub_view;
    TextView tv_number_view;
    TextView tv_add_view;
    private int number = 1;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        tv_number_view.setText(number+"");
    }

    public MyAddSubView(Context context) {
        super(context,null);
    }

    public MyAddSubView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View inflate = View.inflate(context, R.layout.add_sub_view, this);
        tv_sub_view = inflate.findViewById(R.id.tv_sub_view);
        tv_number_view = inflate.findViewById(R.id.tv_number_view);
        tv_add_view = inflate.findViewById(R.id.tv_add_view);
        tv_add_view.setOnClickListener(this);
        tv_sub_view.setOnClickListener(this);

    }
    OnNumberChangeListener onNumberChangeListener;
    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sub_view:
                if (number>1){
                    --number;
                    tv_number_view.setText(number+"");
                    if (onNumberChangeListener != null){
                        onNumberChangeListener.OnNumberChange(number);
                    }
                }else {
                    Toast.makeText(getContext(), "不能再少啦!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_add_view:
                ++number;
                tv_number_view.setText(number+"");
                if (onNumberChangeListener!=null){
                    onNumberChangeListener.OnNumberChange(number);
                }
                break;
        }

    }
    public interface OnNumberChangeListener {
        void OnNumberChange(int number);


    }

}
