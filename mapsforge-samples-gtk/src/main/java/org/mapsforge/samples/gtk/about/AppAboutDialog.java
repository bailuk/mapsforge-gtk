package org.mapsforge.samples.gtk.about;

import org.mapsforge.samples.gtk.Constants;

import java.io.IOException;
import java.io.InputStream;

import ch.bailu.gtk.gdk.Paintable;
import ch.bailu.gtk.gtk.AboutDialog;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.Image;
import ch.bailu.gtk.gtk.License;
import ch.bailu.gtk.gtk.Window;

public class AppAboutDialog {
    private static final int LOGO_SIZE = 128;

    public static void show(Window window) {
        final AboutDialog aboutDialog = new AboutDialog();

        try {
            aboutDialog.setLogo(loadLogo());
        } catch (IOException e) {
            System.err.println("Failed to load logo: " + e.getMessage());
        }

        aboutDialog.setProgramName(Constants.APP_NAME);
        aboutDialog.setVersion(Constants.VERSION_NAME);
        aboutDialog.setWebsite(Constants.WEBSITE);
        aboutDialog.setLicenseType(License.LGPL_3_0_ONLY);
        aboutDialog.setTitlebar(createHeaderBar(aboutDialog));
        aboutDialog.setTransientFor(window);
        aboutDialog.setModal(true);
        aboutDialog.onDestroy(aboutDialog::disconnectSignals);
        aboutDialog.show();
    }


    private static HeaderBar createHeaderBar(AboutDialog aboutDialog) {
        final var closeButton = new Button();
        closeButton.setLabel(Constants.DIALOG_CLOSE);

        final var headerBar = new HeaderBar();
        headerBar.setShowTitleButtons(true);
        headerBar.packEnd(closeButton);
        closeButton.onClicked(aboutDialog::close);
        return headerBar;
    }


    private static Paintable loadLogo() throws IOException {
        try (var inputStream = getResourceAsStream("/mapsforge.svg")) {
            final var pixbuf = ch.bailu.gtk.lib.bridge.Image.load(inputStream, LOGO_SIZE, LOGO_SIZE);
            final var image = Image.newFromPixbufImage(pixbuf);
            image.setSizeRequest(LOGO_SIZE, LOGO_SIZE);
            return image.getPaintable();
        }
    }

    private static InputStream getResourceAsStream(String resource) throws IOException {
        final var result = AppAboutDialog.class.getResourceAsStream(resource);
        if (result == null) {
            throw new IOException("Resource not found: " + resource);
        }
        return result;
    }
}
