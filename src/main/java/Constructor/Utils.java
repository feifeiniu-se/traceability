package Constructor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


    public static HashMap<String, String> codeElement2Name(String codeElement) {
        HashMap<String, String> res = new HashMap<>();
        String returnType = codeElement.substring(codeElement.lastIndexOf(":")+1).replace(" ", "");
        res.put("RT", returnType);
        String[] parameterList = cutString(codeElement, "(", ")").replace(", ", ",").split(",");
        String name = codeElement.substring(codeElement.indexOf(" ")+1, codeElement.indexOf("("));
        String parameterTypes = "(";
        String parameters = "[";
        if(cutString(codeElement, "(", ")").length()<1){//如果parameter为空
            parameterTypes = "()";
            parameters = "[]";
        }else{
            for(String p: parameterList){
                System.out.println(codeElement);
                System.out.println(parameterList.length);
                System.out.println(parameterList.toString());
                parameterTypes = parameterTypes + p.split(" ")[1] + ", ";
                parameters = parameters + p.split(" ")[1] + " " + p.split(" ")[0] + ", ";
            }
            parameterTypes = parameterTypes.substring(0, parameterTypes.length()-2)+")";
            parameters = parameters.substring(0, parameters.length()-2) + "]";
        }

        res.put("MN", returnType+"_"+name+parameterTypes);
        res.put("PA", parameters);
        res.put("PT", parameterTypes);
        return res;
    }
}
