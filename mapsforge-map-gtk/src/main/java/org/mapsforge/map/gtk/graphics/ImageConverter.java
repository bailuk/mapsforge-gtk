package org.mapsforge.map.gtk.graphics;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.app.beans.SVGIcon;

import org.mapsforge.core.graphics.GraphicUtils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import ch.bailu.gtk.wrapper.Bytes;

public class ImageConverter {

    private static final float DEFAULT_SIZE = 400f;

    public static ImageSurface toImageSurface(BufferedImage in) throws IOException {
        in = to32bit(in);
        return new LoadedImageSurface(toBytes(in), in.getWidth(), in.getHeight());
    }

    public static ImageSurface bitmapToImageSurface(InputStream in) throws IOException {
        return toImageSurface(ImageIO.read(in));
    }


    public static ImageSurface svgToImageSurface(InputStream inputStream, String name, float scaleFactor, int width, int height, int percent) throws IOException {
        try {
            URI uri = SVGCache.getSVGUniverse().loadSVG(inputStream, name);
            SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);

            double scale = scaleFactor / Math.sqrt((diagram.getHeight() * diagram.getWidth()) / DEFAULT_SIZE);

            float[] bmpSize = GraphicUtils.imageSize(diagram.getWidth(), diagram.getHeight(), (float) scale, width, height, percent);

            SVGIcon icon = new SVGIcon();
            icon.setAntiAlias(true);
            icon.setAutosize(SVGIcon.AUTOSIZE_STRETCH);
            icon.setPreferredSize(new Dimension((int) bmpSize[0], (int) bmpSize[1]));
            icon.setSvgURI(uri);
            BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            icon.paintIcon(null, bufferedImage.createGraphics(), 0, 0);

            return toImageSurface(bufferedImage);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private static BufferedImage to32bit(BufferedImage in) {
       int type = in.getType();

       if (type == BufferedImage.TYPE_INT_ARGB) {
           return in;

       } else {
           BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
           Graphics g = out.getGraphics();
           g.drawImage(in, 0,0,null);
           g.dispose();
           return out;
       }
    }

    private static Bytes toBytes(BufferedImage in) throws IOException {
        DataBuffer dataBuffer = in.getRaster().getDataBuffer();

        if (dataBuffer instanceof DataBufferInt) {
            int[] pixelData = ((DataBufferInt) dataBuffer).getData();
            ByteBuffer byteBuffer = ByteBuffer.allocate(pixelData.length * 4);
            byteBuffer.asIntBuffer().put(IntBuffer.wrap(pixelData));

            return new Bytes(byteBuffer.array());

        } else {
            throw new IOException("cannot handle image format");
        }

    }
}
