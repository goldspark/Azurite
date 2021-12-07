package graphics.renderer;

import fonts.GlyphRenderer;
import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.Transform;

import java.util.ArrayList;

/**
 * @author Asher Haun
 */
public class TextRendererBatch extends RenderBatch {
    private ArrayList<GlyphRenderer> glyphRenderers; // even though this is an arrayList, maxBatchSize still applies
    private int numberOfGlyphRenderers;

    private float[] resetData;

    /**
     * Create a default type render batch
     * @param maxBatchSize maximum number of sprites in the batch
     * @param zIndex zIndex of the batch. Used for sorting.
     */
    TextRendererBatch(int maxBatchSize, int zIndex) {
        super(maxBatchSize, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT, ShaderDatatype.FLOAT);

        this.glyphRenderers = new ArrayList<>();
        this.numberOfGlyphRenderers = 0;
        resetData = new float[maxBatchSize * primitive.vertexCount * vertexCount];
    }

    /**
     * This function figures out how to add vertices with an origin at the top left
     *
     * @param index index of the primitive to be loaded
     * @param offset offset of where the primitive should start being added to the array
     */
    @Override
    protected void loadVertexProperties(int index, int offset) {

        GlyphRenderer glyphRenderer = glyphRenderers.get(index); // todo
        Vector4f color = glyphRenderer.getColor().toNormalizedVec4f();
        Vector2f[] textureCoordinates = glyphRenderer.getTexCoords();

        int textureID;
        if (glyphRenderer.getTexture() != null)
            textureID = addTexture(glyphRenderer.getTexture());
        else
            textureID = 0;

        // Add vertex with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1:
                    yAdd = 0.0f;
                    break;
                case 2:
                    xAdd = 0.0f;
                    break;
                case 3:
                    yAdd = 1.0f;
                    break;
            }

            // Load position
            Transform spr = glyphRenderer.getLocalTransform();
            data[offset] = spr.getPosition().x + (xAdd * spr.scale.x);
            data[offset + 1] = spr.getPosition().y + (yAdd * spr.scale.y);

            // Load color
            data[offset + 2] = color.x; // Red
            data[offset + 3] = color.y; // Green
            data[offset + 4] = color.z; // Blue
            data[offset + 5] = color.w; // Alpha

            // Load texture coordinates
            data[offset + 6] = textureCoordinates[i].x;
            data[offset + 7] = textureCoordinates[i].y;

            // Load texture ID
            data[offset + 8] = textureID;

            // Sticky to camera or not
            data[offset + 9] = glyphRenderer.isSticky() ? 1.0f : 0.0f;

            offset += vertexCount;
        }
    }

    /**
     * @return the size of the glyphRenders ArrayList in this batch.
     */
    public int getSize () {
        return numberOfGlyphRenderers;
    }

    /**
     * Remove the object at index
     * @param i the index
     */
    public void removeIndex (int i) {
        if (glyphRenderers.size() > 0) {
            glyphRenderers.remove(i);
            remove(i);
            numberOfGlyphRenderers --;
            if (i >= 1)
                super.updateBuffer(i - 1);
        }
    }

    /**
     * Removes all glyph renderers from this batch and cleans up after them.
     */
    public void removeGlyphRenderers () {
        glyphRenderers.clear();
        this.numberOfGlyphRenderers = 0;
        // References an existing empty array to avoid stack allocations over and over again.
        data = resetData;
    }

    /**
     * Load any GlyphRenderers that have been changed.
     */
    @Override
    public void updateBuffer () {
        for (int i = 0; i < glyphRenderers.size(); i ++) {
            if (glyphRenderers.get(i).isDirty()) {
                load(i);
                glyphRenderers.get(i).setClean();
            }
        }
        super.updateBuffer();
    }

    /**
     * Function for calling loadVertexProperties but also sets up necessary stuff relating
     * to uploading data to the gpu.
     * Always call this function instead of calling loadVertexProperties()
     *
     * @param index index of the sprite to be loaded
     */
    @Override
    protected void load(int index) {
        if (index >= spriteCount) spriteCount++;
        loadVertexProperties(index, getOffset(index));
    }

    /**
     * Adds a Text object to this batch
     *
     * @param glyphR the GlyphRenderer to be added to the render batch.
     * @return if the sprite was successfully added to the batch
     */
    public boolean addGlyphRenderer (GlyphRenderer glyphR) {
        // If the batch still has room, and is at the same z index as the sprite, then add it to the batch
        if (hasRoomLeft() && zIndex() == glyphR.getParentText().zIndex()) {
            Texture tex = glyphR.getTexture();
            if (tex == null || (hasTexture(tex) || hasTextureRoom())) {
                // Get the index and add the renderObject to the list
                int index = this.numberOfGlyphRenderers;
                this.glyphRenderers.add(glyphR); // = glyphR;
                this.numberOfGlyphRenderers++;

                // Add properties to local vertices array
                load(index);

                if (this.numberOfGlyphRenderers >= this.maxBatchSize) {
                    this.hasRoom = false;
                }
                return true;
            }
        }
        return false;
    }

}
