package Constructor.Enums;


import Constructor.Utils;
import Model.*;
import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.SideLocation;
import Project.Utils.DiffFile;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.util.MutableInteger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static Constructor.Utils.codeElement2Name;
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
                assert left.getType().equals(FileType.DELETE);
                assert right.getType().equals(FileType.ADD);
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
            PackageTime pkgTime = new PackageTime(rightP, commitTime, Operator.Move_Package, packageBlock);
            mappings.put(rightP, packageBlock);
            //
            assert r.getLeftSideLocations().size()==r.getRightSideLocations().size();
            for(int i=0; i<r.getLeftSideLocations().size(); i++){
                assert(fileList.containsKey(r.getLeftSideLocations().get(i).getFilePath()));
                assert(fileList.containsKey(r.getRightSideLocations().get(i).getFilePath()));
                DiffFile left = fileList.get(r.getLeftSideLocations().get(i).getFilePath());
                DiffFile right = fileList.get(r.getRightSideLocations().get(i).getFilePath());
                assert left.getType().equals(FileType.DELETE);
                assert right.getType().equals(FileType.ADD);
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
                assert left.getType().equals(FileType.DELETE);
                assert right.getType().equals(FileType.ADD);
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
                newPkgTime.getFilePath().add(right.getPath());
                if(left.equals(right))
                    return;
                assert left.getType().equals(FileType.DELETE);
                assert right.getType().equals(FileType.ADD);
                DiffFile renameFile = new DiffFile(FileType.RENAME, right.getPath(), right.getContent(), left.getOldPath(), left.getOldContent());
                fileList.put(right.getPath(), renameFile);
                fileList.put(left.getOldPath(), renameFile);//更新文件名的变更

            }
        }
    },
    Change_Type_Declaration_Kind {//interface class等更改 一般不会影响codeblock
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            assert r.getDescription().split(" ").length==10;
            assert r.getLeftSideLocations().size()==r.getRightSideLocations().size();
            assert r.getLeftSideLocations().size()==1;
            assert r.getLeftSideLocations().get(0).getFilePath().equals(r.getRightSideLocations().get(0).getFilePath());
//            System.out.println(r.getType());
        }
    },
    Collapse_Hierarchy {//较为复杂的 将一个具体的类的内容移到接口类中进行实现
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            String oldName = r.getDescription().split(" ")[2];
            String newName = r.getDescription().split(" ")[4];
            assert mappings.containsKey(oldName);
            assert mappings.containsKey(newName);
            ClassTime oldClassNew = new ClassTime((ClassTime) mappings.get(oldName).getLastHistory(), commitTime, Operator.Collapse_Hierarchy);
            ClassTime newClassNew = new ClassTime((ClassTime) mappings.get(newName).getLastHistory(), commitTime, Operator.Collapse_Hierarchy);
            oldClassNew.getDerivee().add(newClassNew);
            newClassNew.getDeriver().add(oldClassNew);
            assert r.getLeftSideLocations().size()==r.getRightSideLocations().size();
            assert r.getLeftSideLocations().size()==1;
            DiffFile oldFile = fileList.get(r.getLeftSideLocations().get(0).getFilePath());
            DiffFile newFile = fileList.get(r.getRightSideLocations().get(0).getFilePath());
            oldFile.getDeriveTo().add(r.getRightSideLocations().get(0).getFilePath());
            newFile.getDeriveFrom().add(r.getLeftSideLocations().get(0).getFilePath());//更新文件的继承关系
        }
    },
    Extract_Superclass {
        //add a new class, the last filepath on the rightfilepath is the new superclass
        //derive
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            String[] oldNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String newName = r.getDescription().split(" ")[2];
            CodeBlock codeBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Class);
            ClassTime classTime = new ClassTime(newName, commitTime, Operator.Extract_Superclass, codeBlock);
            mappings.put(newName, codeBlock);

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            DiffFile newFile = fileList.get(right.get(right.size()-1).getFilePath());
            assert left.size() == right.size()+1;
            assert left.size() == oldNames.length;
            for(int i=0; i<left.size()-1; i++){
                DiffFile leftFile = fileList.get(left.get(i).getFilePath());
                DiffFile rightFile = fileList.get(right.get(i).getFilePath());
                leftFile.getDeriveTo().add(right.get(i).getFilePath());
                rightFile.getDeriveFrom().add(left.get(i).getFilePath());
                newFile.getDeriveFrom().add(left.get(i).getFilePath());//分别更新左右文件的对应关系，并将左边文件添加到新文件的deriveFrom list中
            }
        }
    },
    Extract_Interface {
        //TODO ***
        //文件重命名 这个有点特殊
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            String newName = r.getDescription().split(" ")[2];
            String[] oldName = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            assert !mappings.containsKey(newName);
            CodeBlock codeBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Class);
            ClassTime classTime = new ClassTime(newName, commitTime, Operator.Extract_Interface, codeBlock);
            mappings.put(newName, codeBlock);

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            String rightFileName = right.get(right.size()-1).getFilePath();
            assert fileList.containsKey(rightFileName);
            DiffFile rightFile = fileList.get(rightFileName);
            assert left.size()==right.size()-1;
            assert left.size()==oldName.length;
            if(right.get(right.size()-2).getFilePath().contains(newName.substring(newName.lastIndexOf(".")+1))){
                //不是内部类
                for(int i=0; i<left.size(); i++){
                    assert mappings.containsKey(oldName[i]);
                    ClassTime originalClassTimeOld = (ClassTime) mappings.get(oldName[i]).getLastHistory();
                    ClassTime originalClassTimeNew = new ClassTime(originalClassTimeOld, commitTime, Operator.Extract_Interface);
                    originalClassTimeNew.getDerivee().add(classTime);
                    classTime.getDeriver().add(originalClassTimeNew);//更新deriver和derivee信息
                    //更新文件继承信息
                    assert fileList.containsKey(left.get(i).getFilePath());
                    DiffFile leftFile = fileList.get(left.get(i).getFilePath());
                    rightFile.getDeriveFrom().add(leftFile.getOldPath());
                    leftFile.getDeriveTo().add(rightFile.getPath());//derive文件 分别添加到deriveFrom 和deriveTo中
                }
            }else{
                System.out.println("Inner class:");
            }
        }
    },
    Extract_Class {
        //将原来代码中的一些方法抽出来，放到新建的类中。新建一个类，将一些方法从旧的类中移到新的类中
        //derive modify
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            assert r.getDescription().split(" ").length==6;
            String newClassName = r.getDescription().split(" ")[2];
            String oldClassName = r.getDescription().split(" ")[5];

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size()==1;
            if(left.get(0).getFilePath().contains(oldClassName.substring(oldClassName.lastIndexOf(".")+1))&&right.get(1).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".")+1))){
                //如果不是内部类
                CodeBlock codeBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Class);
                ClassTime classTime = new ClassTime(newClassName, commitTime, Operator.Extract_Class, codeBlock);
                mappings.put(newClassName, codeBlock);

                assert mappings.containsKey(oldClassName);
                ClassTime oldClassTime = (ClassTime) mappings.get(oldClassName).getLastHistory();
                ClassTime oldClassTimeNew = new ClassTime(oldClassTime, commitTime, Operator.Extract_Class);
                oldClassTimeNew.getDerivee().add(classTime);
                classTime.getDeriver().add(oldClassTimeNew);//添加deriver关系
                //todo 把right中剩下的所有的方法移到newClass中
                DiffFile leftFile = fileList.get(left.get(0).getFilePath());
                DiffFile rightFile = fileList.get(right.get(1).getFilePath());
                rightFile.getDeriveFrom().add(left.get(0).getFilePath());
                leftFile.getDeriveTo().add(right.get(1).getFilePath());
                for(int i=2; i<right.size(); i++){
//                    assert mappings.containsKey(oldClassName.substring(oldClassName.lastIndexOf(".")+1)+":")

                }
            }else{
                System.out.println("Inner class: extract class");
            }
//            //TODO 左边第一个是原始类，右边第一个是原始类，第二个是新的类，剩下的是移动的方法
////            TODO move methods from A to B
        }
    },
    Extract_Subclass {
        //derive class 跟extract class几乎差不多
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            assert r.getDescription().split(" ").length==6;
            String newClassName = r.getDescription().split(" ")[2];
            String oldClassName = r.getDescription().split(" ")[5];

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size()==1;
            if(left.get(0).getFilePath().contains(oldClassName.substring(oldClassName.lastIndexOf(".")+1))&&right.get(1).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".")+1))){
                //如果不是内部类
                CodeBlock codeBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Class);
                ClassTime classTime = new ClassTime(newClassName, commitTime, Operator.Extract_Class, codeBlock);
                mappings.put(newClassName, codeBlock);

                assert mappings.containsKey(oldClassName);
                ClassTime oldClassTime = (ClassTime) mappings.get(oldClassName).getLastHistory();
                ClassTime oldClassTimeNew = new ClassTime(oldClassTime, commitTime, Operator.Extract_Class);
                oldClassTimeNew.getDerivee().add(classTime);
                classTime.getDeriver().add(oldClassTimeNew);//添加deriver关系
                //todo 把right中剩下的所有的方法移到newClass中
                DiffFile leftFile = fileList.get(left.get(0).getFilePath());
                DiffFile rightFile = fileList.get(right.get(1).getFilePath());
                rightFile.getDeriveFrom().add(left.get(0).getFilePath());
                leftFile.getDeriveTo().add(right.get(1).getFilePath());
                for(int i=2; i<right.size(); i++){
//                    assert mappings.containsKey(oldClassName.substring(oldClassName.lastIndexOf(".")+1)+":")

                }
            }else{
                System.out.println("Inner class: extract subclass");
            }
        }
    },
    Merge_Class {
        //derive
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            String[] oldClasses = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String[] desc = r.getDescription().split(" ");
            String newClassName = desc[desc.length-1];
            CodeBlock newClassBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Class);
            ClassTime newClassTime = new ClassTime(newClassName, commitTime, Operator.Merge_Class, newClassBlock);
            mappings.put(newClassName, newClassBlock);

            assert r.getRightSideLocations().size()==1;
            if(!r.getRightSideLocations().get(0).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".")+1))){
                //新增的类是内部类 todo
                System.out.println("Inner class: ");
                return;
            }

            String pkgName = newClassName.substring(0, newClassName.lastIndexOf("."));
            assert mappings.containsKey(pkgName);//如果不存在包的话 就需要新建
            newClassTime.setParentCodeBlock(mappings.get(pkgName));
            PackageTime newPkgTime = new PackageTime(pkgName, commitTime, Operator.Merge_Class, mappings.get(pkgName));
            newPkgTime.getClasses().add(newClassBlock);

            List<SideLocation> left = r.getLeftSideLocations();
            DiffFile rightFile = fileList.get(r.getRightSideLocations().get(0).getFilePath());
            assert oldClasses.length == left.size();
            for(int i=0; i<left.size(); i++){
                String oldName = oldClasses[i];
                String oldFilePath = left.get(i).getFilePath();
                if(!oldFilePath.contains(oldName.substring(oldName.lastIndexOf(".")+1))){
                    System.out.println("inner class: to deal with");
                    return;
                }
                assert mappings.containsKey(oldName);
                CodeBlock oldBlock = mappings.get(oldName);
                ClassTime oldTimeOld = (ClassTime) oldBlock.getLastHistory();
                ClassTime oldTimeNew = new ClassTime(oldTimeOld, commitTime, Operator.Merge_Class);
                oldTimeNew.getDerivee().add(newClassTime);
                newClassTime.getDeriver().add(oldTimeNew);
                newClassTime.getClasses().addAll(oldTimeOld.getClasses());
                newClassTime.getMethods().addAll(oldTimeOld.getMethods());
                newClassTime.getAttributes().addAll(oldTimeOld.getAttributes());
                //把旧类中所包含的所有的类 方法 属性 全都移到新的类中

                assert(fileList.containsKey(left.get(i).getFilePath()));

                DiffFile leftFile = fileList.get(left.get(i).getFilePath());
                rightFile.getDeriveFrom().add(leftFile.getOldPath());//把前边的几个类的名字放到deriveFrom list中 //derive file todo

            }

        }
    },
    Move_Class {
        //TODO 注意内部类的移动情况 63f3a7ebe4915e51acb8b2a8b0b234e5618239ba 根据left right文件名确定文件的对应关系，然后添加新的classBlockTime 进行修改，以及更新内部方法和属性的文件位置，以及签名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            //todo 需要注意如果是内部类的重命名和移动 7c65d01596100a0962d7e9e0b4a55dce461ff54e
            //todo 内部类的内部类 28ef8c9fc78433add3ee0a08f9f38569cd130958
            String[] desc = r.getDescription().split(" ");
            assert desc.length==6;
            String oldClass = desc[2];
            String newClass = desc[5];
            assert mappings.containsKey(oldClass);
            CodeBlock oldClassBlock = mappings.get(oldClass);


            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size()==right.size();
            assert left.size()==1;

            if(left.get(0).getFilePath().contains(oldClass.substring(oldClass.lastIndexOf(".")+1)) && right.get(0).getFilePath().contains(newClass.substring(newClass.lastIndexOf(".")+1))){
                //如果类名在文件路径中出现，就表明不是内部类
                assert !(left.get(0).getFilePath().equals(right.get(0).getFilePath()));
                //todo 需要将旧的time中的parents关系复制过来，或者在遍历的时候获得
//                PackageTime pkgTimeOld =(PackageTime) oldClassBlock.getLastHistory().getParentCodeBlock().getLastHistory();//原父亲的codeBlock
//                PackageTime pkgTimeNew = new PackageTime(pkgTimeOld, commitTime, Operator.Move_Class);
//                pkgTimeNew.getClasses().remove(oldClassBlock);
//                pkgTimeNew.getFilePath().remove(left.get(0).getFilePath());//从旧的父节点中删除

                String newParent = newClass.substring(0, newClass.lastIndexOf("."));//新的父节点
                if(!mappings.containsKey(newParent)){//如果不存在包 就新建
                    CodeBlock pkgBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Package);
                    mappings.put(newParent, pkgBlock);
                    PackageTime pkgTime = new PackageTime(newParent, commitTime, Operator.ADD_Package, pkgBlock);
                }
                assert mappings.containsKey(newParent);
                PackageTime newPkgTime = new PackageTime((PackageTime) mappings.get(newParent).getLastHistory(), commitTime, Operator.Move_Class);
                newPkgTime.getClasses().add(oldClassBlock);
                newPkgTime.getFilePath().add(right.get(0).getFilePath());//更新 新的父节点

                ClassTime classTime = new ClassTime(newClass, commitTime, Operator.Move_Class, oldClassBlock, newPkgTime.getOwner());
                mappings.put(newClass, mappings.get(oldClass));

                DiffFile leftFile = fileList.get(left.get(0).getFilePath());
                DiffFile rightFile = fileList.get(right.get(0).getFilePath());
                if(leftFile.equals(rightFile)){
                    return;
                }
                assert leftFile.getType().equals(FileType.DELETE);
                assert rightFile.getType().equals(FileType.ADD);
                DiffFile renameFile = new DiffFile(FileType.RENAME, rightFile.getPath(), rightFile.getContent(), leftFile.getOldPath(), leftFile.getOldContent());
                fileList.put(rightFile.getPath(), renameFile);
                fileList.put(leftFile.getOldPath(), renameFile);
                //todo 暂时没改变method的签名 留到最后遍历文件时处理
            }else{
                //todo 内部类的处理，不是文件的重命名，但是要对比两个新旧文件的内容 filePairs.put(leftFile, rightFile)
                System.out.println("Inner class remains to be processed...");
            }
        }
    },
    Rename_Class {
        // 创建文件对应关系，然后对应修改classBlockTime，packageBlockTime的内容，然后在最后统一更新method和attribute的内容。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            //todo 需要注意如果是内部类的重命名和移动 7c65d01596100a0962d7e9e0b4a55dce461ff54e
            String[] desc = r.getDescription().split(" ");
            assert desc.length==6;
            String oldClass = desc[2];
            String newClass = desc[5];

            if(r.getLeftSideLocations().get(0).getFilePath().contains(oldClass.substring(oldClass.lastIndexOf(".")+1)) && r.getRightSideLocations().get(0).getFilePath().contains(newClass.substring(newClass.lastIndexOf(".")+1))){
                assert mappings.containsKey(oldClass);
                CodeBlock oldClassBlosk = mappings.get(oldClass);
                ClassTime classTime = new ClassTime(newClass, commitTime, Operator.Rename_Class, oldClassBlosk, oldClassBlosk.getLastHistory().getParentCodeBlock());
                mappings.put(newClass, mappings.get(oldClass));

                List<SideLocation> left = r.getLeftSideLocations();
                List<SideLocation> right = r.getRightSideLocations();
                assert left.size()==right.size();
                assert left.size()==1;
//                Collections.replaceAll(classTime.getParentCodeBlock().getLastHistory().getFilePath(), left.get(0).getFilePath(), right.get(0).getFilePath());//直接替换pkgBlock的文件list的内容


                //如果类名在文件路径中出现，就表明不是内部类
                assert !(left.get(0).getFilePath().equals(right.get(0).getFilePath()));
                DiffFile leftFile = fileList.get(left.get(0).getFilePath());
                DiffFile rightFile = fileList.get(right.get(0).getFilePath());
                assert leftFile.getType().equals(FileType.DELETE);
                assert rightFile.getType().equals(FileType.ADD);
                DiffFile renameFile = new DiffFile(FileType.RENAME, rightFile.getPath(), rightFile.getContent(), leftFile.getOldPath(), leftFile.getOldContent());
                fileList.put(rightFile.getPath(), renameFile);
                fileList.put(leftFile.getOldPath(), renameFile);
                //todo 暂时没改变method的签名 留到最后遍历文件时处理
            }else{
                //todo 内部类的处理，不是文件的重命名，但是要对比两个新旧文件的内容 filePairs.put(leftFile, rightFile)
                System.out.println("Inner class remains to be processed...");
            }


        }
    },
    Move_And_Rename_Class {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            //todo 需要注意如果是内部类的重命名和移动 13a0d1228e15b2386635f4a7df1d814e7a3fe145
            String[] desc = r.getDescription().split(" ");
            assert desc.length==10;
            String oldClass = desc[4];
            String newClass = desc[9];
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size()==right.size();
            assert left.size()==1;

            assert mappings.containsKey(oldClass);
            CodeBlock oldClassBlock = mappings.get(oldClass);

            if(left.get(0).getFilePath().contains(oldClass.substring(oldClass.lastIndexOf(".")+1)) && right.get(0).getFilePath().contains(newClass.substring(newClass.lastIndexOf(".")+1))){
                //如果类名在文件路径中出现，就表明不是内部类
                assert !(left.get(0).getFilePath().equals(right.get(0).getFilePath()));
                PackageTime pkgTimeOld =(PackageTime) oldClassBlock.getLastHistory().getParentCodeBlock().getLastHistory();//原父亲的codeBlock
                PackageTime pkgTimeNew = new PackageTime(pkgTimeOld, commitTime, Operator.Move_And_Rename_Class);
                pkgTimeNew.getClasses().remove(oldClassBlock);
                pkgTimeNew.getFilePath().remove(left.get(0).getFilePath());//从旧的父节点中删除

                String newParent = newClass.substring(0, newClass.lastIndexOf("."));//新的父节点
                if(!mappings.containsKey(newParent)){//如果不存在包 就新建
                    CodeBlock pkgBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Package);
                    mappings.put(newParent, pkgBlock);
                    PackageTime pkgTime = new PackageTime(newParent, commitTime, Operator.ADD_Package, pkgBlock);
                }
                assert mappings.containsKey(newParent);
                PackageTime newPkgTime = new PackageTime((PackageTime) mappings.get(newParent).getLastHistory(), commitTime, Operator.Move_And_Rename_Class);
                newPkgTime.getClasses().add(oldClassBlock);
                newPkgTime.getFilePath().add(right.get(0).getFilePath());//更新 新的父节点

                ClassTime classTime = new ClassTime(newClass, commitTime, Operator.Move_And_Rename_Class, oldClassBlock, newPkgTime.getOwner());
                mappings.put(newClass, mappings.get(oldClass));

                DiffFile leftFile = fileList.get(left.get(0).getFilePath());
                DiffFile rightFile = fileList.get(right.get(0).getFilePath());
                if(leftFile.equals(rightFile)){
                    return;
                }
                assert leftFile.getType().equals(FileType.DELETE);
                assert rightFile.getType().equals(FileType.ADD);
                DiffFile renameFile = new DiffFile(FileType.RENAME, rightFile.getPath(), rightFile.getContent(), leftFile.getOldPath(), leftFile.getOldContent());
                fileList.put(rightFile.getPath(), renameFile);
                fileList.put(leftFile.getOldPath(), renameFile);
                //todo 暂时没改变method的签名 留到最后遍历文件时处理
            }else{
                //todo 内部类的处理，不是文件的重命名，但是要对比两个新旧文件的内容 filePairs.put(leftFile, rightFile)
                System.out.println("Inner class remains to be processed...");
            }
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
    Extract_And_Move_Method {
        //可能从多个方法中提取出一个新的方法， 涉及新建一个methodBlock，有多个derived from，并且移到了一个新的类中，跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_And_Inline_Method {
        //跨文件，移动，移动旧的方法，并拆散方法到已有的方法中。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Move_And_Rename_Method {//跨文件
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
            String[] des = r.getDescription().split(" ");
            String classSig = des[des.length-1];
            String className = classSig.substring(classSig.lastIndexOf(".")+1);
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            HashMap<String, String> oldMethod = Utils.codeElement2Name(left.get(0).getCodeElement());
            HashMap<String, String> newMethod = Utils.codeElement2Name(right.get(0).getCodeElement());
            String oldName = className+":"+oldMethod.get("MN");
            String newName = className+":"+newMethod.get("MN");
            assert mappings.containsKey(oldName);
            CodeBlock codeBlock = mappings.get(oldName);
            MethodTime methodTimeOld = (MethodTime) codeBlock.getLastHistory();
            MethodTime methodTimeNew = new MethodTime(methodTimeOld, commitTime, Operator.Rename_Method);
            methodTimeNew.setSignature(newName);
            methodTimeNew.getFilePath().add(right.get(0).getFilePath());
            methodTimeNew.setParameters(newMethod.get("PA"));
            codeBlock.addHistory(methodTimeNew);
            mappings.put(newName, codeBlock);
//            methodTimeNew.setParameterType(newMethod.get("PT")); //parameterType needs to be updated
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
    Move_And_Rename_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    //    Replace_Attribute_(With_Attribute) {
//        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
//            System.out.println(r.getType());
//        }
//    },//TODO 暂时没找到例子
    Replace_Attribute_With_Variable {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },
    Replace_Anonymous_With_Lambda {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime) {
            System.out.println(r.getType());
        }
    },//TODO 暂时没找到
    ;

    public abstract void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime);

}
