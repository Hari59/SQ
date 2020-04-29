package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class StickQuestGame extends ApplicationAdapter{
	//public static Batch sb;
	public static SpriteBatch sb;
	private OrthographicCamera cam;
	private Player player;
	//private InputHandler inputHandler;
	private float delta;
	private int dir = 2;

	private TiledMap tileMap, collisionMap;
	private OrthogonalTiledMapRenderer tileMapRenderer, collisionMapRenderer;
	private TiledMapTileLayer collisionLayer;

	private Texture whitebirb;
	Array<Rectangle> birds;
	Rectangle playerbox;
	long lastEnemyTime;
	int level = 1;
	int enemiesEncountered = 0;

	Controller controller;
	BitmapFont font;

	public StickQuestGame(int level){
		this.level = level;
	}

	@Override
	public void create () {
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sb.setProjectionMatrix(cam.combined);


		//load map and other stuff---------------------------------------------------------------------
		whitebirb = new Texture(Gdx.files.internal("whitebirb.png"));
		tileMap = new TmxMapLoader().load("SQlvl1.tmx");
		tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

		collisionMap = new TmxMapLoader().load("collision.tmx");
		collisionMapRenderer = new OrthogonalTiledMapRenderer(collisionMap);
		collisionLayer = (TiledMapTileLayer) collisionMap.getLayers().get("Tile Layer 1");

		player = new Player(collisionLayer);

		playerbox = new Rectangle();
		playerbox.x = player.x;
		playerbox.y = player.y;
		playerbox.width=80;
		playerbox.height=80;

		birds = new Array<Rectangle>();
		spawnEnemy();

		controller = new Controller();
		font = new BitmapFont();
		//end load map and other stuff----------------------------------------------------------------
	}

	private void spawnEnemy() {
		Rectangle enemy = new Rectangle();
		enemy.x = MathUtils.random(80, Gdx.graphics.getWidth()-120);
		enemy.y = Gdx.graphics.getHeight()-80;
		enemy.width = 69;
		enemy.height = 42;
		birds.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		delta = Gdx.graphics.getDeltaTime();

		//tile controls n stuff ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			if (controller.isDownPressed() && !player.checkCollisionMap(player.x,player.y)) {
				player.y = player.y - 5;
				if(player.checkCollisionMap(player.x,player.y))
					player.y=player.y+5;
				dir = 1; //down
			}
			if (controller.isUpPressed() && !player.checkCollisionMap(player.x,player.y)) {
				player.y = player.y + 5;
				if(player.checkCollisionMap(player.x,player.y))
					player.y=player.y-5;
				dir = 2; //up
			}
			if (controller.isRightPressed() && !player.checkCollisionMap(player.x,player.y)) {
				player.x = player.x + 5;
				if(player.checkCollisionMap(player.x,player.y))
					player.x=player.x-5;
				dir = 3; //right
			}
			if (controller.isLeftPressed() && !player.checkCollisionMap(player.x,player.y)) {
				player.x = player.x - 5;
				if(player.checkCollisionMap(player.x,player.y))
					player.x=player.x+5;
				dir = 4; //left
			}
		}

		playerbox.x = player.x;
		playerbox.y = player.y;

		if (TimeUtils.nanoTime() - lastEnemyTime > 300000000)
			spawnEnemy();
		Iterator<Rectangle> iter = birds.iterator();
		while (iter.hasNext()) {
			Rectangle enemy = iter.next();
			enemy.y -= 400 * Gdx.graphics.getDeltaTime();
			if (enemy.y < 80) {
				iter.remove();
			}
			if (enemy.overlaps(playerbox)) {
				iter.remove();
				enemiesEncountered+=1;
			}

		}

		//end tile controls n stuff ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		// rendering---------------------
		tileMapRenderer.setView(cam);
		tileMapRenderer.render();
		cam.update();
		controller.draw();
		sb.begin();
		player.render(sb, dir);
		for (Rectangle enemy : birds) {
			    sb.draw(whitebirb, enemy.x, enemy.y);
		}
		font.draw(sb, "Enemies Encountered: "+ enemiesEncountered, 10, 10);
		sb.end();
		//end rendering-----------------
	}
}