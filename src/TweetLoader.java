/*
 * Decompiled with CFR 0_122.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TweetLoader {
    public static List<Tweet> loadData(String filename) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String id = "";
            String text = "";
            String uploader = "";
            Long uplodadTime = (long) 0;
            while ((line = br.readLine()) != null) {
                Tweet new_tweet = new Tweet(line);
                tweets.add(new_tweet);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tweets;
    }
}

