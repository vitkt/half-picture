package ru.reversegraphics.halfpicture;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Виталик on 21.01.2015.
 */
public class SceneStage extends Stage {
    Pixmap pix;
    CanvasActor canvasActor;
    PixmapActor pixmapActor;
    SceneStage(Viewport v)
    {
        super(v);
        pix = new Pixmap(new FileHandle("rain.jpg"));
        pixmapActor = new PixmapActor(pix);
        canvasActor = new CanvasActor(pix.getWidth(),pix.getHeight());
        canvasActor.setX(pixmapActor.getWidth());
        addActor(pixmapActor);
        addActor(canvasActor);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
     isTouched = true;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouched = false;
        return super.touchUp(screenX, screenY, pointer, button);

    }

    boolean isTouched  = false;
    @Override
    public Actor hit(float stageX, float stageY, boolean touchable) {
        return super.hit(stageX, stageY, isTouched);

    }
}
