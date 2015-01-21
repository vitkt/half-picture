package ru.reversegraphics.halfpicture;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Виталик on 21.01.2015.
 */
public class SceneStage extends Stage {
    Pixmap pix;
    CanvasActor _canvasActor;
    PixmapActor _pixmapActor;
    TextButton _compareButton;
    Label _resultLabel;
    ClickListener _clickListener;

    TextButton createButton(String text) {
        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checkedOver = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        return new TextButton(text, skin);
    }
    boolean isCompareWorking()
    {
        return _aResult!=null && !_aResult.isDone();
    }
    SceneStage(Viewport v) {
        super(v);

        _compareButton = createButton("compare");
        pix = new Pixmap(new FileHandle("rain.jpg"));
        _pixmapActor = new PixmapActor(pix);
        _canvasActor = new CanvasActor(pix.getWidth(), pix.getHeight());
        _canvasActor.setX(_pixmapActor.getWidth());
        _compareButton.setY(pix.getHeight());
        _compareButton.setHeight(pix.getHeight() / 2);
        _compareButton.setWidth(pix.getWidth() * 2);
        _resultLabel = createLabel("result");
        _resultLabel.setY(_pixmapActor.getHeight() + _compareButton.getHeight());
        _resultLabel.setWidth(_compareButton.getWidth());
        _resultLabel.setHeight(_compareButton.getHeight());
        _clickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (!isCompareWorking())
                    _aResult = _executor.submit(_compareTask);
            }
        };
        _executor = new AsyncExecutor(1);
        _compareButton.addListener(_clickListener);
        addActor(_pixmapActor);
        addActor(_canvasActor);
        addActor(_compareButton);
        addActor(_resultLabel);

    }

    private Label createLabel(String text) {
        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
        labelStyle.fontColor = Color.BLACK;
        skin.add("default", labelStyle);
        return new Label(text, skin);
    }

    AsyncExecutor _executor;
    AsyncResult<Integer> _aResult;
    AsyncTask<Integer> _compareTask = new AsyncTask<Integer>() {
        @Override
        public Integer call() throws Exception {

            return new Integer((int) (comparePixmaps() * 100f));
        }
    };

    private float comparePixmaps() {
        Pixmap original = _pixmapActor.getPixmap();
        Pixmap userWork = _canvasActor.getPixmap();
        float workSize = original.getWidth()*original.getHeight();
        float goodPixels=0f;
        float workPixels = 0f;
        float badPixels=0f;
        float whitePixels = 0f;
        float whiteWorkPixels = 0f;
        for(int i=0;i<original.getHeight();i++)
        {
            for(int j=0;j<original.getWidth();j++)
            {
                if (original.getPixel(j,i)!=Color.WHITE.toIntBits())
                {
                    workPixels+=1.f;
                    if (userWork.getPixel(original.getWidth()-j, i)!=Color.WHITE.toIntBits())
                    {
                        goodPixels+=1.f;
                    }
                    else
                        badPixels+=1.f;
                }
                else {
                    whiteWorkPixels += 1.0f;
                    if (userWork.getPixel(original.getWidth()-j, i)==Color.WHITE.toIntBits())
                    {
                        whitePixels +=1f;
                    }
                }
            }
        }
        return (goodPixels/workPixels)*(whitePixels/whiteWorkPixels);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!isCompareWorking())
            isTouched = true;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouched = false;
        return super.touchUp(screenX, screenY, pointer, button);

    }

    boolean isTouched = false;

    @Override
    public void act() {
        super.act();
        if (_aResult!=null && !isCompareWorking()) {
            Integer res = _aResult.get();
            _resultLabel.setText(res.toString());
        }

    }

    @Override
    public Actor hit(float stageX, float stageY, boolean touchable) {
        return super.hit(stageX, stageY, isTouched);

    }
}
