package com.survivingwithandroid.listview_sectionindexer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class FastSearchListView extends ListView {

	private Context ctx;

	private static int indWidth = 20;
	private String[] sections;
	private float scaledWidth;
	private float sx;
	private int indexSize;
	private String section;
	private boolean showLetter = true;
	private Handler listHandler;
	
	public FastSearchListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ctx = context;
		
	}

	public FastSearchListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		
	}

	public FastSearchListView(Context context, String keyList) {
		super(context);
		ctx = context;
		
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		scaledWidth = indWidth * getSizeInPixel(ctx);
		sx = this.getWidth() - this.getPaddingRight() - scaledWidth;

		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setAlpha(100);

		canvas.drawRect(sx, this.getPaddingTop(), sx + scaledWidth,
				this.getHeight() - this.getPaddingBottom(), p);
		
		indexSize = (this.getHeight() - this.getPaddingTop() - getPaddingBottom())
				/ sections.length;

		Paint textPaint = new Paint();
		textPaint.setColor(Color.DKGRAY);
		textPaint.setTextSize(scaledWidth / 2);

		for (int i = 0; i < sections.length; i++)
			canvas.drawText(sections[i].toUpperCase(),
					sx + textPaint.getTextSize() / 2, getPaddingTop()
							+ indexSize * (i + 1), textPaint);
		
		// We draw the letter in the middle
		if (showLetter & section != null && !section.equals("")) {
			
			Paint textPaint2 = new Paint();			
			textPaint2.setColor(Color.DKGRAY);
			textPaint2.setTextSize(2 * indWidth);
			
			canvas.drawText(section.toUpperCase(), getWidth() / 2,  getHeight() / 2, textPaint2);
		}
	}

	private static float getSizeInPixel(Context ctx) {
		return ctx.getResources().getDisplayMetrics().density;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if (adapter instanceof SectionIndexer)
			sections = (String[]) ((SectionIndexer) adapter).getSections();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (x < sx)
				return super.onTouchEvent(event);
			else {
				// We touched the index bar
				float y = event.getY() - this.getPaddingTop() - getPaddingBottom();
				int currentPosition = (int) Math.floor(y / indexSize);
				section = sections[currentPosition];
				this.setSelection(((SectionIndexer) getAdapter())
						.getPositionForSection(currentPosition));
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (x < sx)
				return super.onTouchEvent(event);
			else {
				float y = event.getY();
				int currentPosition = (int) Math.floor(y / indexSize);
				section = sections[currentPosition];
				this.setSelection(((SectionIndexer) getAdapter())
						.getPositionForSection(currentPosition));

			}
			break;

		}
		case MotionEvent.ACTION_UP: {
			
			listHandler = new ListHandler();
			listHandler.sendEmptyMessageDelayed(0, 30 * 1000);
			
			break;
		}
	  }
		return true;
	}

	
	private class ListHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {			
			super.handleMessage(msg);			
			showLetter = false;
			FastSearchListView.this.invalidate();
		}
		
		
	}
}
