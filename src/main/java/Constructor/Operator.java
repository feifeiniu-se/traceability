package Constructor;


import Model.AttributeTime;
import Model.CodeBlock;
import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.SideLocation;

import java.util.HashMap;
import java.util.List;

public enum Operator {

    //TODO 为每一种refactoring添加commit的codeblocktime信息
    Rename_Package {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            //Rename Package A to B
            //添加新的pkgTime，修改文件位置、签名以及重构类型
            //TODO create new packageTime, classTime, methodTime, attributeTime, update filepath for each codeBlock, and rename signature for package and class
            //文件重命名
        }
    },
    Move_Package {
        //TODO create new packageTime, classTime, methodTime, attributeTime,
        //文件重命名
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Split_Package {
        //文件重命名
        //TODO create new package, create new packageTime, classTime, methodTime, attributeTime to update the filePath, go through all the file for class level update
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Merge_Package {
        //文件重命名
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Change_Type_Declaration_Kind {//interface class等更改 一般不会影响codeblock
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Collapse_Hierarchy {//包之后 较为复杂的 将一个类中的内容打散 重新生成新的类 等
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Extract_Superclass {
        //add a new class, the last filepath on the rightfilepath is the new superclass
        //TODO can be used to detect file rename
        //文件重命名
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Extract_Interface {
        //TODO ***
        //文件重命名 这个有点特殊
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Extract_Class {
        //将原来代码中的一些方法抽出来，放到新建的类中。新建一个类，将一些方法从旧的类中移到新的类中
        //derive modify
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            String[] description = refactor.getDescription().split(" ");
            String newClass = description[2];
            String oldClass = description[4];
            List<SideLocation> left = refactor.getLeftSideLocations();
            List<SideLocation> right = refactor.getRightSideLocations();
            //TODO 左边第一个是原始文件，第二个是原始文件，右边第二个是新的文件，剩下的是移动的方法
//            TODO move methods from A to B


            System.out.println(refactor.getType());
        }
    },
    Extract_Subclass {
        //derive class 跟extract class几乎差不多

        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Merge_Class {
        //derive
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Move_Class {
        //TODO 注意内部类的移动情况 63f3a7ebe4915e51acb8b2a8b0b234e5618239ba 根据left right文件名确定文件的对应关系，然后添加新的classBlockTime 进行修改，以及更新内部方法和属性的文件位置，以及签名
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Rename_Class {
        // 创建文件对应关系，然后对应修改classBlockTime，packageBlockTime的内容，然后在最后统一更新method和attribute的内容。
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Move_and_Rename_Class {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Extract_Method{
        //add method 从现有方法的代码中抽取部分生成新的方法，methodB derived from methodA， 需要在遍历阶段更新methodBlockTime的信息
        //有可能是从很多方法中提取出的新的方法，所以在存储新的代码块之前需要判断是否已经存在，如果已经存在，就在derived from中添加新的方法
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash){
            System.out.println(refactor.getType());
        }
    },
    Inline_Method{
        //与extract method相反，delete 方法，将方法的内容合并到已有的方法中
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash){
            System.out.println(refactor.getType());
        }
    },
    Pull_Up_Method {
        //move method from one class to super class,将几个子类中的方法移到超类中，跨文件，涉及方法的移动，还可能修改名字，但是不影响文件数目
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Push_Down_Method {
        //将父类中的方法 移到子类中去，一般会在不同的文件之间进行移动，甚至还有rename
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Extract_and_Move_Method {
        //可能从多个方法中提取出一个新的方法， 涉及新建一个methodBlock，有多个derived from，并且移到了一个新的类中，跨文件
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Move_and_Inline_Method {
        //跨文件，移动，移动旧的方法，并拆散方法到已有的方法中。
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Move_and_Rename_Method {//跨文件
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Move_Method {//跨文件
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash){
            System.out.println(refactor.getType());
        }
    },
    Change_Return_Type { //trival 只需要修改返回类型 一般不跨文件
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Rename_Method {//trival 一般不跨文件 只需要修改方法名字
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash){
            System.out.println(refactor.getType());
        }
    },
    Parameterize_Variable {//方法名级别
        //把方法中的一个变量 变为方法的参数 不跨文件，仅需要修改方法的名字 trivial
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Merge_Parameter {//把一个方法的参数进行合并，但是可能会有移动
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Split_Parameter {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Change_Parameter_Type {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Add_Parameter {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {

            AttributeTime attributeTime = new AttributeTime();
//            attributeTime.setTime(currentTime);

            System.out.println(refactor.getType());
        }
    },
    Remove_Parameter {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Reorder_Parameter {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Parameterize_Attribute {//把一个attribute变成一个参数，同时修改属性和方法的参数 一般不跨项目
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
//    Add_Thrown_Exception_Type {
//        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
//            System.out.println(refactor.getType());
//        }
//    },
//    Remove_Thrown_Exception_Type {
//        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
//            System.out.println(refactor.getType());
//        }
//    },
//    Change_Thrown_Exception_Type {
//        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
//            System.out.println(refactor.getType());
//        }
//    },
    Pull_Up_Attribute {//把子类中的属性 移到父类中 跨文件
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Push_Down_Attribute {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Rename_Attribute {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Merge_Attribute {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Split_Attribute {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Change_Attribute_Type {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Extract_Attribute {//涉及增加新的attribute
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Encapsulate_Attribute {
        //Attribute encapsulation is useful when you have an attribute that is affected by several different methods,
        // each of which needs that attribute to be in a known state. To prevent programmers from changing the attribute
        // in the 4GL code, you can make the attribute private so that programmers can only access it from the object's methods.
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },//TODO not_sure_if_neccessarry
    Inline_Attribute {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Move_and_Rename_Attribute {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    //    Replace_Attribute_(with_Attribute) {
//        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
//            System.out.println(refactor.getType());
//        }
//    },//TODO 暂时没找到例子
    Replace_Attribute_with_Variable {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },
    Replace_Anonymous_with_Lambda {
        public void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash) {
            System.out.println(refactor.getType());
        }
    },//TODO 暂时没找到
    ;

    public abstract void apply(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash);

}
