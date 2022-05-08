package Project.RefactoringMiner;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Refactorings {//这是一个commit的内容
    private String repository;
    private String sha1;
    private String url;
    private List<Refactoring> refactorings;

    private static Set<String> filterTypes = new HashSet<>(Arrays.asList(
            "Extract Method",
            "Inline Method",
            "Rename Method",
            "Move Method",
            "Move Attribute",
            "Pull Up Method",
            "Pull Up Attribute",
            "Push Down Method",
            "Push Down Attribute",
            "Extract Superclass",
            "Extract Interface",
            "Move Class",
            "Rename Class",
            "Extract And Move Method",
            "Rename Package",
            "Move And Rename Class",
            "Extract Class",
            "Extract Subclass",
//            "Extract Variable",
//            "Inline Variable",
            "Parameterize Variable",
//            "Rename Variable",
//            "Rename Parameter",
            "Rename Attribute",
            "Move And Rename Attribute",
//            "Replace Variable With Attribute",
//            "Replace Attribute (With Attribute)",//TODO 暂时没找到例子
//            "Merge Variable",
            "Merge Parameter",
            "Merge Attribute",
//            "Split Variable",
            "Split Parameter",
            "Split Attribute",
//            "Change Variable Type",
            "Change Parameter Type",
            "Change Return Type",
            "Change Attribute Type",
            "Extract Attribute",
            "Move And Rename Method",
            "Move And Inline Method",
//            "Add Method Annotation",
//            "Remove Method Annotation",
//            "Modify Method Annotation",
//            "Add Attribute Annotation",
//            "Remove Attribute Annotation",
//            "Modify Attribute Annotation",
//            "Add Class Annotation",
//            "Remove Class Annotation",
//            "Modify Class Annotation",
//            "Add Parameter Annotation",
//            "Remove Parameter Annotation",
//            "Modify Parameter Annotation",
//            "Add Variable Annotation",
//            "Remove Variable Annotation",
//            "Modify Variable Annotation",
            "Add Parameter",
            "Remove Parameter",
            "Reorder Parameter",
//            "Add Thrown Exception Type",
//            "Remove Thrown Exception Type",
//            "Change Thrown Exception Type",
//            "Change Method Access Modifier",
//            "Change Attribute Access Modifier",
            "Encapsulate Attribute",//TODO maybe method&attribute not sure
            //Attribute encapsulation is useful when you have an attribute that is affected by several different methods,
            // each of which needs that attribute to be in a known state. To prevent programmers from changing the attribute
            // in the 4GL code, you can make the attribute private so that programmers can only access it from the object's methods.
            "Parameterize Attribute",
            "Replace Attribute With Variable",
//            "Add Method Modifier (final, static, abstract, synchronized)",
//            "Remove Method Modifier (final, static, abstract, synchronized)",
//            "Add Attribute Modifier (final, static, transient, volatile)",
//            "Remove Attribute Modifier (final, static, transient, volatile)",
//            "Add Variable Modifier (final)",
//            "Add Parameter Modifier (final)",
//            "Remove Variable Modifier (final)",
//            "Remove Parameter Modifier (final)",
//            "Change Class Access Modifier",
//            "Add Class Modifier (final, static, abstract)",
//            "Remove Class Modifier (final, static, abstract)",
            "Move Package",
            "Split Package",
            "Merge Package",
//            "Localize Parameter",
            "Change Type Declaration Kind",
            "Collapse Hierarchy",
//            "Replace Loop With Pipeline",
            "Replace Anonymous With Lambda",//done 暂时没找到 只发生在方法内部
            "Merge Class",
           "Inline Attribute"

    ));

    /**
     * 将不在 filterTypes 中的 type 过滤出去
     */
    public void filter() {
        refactorings = refactorings.stream()
                .filter(refactoring -> filterTypes.contains(refactoring.getType()))
                .collect(Collectors.toList());
    }

    private static Set<String> thirdLevelTypes = new HashSet<>(Arrays.asList(
            "Extract Method",
            "Inline Method",
            "Rename Method",
            "Move Method",
            "Pull Up Method",
            "Pull Up Attribute",
            "Push Down Method",
            "Push Down Attribute",
            "Extract And Move Method",
            "Parameterize Variable",
            "Rename Attribute",
            "Move And Rename Attribute",
            "Move Attribute",
            "Merge Parameter",
            "Merge Attribute",
            "Split Parameter",
            "Split Attribute",
            "Change Parameter Type",
            "Change Return Type",
            "Change Attribute Type",
            "Extract Attribute",
            "Move And Rename Method",
            "Move And Inline Method",
            "Add Parameter",
            "Remove Parameter",
            "Reorder Parameter",
            "Encapsulate Attribute",
            "Parameterize Attribute",
            "Replace Attribute With Variable",
            "Inline Attribute"
    ));
    private static Set<String> secondLevelTypes = new HashSet<>(Arrays.asList(
            "Extract Superclass",
            "Extract Interface",
            "Move Class",
            "Rename Class",
            "Move And Rename Class",
            "Extract Class",
            "Extract Subclass",
            "Change Type Declaration Kind",
            "Collapse Hierarchy",
            "Merge Class"
    ));
    private static Set<String> firstLevelTypes = new HashSet<>(Arrays.asList(
            "Rename Package",
            "Move Package",
            "Split Package",
            "Merge Package"
    ));
    public List<Refactoring> filter(String types){
        List<Refactoring> res = new ArrayList<>();
        if(types.equals("first")){
            for(Refactoring r: refactorings){
                if(firstLevelTypes.contains(r.getType())){
                    res.add(r);
                }
            }

        }else if(types.equals("second")){
            for(Refactoring r: refactorings){
                if(secondLevelTypes.contains(r.getType())){
                    res.add(r);
                }
            }
        }
        else if(types.equals("third")){
            for(Refactoring r: refactorings){
                if(thirdLevelTypes.contains(r.getType())){
                    res.add(r);
                }
            }
        }else{
            return null;
        }
        return res;

    }

}
