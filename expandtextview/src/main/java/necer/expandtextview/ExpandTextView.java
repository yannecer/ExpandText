package necer.expandtextview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by necer on 2017/4/10.
 */

public class ExpandTextView extends RelativeLayout implements View.OnClickListener {

    private TextView mTextView;
    private TextPaint mTextPaint;//textView的画笔
    private int mTextColor;//颜色
    private float mTextSize;//大小
    private int mShowLineNum = 3;//显示的最大行数

    private Bitmap mUpBitmap, mDownBitmap;//指示箭头
    private int bitmapTop;//bitmap距顶部的距离

    private int bitmapWith;//bitmap宽

    private Paint mPain;//画指示箭头

    private int foldHeight;//折叠后的高度
    private int trueHeight;//真实高度
    private int lineHeight;//Textview行数行高
    private int lineCount;//Textview行数

    private String mText;//原始数据
    private String mFoldText;//折叠的数据
    private boolean isFoldEnable = false;//是否可以折叠
    private boolean isFold = true;//是否折叠
    private boolean isEnough = true;//最后一行是否能容纳箭头
    private boolean isDown;//箭头指向

    private float bitmapRightOffset;//箭头距离view右边的距离
    private int mDuration;//动画时间,默认300

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextview);

        mDuration = ta.getInteger(R.styleable.ExpandTextview_duration, 300);
        bitmapRightOffset = ta.getDimension(R.styleable.ExpandTextview_bitmapRightOffet, DensityUtil.dip2px(getContext(), 10));
        mTextColor = ta.getColor(R.styleable.ExpandTextview_textColor, Color.parseColor("#000000"));
        mTextSize = ta.getDimension(R.styleable.ExpandTextview_textSize, DensityUtil.dip2px(getContext(),14));
        mShowLineNum = ta.getInteger(R.styleable.ExpandTextview_showLine, 3);
        ta.recycle();

        mTextView = new TextView(getContext());
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mTextView.setTextColor(mTextColor);
        mTextPaint = mTextView.getPaint();

        addView(mTextView);
        mTextView.setOnClickListener(this);

        mPain = new Paint();
        mUpBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.up2down);
        mDownBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.down2up);
        bitmapWith = mUpBitmap.getWidth();
    }


    public void setText(String text) {
        this.mText = text;

        post(new Runnable() {
            @Override
            public void run() {
                dealText();
            }
        });

    }

    //要在post里面进行，不然getWidth()得不到view的宽，就无法正确得到字符串的行数
    private void dealText() {
        StaticLayout staticLayout = new StaticLayout(mText, mTextPaint, getWidth() - getPaddingLeft() - getPaddingRight(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, true);
        lineCount = staticLayout.getLineCount();
        lineHeight = staticLayout.getHeight() / lineCount;
        isFoldEnable = lineCount > mShowLineNum;

        if (isFoldEnable) {
            int start = staticLayout.getLineStart(mShowLineNum - 1);
            int end = staticLayout.getLineEnd(mShowLineNum - 1);
            //获取最后一行的内容
            String endText = mText.substring(start, end);
            mFoldText = mText.substring(0, start) + endText.substring(0, endText.length() - 2) + "···";

            int lineWidth = (int) staticLayout.getLineWidth(lineCount - 1);
            isEnough = getWidth() - lineWidth - getPaddingLeft() - getPaddingRight() > (bitmapWith + bitmapRightOffset);

            //能容纳真实高度为textview的高度，不能容纳，真实高度为textview高度+一个行高，放箭头
            trueHeight = isEnough ? staticLayout.getHeight() : staticLayout.getHeight() + lineHeight;
            foldHeight = lineHeight * mShowLineNum;

            if (isFold) {
                bitmapTop = foldHeight - lineHeight / 2;//bitmap开始的位置,让箭头在一行的中间
                mTextView.setText(mFoldText);

            } else {
                bitmapTop = trueHeight - lineHeight / 2;
                mTextView.setText(mText);
            }

        } else {
            mTextView.setText(mText);
        }
    }


    @Override
        protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isFoldEnable) {
            return;
        }
        canvas.drawBitmap(isDown ? mDownBitmap : mUpBitmap, getWidth() - bitmapWith - bitmapRightOffset - getPaddingRight(), bitmapTop + getPaddingTop(), mPain);
    }


    public void startAnim(int starHeight, int endHeight, Animator.AnimatorListener animatorListener) {
        final ViewGroup.LayoutParams layoutParams = mTextView.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(starHeight, endHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                mTextView.setLayoutParams(layoutParams);
                bitmapTop = (int) valueAnimator.getAnimatedValue() - lineHeight / 2;
            }
        });
        valueAnimator.addListener(animatorListener);
        valueAnimator.setDuration(mDuration);
        valueAnimator.start();
    }

    @Override
    public void onClick(View view) {
        if (!isFoldEnable) {
            return;
        }
        if (ButtonUtils.isFastClick(mDuration)) {
            return;
        }
        if (isFold) {
            isFold = false;
            mTextView.setText(mText);
            startAnim(foldHeight, trueHeight, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isDown = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } else {
            isFold = true;
            startAnim(trueHeight, foldHeight, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isDown = false;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mTextView.setText(mFoldText);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }
}
