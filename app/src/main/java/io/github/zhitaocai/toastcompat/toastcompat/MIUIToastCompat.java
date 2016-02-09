package io.github.zhitaocai.toastcompat.toastcompat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import io.github.zhitaocai.toastcompat.util.DisplayUtil;

/**
 * TODO 支持队列展示(下一个即将显示的toast，不会在上一个toast还没有消失之前就弹出来) 思路参考如下：简单为一个队列
 * <p/>
 * <pre>
 *      show() {
 *          1. 加入到队列
 *          2. 激活队列
 *              while(队列不空) {
 *                  1. 显示
 *                  2. 阻塞一段时间（显示时间的长度） 或者指定后续操作的执行时间点 (AtTime)
 *                  3. 消失(removeView)
 *                  4. 从队列中移除
 *              }
 *      }
 * </pre>
 * <p/>
 * PS: 你可能会注意到，下面写死了一些常量数字。作为一个Android党，你可能觉得这个写死一个数字会很难做兼容，但是下面用到的数字都不是瞎编的～
 *
 * @author zhitao
 * @since 2016-01-21 14:33
 */
public class MIUIToastCompat implements IToast {

	private static Handler mHandler = new Handler();

	private WindowManager mWindowManager;

	private long mDurationMillis;

	private View mView;

	private WindowManager.LayoutParams mParams;

	private Context mContext;

	public static IToast makeText(Context context, String text, long duration) {
		return new MIUIToastCompat(context).setText(text).setDuration(duration)
				.setGravity(Gravity.BOTTOM, 0, DisplayUtil.dip2px(context, 64));
	}

	public MIUIToastCompat(Context context) {
		mContext = context;
		mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mParams = new WindowManager.LayoutParams();
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.windowAnimations = android.R.style.Animation_Toast;
		mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		mParams.setTitle("Toast");
		mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
		                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		// 默认小米Toast在下方居中
		mParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
	}

	/**
	 * Set the location at which the notification should appear on the screen.
	 *
	 * @param gravity
	 * @param xOffset
	 * @param yOffset
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public IToast setGravity(int gravity, int xOffset, int yOffset) {

		// We can resolve the Gravity here by using the Locale for getting
		// the layout direction
		final int finalGravity;
		if (Build.VERSION.SDK_INT >= 14) {
			final Configuration config = mView.getContext().getResources().getConfiguration();
			finalGravity = Gravity.getAbsoluteGravity(gravity, config.getLayoutDirection());
		} else {
			finalGravity = gravity;
		}
		mParams.gravity = finalGravity;
		if ((finalGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
			mParams.horizontalWeight = 1.0f;
		}
		if ((finalGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
			mParams.verticalWeight = 1.0f;
		}
		mParams.y = yOffset;
		mParams.x = xOffset;
		return this;
	}

	@Override
	public IToast setDuration(long durationMillis) {
		if (durationMillis < 0) {
			mDurationMillis = 0;
		}
		if (durationMillis == Toast.LENGTH_SHORT) {
			mDurationMillis = 2000;
		} else if (durationMillis == Toast.LENGTH_LONG) {
			mDurationMillis = 3500;
		} else {
			mDurationMillis = durationMillis;
		}
		return this;
	}

	/**
	 * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
	 *
	 * @param view
	 *
	 * @return
	 */
	@Override
	public IToast setView(View view) {
		mView = view;
		return this;
	}

	@Override
	public IToast setMargin(float horizontalMargin, float verticalMargin) {
		mParams.horizontalMargin = horizontalMargin;
		mParams.verticalMargin = verticalMargin;
		return this;
	}

	/**
	 * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
	 *
	 * @return
	 */
	@Override
	public IToast setText(String text) {

		// 模拟Toast的布局文件 com.android.internal.R.layout.transient_notification
		// 虽然可以手动用java写，但是不同厂商系统，这个布局的设置好像是不同的，因此我们自己获取原生Toast的view进行配置

		View view = Toast.makeText(mContext, text, Toast.LENGTH_SHORT).getView();
		if (view != null) {
			TextView tv = (TextView) view.findViewById(android.R.id.message);
			tv.setText(text);
			setView(view);
		}

		return this;
	}

	@Override
	public void show() {
		mHandler.post(mShow);
		mHandler.postDelayed(mHide, mDurationMillis);
	}

	@Override
	public void cancel() {
		mHandler.post(mHide);
	}

	private void handleShow() {
		if (mView != null) {
			if (mView.getParent() != null) {
				mWindowManager.removeView(mView);
			}
			mWindowManager.addView(mView, mParams);
		}
	}

	private void handleHide() {
		if (mView != null) {
			// note: checking parent() just to make sure the view has
			// been added...  i have seen cases where we get here when
			// the view isn't yet added, so let's try not to crash.
			if (mView.getParent() != null) {
				mWindowManager.removeView(mView);
			}
			mView = null;
		}
	}

	private final Runnable mShow = new Runnable() {
		@Override
		public void run() {
			handleShow();
		}
	};

	private final Runnable mHide = new Runnable() {
		@Override
		public void run() {
			handleHide();
		}
	};

}