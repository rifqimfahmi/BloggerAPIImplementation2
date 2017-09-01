package renotekno.com.bloggerapiimplementation2.Loader;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import renotekno.com.bloggerapiimplementation2.Model.Data;
import renotekno.com.bloggerapiimplementation2.R;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

/**
 * Created by zcabez on 30/08/2017.
 */

public class PostLoader extends AsyncTaskLoader<List<View>> {

    private String postBodyHTMLContent;

    // array to hold post that has been splitted, general structure based on delimiter <br>
    private List<String> splittedPostComponent = null;

    // Activity context for creating view such as TextView or ImageView
    private Context postActivityContext;
    
    private List<View> viewsToBeInflated = new ArrayList<>();

    public PostLoader(Context context, String postBodyHTMLContent) {
        super(context);
        postActivityContext = context;
        this.postBodyHTMLContent = postBodyHTMLContent;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<View> loadInBackground() {
        // clear all the previously loaded post
        emptyPreviouslyLoadedData();

        // preserve important HTML data
        buildComponent();

        // generate view for each splitted post component
        producueViews();

        return viewsToBeInflated;
    }

    private void emptyPreviouslyLoadedData() {
        Data.imagesSrc.clear();
        Data.imageLink.clear();
    }

    private void producueViews() {
        // create view for each splitted component if not null and empty
        if (splittedPostComponent != null && splittedPostComponent.size() > 0) {
            // loop through each component
            for (int i = 0; i < splittedPostComponent.size(); i++) {
                // create view if collection did not equal to empty string ""
                if (!splittedPostComponent.get(i).trim().equals("")) {
                    generateView(splittedPostComponent.get(i));
                }
            }
        }
    }

    private void generateView(String component) {
        // initiate matcher for component
        Matcher matcher = null;

        // Match <h2> tag and separate it if any
        matcher = Pattern.compile(Data.H2_REGEX_MATCHER).matcher(component);
        if (matcher.find()) {
            // h2 has been found
            h2Detected(component, matcher);
            return;
        }

        // Match <img> tag and separate it if any
        matcher = Pattern.compile(Data.IMG_REGEX_MATCHER).matcher(component);
        if (matcher.find()) {
            // img thas been found
            imgDetected(matcher);
            return;
        }

        // inflate just normal text
        createParagraphView(component);
    }


    /*
     *  creating view function start
     */
    private void createImageView() {
        /**
         * We use @{@link AdjustableImageView} so that we can have behaviour like setAdjustViewBounds(true)
         * for Android below API 18
         */
        viewsToBeInflated.add(new AdjustableImageView(getActivityContext()));
    }

    @NonNull
    private void createH2View(String h2Val) {
        TextView h2View;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            h2View = new TextView(getActivityContext(), null, 0, R.style.h2Style);
        } else {
            h2View = new TextView(getActivityContext());
            h2View.setPadding(0, 12, 0, 22);
            h2View.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            h2View.setTypeface(Typeface.DEFAULT_BOLD);
            // this setTextAppearance is for API below 23 or below Lollipop
            h2View.setTextAppearance(getActivityContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Large);
        }

        // set the h2 text TextView
        h2View.setText(h2Val);
        viewsToBeInflated.add(h2View);
    }

    @NonNull
    private void createParagraphView(String bodyvalue) {
        TextView textView;

        // check if the paragraph is not empty
        if (!Jsoup.clean(bodyvalue, Whitelist.none()).trim().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView = new TextView(getActivityContext(), null, 0, R.style.bodyText);
            } else {
                textView = new TextView(getActivityContext());
                textView.setPadding(0, 0, 0, 28);
                textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextAppearance(getActivityContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Body1);
                textView.setLineSpacing(24f, 1f);
            }

            // set that anchor link can be clicked
            textView.setLinksClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(bodyvalue, FROM_HTML_MODE_COMPACT));
            } else {
                textView.setText(Html.fromHtml(bodyvalue));
            }

            viewsToBeInflated.add(textView);
        }
    }
    /*
     *  creating view function end
     */


    private void imgDetected(Matcher matcher) {
        String component;
        matcher.reset();

        // store src value if <img> exist
        while (matcher.find()) {
            Data.imagesSrc.add(matcher.group(1));
        }

        // separate if the paragraph contain img
        // replace <img> with -+-img-+- to indicate image position in the text
        component = matcher.replaceAll("-+-img-+-");

        //split the delimiter to structure image position
        String[] imageStructure = component.split("-\\+-");

        // start looping the structured text
        int imageFoundIndex = 0;
        for (String structure : imageStructure) {
            // continue if the current index is not empty string ""
            if (!structure.trim().equals("")) {
                // create ImageView if current index value equeal to "img"
                if (structure.trim().equals("img")) {
                    createImageView();
                    imageFoundIndex++;
                } else {
                    // else create textView for the text
                    generateView(structure);
                }
            }
        }
    }

    private void h2Detected(String component, Matcher matcher) {
        matcher.reset();
        List<String> h2s = new ArrayList<>();

        while (matcher.find()) {
            // add h2 value if any
            h2s.add(Jsoup.parse(matcher.group(1)).text());
        }

        // replace h2 value with -+-h2-+- to split the string to find h2 position
        component = matcher.replaceAll("-+-h2-+-");
        // split the paragraph with -+- as the delimiter
        String[] splittedH2 = component.split("-\\+-");

        int h2Found = 0;
        // loop through the splitted array
        for (String h2Place : splittedH2) {
            // if the value is not "" the create view either for h2 or paragraph
            if (!h2Place.trim().equals("")) {
                // if value is h2 create textview for h2
                if (h2Place.equals("h2")) {
                    // Create h2 view
                    createH2View(h2s.get(h2Found).trim());
                    // increase h2 position index after adding it to the view
                    h2Found++;

                    // add the h2 view to the viewgroup
//                    linearLayoutViewGroup.addView(h2View);

                } else {
                    // if not h2 create textview for paragraph
                    generateView(h2Place);
                }
            }
        }
    }

    @NonNull
    private void splitHTMLIntoArrays() {
        // split text into pharagraph as <br> is the delimiter
        String[] splitted = postBodyHTMLContent.split(Data.BR_MATCHER);

        // create List of array that can be modified using linkedList
        splittedPostComponent = new LinkedList<String>(Arrays.asList(splitted));
    }

    private void buildComponent() {
        // remove &nbsp; non-breaking space
        postBodyHTMLContent = postBodyHTMLContent.replaceAll(Data.NON_BREAKING_SPACES, " ");

        // preserve necessary tag for layout mapping
        preserveNecessaryTag();

        // if user use <p> instead of <br> we have to change the closing </p> with br and
        // remove the <p> tag
        cleanPharagraph();

        // remove <br /> tag inside <li> element
        removeBrInsidelist();

        // structure li's element remove opening <li> and replace closing </li> with <br>
        structureListElement();

        // Remove <img> tag that wrapped in <a> tag
        removeAnchorWrappedImage();

        // Split cleaned HTML into arrays with <br> as delimiter
        splitHTMLIntoArrays();
    }

    private void structureListElement() {
        // aa "- " to every element <li> at the start of the element
        postBodyHTMLContent = postBodyHTMLContent.replaceAll(Data.LI_OPENING_REGEX_MATCHER, "- ");

        // replace closing tag </li> with </br> to separate with other list item
        postBodyHTMLContent = postBodyHTMLContent.replaceAll(Data.LI_CLOSING_REGEX_MATCHER, "<br>");
    }

    private void cleanPharagraph() {
        // remove opening p tag
        postBodyHTMLContent = postBodyHTMLContent.replaceAll("[<](\\s+)?p[^>]*[>]", "");

        // replace closing p tag with <br> to indicate paragraph
        postBodyHTMLContent = postBodyHTMLContent.replaceAll("[<](\\s+)?(/)(\\s+)?p[^>]*[>]", "<br>");
    }

    private void removeBrInsidelist() {
        // match li's tag
        Matcher matcher = Pattern.compile(Data.LI_REGEX_MATCHER).matcher(postBodyHTMLContent);

        // loop through each li tag and remove <br/> if exist/founded
        while (matcher.find()) {
            postBodyHTMLContent = postBodyHTMLContent.replace(matcher.group(2), matcher.group(2).replaceAll(Data.BR_MATCHER, ""));
        }
    }

    private void preserveNecessaryTag() {
        // Create tag selection to be preserved
        Whitelist whitelist = Whitelist.none();
        whitelist.addTags("br", "h2", "a", "img", "p", "b", "i", "pre", "li");

        // preserve img and its src value
        whitelist.addAttributes("img", "src");
        // allow this protocol inside img src attribute value
        whitelist.addProtocols("img", "src", "http", "https", "data", "cid");

        // preserve anchor and its href value
        whitelist.addAttributes("a", "href");
        // allow this protocols value inside href attribute value
        whitelist.addProtocols("a", "href", "http", "https", "data", "cid");

        postBodyHTMLContent = Jsoup.clean(postBodyHTMLContent, whitelist);
    }

    private void removeAnchorWrappedImage() {
        Matcher matcher = Pattern.compile("([<](\\s+)?a[^>]*[>](\\s+)?([<](\\s+)?img[^>]*[>])(\\s+)?[<](\\s+)?[\\/]?(\\s+)?a[^>]*[>])").matcher(postBodyHTMLContent);
        // extract the <img> only to array
        while (matcher.find()) {
            Data.imageLink.add(matcher.group(4));
        }
        // reset matcher to the first for replacing wrapped <img> with <a> to <img> only
        matcher.reset();
        // loop through the <img> only array
        for (String image : Data.imageLink) {
            // the loop will equivalent with the matcher find method index
            if (matcher.find()) {
                // replace the wrapped image which is in group 1 of the matcher
                // with the <img> only tag
                postBodyHTMLContent = postBodyHTMLContent.replace(matcher.group(1), image);
            }
        }
    }
    
    private Context getActivityContext(){
        return postActivityContext;
    }
}
