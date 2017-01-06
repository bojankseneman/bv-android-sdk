package com.bazaarvoice.bvsdkdemoandroid.conversations;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.AnswerSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.AnswerSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.QuestionSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.QuestionSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.ReviewSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.ReviewSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

///import static com.iovation.mobile.android.DevicePrint.getBlackbox;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link DemoConversationsAPIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoConversationsAPIFragment extends Fragment {

    private ProgressDialog progress;
    private static final String TEST_PRODUCT_ID = "test1"; // This product ID must be in the catalog for the provided Conversations API key.

    private List<String> TEST_BULK_PRODUCT_IDS = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6");

    private BVConversationsClient client = new BVConversationsClient();

    public DemoConversationsAPIFragment() {
        // Required empty public constructor
    }

    public static DemoConversationsAPIFragment newInstance() {
        DemoConversationsAPIFragment fragment = new DemoConversationsAPIFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_conversations_demo, container, false);
        progress = new ProgressDialog(getContext());

        setUpDisplayActionButtons(view);
        setUpUGCSubmisionActionButtons(view);

        return view;
    }

    private void setUpDisplayActionButtons(View view){

        // Read reviews for product id
        Button readReviewsButton = (Button) view.findViewById(R.id.displayReviewsBtn);
        readReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToReviewsActivity(TEST_PRODUCT_ID);
                }
            }
        });

        // Read questions for product id
        Button readQuestionsButton = (Button) view.findViewById(R.id.displayQABtn);
        readQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToQuestionsActivity(TEST_PRODUCT_ID);
                }
            }
        });

        // Display Product Stats
        Button readProductStats = (Button) view.findViewById(R.id.displayProductPageBtn);
        readProductStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToProductStatsActivity(TEST_PRODUCT_ID);
                }
            }
        });

        // Display Bulk Ratings
        Button buldRatings = (Button) view.findViewById(R.id.displayInlineRatingsPageBtn);
        buldRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToBulkRatingsActivity(new ArrayList<String>(TEST_BULK_PRODUCT_IDS));
                }
            }
        });

    }

    private void setUpUGCSubmisionActionButtons(View view){

        //////////////////////////////
        // Submit Example Buttons
        //////////////////////////////
        this.setupPhotoSubmitButton(view);
        this.setupQuestionSubmitButton(view);
        this.setupAnswerSubmitButton(view);
        this.setupFeedbackSubmitButton(view);

    }

    private void setupPhotoSubmitButton(View view){

        // Submit Review w/ Photo
        Button feedBtn = (Button) view.findViewById(R.id.submitReviewWithPhoto);
        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {

                    // For clients non-EU clients, iovation SDK is required!
//                    String blackbox = getBlackbox(getContext().getApplicationContext());

                    progress.setTitle("Submitting Review...");
                    progress.show();

                    File localImageFile = null;

                    try {
                        InputStream stream = getContext().getAssets().open("puppy.jpg");
                        localImageFile = createFileFromInputStream(stream);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ReviewSubmissionRequest submission = new ReviewSubmissionRequest.Builder(Action.Preview, TEST_PRODUCT_ID)
//                            .fingerPrint(blackbox)  // uncomment me when using iovation SDK
                            .userNickname("shazbat")
                            .userEmail("foo@bar.com")
                            .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
                            .rating(5)
                            .title("Review title")
                            .reviewText("This is the review text the user adds about how great the product is!")
                            .sendEmailAlertWhenCommented(true)
                            .sendEmailAlertWhenPublished(true)
                            .agreedToTermsAndConditions(true)
                            .addPhoto(localImageFile, "What a cute pupper!")
                            .build();

                    client.prepareCall(submission).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {

                        @Override
                        public void onSuccess(ReviewSubmissionResponse response) {
                            progress.dismiss();
                            showDialogWithMessage("Successful review submission.");
                        }

                        @Override
                        public void onFailure(BazaarException exception) {
                            progress.dismiss();
                            showDialogWithMessage(exception.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void setupQuestionSubmitButton(View view){
        // Submit Question
        Button questionBtn = (Button) view.findViewById(R.id.submitQuestion);
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {

                    // For clients non-EU clients, iovation SDK is required!
                    //String blackbox = getBlackbox(getContext().getApplicationContext());

                    progress.setTitle("Submitting Question...");
                    progress.show();

                    QuestionSubmissionRequest submission = new QuestionSubmissionRequest.Builder(Action.Preview, TEST_PRODUCT_ID)
                            .userNickname("shazbat")
                            .userEmail("foo@bar.com")
                            .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
                            .questionDetails("Question details...")
                            .questionSummary("Question summary/title...")
                            .sendEmailAlertWhenPublished(true)
                            .agreedToTermsAndConditions(true)
                            .build();

                    client.prepareCall(submission).loadAsync(new ConversationsCallback<QuestionSubmissionResponse>() {

                        @Override
                        public void onSuccess(QuestionSubmissionResponse response) {
                            progress.dismiss();
                            showDialogWithMessage("Successful question submission.");
                        }

                        @Override
                        public void onFailure(BazaarException exception) {
                            progress.dismiss();
                            showDialogWithMessage(exception.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void setupAnswerSubmitButton(View view){
        // Submit Answer
        Button answerButton = (Button) view.findViewById(R.id.submitAnswer);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {

                    // For clients non-EU clients, iovation SDK is required!
                    //String blackbox = getBlackbox(getContext().getApplicationContext());

                    progress.setTitle("Submitting Answer...");
                    progress.show();

                    AnswerSubmissionRequest submission = new AnswerSubmissionRequest.Builder(Action.Preview, "14679", "User answer text goes here....")
                            //.fingerPrint(blackbox)  // uncomment me when using iovation SDK
                            .userNickname("shazbat")
                            .userEmail("foo@bar.com")
                            .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
                            .sendEmailAlertWhenPublished(true)
                            .agreedToTermsAndConditions(true)
                            .build();

                    client.prepareCall(submission).loadAsync(new ConversationsCallback<AnswerSubmissionResponse>() {

                        @Override
                        public void onSuccess(AnswerSubmissionResponse response) {
                            progress.dismiss();
                            showDialogWithMessage("Successful answer submission.");
                        }

                        @Override
                        public void onFailure(BazaarException exception) {
                            progress.dismiss();
                            showDialogWithMessage(exception.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void setupFeedbackSubmitButton(View view){
        // Submit Feedback
        Button feedbackButton = (Button) view.findViewById(R.id.submitFeedback);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {

                    progress.setTitle("Submitting Feedback...");
                    progress.show();

                    String theReviewId = "83725"; // E.g. the review/question/answer "Id"

                    FeedbackSubmissionRequest submission = new FeedbackSubmissionRequest.Builder(theReviewId)
                            .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicates -- FOR TESTING ONLY!!!
                            .feedbackType(FeedbackType.HELPFULNESS)
                            .feedbackContentType(FeedbackContentType.REVIEW)
                            .feedbackVote(FeedbackVoteType.POSITIVE)
                            .build();

                    client.prepareCall(submission).loadAsync(new ConversationsCallback<FeedbackSubmissionResponse>() {

                        @Override
                        public void onSuccess(FeedbackSubmissionResponse response) {
                            progress.dismiss();
                            showDialogWithMessage("Successful feedback submission.");
                        }

                        @Override
                        public void onFailure(BazaarException exception) {
                            progress.dismiss();
                            showDialogWithMessage(exception.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {

        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();

        String conversationsKey = currentConfig.apiKeyConversations;
        String displayName = currentConfig.displayName;

        if (!DemoConstants.isSet(conversationsKey)) {
            String errorMessage = String.format(
                    getString(R.string.view_demo_error_message),
                    displayName, getString(R.string.demo_conversations)
            );
            showDialogWithMessage(errorMessage);
            return false;
        }

        return true;

    }

    private void showDialogWithMessage(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
    }

    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = File.createTempFile("tmp", "png");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;

        }catch (IOException e) {
            //Logging exception
            e.printStackTrace();
        }

        return null;
    }


}
