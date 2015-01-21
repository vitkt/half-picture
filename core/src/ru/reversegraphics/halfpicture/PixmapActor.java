package ru.reversegraphics.halfpicture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Виталик on 21.01.2015.
 */
public class PixmapActor extends Actor {
    private Pixmap _pixmap;
    private Texture _pixmapTexture;

    PixmapActor(Pixmap pixmap) {
        _pixmap = pixmap;
        setWidth(_pixmap.getWidth());
        setHeight(_pixmap.getHeight());
        _pixmapTexture = new Texture(_pixmap, Pixmap.Format.RGB888, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(_pixmapTexture, getX(), getY());
    }

    public Pixmap getPixmap() {
        return _pixmap;
    }
}
