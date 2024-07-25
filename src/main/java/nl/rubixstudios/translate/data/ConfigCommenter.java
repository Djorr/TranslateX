package nl.rubixstudios.translate.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCommenter {

    private final HashMap<String, String> comments = new HashMap<>();
    private String header = "";

    /**
     * Add comment to a config option.<br>
     * Supports multiline comments!
     *
     * @param path    Config path to add comment to
     * @param comment Comment to add
     */
    public void addComment(String path, String comment) {
        comments.put(path, comment);
    }

    /**
     * Set the header for this config file
     *
     * @param header Header to add
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Saves comments to config file
     *
     * @param file File to save to
     * @throws IOException io
     */
    public void saveComments(File file) throws IOException {
        ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath());
        lines.removeIf(s -> s.trim().startsWith("#") || s.trim().length() <= 4);
        lines.add(0, "# " + header.replace("\n", "\n# ") + "\n");

        for (Map.Entry<String, String> comment : comments.entrySet()) {
            int line = this.findKey(lines, comment.getKey());

            if (line == -1) {
                throw new IllegalStateException("You are trying to add a comment to key " + comment.getKey() + " which does not exist!");
            }

            String prefix = this.repeat(" ", getIndentation(lines.get(line))) + "# ";
            boolean noNewline = getIndentation(lines.get(line)) > getIndentation(lines.get(line - 1));
            if (line >= 0)
                lines.add(line, (noNewline ? "" : "\n") + prefix + comment.getValue().replace("\n", "\n" + prefix));
            else System.out.printf("Failed to find key %s in %s!", comment.getKey(), file);
        }

        FileWriter fw = new FileWriter(file);
        fw.write(String.join("\n", lines));
        fw.close();
    }

    private int getIndentation(String s) {
        if (!s.startsWith(" ")) return 0;
        int i = 0;
        while ((s = s.replaceFirst(" ", "")).startsWith(" ")) i++;
        return i + 1;
    }

    private int findKey(List<String> lines, String key) {
        String[] parts = key.split("\\.");
        String lastPart = parts[parts.length - 1];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.startsWith(lastPart)) {
                return i;
            }
        }
        return -1;
    }

    private String repeat(String string, int count) {
        return new String(new char[count]).replace("\0", string);
    }
}
