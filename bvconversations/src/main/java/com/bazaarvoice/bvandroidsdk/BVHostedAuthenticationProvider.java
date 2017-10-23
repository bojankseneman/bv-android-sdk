package com.bazaarvoice.bvandroidsdk;

/**
 * <p>
 *  Authentication that clients configured for
 *  <a href="http://knowledge.bazaarvoice.com/wp-content/conversations/en_US/Learn/user_authentication.html">Hosted Authentication</a>
 *  should use. You can check which authentication configuration you have by following
 *  <a href="http://knowledge.bazaarvoice.com/wp-content/conversations/en_US/Learn/user_authentication.html#determine-your-authentication-configuration">these steps</a>.
 * </p>
 */
public class BVHostedAuthenticationProvider implements AuthenticationProvider {
  private final String userEmailAddress;
  private final String callbackUrl;
  private final String uas;

  /**
   * <h2>When to use</h2>
   * <p>
   *  Use this constructor if you have not managed to retrieve the User Authentication String
   *  (UAS) for the user. This method will send the user an email to confirm the
   *  {@link ConversationsSubmissionRequest} that this was attached to. The email will contain
   *  a link for them to tap to confirm, and it will be composed of,
   * <ol>
   *   <li>The {@code callbackUrl} you provided here</li>
   *   <li>A {@code bv_authtoken} query parameter generated by Bazaarvoice. You will use this to fetch a UAS.</li>
   * </ol>
   *  <br/>
   *  e.g. <code>http://www.example.com/your/authentication-service?bv_authtoken=a7a4278ff33887d352fcdef30edd143f487dc881</code>
   * </p>
   * <h2>How to retrieve a {@code bv_authtoken}</h2>
   * <p>
   *   You have multiple options for retrieving this {@code bv_authtoken},
   *   <ol>
   *   <li>You may intercept the URL when it is tapped by the user on their phone by registering
   *    an {@link android.content.IntentFilter} in your {@code AndroidManifest.xml}. This will not
   *    work if the user taps/clicks on the link on a different device.</li>
   *   <li>You may setup your backend to retrieve the callback when the user opens the link in a
   *   browser, and then send a push notification to your app for this specific user, with the
   *   {@code bv_authtoken}.</li>
   *   </ol>
   * </p>
   * <h2>What if I do not send a UAS?</h2>
   * <p>
   *   All content will still be submitted and moderated, and will show up in the display requests.
   * </p>
   * <p>
   *   The consequences for not retrieving the UAS, and using the other constructor, is that the
   *   user will continue to be sent an email to confirm for each submission.
   * </p>
   * <p>
   *   The consequences for a user not confirming their submission is that their user profile will
   *   not be merged with the content they submitted. If at any point in the future, your app is able
   *   to send a UAS then all of those cumulative profiles will be merged.
   * </p>
   *
   * @param userEmailAddress The users email address which Bazaarvoice will send an email to, asking them to verify the submission. This may need to be whitelisted by Bazaarvoice.
   * @param callbackUrl The link that will be posted in the email, that a user taps to confirm their submission. This must be whitelisted by Bazaarvoice.
   */
  public BVHostedAuthenticationProvider(String userEmailAddress, String callbackUrl) {
    this.userEmailAddress = userEmailAddress;
    this.callbackUrl = callbackUrl;
    this.uas = "";
  }

  /**
   * Use this constructor if you managed to retrieve the UAS for the user.
   *
   * @param uas The User Authentication String parsed from the query parameters Bazaarvoice appended to your callbackUrl.
   */
  public BVHostedAuthenticationProvider(String uas) {
    this.userEmailAddress = "";
    this.callbackUrl = "";
    this.uas = uas;
  }

  public String getUserEmailAddress() {
    return userEmailAddress;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public String getUas() {
    return uas;
  }
}