package edu.upenn.cis350.workingdogapp;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Export {
    // Exports the database of active and archived dogs to a .txt file
    public void export(List<Database.DogEntry> dogs, List<Database.DogEntry> arch) {
        File root = new File(Environment.getExternalStorageDirectory(), "Download");
        if (!root.exists()) {
            root.mkdirs(); // this will create folder.
        }
        File filepath = new File(root, "dogs.txt");  // file path to save
        FileWriter writer = null;
        try {
            writer = new FileWriter(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.append("ACTIVE DOGS:\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Database.DogEntry d : dogs) {
            try {
                writer.append("Name: " + d.name + "\n\t" +
                        "Age: " + d.age + "\n\t" +
                        "Breed: " + d.breed + "\n\t" +
                        "Foster: " + d.fosterParent + "\n\t" +
                        "Feeder type: " + d.feederType + "\n\t" +
                        "Food type: " + d.foodType + "\n\t" +
                        "Feeding amount: " + d.feedingAmount + "\n\t" +
                        "Treat restrictions: " + d.treatRestrictions + "\n\t" +
                        "Weight (in lbs): " + d.weightLb + "\n\t" +
                        "Height (in inches): " + d.heightInches + "\n\t" +
                        "Body score: " + d.bodyScore + "\n\t" +
                        "Allergies: " + d.allergies + "\n\t" +
                        "Fish oil: " + d.fishOil + "\n\t" +
                        "Comments: \n" + d.comments + "\n\n"
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.append("ARCHIVED DOGS:\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Database.DogEntry d : arch) {
            try {
                writer.append("Name: " + d.name + "\n\t" +
                        "Age: " + d.age + "\n\t" +
                        "Breed: " + d.breed + "\n\t" +
                        "Foster: " + d.fosterParent + "\n\t" +
                        "Feeder type: " + d.feederType + "\n\t" +
                        "Food type: " + d.foodType + "\n\t" +
                        "Feeding amount: " + d.feedingAmount + "\n\t" +
                        "Treat restrictions: " + d.treatRestrictions + "\n\t" +
                        "Weight (in lbs): " + d.weightLb + "\n\t" +
                        "Height (in inches): " + d.heightInches + "\n\t" +
                        "Body score: " + d.bodyScore + "\n\t" +
                        "Allergies: " + d.allergies + "\n\t" +
                        "Fish oil: " + d.fishOil + "\n\t" +
                        "Comments: \n" + d.comments + "\n\n"
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
