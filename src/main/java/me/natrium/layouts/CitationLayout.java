package me.natrium.layouts;

public class CitationLayout {

  private String citation;
  private CitationAuthorLayout author;
  private String createdAt;

  public String getCitation() {
    return citation;
  }

  public CitationAuthorLayout getAuthor() {
    return author;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public static class Builder {

    private String citation;
    private CitationAuthorLayout author;
    private String createdAt;

    public Builder setCitation(String citation) {
      this.citation = citation;
      return this;
    }

    public Builder setAuthor(CitationAuthorLayout author) {
      this.author = author;
      return this;
    }

    public Builder setCreatedAt(String createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public CitationLayout build() {
      return new CitationLayout(this);
    }

  }

  protected CitationLayout(Builder b) {
    this.citation = b.citation;
    this.author = b.author;
    this.createdAt = b.createdAt;
  }

}
