package Constructor;


public class Utils {
    public static String cutString(String str, String start, String end){
        Integer s = str.indexOf(start);
        Integer e = str.indexOf(end);
        return str.substring(s+1, e);
    }
    public static String findPackageName(String[] newPkgNames, String path) {
        for(String n: newPkgNames){
            if(path.replace("/", ".").contains(n)){
                return n;
            }
        }
        return null;
    }
}
