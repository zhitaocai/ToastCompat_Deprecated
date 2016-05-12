package io.github.zhitaocai.toastcompat;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * @author zhitao
 * @since 2016-01-21 14:33
 */
public class SystemToast implements IToast {

	private Toast mToast;

	private Context mContext;

	public static IToast makeText(Context context, String text, long duration) {
		return new SystemToast(context).setText(text).setDuration(duration);
	}

	public SystemToast(Context context) {
		mContext = context;
		// 除非你的toast是自定义view
		// 否则建议采用maketext的方法创建示例，而不是newToast
		mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	}

	@Override
	public IToast setGravity(int gravity, int xOffset, int yOffset) {
		mToast.setGravity(gravity, xOffset, yOffset);
		return this;
	}

	@Override
	public IToast setDuration(long durationMillis) {
		mToast.setDuration((int) durationMillis);
		return this;
	}

	/**
	 * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
	 *
	 * @param view 传入view
	 *
	 * @return 自身对象
	 */
	@Override
	public IToast setView(View view) {
		mToast.setView(view);
		return this;
	}

	@Override
	public IToast setMargin(float horizontalMargin, float verticalMargin) {
		mToast.setMargin(horizontalMargin, verticalMargin);
		return this;
	}

	/**
	 * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
	 *
	 * @param text 传入字符串
	 *
	 * @return 自身对象
	 */
	@Override
	public IToast setText(String text) {
		mToast.setText(text);
		return this;
	}

	@Override
	public void show() {
		if (mToast != null) {
			mToast.show();
		}
	}

	@Override
	public void cancel() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

}
