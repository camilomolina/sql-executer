package cl.bennu.labs4;

import cl.bennu.labs4.comparator.FileComparator;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Camilo on 13-11-2014.
 */
public class ReadTest {

    public static void main(String[] args) throws Exception {
        File file = new File("/home/user_jboss/!SQL/");
        String[] ext = {"sql"};
        Collection files = FileUtils.listFiles(file, ext, true);

        Collections.sort((List) files, new FileComparator());

        Iterator iter = files.iterator();
        while (iter.hasNext()) {
            File sqlFile = (File) iter.next();

            String fileStr = FileUtils.readFileToString(sqlFile);
            System.out.println(fileStr);
        }


        //System.out.println(f.getName());

    }
}
