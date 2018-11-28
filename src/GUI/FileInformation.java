package GUI;
import value.Value;

import java.util.ArrayList;

public class FileInformation {
    public String filePath;
    public String[] columnsNames;
    public boolean isFirstLineWithNames;
    public ArrayList<Class<? extends Value>> columnsTypes = new ArrayList<>();
}
