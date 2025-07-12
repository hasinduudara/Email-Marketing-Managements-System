package lk.ijse.groupproject.emms.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class GoogleAuthService {
    private static final Logger logger = Logger.getLogger(GoogleAuthService.class.getName());
    private static final String APPLICATION_NAME = "Email Marketing Management System";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // Use the standard OAuth2 scopes
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email"
    );

    private static final String CREDENTIALS_FILE_PATH = "/client_secret_975812802035-g6g3ie50rcci9l2vnjpi8li59k5easte.apps.googleusercontent.com.json";


    private static class ManualCodeReceiver implements VerificationCodeReceiver {
        private String code;
        private String redirectUri = "urn:ietf:wg:oauth:2.0:oob";

        @Override
        public String getRedirectUri() {
            return redirectUri;
        }

        @Override
        public String waitForCode() throws IOException {
            return code;
        }

        @Override
        public void stop() throws IOException {
            // Nothing to stop
        }

        public void setCode(String code) {
            this.code = code;
        }
    }


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets from resources
        InputStream in = GoogleAuthService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Google credentials file not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        ManualCodeReceiver receiver = new ManualCodeReceiver();

        String authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(receiver.getRedirectUri())
                .build();

        System.out.println("Opening browser for Google authorization...");
        System.out.println("Authorization URL: " + authorizationUrl);

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(authorizationUrl));
            } else {
                System.out.println("Please open this URL in your browser: " + authorizationUrl);
            }
        } catch (Exception e) {
            System.out.println("Please open this URL in your browser: " + authorizationUrl);
        }

        // Prompt user to enter the authorization code
        String code = javax.swing.JOptionPane.showInputDialog(
                null,
                "Please authorize the application in your browser and paste the authorization code here:",
                "Google Authorization",
                javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (code == null || code.trim().isEmpty()) {
            throw new IOException("Authorization cancelled by user");
        }

        receiver.setCode(code.trim());

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static GoogleUserInfo authenticateUser() throws IOException, GeneralSecurityException {
        logger.info("Starting Google authentication process...");

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        try {
            Credential credential = getCredentials(HTTP_TRANSPORT);

            // Build OAuth2 service
            Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Get user information
            Userinfo userinfo = oauth2.userinfo().get().execute();

            if (userinfo.getEmail() == null || userinfo.getName() == null) {
                throw new IOException("Could not retrieve user information from Google");
            }

            logger.info("Successfully authenticated Google user: " + userinfo.getEmail());
            return new GoogleUserInfo(userinfo.getEmail(), userinfo.getName());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during Google authentication", e);
            throw e;
        }
    }


    public static void clearCredentials() {
        try {
            java.io.File tokensDir = new java.io.File(TOKENS_DIRECTORY_PATH);
            if (tokensDir.exists()) {
                java.io.File[] files = tokensDir.listFiles();
                if (files != null) {
                    for (java.io.File file : files) {
                        if (file.delete()) {
                            System.out.println("Deleted token file: " + file.getName());
                        }
                    }
                }
                System.out.println("Cleared stored credentials");
            }
        } catch (Exception e) {
            System.err.println("Failed to clear credentials: " + e.getMessage());
        }
    }


    public static class GoogleUserInfo {
        private final String email;
        private final String name;

        public GoogleUserInfo(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "GoogleUserInfo{email='" + email + "', name='" + name + "'}";
        }
    }
}