package co.aurasphere.aura.wikipedia;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.EndTagToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.tags.HTMLTag;

public class WikipediaScraper {
	
	public String scrape(String subject){
		String[] listOfTitleStrings = { subject };
	
		User user = new User("", "", "http://en.wikipedia.org/w/api.php");
		user.login();
		List<Page> listOfPages = user.queryContent(listOfTitleStrings);
//		for (Page page : listOfPages) {
		  Page page = listOfPages.get(0);
		  MyWikiModel wikiModel = new MyWikiModel("${image}", "${title}");
		  String html = wikiModel.render(page.toString());
		  System.out.println(html);
		  
		  Document document = Jsoup.parse(html);
//		  HtmlToPlainText.main()
		  String text = document.text().substring(document.text().indexOf("Content: ") + 9);
		  text = format(text);
		  System.out.println(text);
		  if(text.startsWith("REDIRECT ")) return scrape(text.substring("REDIRECT ".length()));
//		}
		return text;
	}
	
	private String format(String sentence){
		
		// remove all notes.
		sentence = sentence.replaceAll("\\[[0-9]+\\]", "");
		
		
		int index = sentence.indexOf(".");
		if(index==-1) return sentence;
		return sentence.substring(0, index);
	}

	//Model which eliminates all Wiki Magic Words returning "" instead.

 class MyWikiModel extends WikiModel{

  public MyWikiModel(Configuration configuration, Locale locale,
    String imageBaseURL, String linkBaseURL) {
      super(configuration, locale, imageBaseURL, linkBaseURL);
  }
  public MyWikiModel(Configuration configuration,
    ResourceBundle resourceBundle, INamespace namespace,
    String imageBaseURL, String linkBaseURL) {
      super(configuration, resourceBundle, namespace, imageBaseURL, linkBaseURL);
  }
  public MyWikiModel(Configuration configuration, String imageBaseURL,
    String linkBaseURL) {
      super(configuration, imageBaseURL, linkBaseURL);
  }
  public MyWikiModel(String imageBaseURL, String linkBaseURL) {
    super(imageBaseURL, linkBaseURL);
  }

  @Override
  public String getRawWikiContent(String namespace, String articleName,
    Map<String, String> templateParameters) {
      String rawContent = super.getRawWikiContent(namespace, articleName, templateParameters);

      if (rawContent == null){
        return "";
      }
      else {
        return rawContent;
      }
    }
}
 
 /**
  * A converter which renders the internal tree node representation as specific
  * HTML text, but which is easier to change in behaviour than its superclass and
  * has a noImages property, which can be set to leave out all images
  *
  */
 public class ExtendedHtmlConverter extends HTMLConverter {

   private boolean noImages;

   public ExtendedHtmlConverter() {
     super();
   }

   public ExtendedHtmlConverter(boolean noLinks) {
     super(noLinks);
   }

   public ExtendedHtmlConverter(boolean noLinks, boolean noImages) {
     this(noLinks);
     this.noImages = noImages;
   }

   protected void renderContentToken(Appendable resultBuffer,
       ContentToken contentToken, IWikiModel model) throws IOException {
     String content = contentToken.getContent();
     content = Utils.escapeXml(content, true, true, true);
     resultBuffer.append(content);
   }

   protected void renderHtmlTag(Appendable resultBuffer, HTMLTag htmlTag,
       IWikiModel model) throws IOException {
     htmlTag.renderHTML(this, resultBuffer, model);
   }

   protected void renderTagNode(Appendable resultBuffer, TagNode tagNode,
       IWikiModel model) throws IOException {
     Map<String, Object> map = tagNode.getObjectAttributes();
     if (map != null && map.size() > 0) {
       Object attValue = map.get("wikiobject");
       if (!noImages) {
         if (attValue instanceof ImageFormat) {
           imageNodeToText(tagNode, (ImageFormat) attValue, resultBuffer, model);
         }
       }
     } else {
       nodeToHTML(tagNode, resultBuffer, model);
     }
   }
 
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
public void nodesToText(List<? extends Object> nodes,
       Appendable resultBuffer, IWikiModel model) throws IOException {
     if (nodes != null && !nodes.isEmpty()) {
       try {
         int level = model.incrementRecursionLevel();

         if (level > Configuration.RENDERER_RECURSION_LIMIT) {
           resultBuffer
               .append("<span class=\"error\">Error - recursion limit exceeded rendering tags in HTMLConverter#nodesToText().</span>");
           return;
         }
         Iterator<? extends Object> childrenIt = nodes.iterator();
         while (childrenIt.hasNext()) {
           Object item = childrenIt.next();
           if (item != null) {
             if (item instanceof List) {
               nodesToText((List) item, resultBuffer, model);
             } else if (item instanceof ContentToken) {
               // render plain text content
               ContentToken contentToken = (ContentToken) item;
               renderContentToken(resultBuffer, contentToken, model);
             } else if (item instanceof HTMLTag) {
               HTMLTag htmlTag = (HTMLTag) item;
               renderHtmlTag(resultBuffer, htmlTag, model);
             } else if (item instanceof TagNode) {
               TagNode tagNode = (TagNode) item;
               renderTagNode(resultBuffer, tagNode, model);
             } else if (item instanceof EndTagToken) {
               EndTagToken node = (EndTagToken) item;
               resultBuffer.append('<');
               resultBuffer.append(node.getName());
               resultBuffer.append("/>");
             }
           }
         }
       } finally {
         model.decrementRecursionLevel();
       }
     }
   }

   protected void nodeToHTML(TagNode node, Appendable resultBuffer,
       IWikiModel model) throws IOException {
     super.nodeToHTML(node, resultBuffer, model);
   }
   
 }
 
 public class MyHtmlConverter extends ExtendedHtmlConverter {

	  public MyHtmlConverter() {
	    super();
	  }

	  public MyHtmlConverter(boolean noLinks) {
	    super(noLinks);
	  }

	  public MyHtmlConverter(boolean noLinks, boolean noImages) {
	    super(noLinks, noImages);
	  }

	  protected void renderContentToken(Appendable resultBuffer,
	      ContentToken contentToken, IWikiModel model) throws IOException {
	    String content = contentToken.getContent();
	    content = content.replaceAll("\\(,", "(").replaceAll("\\(\\)", "()");
	    content = Utils.escapeXml(content, true, true, true);
	    resultBuffer.append(content);
	  }

	  protected void renderHtmlTag(Appendable resultBuffer, HTMLTag htmlTag,
	      IWikiModel model) throws IOException {
	    String tagName = htmlTag.getName();
	    if ((!tagName.equals("ref"))) {
	      super.renderHtmlTag(resultBuffer, htmlTag, model);
	    }
	  }
	}
}
