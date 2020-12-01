package com.flotta.utility;

public class Validator {
  
  public static boolean validPassword(String psw) {
    return psw != null && psw.length() >= 6 && psw.length() <= 20;
  }
  
  public static boolean equalsAndValidPassword(String psw, String psw2) {
    return psw != null && psw.length() >= 6 && psw.length() <= 20 && psw.contentEquals(psw2);
  }
  
  public static boolean validHunPhoneNumber(String number) {
    return number.matches("^(2|3|7|5)0[0-9]{7}$");
  }
  
  //Luhn algorithm
  //length min 15
  public static boolean checkImieWithLuhnAlg(String imei) {
    int i = imei.length() - 1;
    int count = 0;
    boolean isSecondDigit = false;
    while(i >= 0) {
      char ch = imei.charAt(i);
      System.out.println("ch=" + ch + ", i=" + i + ", count=" + count);
      if(!Character.isDigit(ch)) {
        return false;
      }
      if(isSecondDigit) {
        int n = Character.getNumericValue(ch) * 2;
        System.out.println(n);
        count += n > 9 ? n - 9 : n;
      } else {
        count += Character.getNumericValue(ch);
      }
      isSecondDigit = !isSecondDigit;
      i--;
    }
    return count % 10 == 0;
  }
}
