/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.mongodb.DBObject
 *  com.mongodb.util.JSON
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Tweet {
    String text;
    String id;
    String uploader;
    Long uploadTime;    

    public Tweet(String text, String id, String uploader, Long uploadTime) {
        this.text = text;
        this.id = id;
        this.uploader = uploader;
        this.uploadTime = uploadTime;
    }

    public Tweet(String jsonSource) {
        DBObject dbObject = (DBObject)JSON.parse((String)jsonSource);
        this.id = (String)dbObject.get("id_str");
        this.text = (String)dbObject.get("text");
        DBObject tmp_obj = (DBObject)dbObject.get("user");
        this.uploader = (String)tmp_obj.get("screen_name");
        this.text = (String)dbObject.get("text");
        this.uploadTime = Tweet.parseTwitterDate((String)dbObject.get("created_at"));
    }

    public String getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public Long getUploadTime() {
        return this.uploadTime;
    }

    public String getUploader() {
        return this.uploader;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public static Long parseTwitterDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        Long created = null;
        
        try {
			Date date = dateFormat.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try {
            created = dateFormat.parse(dateStr).getTime();
            return created;
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<String> getTerms() {
        List<String> tokens = new ArrayList<String>();
        tokens = TweetPreprocessor.Tokenize(this, true, true);
        return tokens;
    }
}

