package com.koolearn.android.kooreader.book;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

    private String subtitle;
    private String author;
    private String pubdate;
    private String origin_title;
    private String image;
    private String catalog;
    private String alt;
    private String id;
    private String publisher;
    private String title;
    private String url;
    private String author_intro;
    private String summary;
    private String price;
    private String pages;
    private String small;
    private String medium;
    private String large;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPages() {
        return pages;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "subtitle='" + subtitle + '\'' +
                ", author=" + author +
                ", pubdate='" + pubdate + '\'' +
                ", origin_title='" + origin_title + '\'' +
                ", image='" + image + '\'' +
                ", catalog='" + catalog + '\'' +
                ", alt='" + alt + '\'' +
                ", id='" + id + '\'' +
                ", publisher='" + publisher + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", author_intro='" + author_intro + '\'' +
                ", summary='" + summary + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

    private static final String BASE_URL = "http://115.28.224.54:8080/VYReader/api/";

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    public interface IBookResponse<T> {
        void onData(T data);
    }

    public static void searchBooks(int page, int pageSize, final IBookResponse<List<Book>> response) {
        RequestParams params = new RequestParams();
        params.put("page", page);
        params.put("pageSize", pageSize);
//        String keyword = "??????";
//        try{
//            keyword = URLEncoder.encode(keyword, "UTF-8");
//            keyword = URLEncoder.encode(keyword, "UTF-8");
//            params.put("keyword","??????")
//        }catch(UnsupportedEncodingException e){
//            e.printStackTrace();
//        }

        client.get(getAbsoluteUrl("getBook"), params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(new String(responseBody));
                    JSONArray jaBooks = json.optJSONArray("data");
                    List<Book> books = gson.fromJson(jaBooks.toString(), new TypeToken<List<Book>>() {
                    }.getType());
                    response.onData(books);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
