package com.codtech;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;

import java.io.File;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        try {
            // Load dataset
            File dataFile = new File("C:\\Users\\KIRAN V\\mahout-recommender\\src\\main\\resources\\data\\dataset.csv");
            System.out.println("Loading data from: " + dataFile.getAbsolutePath());

            DataModel model = new FileDataModel(dataFile, ",");

            // Print all users and their ratings
            System.out.println("\n🔹 Users & Ratings:");
            LongPrimitiveIterator userIterator = model.getUserIDs();
            while (userIterator.hasNext()) {
                long userId = userIterator.next();
                System.out.print("User " + userId + " rated: ");
                model.getPreferencesFromUser(userId).forEach(pref ->
                    System.out.print("Item " + pref.getItemID() + " (" + pref.getValue() + "), ")
                );
                System.out.println();
            }

            // Define User Similarity & Neighborhood for User-Based Recommender
            UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userSimilarity, model);
            Recommender userRecommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);

            // Generate recommendations for each user
            userIterator = model.getUserIDs();
            while (userIterator.hasNext()) {
                long userId = userIterator.next();
                List<RecommendedItem> recommendations = userRecommender.recommend(userId, 3);

                System.out.println("\n🔹 Recommendations for User " + userId + ":");
                if (recommendations.isEmpty()) {
                    System.out.println("❌ No recommendations found! Try increasing dataset size.");
                } else {
                    for (RecommendedItem recommendation : recommendations) {
                        System.out.println("✔ Item ID: " + recommendation.getItemID() + " | Score: " + recommendation.getValue());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
