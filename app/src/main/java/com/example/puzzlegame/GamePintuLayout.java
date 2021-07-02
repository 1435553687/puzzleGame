package com.example.puzzlegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GamePintuLayout extends RelativeLayout implements View.OnClickListener{

    public static String str;
    private int mMargin = 3;//Item 横向与纵向的边距
    private int mPadding;// 布局的边距
    public Bitmap bitmap;//拼图的图片
    private ImageView[] mGamePintuItems;//存放所有的Item

    private int mWidth; //布局的宽度
    private List<ImagePiece> imagePieces;
    private int mColumn = 2;//设置Item的数量
    private int itemWidth;

    private boolean flag;

    public static int number = 0;//使用的步数
    public static int numguan = 1;//关卡数

    public GamePintuLayout(Context context) {
        super(context, null);
        Log.d("nsc", "11111111");
    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        Log.d("nsc", "22222222");
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMargin,
                getResources().getDisplayMetrics());

        //设置Layout 的内边距，四边一样
        mPadding = min(getPaddingLeft(),getPaddingTop(),getPaddingRight(),getPaddingBottom());
        Log.d("nsc", "mPadding : mMargin"+mPadding+":"+mMargin);
    }

    /**
     * 获取边距的最小值
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("nsc", "onMeasure");
        //获取布局的边长
        mWidth = Math.min(getMeasuredHeight(),getMeasuredWidth());
        if (!flag && bitmap!=null){
            Log.d("nsc", "bitmap");
            numguan = 1;
            initBitmap();
            initItem();
        }
        flag = true;
        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 加载图片和切割图片
     */
    void initBitmap() {
        //加载图片资源
       // if (bitmap == null){
            // bitmap = MainActivity.bitmap;
           // bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ac);
        //}
        //将图片切割，然后按随机打乱顺序
        imagePieces =split(bitmap,mColumn);
        Collections.sort(imagePieces, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece imagePiece, ImagePiece t1) {
                Log.d("nsc", "return="+(Math.random() > 0.5 ? 1 : -1));
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 初始化item
     */
    void initItem(){
        //获取Item的宽度  容器的宽度，除去自己的内边距，除去Item间的间距，然后除以Item一行的个数就得到了Item的宽
        int citemWidth = (mWidth - mPadding*2 - mMargin*(mColumn - 1))/mColumn;
        itemWidth = citemWidth;
        mGamePintuItems = new ImageView[mColumn * mColumn];
        //放置Item
        for (int i=0;i<mGamePintuItems.length;i++){
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(imagePieces.get(i).bitmap);
            mGamePintuItems[i] = item;
            item.setId(i + 1);
            item.setTag(i + "_" + imagePieces.get(i).index);
            RelativeLayout.LayoutParams layoutParams = new LayoutParams(itemWidth,itemWidth);
            //设置横向边距，不是最后一刻
            if ((i+1) % mColumn !=0){
                layoutParams.rightMargin = mMargin;
            }
            //如果不是第一列
            if (i % mColumn !=0){
                layoutParams.addRule(RelativeLayout.RIGHT_OF,mGamePintuItems[i-1].getId());
            }
            //如果不是第一行
            if ((i + 1)> mColumn){
                layoutParams.topMargin = mMargin;
                layoutParams.addRule(RelativeLayout.BELOW,mGamePintuItems[i-mColumn].getId());
            }
            addView(item,layoutParams);
        }
    }

    /**
     * 设置边距为0
     * @param params
     * @return
     */
    private int min(int... params){
        int min = params[0];
        for (int param : params){
            if (min > param) {
                min = param;
            }
        }
        return min;
    }

    private boolean isAniming;//动画运行的标志
    private RelativeLayout mAnimLayout; //动画层
    private ImageView first;
    private ImageView second;
    @Override
    public void onClick(View v) {
        //如果正在执行动画，则屏蔽
        if (isAniming){
            return;
        }
        //如果两次点击相同
        if (first ==v){
            first.setColorFilter(null);
            first = null;
            return;
        }
        //点击第一个item
        if (first == null){
            first = (ImageView)v;
            first.setColorFilter(Color.parseColor("#66990000"));
        }else{//点击第二个item
            second = (ImageView)v;
            changeView();
            // excheangeview();
        }
    }

    /**
     * 交换两个Item图片
     */
    private void changeView() {
        first.setColorFilter(null);
        setUpAnimLayout();
        ImageView mfirst = new ImageView(getContext());
        mfirst.setImageBitmap(imagePieces.get(getImageIndexByTag((String)first.getTag())).bitmap);
        Log.d("nsc","getImageIndexByTag()="+getImageIndexByTag((String)first.getTag()));
        LayoutParams layoutParams = new LayoutParams(itemWidth,itemWidth);
        layoutParams.leftMargin = first.getLeft() - mPadding;
        layoutParams.topMargin = first.getTop() - mPadding;
        mfirst.setLayoutParams(layoutParams);
        mAnimLayout.addView(mfirst);
        //添加SecondView
        ImageView msecond = new ImageView(getContext());
        msecond.setImageBitmap(imagePieces.get(getImageIndexByTag((String)second.getTag())).bitmap);
        LayoutParams layoutParams1 = new LayoutParams(itemWidth,itemWidth);
        layoutParams1.leftMargin = second.getLeft() - mPadding;
        layoutParams1.topMargin = second.getTop() - mPadding;
        msecond.setLayoutParams(layoutParams1);
        mAnimLayout.addView(msecond);

        //设置动画
        TranslateAnimation animation = new TranslateAnimation(0,second.getLeft()
                - first.getLeft(),0,second.getTop() - first.getTop());
        animation.setDuration(300);//动画时间
        animation.setFillAfter(true);
        mfirst.setAnimation(animation);

        TranslateAnimation animationsecond = new TranslateAnimation(0,first.getLeft()
                - second.getLeft(),0,first.getTop() - second.getTop());
        animationsecond.setDuration(300);
        animationsecond.setFillAfter(true);
        msecond.setAnimation(animationsecond);

        //添加动画监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAniming = true;
                first.setVisibility(INVISIBLE);
                second.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String firstTag = (String)first.getTag();
                String secondTag = (String)second.getTag();

                String[] firstParams = firstTag.split("_");
                String[] secondParams = secondTag.split("_");

                first.setImageBitmap(imagePieces.get(Integer.parseInt(secondParams[0])).bitmap);
                second.setImageBitmap(imagePieces.get(Integer.parseInt(firstParams[0])).bitmap);

                first.setTag(secondTag);
                second.setTag(firstTag);

                first.setVisibility(VISIBLE);
                second.setVisibility(VISIBLE);
                first = second =null;
                mAnimLayout.removeAllViews();
                checkSuccess();
                number++;
                isAniming = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     *不加动画切换效果
     */
    public void excheangeview(){
        String firstTag = (String)first.getTag();
        String secondTag = (String)second.getTag();

        String[] firstParams = firstTag.split("_");
        String[] secondParams = secondTag.split("_");

        first.setImageBitmap(imagePieces.get(Integer.parseInt(secondParams[0])).bitmap);
        second.setImageBitmap(imagePieces.get(Integer.parseInt(firstParams[0])).bitmap);

        first.setTag(secondTag);
        second.setTag(firstTag);

        first = second =null;
        //  mAnimLayout.removeAllViews();
        checkSuccess();
    }

    /**
     * 判断游戏是否成功
     */
    private void checkSuccess()
    {
        boolean isSuccess = true;
        //通过判断每次替换两张图片后的顺序跟正确的图片顺序对比，相同则下一关
        for (int i = 0; i < mGamePintuItems.length; i++)
        {
            ImageView first = mGamePintuItems[i];
            Log.e("nsc","checkSuccess:"+ getIndexByTag((String) first.getTag()) + " : mGamePintuItems[i]:"+mGamePintuItems[i]);
            if (getIndexByTag((String) first.getTag()) != i)
            {
                isSuccess = false;
            }
        }
        if (isSuccess)
        {
            Toast.makeText(getContext(),"第"+ numguan +"关，您用了 "+(number+1)+" 步过关了！",
                    Toast.LENGTH_LONG).show();
            str = "第" + numguan + "关，用了 " + (number+1)+ " 步";
            nextLevel();
        }
    }
    public void next(){
        this.removeAllViews();
        mAnimLayout = null;
        number = 0;//重置步数
        initBitmap();
        initItem();
    }
    /**
     * 下一关，移除图片，重新初始化
     */
    public void nextLevel()
    {
        this.removeAllViews();
        mAnimLayout = null;
        mColumn++;
        numguan++;//关卡数++
        number = -1;//重置步数
        initBitmap();
        initItem();
    }
    /**
     * 步数
     * @param tag
     * @return
     */
    private int getIndexByTag(String tag)
    {
        String[] split = tag.split("_");
        Log.d("nsc", "split[1]：" + split[1]);
        return Integer.parseInt(split[1]);
    }
    /**
     *关卡数
     * @param tag
     * @return
     */
    private int getImageIndexByTag(String tag){
        String[] split = tag.split("_");
        Log.d("nsc", "split[0]:"+split[0]);
        return Integer.parseInt(split[0]);
    }
    /**
     * 创建动画层
     */
    private void setUpAnimLayout()
    {
        if (mAnimLayout == null)
        {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }
    /**
     * 将图片切成 , piece *piece
     *
     * @param bitmap
     * @param piece
     * @return
     */
    public static List<ImagePiece> split(Bitmap bitmap, int piece)
    {

        List<ImagePiece> pieces = new ArrayList<ImagePiece>(piece * piece);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pieceWidth = Math.min(width, height) / piece;

        for (int i = 0; i < piece; i++)
        {
            for (int j = 0; j < piece; j++)
            {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.index = j + i * piece;
                Log.e("nsc", "imagePiece.index" + (j + i * piece));
                int xValue = j * pieceWidth;
                int yValue = i * pieceWidth;
                imagePiece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceWidth);
                pieces.add(imagePiece);
            }
        }
        return pieces;
    }
}