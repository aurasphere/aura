package co.aurasphere.aura.nebula.modules.social;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;

import co.aurasphere.aura.nebula.modules.social.util.TextTooLongException;

/**
 * Class which produces a bitmap image with text and the Facebook page name signature.
 */
public class ImageGenerator {

    /**
     * The starting pixel of the signature section. Used to center
     * the signature vertically.
     */
    private static final int SIGNATURE_PIXEL_START = 668;

    /**
     * The maximum font size of the text to be written.
     */
    private static final int MAX_FONT_SIZE = 50;

    /**
     * The minimum font size of the main text.
     */
    private static final int MIN_FONT_SIZE = 10;

    /**
     * The horizontal text padding.
     */
    private static final int TEXT_PADDING = 40;

    /**
     * Max lines number before the fonts get resized automatically.
     */
    private static final int MAX_LINE_NUMBERS = 8;

    /**
     * Interline size.
     */
    private static final int INTERLINE = 2;

    /**
     * The number of characters left on the line at which the program
     * will try to insert a line break at first blank space.
     */
    private static final int LINE_BREAK_CHAR_MARGIN = 10;

    /**
     * The final image.
     */
    private static Bitmap image;

    /**
     * Paint object of final image.
     */
    private static Paint paint;

    /**
     * Canvas object of final image.
     */
    private static Canvas canvas;

    /**
     * Main image text split by lines.
     */
    private static String[] lines;

    /**
     * Creates an image with a quotation in the center and the page name below.
     *
     * @param backgroundFileName the background file name on external storage.
     * @param text               the main text to write.
     * @param pageName           the page name.
     * @return the image as a Bitmap.
     */
    public static Bitmap createImageWithText(String backgroundFileName, String text, String pageName) throws TextTooLongException {
        // Loads background and copies it in a temp file.
        Bitmap background = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + backgroundFileName);
        image = background.copy(Bitmap.Config.ARGB_8888, true);

        // Initializing some parameters.
        canvas = new Canvas(image);
        paint = new Paint();
        paint.setColor(Color.BLACK); // Text Color
        paint.setTextSize(MAX_FONT_SIZE); // Text Size
        canvas.drawBitmap(image, 0, 0, paint);

        // Processing and return.
        addPageName(pageName);
        addMainText(text);

        return image;
    }

    /**
     * Writes the main text.
     *
     * @param text the text to write.
     */
    private static void addMainText(String text) throws TextTooLongException {
        int ascent = (int) paint.getFontMetrics().ascent;
        buildLines(text);
        int currentY = (SIGNATURE_PIXEL_START / 2) + (lines.length / 2 * ascent) + (lines.length / 2 * INTERLINE);
        for (int i = 0; i < lines.length; i++) {
            if(lines[i] != null) {
                writeCenterText(lines[i], currentY - (ascent * i) - INTERLINE);
            }
        }
    }

    /**
     * Writes the page name.
     *
     * @param pageName the page name
     */
    private static void addPageName(String pageName) {
        // Used to calculate font height.
        Rect bounds = new Rect();
        paint.getTextBounds("a", 0, 1, bounds);

        writeCenterText(pageName, (image.getHeight() + SIGNATURE_PIXEL_START + bounds.height()) / 2);
    }

    /**
     * Writes some horizontal-centered text.
     *
     * @param text the text to write.
     * @param y    the y coord.
     */
    private static void writeCenterText(String text, int y) {
        canvas.drawText(text, (image.getWidth() - paint.measureText(text)) / 2, y, paint);
    }

    /**
     * Method which splits the text into a line array.
     *
     * @param text the text to split.
     * @throws TextTooLongException if the text is too long.
     */
    private static void buildLines(String text) throws TextTooLongException {
        // Max length of a single line in pixels.
        int lineLength = image.getWidth() - TEXT_PADDING * 2;
        // Max size of the text in pixels.
        int maxSize = MAX_LINE_NUMBERS * lineLength;
        int currentFontSize = MAX_FONT_SIZE + 1;
        // Text length in pixels.
        float textLength;
        // Gets the max font size possible to use according to text length and
        // image size.
        do {
            currentFontSize--;
            paint.setTextSize(currentFontSize);
            textLength = paint.measureText(text);
            // If I get under the minimum font size I throw an exception.
            if (currentFontSize < MIN_FONT_SIZE) {
                throw new TextTooLongException();
            }
        } while (maxSize < textLength);

        // Numbers of lines of the current text.
        int lineUsed = Math.round(textLength / lineLength) + 1;
        // The actual lines of text.
        lines = new String[lineUsed];
        // Pointer to the current char in the text.
        int cursor = 0;
        float currentLineLength;
        // Approximative single char length used as a reference.
        float singleCharLength = paint.measureText("a");
        StringBuilder builder;
        char currentChar;

        // Iterates all the lines and builds them.
        for (int i = 0; i < lineUsed; i++) {
            builder = new StringBuilder();
            currentLineLength = 0;
            // Builds the single line while there's still space in the line and
            // the cursor is less than the text length.
            while (currentLineLength < lineLength && cursor < text.length()) {
                currentChar = text.charAt(cursor);
                // Adds a char to the line.
                builder.append(currentChar);
                // Measures the current line length.
                currentLineLength = paint.measureText(builder.toString());
                // If in the current line there are LINE_BREAK_CHAR_MARGIN
                // characters or less left, I try to put a linebreak at the
                // first blank space I find. If I can't (because last word
                // is longer than LINE_BREAK_CHAR_MARGIN, then I'll break
                // the line at the max length of the line.
                if (currentLineLength + (singleCharLength * LINE_BREAK_CHAR_MARGIN) >= lineLength) {
                    if (currentChar == ' ') {
                 //       builder.append("\n");
                        break;
                    }
//                    if (currentLineLength >= lineLength) {
//                   //     builder.append("\n");
//                        break;
//                    }
                }
                // Goes to the next character.
                cursor++;
            }
            // Builds the line.
            lines[i] = builder.toString();
        }
    }
}