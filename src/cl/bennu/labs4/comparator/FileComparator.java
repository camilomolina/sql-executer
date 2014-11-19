package cl.bennu.labs4.comparator;

import java.io.File;
import java.util.Comparator;

/**
 * Created by _Camilo on 20-06-2014.
 */
public class FileComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        if (o1 instanceof File && o2 instanceof File) {
            File file1 = (File) o1;
            File file2 = (File) o2;

            return file1.getName().compareTo(file2.getName());
        }

        return 0;
    }
}
