package com.cgtfarmer.demo.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

  public static String mapStackTraceToString(Throwable e) {
    StringWriter sw = new StringWriter();

    PrintWriter pw = new PrintWriter(sw);

    e.printStackTrace(pw);

    return sw.toString();
  }
}
