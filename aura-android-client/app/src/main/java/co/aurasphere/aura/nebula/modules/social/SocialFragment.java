package co.aurasphere.aura.nebula.modules.social;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.restfb.FacebookClient;
import com.restfb.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import co.aurasphere.aura.nebula.R;
import co.aurasphere.aura.nebula.modules.social.util.AtTokenizer;
import co.aurasphere.aura.nebula.modules.social.util.FacebookUserWrapper;
import co.aurasphere.aura.nebula.modules.social.util.TextTooLongException;

/**
 * Created by Donato on 18/04/2016.
 */
public class SocialFragment extends Fragment {

    // TODO: injection e converti in activity.

    private static final String IMAGE_NAME = "blank.jpg";

    private String textContent;

    private String pageName;

    private String pageId;

    private String pageComment;

    private String shareComment;

    private boolean shareFlag;

    private static ArrayAdapter<FacebookUserWrapper> facebookFriendsAdapter;

    private static List<FacebookUserWrapper> friendList;

    private static Context context;

    private static FacebookController fb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.social_layout, container, false);

        context = getActivity();
        fb = FacebookController.getInstance();

        // Initializes the buttons.
        Button postButton = (Button) rootView.findViewById(R.id.social_form_post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the form is valid generates the image and submits it
                if (validateSocialForm(v.getRootView())) {
                    try {
                        Bitmap image = ImageGenerator.createImageWithText(IMAGE_NAME, textContent, pageName);
                        showPreview(rootView);
                        FacebookClient client = fb.getClientById(pageId);

                        if (shareFlag) {
                            Set<String> tagList = getTagList();
                            fb.publishPhotoAndShare(context, client, getInputStream(image), pageComment, fb.MYSELF, shareComment, tagList);
                        } else {
                            fb.publishPhoto(context, client, getInputStream(image), pageComment);
                        }

                        Toast.makeText(context, "Submitting...", Toast.LENGTH_SHORT).show();
                    } catch (TextTooLongException e) {
                        Toast.makeText(context, "Image text is too long!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button refreshButton = (Button) rootView.findViewById(R.id.social_form_refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreview(rootView);
            }
        });


        // Shows the share comment text input if the box is checked otherwise disables it.
        final View shareCommentInput = rootView.findViewById(R.id.social_form_share_comment_input);
        final View shareCommentLabel = rootView.findViewById(R.id.social_form_share_comment_label);
        CheckBox shareCheckBox = (CheckBox) rootView.findViewById(R.id.social_form_share_check_box);
        shareCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shareCommentInput.setVisibility(View.VISIBLE);
                    shareCommentLabel.setVisibility(View.VISIBLE);
                } else {
                    shareCommentInput.setVisibility(View.GONE);
                    shareCommentLabel.setVisibility(View.GONE);
                }

            }
        });

        // Creates the preview image when the text input for the content loses focus.
        View imageTextInput = rootView.findViewById(R.id.social_form_image_text_input);
        imageTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText editText = (EditText) rootView.findViewById(R.id.social_form_image_text_input);
                    textContent = editText.getText().toString();
                    if (!StringUtils.isBlank(textContent)) {
                        showPreview(rootView);
                    }
                }
            }
        });

        // Initializes the MultiAutoCompleteTextView for friends suggestions.
        MultiAutoCompleteTextView autocompleteView = (MultiAutoCompleteTextView)
                rootView.findViewById(R.id.social_form_share_comment_input);
        initAutoCompleteView();
        autocompleteView.setAdapter(facebookFriendsAdapter);
        autocompleteView.setTokenizer(new AtTokenizer());

        return rootView;
    }

    /**
     * If the form is valid, generates a preview image.
     * @param rootView the rootView.
     */

    private void showPreview(View rootView) {
        if (validateSocialForm(rootView)) {
            ImageView imagePreviewView = (ImageView) rootView.findViewById(R.id.social_form_preview_image_view);
            try {
                Bitmap image = ImageGenerator.createImageWithText(IMAGE_NAME, textContent, pageName);
                imagePreviewView.setImageBitmap(image);
            } catch (TextTooLongException e) {
                Toast.makeText(context, "Image text is too long!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Validates the form.
      * @param rootView the rootView.
     * @return true if the form is valid, false otherwise.
     */

    private boolean validateSocialForm(View rootView) {
        EditText imageText = (EditText) rootView.findViewById(R.id.social_form_image_text_input);
        textContent = imageText.getText().toString();
        if (StringUtils.isBlank(textContent)) {
            Toast.makeText(context, "Image text can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            RadioGroup pageRadioGroup = (RadioGroup) rootView.findViewById(R.id.social_form_page_radio_group);
            switch (pageRadioGroup.getCheckedRadioButtonId()) {
                case R.id.social_form_radio_lesdissia:
                    pageName = context.getResources().getString(R.string.lesdissia_name);
                    pageId = "page0";
                    break;
                default:
                    Toast.makeText(context, "Error while getting the page name!", Toast.LENGTH_SHORT).show();
                    return false;
            }
            MultiAutoCompleteTextView shareCommentInput = (MultiAutoCompleteTextView) rootView.findViewById(R.id.social_form_share_comment_input);
            shareComment = shareCommentInput.getText().toString();
            EditText pageCommentInput = (EditText) rootView.findViewById(R.id.social_form_edit_text_page_comment);
            pageComment = pageCommentInput.getText().toString();
            CheckBox shareCheckBox = (CheckBox) rootView.findViewById(R.id.social_form_share_check_box);
            shareFlag = shareCheckBox.isChecked();
            return true;
        }
    }

    /**
     * Converts a Bitmap into an InputStream.
     * @param bitmap the bitmap to convert.
     * @return the equivalent inputStream.
     */

    private InputStream getInputStream(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /**
     * Refresh the ArrayAdapter for the MultiAutoCompleteTextView used for friend tags.
     * @param updatedFriendList the list to add.
     */

    public static void refreshFriendList(List<FacebookUserWrapper> updatedFriendList){
        initAutoCompleteView();
        friendList = updatedFriendList;
        facebookFriendsAdapter.clear();
        facebookFriendsAdapter.addAll(updatedFriendList);
        facebookFriendsAdapter.notifyDataSetChanged();
    }

    /**
     * Initializes the ArrayAdapter for the MultiAutoCompleteTextView used for friend tags.
     */

    private static void initAutoCompleteView() {
        if (facebookFriendsAdapter == null) {
            friendList = new ArrayList<FacebookUserWrapper>();
            facebookFriendsAdapter = new ArrayAdapter<FacebookUserWrapper>(context,
                    android.R.layout.simple_dropdown_item_1line, friendList);
        }
    }

    /**
     * Returns a set of tags in the share comment input text.
     * @return the tag set.
     */

    private Set<String> getTagList(){
        Set<String> tagList = new HashSet<String>();
        StringTokenizer tokenizer = new StringTokenizer(shareComment);
        String currentToken;
        // Gets the list of tags.
        while(tokenizer.hasMoreTokens()){
            currentToken = tokenizer.nextToken();
            if(currentToken.startsWith("@")){
                // Aura has only her name, so I check it before adding the next token to the current one.
                if(currentToken.equals("@Aura")){
                    tagList.add(currentToken);
                } else {
                    currentToken += " " + tokenizer.nextToken();
                    tagList.add(currentToken);
                }
            }
        }

        // Converts the list of tags into a list of id.
        Set<String> tagIds = new HashSet<String>();
        for(String s: tagList){
            for(FacebookUserWrapper friend : friendList){
                // Removes the '@' while searching.
                if(friend.toString().equals(s.substring(1))){
                    tagIds.add(friend.getUser().getId());
                    // Removes the tag from the text.
                     shareComment = shareComment.replaceAll(s, "");
                    break;
                }
            }
            // Removes any leading, trailing spaces.
            shareComment = shareComment.trim();
        }
        return tagIds;
    }


}
