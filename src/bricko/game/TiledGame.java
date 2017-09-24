package bricko.game;

import com.badlogic.gdx.Game;

import bricko.game.screens.Play;

public class TiledGame extends Game{

	@Override
	public void create() {
		setScreen(new Play());

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
