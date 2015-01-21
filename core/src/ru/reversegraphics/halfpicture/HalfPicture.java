package ru.reversegraphics.halfpicture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HalfPicture extends ApplicationAdapter {
	Stage stage;

	@Override
	public void create () {
		stage = new SceneStage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
	}
	public void resize (int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Gdx.gl.glClearColor(1f,1f,0f,1f);
		stage.act();
		stage.draw();
	}

	public void dispose() {
		stage.dispose();
	}
}
