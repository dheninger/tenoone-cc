package com.google.devrel.training.conference.spi;

import static com.google.devrel.training.conference.service.OfyService.ofy;
import static com.google.devrel.training.conference.service.OfyService.factory;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.devrel.training.conference.Constants;
import com.google.devrel.training.conference.domain.Profile;
import com.google.devrel.training.conference.form.ProfileForm;
import com.google.devrel.training.conference.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.Key;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Defines conference APIs.
 */
@SuppressWarnings("JavadocReference")
@Api(name = "conference", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
        Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Conference Central Backend application.")
public class ConferenceApi {
    private static final Logger LOG = Logger.getLogger(ConferenceApi.class.getName());

    /*
     * Get the display name from the user's email. For example, if the email is
     * lemoncake@example.com, then the display name becomes "lemoncake."
     */
    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }

    /**
     * Work around method to cover off null userId for Android Clients.
     * @param user A User object injected by the endpoint.
     * @return the App Engine userId for the user.
     */
    private static String getUserId(User user){
        String userId = user.getUserId();
        return userId;
    }

    /**
     * Creates or updates a Profile object associated with the given user
     * object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @param profileForm
     *            A ProfileForm object sent from the client form.
     * @return Profile object just created.
     * @throws UnauthorizedException
     *             when the User object is null.
     */

    // Declare this method as a method available externally through Endpoints
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
    // The request that invokes this method should provide data that
    // conforms to the fields defined in ProfileForm


    public Profile saveProfile(final User user, final ProfileForm profileForm) throws UnauthorizedException {

        // If the user is not logged in, throw an UnauthorizedException
        if(user == null){
            throw new UnauthorizedException("Authroization required!");
        }
        String displayName = profileForm.getDisplayName();
        TeeShirtSize teeShirtSize = profileForm.getTeeShirtSize();

        // Create a new Profile entity from the
        // userId, displayName, mainEmail and teeShirtSize
       //Profile profile = new Profile(userId, displayName, mainEmail, teeShirtSize);

        // TODO 3 (In Lesson 3)
        Profile profile = ofy().load().key(Key.create(Profile.class, getUserId(user))).now();
        if(profile == null){
            //we don't have an existing so create new.
            LOG.info("New Profile being built and saved.");
            if(displayName==null){
                displayName = extractDefaultDisplayNameFromEmail(user.getEmail());
            }
            if(teeShirtSize == null){
                teeShirtSize = TeeShirtSize.NOT_SPECIFIED;
            }
            profile = new Profile(getUserId(user), displayName,user.getEmail(),teeShirtSize);

        }else{
            LOG.info("Found Profile: "+profile.getUserId());
            LOG.info(profile.getDisplayName());
            LOG.info(profile.getMainEmail());
            LOG.info(profile.getTeeShirtSize().toString());
            profile.update(displayName, teeShirtSize);
        }

        ofy().save().entity(profile).now();
        // Return the profile
        return profile;
    }

    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException
     *             when the User object is null.
     */
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }


        String userId = user.getUserId();
        Key key = Key.create(Profile.class, userId);
        Profile profile = (Profile) ofy().load().key(key).now();
        return profile;
    }
}
