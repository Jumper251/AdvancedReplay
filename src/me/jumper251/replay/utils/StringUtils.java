package me.jumper251.replay.utils;

public class StringUtils {

	  private static char[] chars = new char[]{'a' , 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
          'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A' , 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
          'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

public static String getRandomString(int length) {
    StringBuilder sb = new StringBuilder(length);
    for(int i = 0; i < length; i += 1){
        sb.append(chars[MathUtils.randInt(0, (chars.length - 1))]);
    }
    return sb.toString();
}
}
