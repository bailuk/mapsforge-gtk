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

import javax.imageio.ImageIO;

import ch.bailu.gtk.wrapper.Bytes;
/*
public class ImageConverter {

    private static final int TYPE = BufferedImage.TYPE_INT_ARGB_PRE;
    private static final float DEFAULT_SIZE = 400f;

    public static BufferedImage streamToBufferedImage(InputStream inputStream) throws IOException {
        throwIoExceptionOnNull(inputStream);
        try {
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }


    public static BufferedImage streamToBufferedImage(InputStream inputStream, String name, float scaleFactor, int width, int height, int percent) throws IOException {
        throwIoExceptionOnNull(inputStream);
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
            BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), TYPE);

            icon.paintIcon(null, bufferedImage.createGraphics(), 0, 0);

            return bufferedImage;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private static BufferedImage to32bit(BufferedImage in) throws IOException {
        throwIoExceptionOnNull(in);
        BufferedImage result = in;

        if (result.getType() != TYPE) {
            result = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics g = result.getGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();
        }
        return result;
    }

    private static Bytes toBytes(BufferedImage in) throws IOException {
        throwIoExceptionOnNull(in);

        DataBuffer dataBuffer = in.getRaster().getDataBuffer();

        if (dataBuffer instanceof DataBufferInt) {
            DataBufferInt intBuffer = ((DataBufferInt) dataBuffer);
            return new Bytes(argbToBgra(toBytes(intBuffer)));

        } else {
            throw new IOException("cannot handle image format");
        }

    }

    private static byte[] toBytes(DataBufferInt dataBufferInt) {
        int[] pixelData = dataBufferInt.getData();
        ByteBuffer byteBuffer = ByteBuffer.allocate(pixelData.length * 4);
        byteBuffer.asIntBuffer().put(pixelData);
        return byteBuffer.array();
    }

    public static ImageSurface toImageSurface(BufferedImage in) throws IOException {
        throwIoExceptionOnNull(in);
        return new LoadedImageSurface(toBytes(to32bit(in)), in.getWidth(), in.getHeight());
    }


    private static void throwIoExceptionOnNull(Object o) throws IOException {
        if (o == null) throw new IOException();
    }

    private static byte[] argbToBgra(byte[] buffer) {
        for (int i = 0; i < buffer.length; i += 4) {
            byte tmp = buffer[i];
            buffer[i] = buffer[i + 3];
            buffer[i + 3] = tmp;


            tmp = buffer[i + 1];
            buffer[i + 1] = buffer[i + 2];
            buffer[i + 2] = tmp;
        }
        return buffer;
    }
}
*/