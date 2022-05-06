package Constructor;


import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.*;
import Project.RefactoringMiner.Refactoring;

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
    private static void moveClass(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Operator type, CommitCodeChange commitTime, String oldName, String newName){
        //update class.father, class.son, classTime
        assert mappings.containsKey(oldName);
        CodeBlock classBlock = mappings.get(oldName);
        mappings.put(newName, classBlock);
        ClassTime classTime = (ClassTime) classBlock.getLastHistory().clone();

        //old Father
        CodeBlock oldFather = classTime.getParentCodeBlock();
        assert mappings.containsKey(newName.substring(0, newName.lastIndexOf(".")));
        //new father
        CodeBlock newFather = mappings.get(newName.substring(0, newName.lastIndexOf(".")));

        //update classTime
        classTime.setName(newName);
        classTime.setTime(commitTime);
        classTime.setRefactorType(type);
        classTime.setParentCodeBlock(newFather);
        commitTime.addCodeChange(classTime);
        classBlock.addHistory(classTime);

        //update old father
        CodeBlockTime oldFatherTime = (CodeBlockTime) oldFather.getLastHistory().clone();
        oldFather.addHistory(oldFatherTime);
        commitTime.addCodeChange(oldFatherTime);
        oldFatherTime.setTime(commitTime);
        oldFatherTime.setRefactorType(type);
        oldFatherTime.getClasses().remove(classBlock);

        //update new father
        CodeBlockTime newFatherTime = (CodeBlockTime) newFather.getLastHistory().clone();
        newFather.addHistory(newFatherTime);
        commitTime.addCodeChange(newFatherTime);
        newFatherTime.setTime(commitTime);
        newFatherTime.setRefactorType(type);
        oldFatherTime.getClasses().add(classBlock);

        //update son //todo 递归
    }
    public static void renameClass(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Operator type, CommitCodeChange commitTime, String oldName, String newName){
        //update classTime, class.son
        assert mappings.containsKey(oldName);
        CodeBlock classBlock = mappings.get(oldName);
        mappings.put(newName, classBlock);
        ClassTime classTime = (ClassTime) classBlock.getLastHistory().clone();

        //update classTime
        classTime.setName(newName);
        classTime.setTime(commitTime);
        classTime.setRefactorType(type);
        commitTime.addCodeChange(classTime);
        classBlock.addHistory(classTime);

        //update sons
        List<CodeBlock> classes = classTime.getClasses();//todo 递归
        List<CodeBlock> methods = classTime.getMethods();
        List<CodeBlock> attributes = classTime.getAttributes();
        



    }

//    public static HashMap<String, String> codeElement2Method(String codeElement) {
//        HashMap<String, String> res = new HashMap<>();
//        String returnType;
//        if(codeElement.lastIndexOf(":")>0){
//            returnType = codeElement.substring(codeElement.lastIndexOf(":")+1).replace(" ", "");
//        }else{
//            returnType = "";
//        }
//        res.put("RT", returnType);
//
//        String[] parameterList = cutString(codeElement, "(", ")").replace(", ", ",").split(",");
//        String name = codeElement.substring(codeElement.indexOf(" ")+1, codeElement.indexOf("("));
//        String parameterTypes = "(";
//        String parameters = "[";
//        if(cutString(codeElement, "(", ")").length()<1){//如果parameter为空
//            parameterTypes = "()";
//            parameters = "[]";
//        }else{
//            for(String p: parameterList){
////                System.out.println(codeElement);
////                System.out.println(parameterList.length);
////                System.out.println(parameterList.toString());
//                parameterTypes = parameterTypes + p.split(" ")[1] + ", ";
//                parameters = parameters + p.split(" ")[1] + " " + p.split(" ")[0] + ", ";
//            }
//            parameterTypes = parameterTypes.substring(0, parameterTypes.length()-2)+")";
//            parameters = parameters.substring(0, parameters.length()-2) + "]";
//        }
//
//        res.put("MN", returnType+"_"+name+parameterTypes);
//        res.put("PA", parameters);
//        res.put("PT", parameterTypes);
//        return res;
//    }
}
