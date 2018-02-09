package id.gits.justify_helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by irfanirawansukirman on 09/02/18.
 */

public class JustifyTextView extends AppCompatTextView {

    private boolean justify;
    private float textAreaWidth;
    private float spaceCharSize;
    private float lineY;

    public JustifyTextView(Context context) {
        super(context);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * @param attrs the attributes from the xml
     *              This function loads all the parameters from the xml
     */
    private void init(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.JustifyTextView, 0, 0);

        justify = ta.getBoolean(R.styleable.JustifyTextView_just_for_you, false);

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        textAreaWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());

        spaceCharSize = paint.measureText(" ");

        String text = getText().toString();
        lineY = getTextSize();

        Layout textLayout = getLayout();

        if (textLayout == null)
            return;

        Paint.FontMetrics fm = paint.getFontMetrics();
        int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
        textHeight = (int) (textHeight * getLineSpacingMultiplier() + textLayout.getSpacingAdd());

        for (int i = 0; i < textLayout.getLineCount(); i++) {

            int lineStart = textLayout.getLineStart(i);
            int lineEnd = textLayout.getLineEnd(i);

            float lineWidth = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, paint);
            String line = text.substring(lineStart, lineEnd);

            if (line.charAt(line.length() - 1) == ' ') {
                line = line.substring(0, line.length() - 1);
            }

            if (justify && i < textLayout.getLineCount() - 1) {
                drawLineJustified(canvas, line, lineWidth);
            } else {
                canvas.drawText(line, 0, lineY, paint);
            }

            lineY += textHeight;
        }

    }

    private void drawLineJustified(Canvas canvas, String line, float lineWidth) {
        TextPaint paint = getPaint();

        float emptySpace = textAreaWidth - lineWidth;
        int spaces = line.split(" ").length - 1;
        float newSpaceSize = (emptySpace / spaces) + spaceCharSize;

        float charX = 0;

        for (int i = 0; i < line.length(); i++) {
            String character = String.valueOf(line.charAt(i));
            float charWidth = StaticLayout.getDesiredWidth(character, paint);
            if (!character.equals(" ")) {
                canvas.drawText(character, charX, lineY, paint);
            }

            if (character.equals(" ") && i != line.length() - 1)
                charX += newSpaceSize;
            else
                charX += charWidth;
        }

    }
}
