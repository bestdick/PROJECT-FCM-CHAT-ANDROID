package com.example.project_fcm;

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
    public String CutString(String str, int len){
        byte[] by = str.getBytes();
        int count = 0;
        try  {
            for(int i = 0; i < len; i++) {

                if((by[i] & 0x80) == 0x80) count++; // 핵심 코드

            }

            if((by[len - 1] & 0x80) == 0x80 && (count % 2) == 1) len--; // 핵심코드

            return new String(by, 0, len);

        }
        catch(java.lang.ArrayIndexOutOfBoundsException e)
        {
            return "";
        }
    }
}
