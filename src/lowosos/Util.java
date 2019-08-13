package lowosos;

import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Util {
    public static boolean isSave(File f) {
        String[] name = f.getName().split("\\.");
        if (Util.isFile(f) && name.length == 2 && name[0].equals("saveati") && isSaveNum(name[1])) return true;
        return false;
    }

    public static boolean isSaveFolder(File f) {
        if (!isDirectory(f)) return false;
        File[] files = f.listFiles();
        if (files == null) return false;
        for (File file : files) {
            if (isSave(file)) return true;
        }
        return false;
    }

    private static boolean isSaveNum(String s) {
        Integer n = -1;
        try {
            n = Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        if (n > 15 || n < 0) return false;
        return true;
    }

    public static int getSaveLevel(File f) {
        if (!isSave(f)) return 0;
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            line = reader.readLine();
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found, perhaps already deleted.");
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classifyLevel(line);
    }

    public static String getSaveLevelsInFolder(File folder) {
        ArrayList<Integer> list = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) return "";
        for (File f : Arrays.stream(files).filter(Util::isSave).toArray(File[]::new)) {
            int level = getSaveLevel(f);
            if (level != -1 && !list.contains(level)) list.add(level);
        }
        Collections.sort(list);
        if (list.size() == 0) return "";
        String result = "";
        for (int i : list) result += i + ", ";
        return result.substring(0, result.length() - 2);
    }

    private static int classifyLevel(String s) {
        if (s.startsWith("Caves")) return 1;
        if (s.startsWith("City of Vilcabamba")) return 2;
        if (s.startsWith("Lost Valley")) return 3;
        if (s.startsWith("Tomb of Qualopec")) return 4;
        if (s.startsWith("St. Francis' Folly")) return 5;
        if (s.startsWith("Colosseum")) return 6;
        if (s.startsWith("Palace Midas")) return 7;
        if (s.startsWith("The Cistern")) return 8;
        if (s.startsWith("Tomb of Tihocan")) return 9;
        if (s.startsWith("City of Khamoon")) return 10;
        if (s.startsWith("Obelisk of Khamoon")) return 11;
        if (s.startsWith("Sanctuary of the Scion")) return 12;
        if (s.startsWith("Natla's Mines")) return 13;
        if (s.startsWith("Atlantis")) return 14;
        if (s.startsWith("The Great Pyramid")) return 15;
        else return 0;
    }

    public static void deleteSaves(String curDir) {
        Arrays.stream(new File(curDir).listFiles()).filter(f -> isSave(f)).forEach(f -> f.delete());
    }

    public static void copySaves(File folder, String curDir) {
        Arrays.stream(folder.listFiles()).filter(f -> isSave(f)).forEach(f -> {
            try {
                Files.copy(f.toPath(), new File(curDir + "\\" + f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void gameRunningAlert() {
        alert("Game is still running!");
    }

    public static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String loadDescriptionIn(String absolutePath) {
        String retval = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(absolutePath + "\\description.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                retval += line + " ";
            }
            reader.close();
        } catch (IOException e) {
        }
        return retval;
    }

    public static boolean isFile(File file) {
        return file.getName().contains(".");
    }

    public static boolean isDirectory(File file) {
        return !isFile(file);
    }
}
