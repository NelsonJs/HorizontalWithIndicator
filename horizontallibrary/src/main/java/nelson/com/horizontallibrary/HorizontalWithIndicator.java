package nelson.com.horizontallibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;


public class HorizontalWithIndicator extends ViewGroup implements Serializable{
    private Context mContext;
    private List<String> titles;
    private int bottom;
    private int value;
    private ViewPager viewPager;
    private OnPageChangeListener onPageChangeListener;
    private int mLineWidth;
    private float fontSize;
    private int bgColor;
    private int txtColor;
    private int indicatorColor;
    private boolean canLines;
    private Drawable indicatorDrawable;
    private int contentPaddingTop;
    private int contentPaddingBottom;
    private int indicatorMarginTop;
    private boolean separateContentAndIndicator;
    private int indicatorParentColor;
    private int contentPaddingLeft;
    private int contentPaddingRight;
    private int selectTxtBgColor;
    private int normalTxtBgColor;
    private boolean goneIndicator;
    private float verticalLineWidth;
    private int titlePaddingTop;
    private int titlePaddingBottom;
    private int titlePaddingLeft;
    private int titlePaddingRight;
    private Drawable outDrawable;
    private int selectTxtColor;
    private float selectFontSize;
    private int indicatorWidthType;
    private int sameOfTxt = 1,halfOfTxt = 2;
    private int setIndicatorWidth;
    private int indicatorHeight;
    private int outPaddingBottom;
    private int outPaddingTop;

    public HorizontalWithIndicator(Context context) {
        this(context, null);
    }

    public HorizontalWithIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalWithIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalWithIndicator);
        fontSize = typedArray.getDimension(R.styleable.HorizontalWithIndicator_fontSize, 14);
        bgColor = typedArray.getColor(R.styleable.HorizontalWithIndicator_backGroundColor, Color.BLACK);
        txtColor = typedArray.getColor(R.styleable.HorizontalWithIndicator_textColor, Color.BLACK);
        indicatorColor = typedArray.getColor(R.styleable.HorizontalWithIndicator_indicatorColor, Color.BLACK);
        canLines = typedArray.getBoolean(R.styleable.HorizontalWithIndicator_canLines, false);
        indicatorDrawable = typedArray.getDrawable(R.styleable.HorizontalWithIndicator_indicatorDrawable);
        outDrawable = typedArray.getDrawable(R.styleable.HorizontalWithIndicator_outDrawable);
        contentPaddingTop = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_contentPaddingTop, 0);
        contentPaddingBottom = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_contentPaddingBottom, 0);
        contentPaddingLeft = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_contentPaddingLeft, 0);
        contentPaddingRight = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_contentPaddingRight, 0);
        selectFontSize = typedArray.getDimension(R.styleable.HorizontalWithIndicator_selectFontSize, 0);
        titlePaddingTop = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_titlePaddingTop, 0);
        titlePaddingBottom = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_titlePaddingBottom, 0);
        titlePaddingLeft = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_titlePaddingLeft, 0);
        titlePaddingRight = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_titlePaddingRight, 0);
        indicatorMarginTop = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_indicatorMarginTop, 0);
        separateContentAndIndicator = typedArray.getBoolean(R.styleable.HorizontalWithIndicator_separateContentAndIndicator, false);
        indicatorParentColor = typedArray.getColor(R.styleable.HorizontalWithIndicator_indicatorParentColor, Color.WHITE);
        selectTxtBgColor = typedArray.getColor(R.styleable.HorizontalWithIndicator_selectTxtBgColor, Color.WHITE);
        normalTxtBgColor = typedArray.getColor(R.styleable.HorizontalWithIndicator_normalTxtBgColor, Color.WHITE);
        goneIndicator = typedArray.getBoolean(R.styleable.HorizontalWithIndicator_goneIndicator, false);
        verticalLineWidth = typedArray.getDimension(R.styleable.HorizontalWithIndicator_verticalLineWidth, 0);
        selectTxtColor = typedArray.getColor(R.styleable.HorizontalWithIndicator_selectTxtColor, Color.BLACK);
        indicatorWidthType = typedArray.getInteger(R.styleable.HorizontalWithIndicator_indicatorWidth,0);
        setIndicatorWidth = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_setIndicatorWidth,0);
        indicatorHeight = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_indicatorHeight,10);
        outPaddingBottom = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_outPaddingBottom,0);
        outPaddingTop = (int) typedArray.getDimension(R.styleable.HorizontalWithIndicator_outPaddingTop,0);
        typedArray.recycle();
    }

    public HorizontalWithIndicator bindTitles(List<String> titles) {
        this.titles = titles;
        onFinishInflate();
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LinearLayout outLl = (LinearLayout) getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) outLl.getChildAt(0);
        if (linearLayout == null) return;
        measureChild(outLl, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(outLl.getMeasuredWidth(),
                outLl.getMeasuredHeight()+outPaddingBottom+outPaddingTop);
        /*if (goneIndicator) {
            setMeasuredDimension(linearLayout.getMeasuredWidth(),
                    linearLayout.getMeasuredHeight());
        } else {
            RelativeLayout lineRl = (RelativeLayout) getChildAt(1);
            measureChild(lineRl, widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(linearLayout.getMeasuredWidth(),
                    linearLayout.getMeasuredHeight() + lineRl.getMeasuredHeight() + indicatorMarginTop);
        }*/
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (titles == null) return;
        LinearLayout outLl = new LinearLayout(mContext);
        outLl.setPadding(0,outPaddingTop,0,outPaddingBottom);
        outLl.setOrientation(LinearLayout.VERTICAL);
        MarginLayoutParams paramsOutLl = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        outLl.setLayoutParams(paramsOutLl);
        if (outDrawable != null) {
            outLl.setBackgroundDrawable(outDrawable);
        }
        addView(outLl);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setPadding(contentPaddingLeft, contentPaddingTop, contentPaddingRight, contentPaddingBottom);
        linearLayout.setWeightSum(titles.size());
        linearLayout.setBackgroundColor(bgColor);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        MarginLayoutParams paramsLl = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(paramsLl);
        for (int i = 0; i < titles.size(); i++) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            textView.setText(titles.get(i));
            if (i < titles.size() - 1) {
                params.rightMargin = (int) verticalLineWidth;
            }
            textView.setPadding(titlePaddingLeft, titlePaddingTop, titlePaddingRight, titlePaddingBottom);
            textView.getPaint().setTextSize(fontSize);
            if (!canLines) {
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
            }
            textView.setTextColor(txtColor);
            params.weight = 1;
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
            initTvListener(textView, i);
        }
        //addView(linearLayout);
        outLl.addView(linearLayout);
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        if (separateContentAndIndicator) {
            if (indicatorParentColor != 0) {
                relativeLayout.setBackgroundColor(indicatorParentColor);
            } else {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        } else {
            relativeLayout.setBackgroundColor(bgColor);
        }
        MarginLayoutParams marginLayoutParamsRl = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(marginLayoutParamsRl);
        TextView line = new TextView(mContext);
        line.setBackgroundColor(indicatorColor);
        if (indicatorDrawable != null) {
            line.setBackgroundDrawable(indicatorDrawable);
        }
        TextView wordTv = (TextView) linearLayout.getChildAt(0);
        String s = wordTv.getText().toString();
        TextPaint textPaint = wordTv.getPaint();
        float[] widths = new float[s.length()];
        textPaint.getTextWidths(s, 0, s.length(), widths);
        float w = 0;
        for (int i = 0; i < widths.length; i++) {
            w += widths[i];
        }
        LayoutParams paramsLine = null;
        if (mLineWidth == 0) {
            paramsLine = new LayoutParams((int) w/* / 2*/, indicatorHeight);
            if (indicatorWidthType == sameOfTxt){
                paramsLine = new LayoutParams((int) w, indicatorHeight);
            } else if (indicatorWidthType == halfOfTxt){
                paramsLine = new LayoutParams((int) w / 2, indicatorHeight);
            }
        } else {
            paramsLine = new LayoutParams((int) mLineWidth, indicatorHeight);
        }
        if (setIndicatorWidth > 0){
            paramsLine = new LayoutParams((int) setIndicatorWidth, indicatorHeight);//如果有设置indicator的宽度，则直接覆盖
        }
        line.setLayoutParams(paramsLine);
        relativeLayout.addView(line);
        //addView(relativeLayout);
        outLl.addView(relativeLayout);
        updateTextView(0);
    }


    private void initTvListener(TextView textView, final int i) {
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPageChangeListener != null && viewPager == null) {
                    onPageChangeListener.onSelected(i);
                }
                if (viewPager == null || viewPager.getAdapter() == null || viewPager.getAdapter().getCount() <= i) return;
                viewPager.setCurrentItem(i);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LinearLayout outLl = (LinearLayout) getChildAt(0);
        if (outLl == null) return;
        LinearLayout linearLayout = (LinearLayout) outLl.getChildAt(0);
        outLl.layout(0, outPaddingTop, outLl.getMeasuredWidth(), outLl.getMeasuredHeight()+outPaddingBottom);
        TextView textView = (TextView) linearLayout.getChildAt(0);
        bottom = textView.getBottom();
        RelativeLayout lineRl = (RelativeLayout) outLl.getChildAt(1);
        TextView line = (TextView) lineRl.getChildAt(0);
        if (value == 0) {
            /*lineRl.layout(0, (int) (bottom + contentPaddingBottom + indicatorMarginTop+outLl.getTop()), lineRl.getMeasuredWidth(),
                    lineRl.getMeasuredHeight() + bottom + contentPaddingBottom + indicatorMarginTop);*/
            int left = (textView.getMeasuredWidth() - line.getMeasuredWidth()) / 2;
            line.layout(left + contentPaddingLeft,
                    0, (mLineWidth == 0 ? line.getMeasuredWidth() : mLineWidth) + left + contentPaddingLeft,
                    line.getMeasuredHeight());
        } else {
            line.layout(value, 0, mLineWidth + value, line.getMeasuredHeight());
        }
    }

    public TextView getTitleView(int position){
        LinearLayout outLl = (LinearLayout) getChildAt(0);
        if (outLl == null) return null;
        LinearLayout linearLayout = (LinearLayout) outLl.getChildAt(0);
        if (position < linearLayout.getChildCount()){
            TextView textView = (TextView) linearLayout.getChildAt(position);
            return textView;
        }
        return null;
    }

    public void setTitle(int position,String title){
        LinearLayout outLl = (LinearLayout) getChildAt(0);
        if (outLl == null) return ;
        LinearLayout linearLayout = (LinearLayout) outLl.getChildAt(0);
        if (position < linearLayout.getChildCount()){
            TextView textView = (TextView) linearLayout.getChildAt(position);
            if (title != null){
                textView.setText(title);
                changeLinePosition(position);
            }
        }
    }

    public void bindVp(ViewPager viewPager) {
        if (viewPager == null) return;
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                changeLinePosition(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onSelected(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private float lineWidth;

    public void changeLinePosition(int i) {
        LinearLayout outLl = (LinearLayout) getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) outLl.getChildAt(0);
        if (i >= linearLayout.getChildCount()) {
            return;
        }
        if (linearLayout.getChildAt(i) instanceof TextView) {

        } else {
            return;
        }
        lineWidth = 0;
        TextView textView = (TextView) linearLayout.getChildAt(i);
        updateTextView(i);
        if (!goneIndicator) {
            String content = textView.getText().toString();
            float[] widths = new float[content.length()];
            textView.getPaint().getTextWidths(content, 0, content.length(), widths);
            for (int j = 0; j < widths.length; j++) {
                lineWidth += widths[j];
            }
            if (indicatorWidthType == sameOfTxt){
                lineWidth -= 5;//稍微缩减5个像素
            } else if (indicatorWidthType == halfOfTxt){
                lineWidth /= 2;
            }
            if (setIndicatorWidth > 0){
                lineWidth = setIndicatorWidth;//如果有设置indicator的宽度，则直接覆盖
            }
            int left = textView.getLeft();
            left += textView.getMeasuredWidth() / 2 - lineWidth / 2;
            if (outLl.getChildAt(1) instanceof RelativeLayout) {

            } else {
                return;
            }
            RelativeLayout lineRl = (RelativeLayout) outLl.getChildAt(1);
            final TextView line = (TextView) lineRl.getChildAt(0);
            int lineLeft = line.getLeft();
            int start = lineLeft;
            int end = left;

            ValueAnimator lineAnimator = ValueAnimator.ofInt(line.getMeasuredWidth(), (int) lineWidth);
            lineAnimator.setDuration(200);
            lineAnimator.setInterpolator(new DecelerateInterpolator());
            lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mLineWidth = (int) animation.getAnimatedValue();
                }
            });
            lineAnimator.start();
            ValueAnimator animator = ValueAnimator.ofInt(start, end);
            animator.setDuration(200);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    value = (int) animation.getAnimatedValue();
                    line.layout(value, 0, mLineWidth + value, line.getMeasuredHeight());
                }
            });
            animator.start();
        }
    }

    private void updateTextView(int position) {
        LinearLayout outLl = (LinearLayout) getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) outLl.getChildAt(0);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            if (i == position) {
                if (selectFontSize == 0){
                    selectFontSize = fontSize;
                }
                textView.getPaint().setTextSize(selectFontSize);
                textView.setTextColor(selectTxtColor);
                textView.setBackgroundColor(selectTxtBgColor);
            } else {
                textView.getPaint().setTextSize(fontSize);
                textView.setBackgroundColor(normalTxtBgColor);
                textView.setTextColor(txtColor);
            }
        }

    }

    public abstract static class OnPageChangeListener {
        public abstract void onSelected(int position);
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.onPageChangeListener = listener;
    }

}
