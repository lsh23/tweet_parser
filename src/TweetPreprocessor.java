/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.apache.lucene.analysis.TokenStream
 *  org.apache.lucene.analysis.standard.StandardAnalyzer
 *  org.apache.lucene.analysis.tokenattributes.CharTermAttribute
 *  org.apache.lucene.util.Attribute
 *  org.apache.lucene.util.Version
 */

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TweetPreprocessor {
    private static StandardAnalyzer analyzer;
    private static final Pattern urlPattern;
    private static final Pattern hashtagPattern;
    private static final Pattern usermentionPattern;

    public static List<String> Tokenize(Tweet tweet, boolean include_user_mentions, boolean include_hashtags) {
        if (analyzer == null) {
            try {
                analyzer = new StandardAnalyzer((Reader)new StringReader(TweetStopWords.stopWords));
            }
            catch (IOException ex) {
                Logger.getLogger(TweetPreprocessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ArrayList<String> tokensList = new ArrayList<String>();
        try {
            String tmp_content = tweet.getText();
    
            if (tmp_content == null)
                tmp_content = "";
            
            //System.out.println(tmp_content);
            
            //URL 제거
            List<String> urls = TweetPreprocessor.getURLStrings(tweet.getText());
            for (String url : urls) 
                tmp_content = tmp_content.replace(url, "");

            
            if (!include_hashtags) {
                List<String> hashtags = TweetPreprocessor.getHashtags(tmp_content);
                for (String hashtag : hashtags) {
                    tmp_content = tmp_content.replace(hashtag, "");
                }
            }

            if (!include_user_mentions) {
                List<String> usermentions = TweetPreprocessor.getUsermentions(tmp_content);
                for (String usermention : usermentions) {
                    tmp_content = tmp_content.replace(usermention, "");
                }
            }
            if (tmp_content == null) {
                tmp_content = "";
            }
            TokenStream stream = analyzer.tokenStream(null, (Reader)new StringReader(tmp_content));
            stream.reset();
            while (stream.incrementToken()) {
                tokensList.add(((CharTermAttribute)stream.getAttribute(CharTermAttribute.class)).toString());
            }
            stream.close();
            
            
            //System.out.println(tokensList);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tokensList;
    }

    public static List<String> TokenizeForSentimentAnalysis(Tweet tweet) {
        return null;
    }

    public static List<URL> ExtractURLs(Tweet tweet) {
        ArrayList<URL> urls = new ArrayList<URL>();
        List<String> url_strings = TweetPreprocessor.getURLStrings(tweet.getText());
        for (int i = 0; i < url_strings.size(); ++i) {
            try {
                urls.add(new URL(url_strings.get(i)));
                continue;
            }
            catch (MalformedURLException ex) {
                // empty catch block
            }
        }
        return urls;
    }

    public static List<String> getURLStrings(String originalString) {
        ArrayList<String> urlsSet = new ArrayList<String>();
        Matcher matcher = urlPattern.matcher(originalString);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            String tmpUrl = originalString.substring(matchStart, matchEnd);
            urlsSet.add(tmpUrl);
            originalString = originalString.replace(tmpUrl, "");
            matcher = urlPattern.matcher(originalString);
        }
        return urlsSet;
    }

    public static List<String> getHashtags(String originalString) {
        ArrayList<String> hashtagSet = new ArrayList<String>();
        Matcher matcher = hashtagPattern.matcher(originalString);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            String tmpHashtag = originalString.substring(matchStart, matchEnd);
            hashtagSet.add(tmpHashtag);
            originalString = originalString.replace(tmpHashtag, "");
            matcher = hashtagPattern.matcher(originalString);
        }
        return hashtagSet;
    }

    public static List<String> getUsermentions(String originalString) {
        ArrayList<String> usermentionsSet = new ArrayList<String>();
        Matcher matcher = usermentionPattern.matcher(originalString);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            String tmpUsermention = originalString.substring(matchStart, matchEnd);
            usermentionsSet.add(tmpUsermention);
            originalString = originalString.replace(tmpUsermention, "");
            matcher = usermentionPattern.matcher(originalString);
        }
        return usermentionsSet;
    }

    static {
        urlPattern = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)", 42);
        hashtagPattern = Pattern.compile("(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)");
        usermentionPattern = Pattern.compile("(?:^|\\s|[\\p{Punct}&&[^/]])(@[\\p{L}0-9-_]+)");
    }
}

