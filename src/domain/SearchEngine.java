package domain;

public class SearchEngine {
    public String name;
    public String keywords;
    public String contents;



    public SearchEngine(String name, String keywords, String contents) {
        this.name = name;
        this.keywords = keywords;
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return
                "Doc Name ='" + name + '\'' +
                " | " + keywords +
                " | " + contents + '\'';
    }
}


