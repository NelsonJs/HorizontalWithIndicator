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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class PHorizontalView extends ViewGroup {
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

    public PHorizontalView(Context context) {
        this(context, null);
    }

    public PHorizontalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PHorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomHorizontalScroll);
        fontSize = typedArray.getFloat(R.styleable.CustomHorizontalScroll_fontSize, 14);
        bgColor = typedArray.getColor(R.styleable.CustomHorizontalScroll_backGroundColor, Color.BLACK);
        txtColor = typedArray.getColor(R.styleable.CustomHorizontalScroll_textColor, Color.BLACK);
        indicatorColor = typedArray.getColor(R.styleable.CustomHorizontalScroll_indicatorColor, Color.BLACK);
        canLines = typedArray.getBoolean(R.styleable.CustomHorizontalScroll_canLines, false);
        indicatorDrawable = typedArray.getDrawable(R.styleable.CustomHorizontalScroll_indicatorDrawable);
        outDrawable = typedArray.getDrawable(R.styleable.CustomHorizontalScroll_outDrawable);
        contentPaddingTop = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_contentPaddingTop, 0);
        contentPaddingBottom = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_contentPaddingBottom, 0);
        contentPaddingLeft = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_contentPaddingLeft, 0);
        contentPaddingRight = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_contentPaddingRight, 0);
        selectFontSize = typedArray.getFloat(R.styleable.CustomHorizontalScroll_selectFontSize, 0);
        titlePaddingTop = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_titlePaddingTop, 0);
        titlePaddingBottom = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_titlePaddingBottom, 0);
        titlePaddingLeft = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_titlePaddingLeft, 0);
        titlePaddingRight = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_titlePaddingRight, 0);

        indicatorMarginTop = (int) typedArray.getDimension(R.styleable.CustomHorizontalScroll_indicatorMarginTop, 0);
        separateContentAndIndicator = typedArray.getBoolean(R.styleable.CustomHorizontalScroll_separateContentAndIndicator, false);
        indicatorParentColor = typedArray.getColor(R.styleable.CustomHorizontalScroll_indicatorParentColor, Color.WHITE);
        selectTxtBgColor = typedArray.getColor(R.styleable.CustomHorizontalScroll_selectTxtBgColor, Color.WHITE);
        normalTxtBgColor = typedArray.getColor(R.styleable.CustomHorizontalScroll_normalTxtBgColor, Color.WHITE);
        goneIndicator = typedArray.getBoolean(R.styleable.CustomHorizontalScroll_goneIndicator, false);
        verticalLineWidth = typedArray.getDimension(R.styleable.CustomHorizontalScroll_verticalLineWidth, 0);
        selectTxtColor = typedArray.getColor(R.styleable.CustomHorizontalScroll_selectTxtColor, Color.BLACK);
        typedArray.recycle();
    }

    public PHorizontalView bindTitles(List<String> titles) {
        this.titles = titles;
        onFinishInflate();
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        if (linearLayout == null) return;
        measureChild(linearLayout, widthMeasureSpec, heightMeasureSpec);
        if (goneIndicator) {
            setMeasuredDimension(linearLayout.getMeasuredWidth(),
                    linearLayout.getMeasuredHeight());
        } else {
            RelativeLayout lineRl = (RelativeLayout) getChildAt(1);
            measureChild(lineRl, widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(linearLayout.getMeasuredWidth(),
                    linearLayout.getMeasuredHeight() + lineRl.getMeasuredHeight() + indicatorMarginTop);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (titles == null) return;
        LinearLayout linearLayout = new LinearLayout(mContext);
        if (outDrawable != null) {
            linearLayout.setBackgroundDrawable(outDrawable);
        }
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
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
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
        addView(linearLayout);

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
            paramsLine = new LayoutParams((int) w/* / 2*/, 10);
        } else {
            paramsLine = new LayoutParams((int) mLineWidth, 10);
        }
        line.setLayoutParams(paramsLine);
        relativeLayout.addView(line);
        addView(relativeLayout);
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
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        if (linearLayout == null) return;
        linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());
        TextView textView = (TextView) linearLayout.getChildAt(0);
        bottom = textView.getBottom();
        RelativeLayout lineRl = (RelativeLayout) getChildAt(1);
        if (value == 0) {
            lineRl.layout(0, bottom + contentPaddingBottom + indicatorMarginTop, lineRl.getMeasuredWidth(),
                    lineRl.getMeasuredHeight() + bottom + contentPaddingBottom + indicatorMarginTop);
            TextView line = (TextView) lineRl.getChildAt(0);
            int left = (textView.getMeasuredWidth() - line.getMeasuredWidth()) / 2;
            line.layout(left + contentPaddingLeft,
                    0, (mLineWidth == 0 ? line.getMeasuredWidth() : mLineWidth) + left + contentPaddingLeft,
                    line.getMeasuredHeight());
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
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
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
            //lineWidth /= 2;
            lineWidth -= 5;
            int left = textView.getLeft();
            left += textView.getMeasuredWidth() / 2 - lineWidth / 2;
            if (getChildAt(1) instanceof RelativeLayout) {

            } else {
                return;
            }
            RelativeLayout lineRl = (RelativeLayout) getChildAt(1);
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
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            if (i == position) {
                if (selectFontSize == 0){
                    selectFontSize = fontSize;
                }
                textView.setTextSize(selectFontSize);
                textView.setTextColor(selectTxtColor);
                textView.setBackgroundColor(selectTxtBgColor);
            } else {
                textView.setTextSize(fontSize);
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
