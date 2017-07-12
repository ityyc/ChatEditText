package testdemo.qihoo.com.chatedittext.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import testdemo.qihoo.com.chatedittext.R;
import testdemo.qihoo.com.chatedittext.constant.GlobalConfig;

/**
 * 主Activity
 *
 * @author yuanyc
 * @time 2017/7/11 16:43
 */
public class ChatActivity extends Activity {

    @BindView(R.id.edit)
    EditText edit;
    /**
     * 用于存储@的id、name的map集合(已经@过的)
     */
    private ArrayList<String> ids = new ArrayList<>();
    /**
     * 返回的所有的用户名,用于识别输入框中的所有要@的人
     * <p>
     * 如果用户删除过，会出现不匹配的情况，需要在for循环中做处理
     */
    private String nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //为editText设置过滤器
        edit.setFilters(new InputFilter[]{new MyInputFilter()});
    }


    /**
     * 自定义输入框的过滤器
     */
    private class MyInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //如果输入框中的内容包含@符号，则进行过滤处理
            String targetStr = source.toString();
            if (targetStr.equalsIgnoreCase("@") || targetStr.equalsIgnoreCase("＠")) {
                //跳转到GroupPersonActivity页面
                goNextActivity();
            }
            return source;
        }
    }

    private void goNextActivity() {
        //需要将已经选中的人的id传递过去，下一个页面需要用到，做过滤(传递过去的时候“”进行隔离)
        StringBuffer buffer = new StringBuffer();
        if (null != ids && ids.size() >= 1) {
            for (String id : ids) {
                buffer.append(id).append(",");
            }
        }
        Intent intent = new Intent(this, GroupPersonActivity.class);
        intent.putExtra(GlobalConfig.KEY_SELECTED, buffer.toString());
        startActivityForResult(intent, GlobalConfig.CODE_PERSON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果响应不成功
        if (resultCode != RESULT_OK) {
            return;
        }
        //如果请求码一致
        if (requestCode == GlobalConfig.CODE_PERSON) {
            //获取返回的数据内容
            String idExtraStr = data.getStringExtra(GlobalConfig.KEY_ID);
            String nameExtraStr = data.getStringExtra(GlobalConfig.KEY_NAME);
            Log.i(GlobalConfig.TAG, "idExtraStr:" + idExtraStr);
            Log.i(GlobalConfig.TAG, "nameExtraStr:" + nameExtraStr);
            ids.add(idExtraStr);
            if (null == nameStr) {
                nameStr = nameExtraStr;
            } else {
                nameStr = nameStr + nameExtraStr;
            }
            //获取光标的当前位置
            int index = edit.getSelectionStart();
            // 把要@的人插入光标所在位置
            edit.getText().insert(index, nameExtraStr);
            // 通过输入@符号进入好友列表并返回@的人，要删除之前输入的@(因为返回的数据中有@符号)
            if (index >= 1) {
                edit.getText().replace(index - 1, index, "");
            }
            setAtImageSpan(nameStr);
        }
    }

    private void setAtImageSpan(String nameStr) {
        //获取输入框中的内容
        String content = String.valueOf(edit.getText().toString());
        Log.i(GlobalConfig.TAG, "content:" + content);
        //针对用户输入@符号了，但是没有选择要@的人
        if (content.endsWith("@") || content.endsWith("＠")) {
            content = content.substring(0, content.length() - 1);
        }
        //用于
        String tempContent = content;
        SpannableString ss = new SpannableString(tempContent);
        if (null != nameStr) {
            String[] names = nameStr.split(" ");
            for (String name : names) {
                if (null != name && name.trim().toString().length() > 0) {
                    final Bitmap bmp = getNameBitmap(name);
                    // 这里会出现删除过的用户，需要做判断，过滤掉
                    if (tempContent.indexOf(name) >= 0 && tempContent.indexOf(name) + name.length() <= tempContent.length()) {
                        // 把取到的要@的人名，用DynamicDrawableSpan代替
                        ss.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
                            @Override
                            public Drawable getDrawable() {
                                BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
                                bitmapDrawable.setBounds(0, 0, bmp.getWidth(), bmp.getHeight());
                                return bitmapDrawable;
                            }
                        }, tempContent.indexOf(name), tempContent.indexOf(name) + name.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

        }
        edit.setTextKeepState(ss);
    }

    /**
     * 把@相关的字符串转换成bitmap 然后使用DynamicDrawableSpan加入输入框中
     *
     * @param name
     * @return
     */
    private Bitmap getNameBitmap(String name) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.color_blue));
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        Rect rect = new Rect();
        paint.getTextBounds(name, 0, name.length(), rect);
        int width = (int) paint.measureText(name);
        Bitmap bitmap = Bitmap.createBitmap(width, rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(name, rect.left, rect.height() - rect.bottom, paint);
        return bitmap;
    }
}
