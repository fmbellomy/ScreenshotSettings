package com.quantumgarbage.screenshotsettings.util;

import org.w3c.dom.NodeList;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class PNGMetadataManipulator {
    // These first two methods are proudly stolen from Stack Overflow.
    private static void addTextEntry(final IIOMetadata metadata, final String key, final String value)
            throws IIOInvalidTreeException {
        final IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
        textEntry.setAttribute("keyword", key);
        textEntry.setAttribute("value", value);

        final IIOMetadataNode text = new IIOMetadataNode("Text");
        text.appendChild(textEntry);

        final IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
        root.appendChild(text);

        metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
    }


    private static String getTextEntry(final IIOMetadata metadata, final String key) {
        final IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
        final NodeList entries = root.getElementsByTagName("TextEntry");

        for (int i = 0; i < entries.getLength(); i++) {
            final IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
            if (node.getAttribute("keyword").equals(key)) {
                return node.getAttribute("value");
            }
        }

        return null;
    }

    private static void removeTextEntry(final IIOMetadata metadata, final String key) {
        final IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
        final NodeList entries = root.getElementsByTagName("TextEntry");

        for (int i = 0; i < entries.getLength(); i++) {
            final IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
            if (node.getAttribute("keyword").equals(key)) {
                // feels a little weird to do this, but I'm fairly sure it should work.
                node.getParentNode().removeChild(node);
            }
        }
    }

    public static void attachMetadata(final File f, final Map<String, String> meta) {
        try {
            final ImageInputStream input = ImageIO.createImageInputStream(f);
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            final ImageReader reader = readers.next();
            reader.setInput(input);
            final IIOImage img = reader.readAll(0, null);
            // Image is fully loaded into memory. Now we add metadata and overwrite the original.
            for (final Map.Entry<String, String> entry : meta.entrySet()) {
                addTextEntry(img.getMetadata(), entry.getKey(), entry.getValue());
            }
            input.close();
            final ImageOutputStream out = ImageIO.createImageOutputStream(f);
            final ImageWriter writer = ImageIO.getImageWriter(reader);
            writer.setOutput(out);
            writer.write(img);
            out.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }


    }
}
