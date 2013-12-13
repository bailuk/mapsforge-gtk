/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.android.graphics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.graphics.ResourceBitmap;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public final class AndroidGraphicFactory implements GraphicFactory {

	// turn on for bitmap accounting
	public static final boolean debugBitmaps = false;

	// if true resourceBitmaps will be kept in the cache to avoid
	// multiple loading
	public static boolean keepResourceBitmaps = true;

	// determines size of bitmaps used, RGB_565 is 2 bytes per pixel
	// note that ARGB_8888 uses 4 bytes per pixel (with severe impact
	// on memory use) and seems also to have buffer corruption
	// problems
	static public final Config bitmapConfig = Config.RGB_565;
	static final int DEFAULT_BACKGROUND_COLOR = android.graphics.Color.rgb(238, 238, 238);

	public static AndroidGraphicFactory INSTANCE;

	private static final String PREFIX_ASSETS = "assets:";
	private final Application application;
	private final int defaultTileSize = Tile.TILE_SIZE;
	private final float scaleFactor;
	private float userScaleFactor = 1.0f;
	private int backgroundColor = DEFAULT_BACKGROUND_COLOR;

	/**
	 * return the byte usage per pixel of a bitmap based on its configuration.
	 */
	static public int getBytesPerPixel(Config config) {
		if (config == Config.ARGB_8888) {
			return 4;
		} else if (config == Config.RGB_565) {
			return 2;
		} else if (config == Config.ARGB_4444) {
			return 2;
		} else if (config == Config.ALPHA_8) {
			return 1;
		}
		return 1;
	}

	public static Bitmap convertToBitmap(Drawable drawable) {
		android.graphics.Bitmap bitmap;
		if (drawable instanceof BitmapDrawable) {
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		} else {
			int width = drawable.getIntrinsicWidth();
			int height = drawable.getIntrinsicHeight();
			bitmap = android.graphics.Bitmap.createBitmap(width, height, Config.ARGB_8888);
			android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);

			Rect rect = drawable.getBounds();
			drawable.setBounds(0, 0, width, height);
			drawable.draw(canvas);
			drawable.setBounds(rect);
		}

		return new AndroidBitmap(bitmap);
	}

	public static Canvas createGraphicContext(android.graphics.Canvas canvas) {
		return new AndroidCanvas(canvas);
	}

	public static void createInstance(Application app) {
		INSTANCE = new AndroidGraphicFactory(app);
	}

	static android.graphics.Bitmap getBitmap(Bitmap bitmap) {
		return ((AndroidBitmap) bitmap).bitmap;
	}

	public static android.graphics.Canvas getCanvas(Canvas canvas) {
		return ((AndroidCanvas) canvas).canvas;
	}

	static int getColor(Color color) {
		switch (color) {
			case BLACK:
				return android.graphics.Color.BLACK;
			case BLUE:
				return android.graphics.Color.BLUE;
			case GREEN:
				return android.graphics.Color.GREEN;
			case RED:
				return android.graphics.Color.RED;
			case TRANSPARENT:
				return android.graphics.Color.TRANSPARENT;
			case WHITE:
				return android.graphics.Color.WHITE;
		}

		throw new IllegalArgumentException("unknown color: " + color);
	}

	static android.graphics.Matrix getMatrix(Matrix matrix) {
		return ((AndroidMatrix) matrix).matrix;
	}

	public static android.graphics.Paint getPaint(Paint paint) {
		return ((AndroidPaint) paint).paint;
	}

	static android.graphics.Path getPath(Path path) {
		return ((AndroidPath) path).path;
	}

	private AndroidGraphicFactory(Application app) {
		this.application = app;
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) app.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
		// we need to scale up proportionally to the scaledDensity otherwise the map display gets too small
		this.scaleFactor = metrics.scaledDensity;
		setTileSize();
	}

	private void setTileSize() {
		Tile.TILE_SIZE = (int) (this.defaultTileSize * this.scaleFactor * this.userScaleFactor);
	}

	@Override
	public Bitmap createBitmap(int width, int height) {
		return new AndroidBitmap(width, height);
	}

	@Override
	public Canvas createCanvas() {
		return new AndroidCanvas();
	}

	@Override
	public int createColor(Color color) {
		return getColor(color);
	}

	@Override
	public int createColor(int alpha, int red, int green, int blue) {
		return android.graphics.Color.argb(alpha, red, green, blue);
	}

	@Override
	public Matrix createMatrix() {
		return new AndroidMatrix();
	}

	@Override
	public Paint createPaint() {
		return new AndroidPaint();
	}

	@Override
	public Path createPath() {
		return new AndroidPath();
	}

    @Override
    public ResourceBitmap createResourceBitmap(InputStream inputStream, int hash) {
        return new AndroidResourceBitmap(inputStream, hash);
    }

	@Override
	public TileBitmap createTileBitmap() {
		return new AndroidTileBitmap();
	}

	@Override
	public TileBitmap createTileBitmap(InputStream inputStream) {
		return new AndroidTileBitmap(inputStream);
	}

	@Override
	public int getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public float getScaleFactor() {
		return this.scaleFactor * this.userScaleFactor;
	}

	public float getUserScaleFactor() {
		return this.userScaleFactor;
	}

	@Override
	public InputStream platformSpecificSources(String relativePathPrefix, String src) throws IOException {
		// this allows loading of resource bitmaps from the Andorid assets folder
		if (src.startsWith(PREFIX_ASSETS)) {
			String absoluteName = relativePathPrefix + src.substring(PREFIX_ASSETS.length());
			InputStream inputStream = this.application.getAssets().open(absoluteName);
			if (inputStream == null) {
				throw new FileNotFoundException("resource not found: " + absoluteName);
			}
			return inputStream;
		}
		return null;
	}

	@Override
	public ResourceBitmap renderSvg(InputStream inputStream, int hash) {
		return new AndroidSvgBitmap(inputStream, hash, this.scaleFactor);
	}

	@Override
	public void setBackgroundColor(int color) {
		this.backgroundColor = color;
	}

	public void setUserScaleFactor(float scaleFactor) {
		this.userScaleFactor = scaleFactor;
		setTileSize();
	}

}