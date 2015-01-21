package ru.reversegraphics.halfpicture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Виталик on 21.01.2015.
 */
public class CanvasActor extends Actor {
    Pixmap _canvas;
    Texture _canvasTexture;
    CanvasActor(int width, int height)
    {
        setWidth(width);
        setHeight(height);
        _canvas = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        _canvas.setColor(1f, 1f, 1f, 1f);
        _canvas.fillRectangle(0, 0, width,height);
        _canvasTexture = new Texture(_canvas, Pixmap.Format.RGB888, false);
    }

    public Actor hit (float x, float y, boolean touchable) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
        {
            if (touchable)
            {
                makePoint(x,y);
                return this;
            }
        }
        return null;
    }

    private void makePoint(float x, float y) {
        _canvas.setColor(Color.BLACK);
        _canvas.drawPixel((int)x, _canvas.getHeight()-(int)y);
        _canvasTexture = new Texture(_canvas, Pixmap.Format.RGB888, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(_canvasTexture, getX(),getY());
    }
}
