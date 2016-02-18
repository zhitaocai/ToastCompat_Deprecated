package io.github.zhitaocai.toastcompat.toastcompat;

import android.view.View;

/**
 * @author zhitao
 * @since 2016-01-21 14:30
 */
public interface IToast {

	IToast setGravity(int gravity, int xOffset, int yOffset);

	IToast setDuration(long durationMillis);

	/**
	 * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
	 */
	IToast setView(View view);

	IToast setMargin(float horizontalMargin, float verticalMargin);

	/**
	 * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
	 */
	IToast setText(String text);

	void show();

	void cancel();

}
