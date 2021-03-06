package com.testinprod.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.testinprod.popularmovies.models.MovieModel;
import com.testinprod.popularmovies.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by Tim on 8/2/2015.
 */
public class TestUtilities extends AndroidTestCase {

    // Taken from Udacity Sunshine 2 examples
    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    // Adapted from Udacity Sunshine 2 examples
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            switch (valueCursor.getType(idx))
            {
                case Cursor.FIELD_TYPE_FLOAT:
                    float floatValue = Float.parseFloat(entry.getValue().toString());
                    assertEquals("Value '" + valueCursor.getFloat(idx) +
                            "' did not match the expected value '" +
                            floatValue + "'. " + error, floatValue, valueCursor.getFloat(idx));
                    break;
                default:
                    assertEquals("Value '" + valueCursor.getString(idx) +
                            "' did not match the expected value '" +
                            expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));

            }
        }
    }

    public static ContentValues createReviewValues()
    {
        ContentValues values = new ContentValues();
        values.put(MovieContract.ReviewEntry.COLUMN_API_ID, "559238f89251415df80000aa");
        values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, 135397);
        values.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Review Author");
        values.put(MovieContract.ReviewEntry.COLUMN_CONTENT, "Ye on properly handsome returned throwing am no whatever. In without wishing he of picture no exposed talking minutes. Curiosity continual belonging offending so explained it exquisite. Do remember to followed yourself material mr recurred carriage. High drew west we no or at john. About or given on witty event. Or sociable up material bachelor bringing landlord confined. Busy so many in hung easy find well up. So of exquisite my an explained remainder. Dashwood denoting securing be on perceive my laughing so. ");
        values.put(MovieContract.ReviewEntry.COLUMN_URL, "http://udacity.com");
        return values;
    }

    public static ContentValues createVideoValues()
    {
        ContentValues values = new ContentValues();
        values.put(MovieContract.VideoEntry.COLUMN_API_ID, "5576eac192514111e4001b03");
        values.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, 135397);
        values.put(MovieContract.VideoEntry.COLUMN_KEY, "lP-sUUUfamw");
        values.put(MovieContract.VideoEntry.COLUMN_NAME, "Official Trailer 3");
        values.put(MovieContract.VideoEntry.COLUMN_SITE, "YouTube");
        values.put(MovieContract.VideoEntry.COLUMN_SIZE, 720);
        values.put(MovieContract.VideoEntry.COLUMN_TYPE, "Trailer");
        return values;
    }

    public static ContentValues createDiscoveryValues()
    {
        ContentValues discoveryValues = new ContentValues();
        discoveryValues.put(MovieContract.DiscoverEntry.COLUMN_MOVIE_ID, 42);
        discoveryValues.put(MovieContract.DiscoverEntry.COLUMN_ORDER, 1);
        discoveryValues.put(MovieContract.DiscoverEntry.COLUMN_SORTING, "test.desc");
        return discoveryValues;
    }


    public static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_ADULT, 0); // false
        movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "/9eKd1DDDAbrDNXR2he7ZJEu7UkI.jpg");
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 207703);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Kingsman: The Secret Service");
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Kingsman: The Secret Service tells the story of a super-secret spy organization that recruits an unrefined but promising street kid into the agency's ultra-competitive training program just as a global threat emerges from a twisted tech genius.");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, 1423807200);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/oAISjx6DvR2yUn9dxj00vP8OcJJ.jpg");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 17.71923d);
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Kingsman: The Secret Service");
        movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, 0); // false
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 7.7);
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, 1044);

        return movieValues;
    }

    public static MovieModel createMovieModel()
    {
        MovieModel movie = new MovieModel();
        movie.setAdult(false);
        movie.setBackdropPath("/9eKd1DDDAbrDNXR2he7ZJEu7UkI.jpg");
        movie.setId(207703);
        movie.setOriginalLanguage("en");
        movie.setOriginalTitle("Kingsman: The Secret Service");
        movie.setOverview("Kingsman: The Secret Service tells the story of a super-secret spy organization that recruits an unrefined but promising street kid into the agency's ultra-competitive training program just as a global threat emerges from a twisted tech genius.");
        movie.setReleaseDate("2015-02-13");
        movie.setPosterPath("/oAISjx6DvR2yUn9dxj00vP8OcJJ.jpg");
        movie.setPopularity(17.71923d);
        movie.setTitle("Kingsman: The Secret Service");
        movie.setVideo(false);
        movie.setVoteAverage(7.7);
        movie.setVoteCount(1044);

        return movie;
    }

    static long insertExampleMovie(Context context)
    {
        ContentValues values = createMovieValues();
        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();

        long newRow = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

        assertTrue("Error: unable to insert example movie to database", newRow != -1);

        db.close();

        return newRow;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    public static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    public static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
