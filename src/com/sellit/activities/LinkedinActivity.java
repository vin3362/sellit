package com.sellit.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.enumeration.SearchParameter;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.People;
import com.google.code.linkedinapi.schema.Person;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class LinkedinActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    static final String CONSUMER_KEY = "vlju7h7w97rp";
    static final String CONSUMER_SECRET = "hxicYkIm9p536EDm";

    static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
    static final String OAUTH_CALLBACK_HOST = "self";
    static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
    static final String OAUTH_QUERY_TOKEN = "oauth_token";
    static final String OAUTH_QUERY_VERIFIER = "oauth_verifier";
    static final String OAUTH_QUERY_PROBLEM = "oauth_problem";

    final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(CONSUMER_KEY,
                    CONSUMER_SECRET);
    final LinkedInApiClientFactory factory = LinkedInApiClientFactory
            .newInstance(CONSUMER_KEY, CONSUMER_SECRET);

    static final String OAUTH_PREF = "LIKEDIN_OAUTH";
    static final String PREF_TOKEN = "token";
    static final String PREF_TOKENSECRET = "tokenSecret";
    static final String PREF_REQTOKENSECRET = "requestTokenSecret";

    TextView tv = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        setContentView(tv);
        final SharedPreferences pref = getSharedPreferences(OAUTH_PREF,
                MODE_PRIVATE);
        final String token = pref.getString(PREF_TOKEN, null);
        final String tokenSecret = pref.getString(PREF_TOKENSECRET, null);
        if (token == null || tokenSecret == null) {
            startAuthenticate();
        } else {
            showCurrentUser(new LinkedInAccessToken(token, tokenSecret));
        }
    }

    void startAuthenticate() {
        final LinkedInRequestToken liToken = oAuthService
                .getOAuthRequestToken(OAUTH_CALLBACK_URL);
        final String uri = liToken.getAuthorizationUrl();
        getSharedPreferences(OAUTH_PREF, MODE_PRIVATE).edit()
                .putString(PREF_REQTOKENSECRET, liToken.getTokenSecret())
                .commit();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(i);
    }

    void finishAuthenticate(final Uri uri) {
        Properties systemSettings = System.getProperties();
        systemSettings.put("http.proxyHost", "proxy");
        systemSettings.put("http.proxyPort", "8080");
        System.setProperties(systemSettings);
        if (uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
            final String problem = uri.getQueryParameter(OAUTH_QUERY_PROBLEM);
            if (problem == null) {
                final SharedPreferences pref = getSharedPreferences(OAUTH_PREF,
                        MODE_PRIVATE);
                final LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(new LinkedInRequestToken(uri
                        .getQueryParameter(OAUTH_QUERY_TOKEN), pref.getString(PREF_REQTOKENSECRET, null)),
                        uri.getQueryParameter(OAUTH_QUERY_VERIFIER));
                pref.edit()
                    .putString(PREF_TOKEN, accessToken.getToken())
                    .putString(PREF_TOKENSECRET,
                            accessToken.getTokenSecret())
                    .remove(PREF_REQTOKENSECRET).commit();
                showCurrentUser(accessToken);
            } else {
                Toast.makeText(this,
                        "Appliaction down due OAuth problem: " + problem,
                        Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    void clearTokens() {
        getSharedPreferences(OAUTH_PREF, MODE_PRIVATE).edit()
                .remove(PREF_TOKEN).remove(PREF_TOKENSECRET)
                .remove(PREF_REQTOKENSECRET).commit();
    }

    void showCurrentUser(final LinkedInAccessToken accessToken) {
        final LinkedInApiClient client = factory
                .createLinkedInApiClient(accessToken);
        try {

            final Person p = client.getProfileForCurrentUser();
            Map<SearchParameter, String> searchParameters = new EnumMap<SearchParameter,
                    String>(SearchParameter.class);
            Set<ProfileField> set = ProfileField.valuesForConnections();
            searchParameters.put(SearchParameter.FIRST_NAME,"Vinodh");
            People people = client.searchPeople(searchParameters,set);
            System.out.println("Total search result:" + people.getCount());
            for (Person person : people.getPersonList()) {
                System.out.println(person.getId() + ":" + person.getFirstName() + " " + person.getLastName() + ":" +
                                   person.getHeadline());
            }
            // /////////////////////////////////////////////////////////
            // here you can do client API calls ...
            // client.postComment(arg0, arg1);
            // client.updateCurrentStatus(arg0);
            // or any other API call (this sample only check for current user
            // and shows it in TextView)
            // /////////////////////////////////////////////////////////
            tv.setText(p.getLastName() + ", " + p.getFirstName());
        } catch (LinkedInApiClientException ex) {
            clearTokens();
            Toast.makeText(
                    this,
                    "Application down due LinkedInApiClientException: "
                    + ex.getMessage()
                    + " Authokens cleared - try run application again.",
                    Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        finishAuthenticate(intent.getData());
    }

    protected void onResume() {
        super.onResume();
        if (this.getIntent() != null) {
            finishAuthenticate(this.getIntent().getData());
        }
    }

}
