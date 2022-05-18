package raf.si.bolnica.user.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class FileUtils {

    public static List<String> readUserTitles() {
        Scanner titlesFile = null;
        try {
            titlesFile = new Scanner(new ClassPathResource("Titule.txt").getInputStream()).useDelimiter("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert titlesFile != null;
        return readFromFile(titlesFile);
    }

    private static List<String> readFromFile(Scanner titlesFile) {
        String title;

        List<String> titleList = new ArrayList<>();

        while (titlesFile.hasNext()) {
            title = titlesFile.next();
            titleList.add(title.trim());
        }
        titlesFile.close();

        return titleList;
    }

    public static List<String> readUserProfessions() {
        Scanner professionFile = null;
        try {
            professionFile = new Scanner(new ClassPathResource("Zanimanja.txt").getInputStream()).useDelimiter("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert professionFile != null;
        return readFromFile(professionFile);
    }

}
