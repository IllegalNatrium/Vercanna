package me.natrium.layouts;

public class CitationAuthorLayout {

  private String name;

  private int manyCitations;

  public String getName() {
    return name;
  }

  public int getManyCitations() {
    return manyCitations;
  }

  public static class Builder {

    private String name;

    private int manyCitations;

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setManyCitations(int manyCitations) {
      this.manyCitations = manyCitations;
      return this;
    }

    public CitationAuthorLayout build() {
      return new CitationAuthorLayout(this);
    }

  }

  public CitationAuthorLayout(Builder b) {
    this.name = b.name;
    this.manyCitations = b.manyCitations;
  }

}
