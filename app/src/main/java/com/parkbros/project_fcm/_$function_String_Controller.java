package com.parkbros.project_fcm;

public class _$function_String_Controller {

    public int StrLength(String str){
        int en = 0;
        int ko = 0;
        int etc = 0;

        char[] string = str.toCharArray();

        for (int j=0; j<string.length; j++) {
            if (string[j]>='A' && string[j]<='z') {
                en++;
            }
            else if (string[j]>='\uAC00' && string[j]<='\uD7A3') {
                ko++;
            }
            else {
                etc++;
            }
        }

        return (en + ko + etc);
    }
    public String TrimRemoveLineSpace(String str){
        String temp = str.trim();
        temp = temp.replaceAll("\\r\\n|\\r|\\n", " ");
//
//        temp = temp.replaceAll("\n", "1");
//        temp = temp.replaceAll("\\n", "2");
//        temp = temp.replaceAll("\\\\n", "3");

        return temp;
    }
    public String CutString(String str,int byteLength,int sizePerLetter){
        int retLength = 0;
        int tempSize = 0;
        int asc;
        if (str == null || "".equals(str) || "null".equals(str)) {
            str = "";
        }

        int length = str.length();

        for (int i = 1; i <= length; i++) {
            asc = (int) str.charAt(i - 1);
            if (asc > 127) {
                if (byteLength >= tempSize + sizePerLetter) {
                    tempSize += sizePerLetter;
                    retLength++;
                }
            } else {
                if (byteLength > tempSize) {
                    tempSize++;
                    retLength++;
                }
            }
        }

        return str.substring(0, retLength);

    }
}
