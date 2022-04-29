package Constructor.Enums;


import Constructor.Utils;
import Model.AttributeTime;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Model.PackageTime;
import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.SideLocation;
import Project.Utils.DiffFile;

import java.util.HashMap;
import java.util.List;

import static Constructor.Utils.findPackageName;

public enum Operator {
    ADD_Package {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {

        }
    },
    ADD_Class {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {

        }
    },
    ADD_Method{
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {

        }
    },
    ADD_Attribute{
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {

        }
    },
    DELETE {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {

        }
    },

    //TODO 为每一种refactoring添加commit的codeblocktime信息
    Rename_Package {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            //Rename Package A to B
            //添加新的pkgTime，修改文件位置、签名以及重构类型
            //TODO create new packageTime, classTime, methodTime, attributeTime, update filepath for each codeBlock, and rename signature for package and class
            String[] des = r.getDescription().split(" ");
            String leftP = des[2];
            String  rightP = des[4];
            //package signature 变更
            assert(mappings.containsKey(leftP));
            CodeBlock packageBlock = mappings.get(leftP);
            PackageTime pkgTime = new PackageTime(rightP, commitTime, Operator.valueOf(r.getType().replace(" ", "_")), packageBlock);
            mappings.put(rightP, packageBlock);
            //
            assert r.getLeftSideLocations().size()==r.getRightSideLocations().size();
            for(int i=0; i<r.getLeftSideLocations().size(); i++){
                assert(fileList.containsKey(r.getLeftSideLocations().get(i).getFilePath()));
                assert(fileList.containsKey(r.getRightSideLocations().get(i).getFilePath()));
                DiffFile left = fileList.get(r.getLeftSideLocations().get(i).getFilePath());
                DiffFile right = fileList.get(r.getRightSideLocations().get(i).getFilePath());
                DiffFile renameFile = new DiffFile(FileType.RENAME, right.getPath(), right.getContent(), left.getOldPath(), left.getOldContent());
                fileList.put(right.getPath(), renameFile);
                fileList.put(left.getOldPath(), renameFile);//更新文件名的变更
                pkgTime.getFilePath().add(right.getPath());
                // todo 只修改了package的名字 有待修改classBlock, 或者在其他地方修改

            }
        }
    },
    Move_Package {
        //TODO create new packageTime,
        //文件重命名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            String[] des = r.getDescription().split(" ");
            String leftP = des[2];
            String  rightP = des[4];
            //package signature 变更
            assert(mappings.containsKey(leftP));
            CodeBlock packageBlock = mappings.get(leftP);
            PackageTime pkgTime = new PackageTime(rightP, commitTime, Operator.valueOf(r.getType().replace(" ", "_")), packageBlock);
            mappings.put(rightP, packageBlock);
            //
            assert r.getLeftSideLocations().size()==r.getRightSideLocations().size();
            for(int i=0; i<r.getLeftSideLocations().size(); i++){
                assert(fileList.containsKey(r.getLeftSideLocations().get(i).getFilePath()));
                assert(fileList.containsKey(r.getRightSideLocations().get(i).getFilePath()));
                DiffFile left = fileList.get(r.getLeftSideLocations().get(i).getFilePath());
                DiffFile right = fileList.get(r.getRightSideLocations().get(i).getFilePath());
                DiffFile renameFile = new DiffFile(FileType.RENAME, right.getPath(), right.getContent(), left.getOldPath(), left.getOldContent());
                fileList.put(right.getPath(), renameFile);
                fileList.put(left.getOldPath(), renameFile);//更新文件名的变更
                pkgTime.getFilePath().add(right.getPath());
                // todo 只修改了package的名字 有待修改classBlock, 或者在其他地方修改

            }
        }
    },
    Split_Package {
        //文件重命名
        //TODO create new package, create new packageTime, classTime, methodTime, attributeTime to update the filePath, go through all the file for class level update
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            String[] newPkgNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String oldPkgName = r.getDescription().split(" ")[2];
            assert mappings.containsKey(oldPkgName);
            CodeBlock oldPkgBlock = mappings.get(oldPkgName);
            PackageTime newPkgTime4Old = new PackageTime(oldPkgName, commitTime, Operator.Split_Package, oldPkgBlock);
            oldPkgBlock.setStatus(Status.DERIVED);

            //TODO 先假设两个新包名字不一样，直接新增两个新的包
            assert !newPkgNames[0].equals(newPkgNames[1]);
            for(String pkgName: newPkgNames){
                CodeBlock pkgBlockNew = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Package);
                PackageTime pkgTime = new PackageTime(pkgName, commitTime, Operator.Split_Package, newPkgTime4Old, pkgBlockNew);
                mappings.put(pkgName, pkgBlockNew);
            }
            //遍历左右两边的文件 构建文件rename关系，以及更新packageTime中的文件list
            assert(r.getLeftSideLocations().size()==r.getRightSideLocations().size());
            for(int i=0; i<r.getLeftSideLocations().size(); i++){
                assert(fileList.containsKey(r.getLeftSideLocations().get(i).getFilePath()));
                assert(fileList.containsKey(r.getRightSideLocations().get(i).getFilePath()));
                DiffFile left = fileList.get(r.getLeftSideLocations().get(i).getFilePath());
                DiffFile right = fileList.get(r.getRightSideLocations().get(i).getFilePath());
                DiffFile renameFile = new DiffFile(FileType.RENAME, right.getPath(), right.getContent(), left.getOldPath(), left.getOldContent());
                fileList.put(right.getPath(), renameFile);
                fileList.put(left.getOldPath(), renameFile);//更新文件名的变更
                mappings.get(findPackageName(newPkgNames, right.getPath())).getLastHistory().getFilePath().add(right.getPath());
            }
        }
    },
    Merge_Package {
        //文件重命名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            String[] oldPkgNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String[] desc = r.getDescription().split(" ");
            String newPkgName = desc[desc.length-1];
            CodeBlock newPkgBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Package);
            PackageTime newPkgTime = new PackageTime(newPkgName, commitTime, Operator.Merge_Package, newPkgBlock);
            mappings.put(newPkgName, newPkgBlock);

            for(String pkgName: oldPkgNames){
                assert mappings.containsKey(pkgName);
                CodeBlock oldBlock = mappings.get(pkgName);
                PackageTime newPkgTime4Old = new PackageTime(pkgName, commitTime, Operator.Merge_Package, newPkgTime, oldBlock);
                oldBlock.setStatus(Status.DERIVED);
            }

            assert(r.getLeftSideLocations().size()==r.getRightSideLocations().size());
            for(int i=0; i<r.getLeftSideLocations().size(); i++){
                assert(fileList.containsKey(r.getLeftSideLocations().get(i).getFilePath()));
                assert(fileList.containsKey(r.getRightSideLocations().get(i).getFilePath()));
                DiffFile left = fileList.get(r.getLeftSideLocations().get(i).getFilePath());
                DiffFile right = fileList.get(r.getRightSideLocations().get(i).getFilePath());
                DiffFile renameFile = new DiffFile(FileType.RENAME, right.getPath(), right.getContent(), left.getOldPath(), left.getOldContent());
                fileList.put(right.getPath(), renameFile);
                fileList.put(left.getOldPath(), renameFile);//更新文件名的变更
                newPkgTime.getFilePath().add(right.getPath());
            }
        }
    },
    Change_Type_Declaration_Kind {//interface class等更改 一般不会影响codeblock
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Collapse_Hierarchy {//包之后 较为复杂的 将一个类中的内容打散 重新生成新的类 等
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Extract_Superclass {
        //add a new class, the last filepath on the rightfilepath is the new superclass
        //TODO can be used to detect file rename
        //文件重命名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Extract_Interface {
        //TODO ***
        //文件重命名 这个有点特殊
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Extract_Class {
        //将原来代码中的一些方法抽出来，放到新建的类中。新建一个类，将一些方法从旧的类中移到新的类中
        //derive modify
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
//            String[] description = refactor.getDescription().split(" ");
//            String newClass = description[2];
//            String oldClass = description[4];
//            List<SideLocation> left = refactor.getLeftSideLocations();
//            List<SideLocation> right = refactor.getRightSideLocations();
//            //TODO 左边第一个是原始文件，第二个是原始文件，右边第二个是新的文件，剩下的是移动的方法
////            TODO move methods from A to B


            System.out.println(r.getType());
        }
    },
    Extract_Subclass {
        //derive class 跟extract class几乎差不多

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Merge_Class {
        //derive
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_Class {
        //TODO 注意内部类的移动情况 63f3a7ebe4915e51acb8b2a8b0b234e5618239ba 根据left right文件名确定文件的对应关系，然后添加新的classBlockTime 进行修改，以及更新内部方法和属性的文件位置，以及签名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Rename_Class {
        // 创建文件对应关系，然后对应修改classBlockTime，packageBlockTime的内容，然后在最后统一更新method和attribute的内容。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_and_Rename_Class {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Extract_Method{
        //add method 从现有方法的代码中抽取部分生成新的方法，methodB derived from methodA， 需要在遍历阶段更新methodBlockTime的信息
        //有可能是从很多方法中提取出的新的方法，所以在存储新的代码块之前需要判断是否已经存在，如果已经存在，就在derived from中添加新的方法
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime){
            System.out.println(r.getType());
        }
    },
    Inline_Method{
        //与extract method相反，delete 方法，将方法的内容合并到已有的方法中
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime){
            System.out.println(r.getType());
        }
    },
    Pull_Up_Method {
        //move method from one class to super class,将几个子类中的方法移到超类中，跨文件，涉及方法的移动，还可能修改名字，但是不影响文件数目
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Push_Down_Method {
        //将父类中的方法 移到子类中去，一般会在不同的文件之间进行移动，甚至还有rename
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Extract_and_Move_Method {
        //可能从多个方法中提取出一个新的方法， 涉及新建一个methodBlock，有多个derived from，并且移到了一个新的类中，跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_and_Inline_Method {
        //跨文件，移动，移动旧的方法，并拆散方法到已有的方法中。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_and_Rename_Method {//跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_Method {//跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime){
            System.out.println(r.getType());
        }
    },
    Change_Return_Type { //trival 只需要修改返回类型 一般不跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Rename_Method {//trival 一般不跨文件 只需要修改方法名字
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime){
            System.out.println(r.getType());
        }
    },
    Parameterize_Variable {//方法名级别
        //把方法中的一个变量 变为方法的参数 不跨文件，仅需要修改方法的名字 trivial
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Merge_Parameter {//把一个方法的参数进行合并，但是可能会有移动
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Split_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Change_Parameter_Type {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Add_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {

//            AttributeTime attributeTime = new AttributeTime();
//            attributeTime.setTime(currentTime);

            System.out.println(r.getType());
        }
    },
    Remove_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Reorder_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Parameterize_Attribute {//把一个attribute变成一个参数，同时修改属性和方法的参数 一般不跨项目
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    //    Add_Thrown_Exception_Type {
//        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
//            System.out.println(r.getType());
//        }
//    },
//    Remove_Thrown_Exception_Type {
//        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
//            System.out.println(r.getType());
//        }
//    },
//    Change_Thrown_Exception_Type {
//        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
//            System.out.println(r.getType());
//        }
//    },
    Pull_Up_Attribute {//把子类中的属性 移到父类中 跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Push_Down_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Rename_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Merge_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Split_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Change_Attribute_Type {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Extract_Attribute {//涉及增加新的attribute
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Encapsulate_Attribute {
        //Attribute encapsulation is useful when you have an attribute that is affected by several different methods,
        // each of which needs that attribute to be in a known state. To prevent programmers from changing the attribute
        // in the 4GL code, you can make the attribute private so that programmers can only access it from the object's methods.
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },//TODO not_sure_if_neccessarry
    Inline_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_and_Rename_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    //    Replace_Attribute_(with_Attribute) {
//        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
//            System.out.println(r.getType());
//        }
//    },//TODO 暂时没找到例子
    Replace_Attribute_with_Variable {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Replace_Anonymous_with_Lambda {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },//TODO 暂时没找到
    ;

    public abstract void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime);

}
