package com.biruk.analogclockview;

import java.util.Calendar;
import android.view.View;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.content.res.TypedArray;

public class AnalogClockView extends View {

	private Paint paint;
	private int hourHandColor = Color.BLACK;
	private int minuteHandColor = Color.BLACK;
	private int secondHandColor = Color.RED;
	private int numeralColor = Color.BLACK;
	private int clockFaceColor = Color.GRAY;

	private float hourHandThickness = 10f;
	private float minuteHandThickness = 8f;
	private float secondHandThickness = 4f;

	private int width, height, radius, padding = 0;
	private int fontSize = 40;
	private boolean isInit = false;

	private String[] numbers = {"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};

	private boolean is24HourFormat = false;
	private Typeface numeralFont = Typeface.DEFAULT;

	private final Handler handler = new Handler();
	private final Runnable ticker = new Runnable() {
		@Override
		public void run() {
			invalidate();
			handler.postDelayed(this, 1000);
		}
	};

	public AnalogClockView(Context context) {
		super(context);
		init();
	}

	public AnalogClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributes(context, attrs);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		handler.post(ticker);
	}

	private void initAttributes(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView);

			hourHandColor = a.getColor(R.styleable.AnalogClockView_hourHandColor, hourHandColor);
			minuteHandColor = a.getColor(R.styleable.AnalogClockView_minuteHandColor, minuteHandColor);
			secondHandColor = a.getColor(R.styleable.AnalogClockView_secondHandColor, secondHandColor);
			numeralColor = a.getColor(R.styleable.AnalogClockView_numeralColor, numeralColor);
			clockFaceColor = a.getColor(R.styleable.AnalogClockView_clockFaceColor, clockFaceColor);

			hourHandThickness = a.getDimension(R.styleable.AnalogClockView_hourHandThickness, hourHandThickness);
			minuteHandThickness = a.getDimension(R.styleable.AnalogClockView_minuteHandThickness, minuteHandThickness);
			secondHandThickness = a.getDimension(R.styleable.AnalogClockView_secondHandThickness, secondHandThickness);

			fontSize = a.getDimensionPixelSize(R.styleable.AnalogClockView_fontSize, fontSize);
			is24HourFormat = a.getBoolean(R.styleable.AnalogClockView_is24HourFormat, false);

			int fontResId = a.getResourceId(R.styleable.AnalogClockView_numeralFont, -1);
			if (fontResId != -1) {
				numeralFont = Typeface.create(getResources().getFont(fontResId), Typeface.NORMAL);
			}

			a.recycle();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		int min = Math.min(w, h);
		padding = 50;
		radius = (min / 2) - padding;
		isInit = true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!isInit)
			return;

		canvas.drawColor(Color.WHITE);
		drawClockFace(canvas);
		drawNumerals(canvas);
		drawSecondTicks(canvas);
		drawHands(canvas);
		drawCenterDot(canvas);
	}

	private void drawClockFace(Canvas canvas) {
		paint.reset();
		paint.setColor(clockFaceColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		canvas.drawCircle(width / 2f, height / 2f, radius, paint);
	}

	private void drawCenterDot(Canvas canvas) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.DKGRAY);
		canvas.drawCircle(width / 2f, height / 2f, 12, paint);
	}

	private void drawNumerals(Canvas canvas) {
		paint.setTextSize(fontSize);
		paint.setTypeface(numeralFont);
		paint.setColor(numeralColor);
		paint.setTextAlign(Paint.Align.CENTER);

		for (int i = 0; i < numbers.length; i++) {
			String number = numbers[i];
			double angle = Math.toRadians(i * 30 - 90);
			float x = (float) (width / 2 + Math.cos(angle) * (radius - 60));
			float y = (float) (height / 2 + Math.sin(angle) * (radius - 60) + fontSize / 3f);
			canvas.drawText(number, x, y, paint);
		}
	}

	private void drawSecondTicks(Canvas canvas) {
		paint.setColor(clockFaceColor);
		paint.setStrokeWidth(2);

		for (int i = 0; i < 60; i++) {
			double angle = Math.toRadians(i * 6 - 90);
			float startX = (float) (width / 2 + Math.cos(angle) * (radius - 10));
			float startY = (float) (height / 2 + Math.sin(angle) * (radius - 10));
			float endX = (float) (width / 2 + Math.cos(angle) * radius);
			float endY = (float) (height / 2 + Math.sin(angle) * radius);
			canvas.drawLine(startX, startY, endX, endY, paint);
		}
	}

	private void drawHands(Canvas canvas) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		if (!is24HourFormat) {
			hour = hour % 12;
		}

		float hourAngle = (hour + minute / 60f) * 30f - 90;
		float minuteAngle = minute * 6f - 90;
		float secondAngle = second * 6f - 90;

		paint.setStyle(Paint.Style.STROKE);

		paint.setColor(hourHandColor);
		drawHand(canvas, hourAngle, radius * 0.5f, hourHandThickness);

		paint.setColor(minuteHandColor);
		drawHand(canvas, minuteAngle, radius * 0.7f, minuteHandThickness);

		paint.setColor(secondHandColor);
		drawHand(canvas, secondAngle, radius * 0.9f, secondHandThickness);
	}

	private void drawHand(Canvas canvas, float angle, float length, float strokeWidth) {
		double rad = Math.toRadians(angle);
		float endX = (float) (width / 2 + Math.cos(rad) * length);
		float endY = (float) (height / 2 + Math.sin(rad) * length);
		paint.setStrokeWidth(strokeWidth);
		canvas.drawLine(width / 2f, height / 2f, endX, endY, paint);
	}
	public void setHourHandColor(int color) {
		this.hourHandColor = color;
		invalidate();
	}

	public void setMinuteHandColor(int color) {
		this.minuteHandColor = color;
		invalidate();
	}

	public void setSecondHandColor(int color) {
		this.secondHandColor = color;
		invalidate();
	}

	public void setHourHandThickness(float thickness) {
		this.hourHandThickness = thickness;
		invalidate();
	}

	public void setMinuteHandThickness(float thickness) {
		this.minuteHandThickness = thickness;
		invalidate();
	}

	public void setSecondHandThickness(float thickness) {
		this.secondHandThickness = thickness;
		invalidate();
	}

	public void setNumeralColor(int color) {
		this.numeralColor = color;
		invalidate();
	}

	public void setClockFaceColor(int color) {
		this.clockFaceColor = color;
		invalidate();
	}

	public void setFontSize(int size) {
		this.fontSize = size;
		invalidate();
	}

	public void setNumerals(String[] customNumerals) {
		if (customNumerals != null && customNumerals.length == 12) {
			this.numbers = customNumerals;
			invalidate();
		}
	}

	public void set24HourFormat(boolean is24Hour) {
		this.is24HourFormat = is24Hour;
		invalidate();
	}

	public void setNumeralFont(Typeface typeface) {
		this.numeralFont = typeface;
		invalidate();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		handler.removeCallbacks(ticker);
	}
}

