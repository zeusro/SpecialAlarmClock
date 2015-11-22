package com.gdtel.eshore.androidframework.ui.view.report.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

public class TableView4TitleTextView_lib extends TextView{
	private int position;
	private boolean /*left = true, top = true,*/right = true,bottom = true;
	private Paint GARY_PAINT = new Paint();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		switch (position) {
		case 0: // 右线不画
			right = true;
			bottom = false;
			break;
		case 1:
			right = true;
			bottom = true;
			break;
		case 2:
			right = true;
			bottom = true;
			break;
		case 3:
			right = false;
			bottom = true;
			break;
		default:
			break;
		}
		GARY_PAINT.setColor(Color.DKGRAY);
		if (right) {
			// Right
			canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
					this.getHeight() - 1, GARY_PAINT);//画单元格的右线
		}else {
			canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
					this.getHeight() - 1, GARY_PAINT);//画单元格的右线
			canvas.drawLine(0,0,this.getWidth() - 1,this.getHeight() - 1,  GARY_PAINT);//画斜线
		}
		// Bottom
		if (bottom) {
			canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
					this.getHeight() - 1, GARY_PAINT);//画单元格的下线
		}
		
	}

	public TableView4TitleTextView_lib(Context context, int position) {
		super(context);
		this.position = position;
	}

	public TableView4TitleTextView_lib(Context context) {
		super(context);
	}
}
