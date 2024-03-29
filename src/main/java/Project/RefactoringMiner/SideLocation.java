package Project.RefactoringMiner;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

import static Constructor.Utils.cutString;

@Data
public class SideLocation {
    private String filePath;
    private int startLine;
    private int endLine;
    private int startColumn;
    private int endColumn;
    private String codeElementType;
    private String description;
    private String codeElement;

    private List<String> codeLines;  // [startLine, endLine] 对应的源代码

    public HashMap<String, String> parseMethodDeclaration(){
//        System.out.println(codeElement);
        HashMap<String, String> res = new HashMap<>();
        String returnType;
//        System.out.println(filePath);
        if(codeElement.lastIndexOf(":")>0){
            returnType = codeElement.substring(codeElement.lastIndexOf(":")+1).replace(" ", "");
        }else{
            returnType = "";
        }
        res.put("RT", returnType);

        String[] parameterList = cutString(codeElement, "(", ")").split(", ");
        String name = codeElement.substring(codeElement.substring(0, codeElement.indexOf("(")).lastIndexOf(" ")+1, codeElement.indexOf("("));
        String parameterTypes = "(";
        String parameters = "[";
        if(cutString(codeElement, "(", ")").length()<1){//如果parameter为空
            parameterTypes = "()";
            parameters = "[]";
        }else{
            for(String p: parameterList){
//                System.out.println(codeElement);
//                System.out.println(parameterList.length);
//                System.out.println(parameterList.toString());
                parameterTypes = parameterTypes + p.split(" ")[1] + ", ";
                parameters = parameters + p.split(" ")[1] + " " + p.split(" ")[0] + ", ";
            }
            parameterTypes = parameterTypes.substring(0, parameterTypes.length()-2)+")";
            parameters = parameters.substring(0, parameters.length()-2) + "]";
        }
        if(returnType==""){
            res.put("MN", name+parameterTypes);
        }else{
            res.put("MN", returnType+"_"+name+parameterTypes);
        }
        res.put("PA", parameters);
        res.put("PT", parameterTypes);
        return res;
    }
    public String parseAttributeOrParameter(){
        String[] tmp = codeElement.replace("[]", "").split(" : ");
        assert  tmp.length==2;
        return tmp[1]+"_"+tmp[0].substring(tmp[0].indexOf(" ")+1);
    }
}
