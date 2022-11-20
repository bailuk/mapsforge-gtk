package org.mapsforge.samples.gtk.lib;

import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.FileChooser;
import ch.bailu.gtk.gtk.FileChooserAction;
import ch.bailu.gtk.gtk.FileChooserDialog;
import ch.bailu.gtk.gtk.ResponseType;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;

public class FileDialog {

    private String title = "";
    private String cancel = "Cancel";
    private String ok = "OK";
    private String initialPath = "";
    private OnResponse onResponseCallback = path -> {};

    private int action = FileChooserAction.OPEN;

    public interface OnResponse {
        void onResponse(String path);
    }

    public FileDialog title(String label) {
        this.title = label;
        return this;
    }

    public FileDialog ok(String label) {
        this.ok = label;
        return this;
    }

    public FileDialog cancel(String label) {
        this.cancel = label;
        return this;
    }

    public FileDialog open() {
        this.action = FileChooserAction.OPEN;
        return this;
    }

    public FileDialog save() {
        this.action = FileChooserAction.SAVE;
        return this;
    }

    public FileDialog onResponse(OnResponse response) {
        this.onResponseCallback = response;
        return this;
    }

    public FileDialog selectFolder() {
        this.action = FileChooserAction.SELECT_FOLDER;
        return this;
    }

    public FileDialog path(String path) {
        this.initialPath = path;
        return this;
    }

    public void show(Window window) {
        var dialog = new FileChooserDialog(title, window, action, null);

        var initialPath = new Str(this.initialPath);

        setInitialPath(dialog, initialPath);
        dialog.addButton(cancel, ResponseType.CANCEL);
        dialog.addButton(ok, ResponseType.OK);
        dialog.onDestroy(() -> {
            dialog.disconnectSignals();
            initialPath.destroy();
        });
        dialog.onResponse((response) -> {
            String path = "";
            if (response == ResponseType.OK) {
                path = getPath(dialog);
            }
            onResponseCallback.onResponse(path);
            dialog.close();
        });
        dialog.show();
    }

    private static void setInitialPath(FileChooserDialog dialog, Str initialPath) {
        if (initialPath.getSize() > 1) {
            try {
                var path = File.newForPath(initialPath);
                new FileChooser(dialog.cast()).setFile(path);
                unref(path);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String getPath(FileChooserDialog dialog) {
        var chooser = new FileChooser(dialog.cast());
        var file = chooser.getFile();
        var path = file.getPath();
        var result = path.toString();

        unref(file);
        path.destroy();

        return result;
    }

    private static void unref(File file) {
        new ch.bailu.gtk.gobject.Object(file.cast()).unref();
    }
}
