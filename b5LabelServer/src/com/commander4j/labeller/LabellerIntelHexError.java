package com.commander4j.labeller;

public class LabellerIntelHexError extends Exception {

private static final long serialVersionUID = 1L;

private int exceptionNumber = 0;

  public LabellerIntelHexError() {
    super();
    this.exceptionNumber = 0;
  }

  public LabellerIntelHexError(String s) {
    super(s);
    this.exceptionNumber = 0;
  }

  public LabellerIntelHexError(int exceptionNumber) {
    super();
    this.exceptionNumber = exceptionNumber;
  }

  public LabellerIntelHexError(int exceptionNumber, String s) {
    super(s);
    this.exceptionNumber = exceptionNumber;
  }

  public int getExceptionNumber() {
    return exceptionNumber;
  }

  public String toString() {
    return "" + this.exceptionNumber + " - " + super.toString();
  }
}