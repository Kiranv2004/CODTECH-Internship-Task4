package com.codtech;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RecommenderSystem {
    public static void main(String[] args) {
        try {
            // Load data from CSV file
            File dataFile = new File("data/dataset.csv"); // Ensure dataset.csv is placed inside the 'data' folder
            DataModel model = new FileDataModel(dataFile);

            // Compute similarity between users
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // Find nearest user neighborhood
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

            // Create recommender system
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Recommend items for a specific user (user ID: 1)
            List<RecommendedItem> recommendations = recommender.recommend(1, 3);

            // Print recommendations
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("Recommended Item: " + recommendation.getItemID() +
                        " with preference: " + recommendation.getValue());
            }

        } catch (IOException | TasteException e) {
            e.printStackTrace();
        }
    }
}
