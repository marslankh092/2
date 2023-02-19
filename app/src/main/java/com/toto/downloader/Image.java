package com.toto.downloader;

public class Image {
    private String indratechpic, link;

    public Image(){

    }

    public Image(String indratechpic, String link) {
        this.indratechpic = indratechpic;
        this.link = link;
    }

    public String getPic() {
        return indratechpic;
    }

    public void setPic(String indratechpic) {
        this.indratechpic = indratechpic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
