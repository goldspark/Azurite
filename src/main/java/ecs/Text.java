package ecs;
//{This comment is intentionally added to create a git merge conflict}
import fonts.Font;
import fonts.Glyph;
import fonts.GlyphRenderer;
import graphics.Color;
import graphics.HSLColor;
import graphics.renderer.TextRenderer;
import graphics.renderer.TextRendererBatch;
import org.joml.Vector2f;
import util.Transform;
import util.Engine;
import util.Logger;
import util.Utils;

import java.util.ArrayList;

/**
 * @author Asher Haun
 */
public class Text {
    private Color color = Color.WHITE;

    private Sprite sprite;

    private Transform lastTransform = new Transform();
    private boolean isSticky = false;
    int zIndex;

    ArrayList<GlyphRenderer> glyphRenderers;

    Transform transform = new Transform();
    Font font;
    CharSequence text;

    TextRendererBatch currentBatch;

    public Text (String string, Font font, Color color, float x, float y, int zIndex, boolean isSticky) {
        this.text = string;
        if (string.length() >= TextRenderer.getMaxBatchSize()) {
            Logger.logInfo("The String \"" + string.substring(0, 7) + "...\" passed is longer than the allowed string size for text: " + TextRenderer.getMaxBatchSize());
            this.text = string.substring(0, TextRenderer.getMaxBatchSize() - 4) + "...";
        }
        this.font = font;
        this.color = color;
        this.transform.setPosition(new Vector2f(x, y));
        this.lastTransform.setPosition(new Vector2f(x, y));
        this.zIndex = zIndex;
        this.isSticky = isSticky;

        glyphRenderers = new ArrayList<>();

        generateGlyphs();
        Engine.scenes().currentScene().textRenderer.add(this);
        Engine.scenes().currentScene().addUiObject(this);

        currentBatch = glyphRenderers.get(0).getBatch();

    }

    public Text (String string, Font font, Color color, float x, float y) {
        this(string, font, color, x, y, 1, true);
    }

    public Text (String string, Color color, float x, float y) {
        this(string, new Font(), color, x, y, 1, true);
    }

    public Text (String string, float x, float y) {
        this(string, new Font(), Color.WHITE, x, y, 1, true);
    }

    public void update () {
        if (!lastTransform.equals(this.transform)) {
            Vector2f movementDelta = new Vector2f(transform.getX() - lastTransform.getX(), transform.getY() - lastTransform.getY());

            for (GlyphRenderer i : glyphRenderers) {
                i.updatePosition(movementDelta);
            }
            for (GlyphRenderer i : glyphRenderers) {
                i.update(Engine.deltaTime());
            }
        }
        lastTransform.setX(transform.getX());
        lastTransform.setY(transform.getY());
    }

    public void change (String string) {
        currentBatch = glyphRenderers.get(0).getBatch();
        glyphRenderers.clear();

        this.text = string + " ";

        generateGlyphs();
        Engine.scenes().currentScene().textRenderer.changeText(this, currentBatch);
    }

    char ch;
    private void generateGlyphs () {
        int textHeight = font.getHeight(text);
        int lineIncreases = 0;

        Transform t = transform.copy();

        float drawX = t.getX();
        float drawY = t.getY();

        for (int i = 0; i < text.length(); i++) {
            if (i >= TextRenderer.getMaxBatchSize() - 3) {
                if (i < TextRenderer.getMaxBatchSize()) {
                    ch = '.';
                } else break;
            } else ch = text.charAt(i);

            if (ch == '\n') {
                lineIncreases ++;
                /* Line feed, set x and y to draw at the next line */
                drawY = t.getY() + (font.getFontHeight() * lineIncreases);
                drawX = t.getX();
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = font.getGlyphs().get(ch);

            glyphRenderers.add(new GlyphRenderer(new Transform(drawX, drawY, g.width, g.height), g, this, ch, isSticky, this.color));

            drawX += g.width;
        }
        if (textHeight > font.getFontHeight()) {
            drawY += textHeight - font.getFontHeight();
        }
    }

    public int getBatchIndex () {
        if (glyphRenderers.size() == 0) return -1;
        return glyphRenderers.get(0).getBatchIndex();
    }

    public ArrayList<GlyphRenderer> getGlyphRenderers () {
        return glyphRenderers;
    }

    public void setColor (Color color) {
        this.color = color;
        for (GlyphRenderer g : this.glyphRenderers) {
            g.setColor(this.color);
        }
    }

    public Color getColor () {
        return this.color;
    }

    public void rainbowify () {
        for (int i = 0; i < this.glyphRenderers.size(); i ++) {
            GlyphRenderer g = this.glyphRenderers.get(i);

            g.setColor(new HSLColor(Utils.map(i, 0, this.glyphRenderers.size(), 0, 360), 100, 50, 1).toRGBColor());
        }
    }

    public boolean isSticky () {
        return isSticky;
    }

    /**
     * @return Transform of the gameObject
     */
    public Transform getTransform() {
        return this.transform;
    }

    /**
     * Takes a Transform as a parameter and sets this instance to a copy of that transform
     *
     * @param t
     */
    public void setTransform(Transform t) {
        this.transform = t.copy();
    }

    public int zIndex () {
        return zIndex;
    }

    public void setZindex(int z) {
        zIndex = z;
    }

    public float getX () {
        return transform.getX();
    }

    public float getY () {
        return transform.getY();
    }

    public void setX (float x) {
        transform.setX(x);
    }

    public void setY (float y) {
        transform.setY(y);
    }

    public void addY (float y) {
        transform.addY(y);
    }

    public void addX (float x) {
        transform.addX(x);
    }
}
