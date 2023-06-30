package co.edu.unal.data;

import java.util.Set;

public class DatabaseKeywordData {

  private String keywordGroup;
  private Set<String> keywords;

  public DatabaseKeywordData(String keywordGroup, Set<String> keywords) {
    this.keywordGroup = keywordGroup;
    this.keywords = keywords;
  }

  public String getKeywordGroup() {
    return keywordGroup;
  }

  public void setKeywordGroup(String keywordGroup) {
    this.keywordGroup = keywordGroup;
  }

  public Set<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(Set<String> keywords) {
    this.keywords = keywords;
  }
}
