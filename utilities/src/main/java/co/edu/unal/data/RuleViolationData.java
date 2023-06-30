package co.edu.unal.data;

import org.apache.commons.lang3.StringUtils;

public class RuleViolationData {

  private String problem;
  private String packageValue;
  private String file;
  private String priority;
  private String line;
  private String description;
  private String ruleSet;
  private String rule;

  public RuleViolationData() {
    this.problem = StringUtils.EMPTY;
    this.packageValue = StringUtils.EMPTY;
    this.priority = StringUtils.EMPTY;
    this.line = StringUtils.EMPTY;
    this.description = StringUtils.EMPTY;
    this.ruleSet = StringUtils.EMPTY;
    this.rule = StringUtils.EMPTY;
  }

  public String getProblem() {
    return problem;
  }

  public RuleViolationData setProblem(String problem) {
    this.problem = problem;
    return this;
  }

  public String getPackageValue() {
    return packageValue;
  }

  public RuleViolationData setPackageValue(String packageValue) {
    this.packageValue = packageValue;
    return this;
  }

  public String getFile() {
    return file;
  }

  public RuleViolationData setFile(String file) {
    this.file = file;
    return this;
  }

  public String getPriority() {
    return priority;
  }

  public RuleViolationData setPriority(String priority) {
    this.priority = priority;
    return this;
  }

  public String getLine() {
    return line;
  }

  public RuleViolationData setLine(String line) {
    this.line = line;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public RuleViolationData setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getRuleSet() {
    return ruleSet;
  }

  public RuleViolationData setRuleSet(String ruleSet) {
    this.ruleSet = ruleSet;
    return this;
  }

  public String getRule() {
    return rule;
  }

  public RuleViolationData setRule(String rule) {
    this.rule = rule;
    return this;
  }
}
