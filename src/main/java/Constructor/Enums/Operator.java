package Constructor.Enums;


import Model.*;
import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.SideLocation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public enum Operator {
    Add_Package {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //create new codeBlock, update mapping, commitCodeChange
            CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
            mappings.put(name, codeBlock);//更新mapping， codeblocks， commitcodechange
            PackageTime packageTime = new PackageTime(name, commitTime, Operator.Add_Package, codeBlock);
            codeBlocks.add(codeBlock);
        }
    },
    Remove_Package {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },
    Add_Class {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //create codeblock, create codeblocktime, mapping update
        }
    },
    Remove_Class {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //use mapping to find codeblock, create codeblocktime,
        }
    },
    Add_Method {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },
    Remove_Method {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },
    Add_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },
    Remove_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },
    //TODO 为每一种refactoring添加commit的codeblocktime信息
    Rename_Package {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //Rename Package A to B
            //添加新的pkgTime，修改文件位置、签名以及重构类型
            //TODO create new packageTime, classTime, methodTime, attributeTime, update filepath for each codeBlock, and rename signature for package and class
            String[] des = r.getDescription().split(" ");
            String leftP = des[2];
            String rightP = des[4];
            //package signature 变更
            assert (mappings.containsKey(leftP));
            CodeBlock packageBlock = mappings.get(leftP);
            PackageTime pkgTime = new PackageTime(rightP, commitTime, Operator.valueOf(r.getType().replace(" ", "_")), packageBlock);
            mappings.put(rightP, packageBlock);
            System.out.println(r.getType());
        }
    },
    Move_Package {
        //TODO create new packageTime,
        //文件重命名
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //use mappings to find codeblock, create packagetime, update mapping package, its classes, etc.
//            String[] des = r.getDescription().split(" ");
//            String leftP = des[2];
//            String rightP = des[4];
//            //package signature 变更
//            assert (mappings.containsKey(leftP));
//            CodeBlock packageBlock = mappings.get(leftP);
//            PackageTime pkgTime = new PackageTime(rightP, commitTime, Operator.Move_Package, packageBlock);
//            if (pkgTime.getOwner() != null) {
//                //create packagetimes for owner
//            }
//            mappings.put(rightP, packageBlock);
//            //
//            for (CodeBlock classBlock : pkgTime.getClasses()) {
//                classBlock.updateMapping(leftP, rightP);
//            }
            System.out.println(r.getType());
        }
    },
    Split_Package {
        //文件重命名
        //TODO create new package, create new packageTime, classTime, methodTime, attributeTime to update the filePath, go through all the file for class level update
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String[] newPkgNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
//            String oldPkgName = r.getDescription().split(" ")[2];
//            assert mappings.containsKey(oldPkgName);
//            CodeBlock oldPkgBlock = mappings.get(oldPkgName);
//            PackageTime newPkgTime4Old = new PackageTime(oldPkgName, commitTime, Operator.Split_Package, oldPkgBlock);
//
//            //TODO 先假设两个新包名字不一样，直接新增两个新的包
//            assert !newPkgNames[0].equals(newPkgNames[1]);
//            for (String pkgName : newPkgNames) {
//                CodeBlock pkgBlockNew = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
//                PackageTime pkgTime = new PackageTime(pkgName, commitTime, Operator.Split_Package, newPkgTime4Old, pkgBlockNew);
//                mappings.put(pkgName, pkgBlockNew);
//            }
            System.out.println(r.getType());
        }
    },
    Merge_Package {
        //文件重命名
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String[] oldPkgNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
//            String[] desc = r.getDescription().split(" ");
//            String newPkgName = desc[desc.length - 1];
//            CodeBlock newPkgBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
//            PackageTime newPkgTime = new PackageTime(newPkgName, commitTime, Operator.Merge_Package, newPkgBlock);
//            mappings.put(newPkgName, newPkgBlock);
//
//            for (String pkgName : oldPkgNames) {
//                assert mappings.containsKey(pkgName);
//                CodeBlock oldBlock = mappings.get(pkgName);
//                PackageTime newPkgTime4Old = new PackageTime(pkgName, commitTime, Operator.Merge_Package, newPkgTime, oldBlock);
//            }
            System.out.println(r.getType());

        }
    },
    Change_Type_Declaration_Kind {//interface class, if the name should change, just update the name, no other changes
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            assert r.getLeftSideLocations().size() == r.getRightSideLocations().size();
            assert r.getLeftSideLocations().size() == 1;
            String nameOld = r.getLeftSideLocations().get(0).getCodeElement();
            String nameNew = r.getRightSideLocations().get(0).getCodeElement();
            if (nameOld.equals(nameNew)) {
                return;//nothing changes, return
            }
            //change name
            assert mappings.containsKey(nameOld);
            CodeBlock classBlock = mappings.get(nameOld);
            ClassTime classTime = (ClassTime) classBlock.getLastHistory().clone();
            classTime.setName(nameNew);
            mappings.put(nameNew, classBlock);
            classTime.setTime(commitTime);
            classTime.setRefactorType(Operator.Change_Type_Declaration_Kind);
            commitTime.addCodeChange(classTime);
            classBlock.addHistory(classTime);

//            System.out.println(r.getType());
        }
    },
    Collapse_Hierarchy {//较为复杂的 将一个具体的类的内容移到接口类中进行实现
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String oldName = r.getDescription().split(" ")[2];
//            String newName = r.getDescription().split(" ")[4];
//            assert mappings.containsKey(oldName);
//            assert mappings.containsKey(newName);
//            ClassTime oldClassNew = new ClassTime((ClassTime) mappings.get(oldName).getLastHistory(), commitTime, Operator.Collapse_Hierarchy);
//            ClassTime newClassNew = new ClassTime((ClassTime) mappings.get(newName).getLastHistory(), commitTime, Operator.Collapse_Hierarchy);
//            oldClassNew.getDerivee().add(newClassNew);
//            newClassNew.getDeriver().add(oldClassNew);
//            assert r.getLeftSideLocations().size() == r.getRightSideLocations().size();
//            assert r.getLeftSideLocations().size() == 1;
            System.out.println(r.getType());
        }
    },
    Extract_Superclass {
        //add a new class, the last filepath on the rightfilepath is the new superclass
        //derive
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String[] oldNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
//            String newName = r.getDescription().split(" ")[2];
//            CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
//            ClassTime classTime = new ClassTime(newName, commitTime, Operator.Extract_Superclass, codeBlock);
//            mappings.put(newName, codeBlock);
//
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert left.size() == right.size() + 1;
//            assert left.size() == oldNames.length;
            System.out.println(r.getType());
        }
    },
    Extract_Interface {
        //TODO ***
        //todo 文件重命名 这个有点特殊
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //左边、右边前几个分别是deriver类的声明，右边最后一个是新的interface
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();

            String className = right.get(right.size() - 1).getCodeElement();
            assert left.size() == right.size() - 1;
            assert !mappings.containsKey(className);
            //create new className
            CodeBlock classBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
            ClassTime classTime = new ClassTime(className, commitTime, Operator.Extract_Interface, classBlock, mappings.get(className.substring(0, className.lastIndexOf("."))));
            mappings.put(className, classBlock);
            codeBlocks.add(classBlock);

            //derive and deriver relation
            for (int i = 0; i < left.size(); i++) {
                String oldName = left.get(i).getCodeElement();
                String newName = right.get(i).getCodeElement();
                assert mappings.containsKey(oldName);
                CodeBlock originalClassBlock = mappings.get(oldName);
                if (!oldName.equals(newName)) {
                    mappings.put(newName, originalClassBlock);
                }
                ClassTime originalClassTime = (ClassTime) originalClassBlock.getLastHistory().clone();
                originalClassTime.setName(newName);
                originalClassTime.setTime(commitTime);
                originalClassTime.setRefactorType(Operator.Extract_Interface);
                originalClassTime.getDerivee().add(classTime);
                classTime.getDeriver().add(originalClassTime);
                originalClassBlock.addHistory(originalClassTime);
                commitTime.addCodeChange(originalClassTime);
            }
//            System.out.println(r.getType());
        }
    },
    Extract_Class { // TODO 移动的方法 也会在后边的move method方法中出现 所以需要注意
        @Override
        //将原来代码中的一些方法抽出来，放到新建的类中。新建一个类，将一些方法从旧的类中移到新的类中
        //derive modify
        //create new classBlock, move methods from old class to new class, derive relation
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            String className = right.get(1).getCodeElement();
//            String oldClassNameOld = left.get(0).getCodeElement();
//            String oldClassNameNew = right.get(0).getCodeElement();
//            //
////            System.out.println(commitTime.getCommitID());
////            System.out.println(oldClassNameOld+" : "+oldClassNameNew);
//            assert oldClassNameNew.equals(oldClassNameOld);//如果新旧名字不一样 就需要更新名字
//            assert mappings.containsKey(oldClassNameOld);
//
//            //add new classBlock
//            CodeBlock classBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
//            ClassTime classTime = new ClassTime(className, commitTime, Operator.Extract_Class, classBlock, mappings.get(className.substring(0, className.lastIndexOf("."))));
//            mappings.put(className, classBlock);
//            codeBlocks.add(classBlock);
//            commitTime.addCodeChange(classTime);
//
//            //create oldClassTime
//            CodeBlock oldClassBlock = mappings.get(oldClassNameOld);
//            ClassTime oldClassTime = (ClassTime) oldClassBlock.getLastHistory().clone();
//            oldClassTime.setName(oldClassNameNew);
//            oldClassTime.setTime(commitTime);
//            oldClassTime.setRefactorType(Extract_Class);
//            oldClassTime.getDerivee().add(classTime);
//            classTime.getDeriver().add(oldClassTime);
//            oldClassBlock.addHistory(oldClassTime);
//            commitTime.addCodeChange(oldClassTime);
//
//            // move from oldClassTime to newClassTime
//            List<SideLocation> extractedMethod = r.rightFilter("extracted method declaration");
//            for (SideLocation s : extractedMethod) {
//                HashMap<String, String> methodInfo = s.parseMethodDeclaration();
//                assert mappings.containsKey(oldClassNameOld + ":" + methodInfo.get("MN"));
//                CodeBlock methodBlock = mappings.get(oldClassNameOld + ":" + methodInfo.get("MN"));
//                mappings.put(className + ":" + methodInfo.get("MN"), methodBlock);
//
//                MethodTime methodTime = (MethodTime) methodBlock.getLastHistory().clone();
//                methodTime.setTime(commitTime);
//                methodTime.setRefactorType(Extract_Class);
//                methodTime.setParentCodeBlock(classBlock);
//                methodBlock.addHistory(methodTime);
//                commitTime.addCodeChange(methodTime);
//
//                assert oldClassTime.getMethods().contains(methodBlock);
//                oldClassTime.getMethods().remove(methodBlock);
//                classTime.getMethods().add(methodBlock);
//            }

//            System.out.println(r.getType());
        }
    },
    Extract_Subclass {
        @Override
        //derive class 跟extract class几乎差不多
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            assert r.getDescription().split(" ").length == 6;
//            String newClassName = r.getDescription().split(" ")[2];
//            String oldClassName = r.getDescription().split(" ")[5];
//
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert left.size() == 1;
//            if (left.get(0).getFilePath().contains(oldClassName.substring(oldClassName.lastIndexOf(".") + 1)) && right.get(1).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".") + 1))) {
//                //如果不是内部类
//                CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
//                ClassTime classTime = new ClassTime(newClassName, commitTime, Operator.Extract_Class, codeBlock);
//                mappings.put(newClassName, codeBlock);
//
//                assert mappings.containsKey(oldClassName);
//                ClassTime oldClassTime = (ClassTime) mappings.get(oldClassName).getLastHistory();
//                ClassTime oldClassTimeNew = new ClassTime(oldClassTime, commitTime, Operator.Extract_Class);
//                oldClassTimeNew.getDerivee().add(classTime);
//                classTime.getDeriver().add(oldClassTimeNew);//添加deriver关系
//                //todo 把right中剩下的所有的方法移到newClass中
//
//                for (int i = 2; i < right.size(); i++) {
////                    assert mappings.containsKey(oldClassName.substring(oldClassName.lastIndexOf(".")+1)+":")
//
//                }
//            } else {
//                System.out.println("Inner class: extract subclass");
//            }
            System.out.println(r.getType());
        }
    },
    Merge_Class {
        //derive
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String[] oldClasses = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
//            String[] desc = r.getDescription().split(" ");
//            String newClassName = desc[desc.length - 1];
//            CodeBlock newClassBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
//            ClassTime newClassTime = new ClassTime(newClassName, commitTime, Operator.Merge_Class, newClassBlock);
//            mappings.put(newClassName, newClassBlock);
//
//            assert r.getRightSideLocations().size() == 1;
//            if (!r.getRightSideLocations().get(0).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".") + 1))) {
//                //新增的类是内部类 todo
//                System.out.println("Inner class: ");
//                return;
//            }
//
//            String pkgName = newClassName.substring(0, newClassName.lastIndexOf("."));
//            assert mappings.containsKey(pkgName);//如果不存在包的话 就需要新建
//            newClassTime.setParentCodeBlock(mappings.get(pkgName));
//            PackageTime newPkgTime = new PackageTime(pkgName, commitTime, Operator.Merge_Class, mappings.get(pkgName));
//            newPkgTime.getClasses().add(newClassBlock);
//
//            List<SideLocation> left = r.getLeftSideLocations();

//            assert oldClasses.length == left.size();
//            for (int i = 0; i < left.size(); i++) {
//                String oldName = oldClasses[i];
//                String oldFilePath = left.get(i).getFilePath();
//                if (!oldFilePath.contains(oldName.substring(oldName.lastIndexOf(".") + 1))) {
//                    System.out.println("inner class: to deal with");
//                    return;
//                }
//                assert mappings.containsKey(oldName);
//                CodeBlock oldBlock = mappings.get(oldName);
//                ClassTime oldTimeOld = (ClassTime) oldBlock.getLastHistory();
//                ClassTime oldTimeNew = new ClassTime(oldTimeOld, commitTime, Operator.Merge_Class);
//                oldTimeNew.getDerivee().add(newClassTime);
//                newClassTime.getDeriver().add(oldTimeNew);
//                newClassTime.getClasses().addAll(oldTimeOld.getClasses());
//                newClassTime.getMethods().addAll(oldTimeOld.getMethods());
//                newClassTime.getAttributes().addAll(oldTimeOld.getAttributes());
//                //把旧类中所包含的所有的类 方法 属性 全都移到新的类中
//
//
//            }
            System.out.println(r.getType());
        }
    },
    Move_Class {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //uodate class, class.parent, class.son
            //todo 需要注意如果是内部类的重命名和移动 7c65d01596100a0962d7e9e0b4a55dce461ff54e
            //todo 内部类的内部类 28ef8c9fc78433add3ee0a08f9f38569cd130958
            //update class.father, class.son, classTime
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;
            String oldName = left.get(0).getCodeElement();
            String newName = right.get(0).getCodeElement();
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
            classTime.setRefactorType(Operator.Move_Class);
            classTime.setParentCodeBlock(newFather);
            commitTime.addCodeChange(classTime);
            classBlock.addHistory(classTime);

            //update old father
            CodeBlockTime oldFatherTime = (CodeBlockTime) oldFather.getLastHistory().clone();
            oldFather.addHistory(oldFatherTime);
            commitTime.addCodeChange(oldFatherTime);
            oldFatherTime.setTime(commitTime);
            oldFatherTime.setRefactorType(Operator.Move_Class);
            oldFatherTime.getClasses().remove(classBlock);

            //update new father
            CodeBlockTime newFatherTime = (CodeBlockTime) newFather.getLastHistory().clone();
            newFather.addHistory(newFatherTime);
            commitTime.addCodeChange(newFatherTime);
            newFatherTime.setTime(commitTime);
            newFatherTime.setRefactorType(Operator.Move_Class);
            oldFatherTime.getClasses().add(classBlock);

            //update sons
            List<CodeBlock> classes = classTime.getClasses();//todo 递归
            List<CodeBlock> methods = classTime.getMethods();
            List<CodeBlock> attributes = classTime.getAttributes();

            System.out.println(r.getType());
        }
    },
    Rename_Class {
        @Override
        // 创建文件对应关系，然后对应修改classBlockTime，packageBlockTime的内容，然后在最后统一更新method和attribute的内容。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //todo 需要注意如果是内部类的重命名和移动 7c65d01596100a0962d7e9e0b4a55dce461ff54e
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;
            String oldName = left.get(0).getCodeElement();
            String newName = right.get(0).getCodeElement();
            assert mappings.containsKey(oldName);
            CodeBlock classBlock = mappings.get(oldName);
            mappings.put(newName, classBlock);
            ClassTime classTime = (ClassTime) classBlock.getLastHistory().clone();

            //update classTime
            classTime.setName(newName);
            classTime.setTime(commitTime);
            classTime.setRefactorType(Operator.Rename_Class);
            commitTime.addCodeChange(classTime);
            classBlock.addHistory(classTime);

            //update sons
            List<CodeBlock> classes = classTime.getClasses();//todo 递归
            List<CodeBlock> methods = classTime.getMethods();
            List<CodeBlock> attributes = classTime.getAttributes();

            System.out.println(r.getType());
        }
    },
    Move_And_Rename_Class {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //todo 需要注意如果是内部类的重命名和移动 13a0d1228e15b2386635f4a7df1d814e7a3fe145
//            String[] desc = r.getDescription().split(" ");
//            assert desc.length == 10;
//            String oldClass = desc[4];
//            String newClass = desc[9];
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert left.size() == right.size();
//            assert left.size() == 1;
//
//            assert mappings.containsKey(oldClass);
//            CodeBlock oldClassBlock = mappings.get(oldClass);
//
//            if (left.get(0).getFilePath().contains(oldClass.substring(oldClass.lastIndexOf(".") + 1)) && right.get(0).getFilePath().contains(newClass.substring(newClass.lastIndexOf(".") + 1))) {
//                //如果类名在文件路径中出现，就表明不是内部类
//                assert !(left.get(0).getFilePath().equals(right.get(0).getFilePath()));
//                PackageTime pkgTimeOld = (PackageTime) oldClassBlock.getLastHistory().getParentCodeBlock().getLastHistory();//原父亲的codeBlock
//                PackageTime pkgTimeNew = new PackageTime(pkgTimeOld, commitTime, Operator.Move_And_Rename_Class);
//                pkgTimeNew.getClasses().remove(oldClassBlock);

//                String newParent = newClass.substring(0, newClass.lastIndexOf("."));//新的父节点
//                if (!mappings.containsKey(newParent)) {//如果不存在包 就新建
//                    CodeBlock pkgBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
//                    mappings.put(newParent, pkgBlock);
//                    PackageTime pkgTime = new PackageTime(newParent, commitTime, Operator.Add_Package, pkgBlock);
//                }
//                assert mappings.containsKey(newParent);
//                PackageTime newPkgTime = new PackageTime((PackageTime) mappings.get(newParent).getLastHistory(), commitTime, Operator.Move_And_Rename_Class);
//                newPkgTime.getClasses().add(oldClassBlock);//更新 新的父节点
//
//                ClassTime classTime = new ClassTime(newClass, commitTime, Operator.Move_And_Rename_Class, oldClassBlock, newPkgTime.getOwner());
//                mappings.put(newClass, mappings.get(oldClass));
//
//                //todo 暂时没改变method的签名 留到最后遍历文件时处理
//            } else {
//                //todo 内部类的处理，不是文件的重命名，但是要对比两个新旧文件的内容 filePairs.put(leftFile, rightFile)
//                System.out.println("Inner class remains to be processed...");
//            }
            System.out.println(r.getType());
        }
    },
    Extract_Method {
        //add method 从现有方法的代码中抽取部分生成新的方法，methodB derived from methodA，
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            assert className.contains(".");
            assert r.getLeftSideLocations().get(0).getFilePath().equals(r.getRightSideLocations().get(0).getFilePath());
            HashMap<String, String> oldMethod = r.getLeftSideLocations().get(0).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = r.getRightSideLocations().get(0).parseMethodDeclaration();
            HashMap<String, String> oldMethodNew = r.getRightSideLocations().get(r.getLeftSideLocations().size()).parseMethodDeclaration();
            assert mappings.containsKey(className+":"+oldMethod.get("MN"));
            assert mappings.containsKey(className);
            CodeBlock oldMethodBlock = mappings.get(className+":"+oldMethod.get("MN"));
            CodeBlock classBlock = mappings.get(className);
            if(!oldMethod.get("MN").equals(oldMethodNew.get("MN"))){
                mappings.put(className+":"+oldMethodNew.get("MN"), oldMethodBlock);
            }
            //create new methodBlock
            CodeBlock newMethodBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Method);
            MethodTime methodTime = new MethodTime(className+":"+newMethod.get("MN"), commitTime, Operator.Extract_Method, newMethodBlock, classBlock, newMethod.get("PA"));
            mappings.put(className+":"+newMethod.get("MN"), newMethodBlock);
            codeBlocks.add(newMethodBlock);
            //add new method to class
            //在创建新的method时就已经更新了
            //create methodblock for old method
            MethodTime oldMethodTime = (MethodTime) oldMethodBlock.getLastHistory().clone();
            oldMethodTime.setTime(commitTime);
            oldMethodTime.setRefactorType(Operator.Extract_Method);
            oldMethodTime.getDerivee().add(methodTime);
            methodTime.getDeriver().add(oldMethodTime);
            commitTime.addCodeChange(oldMethodTime);
            oldMethodBlock.addHistory(oldMethodTime);
            System.out.println(r.getType());
        }
    },
    Inline_Method {
        //与extract method相反，delete 方法，将方法的内容合并到已有的方法中 左边第一个是被inline方法的声明，左边倒数第二个是target method 原来的声明 右边第一个是target method的新声明
        //deriver, derivee, parentBlock.method remove
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            assert className.contains(".");
            assert r.getLeftSideLocations().get(0).getFilePath().equals(r.getRightSideLocations().get(0).getFilePath());
            HashMap<String, String> inlinedMethod = r.getLeftSideLocations().get(0).parseMethodDeclaration();//parse the method name
            HashMap<String, String> methodNameOld = r.getLeftSideLocations().get(r.getLeftSideLocations().size()-2).parseMethodDeclaration();
            HashMap<String, String> methodNameNew = r.getRightSideLocations().get(0).parseMethodDeclaration();
            assert mappings.containsKey(className+":"+inlinedMethod.get("MN"));
            assert mappings.containsKey(className);
            assert mappings.containsKey(className+":"+methodNameOld);
            CodeBlock inlinedMethodBlock = mappings.get(className+":"+inlinedMethod.get("MN"));
            CodeBlock classBlock = mappings.get(className);
            CodeBlock targetMethodBlock = mappings.get(className+":"+methodNameOld.get("MN"));
            if(!methodNameNew.get("MN").equals(methodNameOld.get("MN"))){
                mappings.put(className+":"+methodNameNew.get("MN"), targetMethodBlock);
            }
            //remove inlined methodBlock
            MethodTime inlinedMethodTime = (MethodTime) inlinedMethodBlock.getLastHistory().clone();
            inlinedMethodTime.setTime(commitTime);
            inlinedMethodTime.setRefactorType(Operator.Inline_Method);
            inlinedMethodBlock.addHistory(inlinedMethodTime);
            commitTime.addCodeChange(inlinedMethodTime);

            //derive relation
            MethodTime targetMethodTime = (MethodTime) targetMethodBlock.getLastHistory().clone();
            targetMethodTime.setTime(commitTime);
            targetMethodTime.setRefactorType(Operator.Inline_Method);
            targetMethodTime.getDeriver().add(inlinedMethodTime);
            inlinedMethodTime.getDerivee().add(targetMethodTime);
            commitTime.addCodeChange(targetMethodTime);
            targetMethodBlock.addHistory(targetMethodTime);

            //remove from class
            ClassTime classTime = (ClassTime) classBlock.getLastHistory().clone();
            classTime.setTime(commitTime);
            classTime.setRefactorType(Operator.Inline_Method);
            classTime.getMethods().remove(inlinedMethodBlock);
            commitTime.addCodeChange(classTime);
            classBlock.addHistory(classTime);
            System.out.println(r.getType());
        }
    },
    Pull_Up_Method {
        //move method from one class to super class,将几个子类中的方法移到超类中，跨文件，涉及方法的移动，还可能修改名字，但是不影响文件数目,一般情况下 一个refactoring只涉及一个类
        //相当于是move method
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //find method, oldclass and newclass from mappings, change parentBlock from oldclass to newClass
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert r.getLeftSideLocations().size() == r.getRightSideLocations().size();
            assert r.getRightSideLocations().size() == 1;
            HashMap<String, String> oldMethod = r.getLeftSideLocations().get(0).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = r.getRightSideLocations().get(0).parseMethodDeclaration();

            assert mappings.containsKey(oldClass + ":" + oldMethod.get("MN"));
            assert mappings.containsKey(oldClass);
            CodeBlock methodBlock = mappings.get(oldClass + ":" + oldMethod.get("MN"));
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            assert classBlockOld.equals(methodBlock.getLastHistory().getParentCodeBlock());

            assert mappings.containsKey(newClass);
            CodeBlock classBlockNew = mappings.get(newClass);

            // create new methodTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            MethodTime methodTimeNew = (MethodTime) methodBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Pull_Up_Method);
            methodTimeNew.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(methodTimeNew);
            methodBlock.addHistory(methodTimeNew);
            mappings.put(newClass + ":" + newMethod.get("MN"), methodBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Pull_Up_Method);
            assert oldClassTimeNew.getMethods().contains(methodBlock);
            oldClassTimeNew.getMethods().remove(methodBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Pull_Up_Method);
            newClassTimeNew.getMethods().add(methodBlock);
            classBlockNew.addHistory(newClassTimeNew);

//            System.out.println(r.getType());
        }
    },
    Push_Down_Method {
        @Override
        //将父类中的方法 移到子类中去，一般会在不同的文件之间进行移动，甚至还有rename pull_Up_method的对立 move and rename method
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //find method, oldclass and newclass from mappings, change parentBlock from oldclass to newClass
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert r.getLeftSideLocations().size() == r.getRightSideLocations().size();
            assert r.getRightSideLocations().size() == 1;
            HashMap<String, String> oldMethod = r.getLeftSideLocations().get(0).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = r.getRightSideLocations().get(0).parseMethodDeclaration();

            assert mappings.containsKey(oldClass + ":" + oldMethod.get("MN"));
            assert mappings.containsKey(oldClass);
            CodeBlock methodBlock = mappings.get(oldClass + ":" + oldMethod.get("MN"));
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            assert classBlockOld.equals(methodBlock.getLastHistory().getParentCodeBlock());

            assert mappings.containsKey(newClass);
            CodeBlock classBlockNew = mappings.get(newClass);
            // create new methodTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            MethodTime methodTimeNew = (MethodTime) methodBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Push_Down_Method);
            methodTimeNew.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(methodTimeNew);
            methodBlock.addHistory(methodTimeNew);
            mappings.put(newClass + ":" + newMethod.get("MN"), methodBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Push_Down_Method);
            assert oldClassTimeNew.getMethods().contains(methodBlock);
            oldClassTimeNew.getMethods().remove(methodBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Push_Down_Method);
            newClassTimeNew.getMethods().add(methodBlock);
            classBlockNew.addHistory(newClassTimeNew);
            System.out.println(r.getType());
        }
    },
    Extract_And_Move_Method {
        @Override
        //可能从多个方法中提取出一个新的方法， 涉及新建一个methodBlock，有多个derived from，并且移到了一个新的类中，跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String oldClassName = r.getLastClassName();
            String newClassName = r.getFirstClassName();
            assert oldClassName.contains(".");
            HashMap<String, String> oldMethod = r.getLeftSideLocations().get(0).parseMethodDeclaration();//parse the method name
            HashMap<String, String> deriveeMethod = r.getRightSideLocations().get(0).parseMethodDeclaration();
            HashMap<String, String> oldMethodNew = r.getRightSideLocations().get(r.getLeftSideLocations().size()).parseMethodDeclaration();
            assert mappings.containsKey(oldClassName+":"+oldMethod.get("MN"));
            assert mappings.containsKey(newClassName);
            CodeBlock oldMethodBlock = mappings.get(oldClassName+":"+oldMethod.get("MN"));
            CodeBlock newClassBlock = mappings.get(newClassName);
            if(!oldMethod.get("MN").equals(oldMethodNew.get("MN"))){
                mappings.put(oldClassName+":"+oldMethodNew.get("MN"), oldMethodBlock);
            }
            //create new methodBlock
            CodeBlock newMethodBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Method);
            MethodTime methodTime = new MethodTime(newClassName+":"+deriveeMethod.get("MN"), commitTime, Operator.Extract_And_Move_Method, newMethodBlock, newClassBlock, deriveeMethod.get("PA"));
            mappings.put(newClassName+":"+deriveeMethod.get("MN"), newMethodBlock);
            codeBlocks.add(newMethodBlock);

            //create methodblock for old method
            MethodTime oldMethodTime = (MethodTime) oldMethodBlock.getLastHistory().clone();
            oldMethodTime.setTime(commitTime);
            oldMethodTime.setRefactorType(Operator.Extract_Method);
            oldMethodTime.getDerivee().add(methodTime);
            methodTime.getDeriver().add(oldMethodTime);
            commitTime.addCodeChange(oldMethodTime);
            oldMethodBlock.addHistory(oldMethodTime);
            System.out.println(r.getType());
        }
    },
    Move_And_Inline_Method {
        @Override
        //跨文件，移动，移动旧的方法，并拆散方法到已有的方法中。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Move_And_Rename_Method {//跨文件
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //find method, oldclass and newclass from mappings, change parentBlock from oldclass to newClass, update method name
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert left.size() == right.size();
            assert left.size() == 1;
            HashMap<String, String> oldMethod = left.get(0).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = right.get(0).parseMethodDeclaration();
            String oldSig = oldClass + ":" + oldMethod.get("MN");
            String newSig = newClass + ":" + newMethod.get("MN");
            assert mappings.containsKey(oldSig);
            assert mappings.containsKey(oldClass);
            assert mappings.containsKey(newClass);
            CodeBlock methodBlock = mappings.get(oldSig);
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            CodeBlock classBlockNew = mappings.get(newClass);
            if (methodBlock.getLastHistory().getParentCodeBlock().equals(classBlockNew)) {
                return;
            }// 如果已经移动过了，就结束本次；如果还没有移动过，就进行迁移
            assert classBlockOld.equals(methodBlock.getLastHistory().getParentCodeBlock());

            // create new methodTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            MethodTime methodTimeNew = (MethodTime) methodBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Move_And_Rename_Method);
            methodTimeNew.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(methodTimeNew);
            methodBlock.addHistory(methodTimeNew);
            mappings.put(newSig, methodBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Move_And_Rename_Method);
            assert oldClassTimeNew.getMethods().contains(methodBlock);
            oldClassTimeNew.getMethods().remove(methodBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Move_And_Rename_Method);
            newClassTimeNew.getMethods().add(methodBlock);
            classBlockNew.addHistory(newClassTimeNew);
            System.out.println(r.getType());
        }
    },
    Move_Method {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //find method, oldclass and newclass from mappings, change parentBlock from oldclass to newClass
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert left.size() == right.size();
            assert left.size() == 1;
            HashMap<String, String> oldMethod = left.get(0).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = right.get(0).parseMethodDeclaration();
            String oldSig = oldClass + ":" + oldMethod.get("MN");
            String newSig = newClass + ":" + newMethod.get("MN");
            assert mappings.containsKey(oldSig);
            assert mappings.containsKey(oldClass);
            assert mappings.containsKey(newClass);
            CodeBlock methodBlock = mappings.get(oldSig);
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            CodeBlock classBlockNew = mappings.get(newClass);
            if (methodBlock.getLastHistory().getParentCodeBlock().equals(classBlockNew)) {
                return;
            }// 如果已经在extractmethod的时候移动过了，就结束本次；如果还没有移动过，就进行迁移
            assert classBlockOld.equals(methodBlock.getLastHistory().getParentCodeBlock());

            // create new methodTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            MethodTime methodTimeNew = (MethodTime) methodBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Move_Method);
            methodTimeNew.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(methodTimeNew);
            methodBlock.addHistory(methodTimeNew);
            mappings.put(newSig, methodBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Move_Method);
            assert oldClassTimeNew.getMethods().contains(methodBlock);
            oldClassTimeNew.getMethods().remove(methodBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Move_Method);
            newClassTimeNew.getMethods().add(methodBlock);
            classBlockNew.addHistory(newClassTimeNew);
//            System.out.println(r.getType());
        }
    },
    Change_Return_Type { //trival 只需要修改返回类型 一般不跨文件
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String className = r.getLastClassName();
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert left.size() == right.size();
//            assert left.size() == 2;
//            System.out.println(commitTime.getCommitID());
//            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
//            HashMap<String, String> oldMethod = left.get(1).parseMethodDeclaration();//parse the method name
//            HashMap<String, String> newMethod = right.get(1).parseMethodDeclaration();
//            String newSig = className + ":" + newMethod.get("MN");
//            assert mappings.containsKey(className + ":" + oldMethod.get("MN"));
//            CodeBlock methodBlock = mappings.get(className + ":" + oldMethod.get("MN"));
//            MethodTime methodTimeNew = (MethodTime) methodBlock.getLastHistory().clone();
//            methodTimeNew.setName(newMethod.get("MN"));
//            methodTimeNew.setParameters(newMethod.get("PA"));
//            //todo return type
//            methodTimeNew.setTime(commitTime);
//            methodTimeNew.setRefactorType(Operator.Change_Return_Type);
//            commitTime.addCodeChange(methodTimeNew);
//            methodBlock.addHistory(methodTimeNew);
//            mappings.put(newSig, methodBlock);
//            System.out.println(r.getType());
        }
    },
    Rename_Method {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String className = r.getLastClassName();
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert left.size() == right.size();
//            assert left.size() == 1;
//            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
//            HashMap<String, String> oldMethod = left.get(0).parseMethodDeclaration();//parse the method name
//            HashMap<String, String> newMethod = right.get(0).parseMethodDeclaration();
//            String oldSig = className + ":" + oldMethod.get("MN");
//            String newSig = className + ":" + newMethod.get("MN");
//            assert mappings.containsKey(oldSig);
//            CodeBlock codeBlock = mappings.get(oldSig);
//            MethodTime methodTimeNew = (MethodTime) codeBlock.getLastHistory().clone();
//            methodTimeNew.setName(newMethod.get("MN"));
//            methodTimeNew.setParameters(newMethod.get("PA"));
//            methodTimeNew.setTime(commitTime);
//            methodTimeNew.setRefactorType(Operator.Rename_Method);
//            commitTime.addCodeChange(methodTimeNew);
//            codeBlock.addHistory(methodTimeNew);
//            mappings.put(newSig, codeBlock);
//            System.out.println(r.getType());
        }
    },
    Parameterize_Variable {//方法名级别
        @Override
        //把方法中的一个变量 变为方法的参数 不跨文件，仅需要修改方法的名字
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 2;
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            HashMap<String, String> oldMethod = left.get(1).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = right.get(1).parseMethodDeclaration();
            String oldSig = className + ":" + oldMethod.get("MN");
            String newSig = className + ":" + newMethod.get("MN");
            assert mappings.containsKey(oldSig);
            CodeBlock methodBlock = mappings.get(oldSig);
            MethodTime methodTimeNew = (MethodTime) methodBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            //todo return type
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Parameterize_Variable);
            commitTime.addCodeChange(methodTimeNew);
            methodBlock.addHistory(methodTimeNew);
            mappings.put(newSig, methodBlock);
//            System.out.println(r.getType());
        }
    },
    Merge_Parameter {//把一个方法的参数进行合并，但是可能会有移动 左右两边的最后一个 分别是旧新方法的声明
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            HashMap<String, String> oldMethod = left.get(left.size()-1).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = right.get(right.size()-1).parseMethodDeclaration();
            String oldSig = className + ":" + oldMethod.get("MN");
            String newSig = className + ":" + newMethod.get("MN");
            assert mappings.containsKey(oldSig);
            CodeBlock codeBlock = mappings.get(oldSig);
            MethodTime methodTimeNew = (MethodTime) codeBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Merge_Parameter);
            commitTime.addCodeChange(methodTimeNew);
            codeBlock.addHistory(methodTimeNew);
            mappings.put(newSig, codeBlock);
            System.out.println(r.getType());
        }
    },
    Split_Parameter {//method name change, parameterList change 左右两边的最后一个分别是旧、新方法的声明
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            HashMap<String, String> oldMethod = left.get(left.size()-1).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = right.get(right.size()-1).parseMethodDeclaration();
            String oldSig = className + ":" + oldMethod.get("MN");
            String newSig = className + ":" + newMethod.get("MN");
            assert mappings.containsKey(oldSig);
            CodeBlock codeBlock = mappings.get(oldSig);
            MethodTime methodTimeNew = (MethodTime) codeBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Split_Parameter);
            commitTime.addCodeChange(methodTimeNew);
            codeBlock.addHistory(methodTimeNew);
            mappings.put(newSig, codeBlock);
            System.out.println(r.getType());
        }
    },
    Change_Parameter_Type {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String className = r.getLastClassName();
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert right.size() == 2;
//            assert left.size() == 2;
////            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
//            HashMap<String, String> oldMethod = left.get(1).parseMethodDeclaration();
//            HashMap<String, String> newMethod = right.get(1).parseMethodDeclaration();
//            System.out.println(className + ":" + oldMethod.get("MN"));
//            assert mappings.containsKey(className + ":" + oldMethod.get("MN"));//todo 会出错 664146363dc6ad39bb29b7c108eed6236457d189
//            CodeBlock codeBlock = mappings.get(className + ":" + oldMethod.get("MN"));
//            mappings.put(className + ":" + newMethod.get("MN"), codeBlock);
//            MethodTime methodTime = (MethodTime) codeBlock.getLastHistory().clone();
//            methodTime.setName(newMethod.get("MN"));
//            methodTime.setTime(commitTime);
//            methodTime.setRefactorType(Operator.Change_Parameter_Type);
//            methodTime.setParameters(newMethod.get("PA"));//update parameterType
//            commitTime.addCodeChange(methodTime);
//            codeBlock.addHistory(methodTime);
            //todo parameterType
//            System.out.println(r.getType());
        }
    },
    Add_Parameter {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String className = r.getLastClassName();
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert right.size() == 2;
//            assert left.size() == 1;
//            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
//            HashMap<String, String> oldMethod = left.get(0).parseMethodDeclaration();
//            HashMap<String, String> newMethod = right.get(1).parseMethodDeclaration();
//            assert mappings.containsKey(className + ":" + oldMethod.get("MN"));
//            CodeBlock codeBlock = mappings.get(className + ":" + oldMethod.get("MN"));
//            mappings.put(className + ":" + newMethod.get("MN"), codeBlock);
//            MethodTime methodTime = (MethodTime) codeBlock.getLastHistory().clone();
//            methodTime.setName(newMethod.get("MN"));
//            methodTime.setTime(commitTime);
//            methodTime.setRefactorType(Operator.Add_Parameter);
//            methodTime.setParameters(newMethod.get("PA"));//update parameterType
//            commitTime.addCodeChange(methodTime);
//            codeBlock.addHistory(methodTime);//todo return type
//            System.out.println(r.getType());
        }
    },
    Remove_Parameter {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String className = r.getLastClassName();
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert right.size() == 1;
//            assert left.size() == 2;
//            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
//            HashMap<String, String> oldMethod = left.get(1).parseMethodDeclaration();
//            HashMap<String, String> newMethod = right.get(0).parseMethodDeclaration();
//            System.out.println(commitTime.getCommitID());
//            assert mappings.containsKey(className);
//            System.out.println(mappings.get(className).getLastHistory());
//            assert mappings.containsKey(className + ":" + oldMethod.get("MN"));
//            CodeBlock codeBlock = mappings.get(className + ":" + oldMethod.get("MN"));
//            mappings.put(className + ":" + newMethod.get("MN"), codeBlock);
//            MethodTime methodTime = (MethodTime) codeBlock.getLastHistory().clone();
//            methodTime.setName(newMethod.get("MN"));
//            methodTime.setTime(commitTime);
//            methodTime.setRefactorType(Operator.Remove_Parameter);
//            methodTime.setParameters(newMethod.get("PA"));//update parameterType
//            commitTime.addCodeChange(methodTime);
//            codeBlock.addHistory(methodTime);
//            System.out.println(r.getType());
        }
    },
    Reorder_Parameter {// only change method name & method parameterList, parameterType 左边和右边的最后一个分别是旧、新方法的声明
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            HashMap<String, String> oldMethod = left.get(left.size()-1).parseMethodDeclaration();//parse the method name
            HashMap<String, String> newMethod = right.get(right.size()-1).parseMethodDeclaration();
            String oldSig = className + ":" + oldMethod.get("MN");
            String newSig = className + ":" + newMethod.get("MN");
            assert mappings.containsKey(oldSig);
            CodeBlock codeBlock = mappings.get(oldSig);
            MethodTime methodTimeNew = (MethodTime) codeBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Reorder_Parameter);
            commitTime.addCodeChange(methodTimeNew);
            codeBlock.addHistory(methodTimeNew);
            mappings.put(newSig, codeBlock);
            System.out.println(r.getType());
        }
    },
    Parameterize_Attribute {//把一个attribute变成一个参数，同时修改属性和方法的参数 一般不跨项目
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert right.size() == 2;
            assert left.size() == 2;
            String oldAttri = left.get(0).parseAttributeOrParameter();
            String newAttri = right.get(0).parseAttributeOrParameter();
            HashMap<String, String> oldMethod = left.get(1).parseMethodDeclaration();
            HashMap<String, String> newMethod = right.get(1).parseMethodDeclaration();

            //update attributeTime
//            System.out.println(r.getDescription());
            assert mappings.containsKey(className + ":" + oldAttri);
            mappings.put(className + ":" + newAttri, mappings.get(className + ":" + oldAttri));// update signature
            AttributeTime attributeTime = (AttributeTime) mappings.get(className + ":" + oldAttri).getLastHistory().clone();//create new attributeTimeBlock
            attributeTime.setName(newAttri);
            attributeTime.setTime(commitTime);
            attributeTime.setRefactorType(Operator.Parameterize_Attribute);
            mappings.get(className + ":" + newAttri).addHistory(attributeTime);
            commitTime.addCodeChange(attributeTime);

            //update method
            assert mappings.containsKey(className + ":" + oldMethod.get("MN"));
            CodeBlock methodBlock = mappings.get(className + ":" + oldMethod.get("MN"));
            MethodTime methodTimeNew = (MethodTime) methodBlock.getLastHistory().clone();
            methodTimeNew.setName(newMethod.get("MN"));
            methodTimeNew.setParameters(newMethod.get("PA"));
            methodTimeNew.setTime(commitTime);
            methodTimeNew.setRefactorType(Operator.Parameterize_Attribute);
            commitTime.addCodeChange(methodTimeNew);
            methodBlock.addHistory(methodTimeNew);
            mappings.put(className + ":" + newMethod.get("MN"), methodBlock);

            System.out.println(r.getType());
        }
    },
    Pull_Up_Attribute {//把子类中的属性 移到父类中 跨文件
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert r.getRightSideLocations().size() == 1;
            String oldAttri = r.getLeftSideLocations().get(0).parseAttributeOrParameter();//parse the attribute name
            String newAttri = r.getRightSideLocations().get(0).parseAttributeOrParameter();

            assert mappings.containsKey(oldClass + ":" + oldAttri);
            assert mappings.containsKey(oldClass);
            CodeBlock attriBlock = mappings.get(oldClass + ":" + oldAttri);
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            assert classBlockOld.equals(attriBlock.getLastHistory().getParentCodeBlock());

            //if newClass doesn't exist, create; otherwise find from mappings
            CodeBlock classBlockNew; //new class, find from mappings or create
            if (mappings.containsKey(newClass)) {
                classBlockNew = mappings.get(newClass);
            } else {
                classBlockNew = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                ClassTime newClassTimeNew = new ClassTime(newClass, commitTime, Operator.Add_Class, classBlockNew, mappings.get(newClass.substring(0, newClass.lastIndexOf("."))));//todo don't know parentBlock
                mappings.put(newClass, classBlockNew);
                codeBlocks.add(classBlockNew);
            }
            // create new attributeTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            AttributeTime attriTime = (AttributeTime) attriBlock.getLastHistory().clone();
            attriTime.setName(newAttri);
            attriTime.setTime(commitTime);
            attriTime.setRefactorType(Operator.Pull_Up_Attribute);
            attriTime.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(attriTime);
            attriBlock.addHistory(attriTime);
            mappings.put(newClass + ":" + newAttri, attriBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Pull_Up_Attribute);
            assert oldClassTimeNew.getAttributes().contains(attriBlock);
            oldClassTimeNew.getAttributes().remove(attriBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Pull_Up_Attribute);
            newClassTimeNew.getAttributes().add(attriBlock);
            classBlockNew.addHistory(newClassTimeNew);
//            System.out.println(r.getType());
        }
    },
    Push_Down_Attribute {// move attribute from father class to son class, normally cross class files. ≈move attribute
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert left.size() == right.size();
            assert left.size() == 1;
            String oldAttri = left.get(0).parseAttributeOrParameter();//parse the attribute name
            String newAttri = right.get(0).parseAttributeOrParameter();
            String oldSig = oldClass + ":" + oldAttri;
            String newSig = newClass + ":" + newAttri;
            assert mappings.containsKey(oldSig);
            assert mappings.containsKey(oldClass);
            assert mappings.containsKey(newClass);
            CodeBlock attriBlock = mappings.get(oldSig);
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            CodeBlock classBlockNew = mappings.get(newClass);
            if (attriBlock.getLastHistory().getParentCodeBlock().equals(classBlockNew)) {
                return;
            }// 如果已经在前边移动过了，就结束本次；如果还没有移动过，就进行迁移
            assert classBlockOld.equals(attriBlock.getLastHistory().getParentCodeBlock());

            // create new attributeTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            AttributeTime attriTime = (AttributeTime) attriBlock.getLastHistory().clone();
            attriTime.setName(newAttri);
            attriTime.setTime(commitTime);
            attriTime.setRefactorType(Operator.Push_Down_Method);
            attriTime.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(attriTime);
            attriBlock.addHistory(attriTime);
            mappings.put(newSig, attriBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Push_Down_Method);
            assert oldClassTimeNew.getAttributes().contains(attriBlock);
            oldClassTimeNew.getAttributes().remove(attriBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Push_Down_Method);
            newClassTimeNew.getAttributes().add(attriBlock);
            classBlockNew.addHistory(newClassTimeNew);
            System.out.println(r.getType());
        }
    },
    Move_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert left.size() == right.size();
            assert left.size() == 1;
            String oldAttri = left.get(0).parseAttributeOrParameter();//parse the method name
            String newAttri = right.get(0).parseAttributeOrParameter();
            String oldSig = oldClass + ":" + oldAttri;
            String newSig = newClass + ":" + newAttri;
            assert mappings.containsKey(oldSig);
            assert mappings.containsKey(oldClass);
            assert mappings.containsKey(newClass);
            CodeBlock attriBlock = mappings.get(oldSig);
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            CodeBlock classBlockNew = mappings.get(newClass);
            if (attriBlock.getLastHistory().getParentCodeBlock().equals(classBlockNew)) {
                return;
            }// 如果已经在前边移动过了，就结束本次；如果还没有移动过，就进行迁移
            assert classBlockOld.equals(attriBlock.getLastHistory().getParentCodeBlock());

            // create new attributeTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            AttributeTime attriTime = (AttributeTime) attriBlock.getLastHistory().clone();
            attriTime.setName(newAttri);
            attriTime.setTime(commitTime);
            attriTime.setRefactorType(Operator.Move_Attribute);
            attriTime.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(attriTime);
            attriBlock.addHistory(attriTime);
            mappings.put(newSig, attriBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Move_Attribute);
            assert oldClassTimeNew.getAttributes().contains(attriBlock);
            oldClassTimeNew.getAttributes().remove(attriBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Move_Attribute);
            newClassTimeNew.getAttributes().add(attriBlock);
            classBlockNew.addHistory(newClassTimeNew);
            System.out.println(r.getType());
        }
    },
    Rename_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            String oldName = left.get(0).parseAttributeOrParameter();
            String newName = right.get(0).parseAttributeOrParameter();
            String returnType = newName.substring(0, newName.indexOf("_"));//todo 返回值类型
            assert mappings.containsKey(className + ":" + oldName);
            mappings.put(className + ":" + newName, mappings.get(className + ":" + oldName));// update signature
            AttributeTime attributeTime = (AttributeTime) mappings.get(className + ":" + oldName).getLastHistory().clone();//create new attributeTimeBlock
            attributeTime.setName(newName);
            attributeTime.setTime(commitTime);
            attributeTime.setRefactorType(Operator.Rename_Attribute);
            mappings.get(className + ":" + newName).addHistory(attributeTime);
            commitTime.addCodeChange(attributeTime);
            System.out.println(r.getType());
        }
    },
    Merge_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //create new attribute(update class.attribute), derive from oldAttribute A, B, C, etc.
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert right.size() == 1;
            String attriNameNew = right.get(0).parseAttributeOrParameter();

            //create new codeBlock
            assert mappings.containsKey(className);
            CodeBlock attriBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Attribute);
            AttributeTime attributeTime = new AttributeTime(attriNameNew, commitTime, Operator.Add_Attribute, attriBlock, mappings.get(className));
            mappings.put(attriNameNew, attriBlock);
            codeBlocks.add(attriBlock);
            commitTime.addCodeChange(attributeTime);

            //derive from old attributes
            for (int i = 0; i < left.size(); i++) {
                String oldAttriName = left.get(i).parseAttributeOrParameter();
                assert mappings.containsKey(className + ":" + oldAttriName);
                CodeBlock oldAttriBlock = mappings.get(className + ":" + oldAttriName);
                AttributeTime attributeTimeOld = (AttributeTime) oldAttriBlock.getLastHistory().clone();
                attributeTimeOld.setTime(commitTime);
                attributeTimeOld.setRefactorType(Operator.Merge_Attribute);
                attributeTimeOld.getDerivee().add(attributeTime);
                attributeTime.getDeriver().add(attributeTimeOld);
                oldAttriBlock.addHistory(attributeTimeOld);
                commitTime.addCodeChange(attributeTimeOld);
            }
            System.out.println(r.getType());
        }
    },
    Split_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == 1;
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());//假设文件没有变化 如果有变化 就需要把classBlock的更新换到for循环里边进行
            String oldAttriName = left.get(0).parseAttributeOrParameter();

            //create new attributeTime
            assert mappings.containsKey(className);
            assert mappings.containsKey(className + ":" + oldAttriName);
            CodeBlock oldAttriBlock = mappings.get(className + ":" + oldAttriName);
            CodeBlock classBlock = mappings.get(className);

            AttributeTime oldAttriTime = (AttributeTime) oldAttriBlock.getLastHistory().clone();
            oldAttriTime.setTime(commitTime);
            oldAttriTime.setRefactorType(Operator.Split_Attribute);
            oldAttriBlock.addHistory(oldAttriTime);
            commitTime.addCodeChange(oldAttriTime);

            //new derivee attributes
            for (int i = 0; i < right.size(); i++) {
                String newAttriName = right.get(0).parseAttributeOrParameter();
                CodeBlock attriBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Attribute);
                AttributeTime attriTime = new AttributeTime(newAttriName, commitTime, Operator.Split_Attribute, attriBlock, classBlock);
                mappings.put(newAttriName, attriBlock);
                codeBlocks.add(attriBlock);
                commitTime.addCodeChange(attriTime);

                attriTime.getDeriver().add(oldAttriTime);
                oldAttriTime.getDerivee().add(attriTime);
            }

            System.out.println(r.getType());
        }
    },
    Change_Attribute_Type {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
//            String className = r.getLastClassName();
//            List<SideLocation> left = r.getLeftSideLocations();
//            List<SideLocation> right = r.getRightSideLocations();
//            assert left.size() == right.size();
//            assert left.size() == 1;
//            System.out.println(r.getDescription());//todo 如果是先rename了类，那么这里显示的是更新后的类名
//            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());//todo 会出错 Change Attribute Type pi : DistributedPipeInput to pi : PipeInput in class org.jboss.test.messaging.core.distributed.SharedPipeOutputTest.Sender
//            String[] leftCodeElement = left.get(0).getCodeElement().split(" : ");
//            String[] rightCodeElement = right.get(0).getCodeElement().split(" : ");
//            assert leftCodeElement.length == 2;
//            assert rightCodeElement.length == 2;
//            String oldName = leftCodeElement[1] + "_" + leftCodeElement[0];
//            String newName = rightCodeElement[1] + "_" + rightCodeElement[0];
//            String returnType = rightCodeElement[1];//todo 返回值类型
//            assert mappings.containsKey(className + ":" + oldName);
//            mappings.put(className + ":" + newName, mappings.get(className + ":" + oldName));// update signature
//            AttributeTime attributeTime = (AttributeTime) mappings.get(className + ":" + oldName).getLastHistory().clone();//create new attributeTimeBlock
//            attributeTime.setName(newName);
//            attributeTime.setTime(commitTime);
//            attributeTime.setRefactorType(Operator.Change_Attribute_Type);
//            mappings.get(className + ":" + newName).addHistory(attributeTime);
//            commitTime.addCodeChange(attributeTime);
//            System.out.println(r.getType());
        }
    },
    Extract_Attribute {//涉及增加新的attribute
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String className = r.getLastClassName();
            assert r.getLeftSideLocations().get(0).getFilePath().equals(r.getRightSideLocations().get(0).getFilePath());
            assert mappings.containsKey(className);
            CodeBlock classBlock = mappings.get(className);
            String attriName = r.getRightSideLocations().get(0).parseAttributeOrParameter();
            assert !mappings.containsKey(className + ":" + attriName);
            CodeBlock attriBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Attribute);
            AttributeTime attributeTime = new AttributeTime(attriName, commitTime, Operator.Add_Attribute, attriBlock, classBlock);
            mappings.put(attriName, attriBlock);
            codeBlocks.add(attriBlock);
//            System.out.println(r.getType());
        }
    },
    Encapsulate_Attribute {//应该是只增加了一个get方法
        @Override
        //Attribute encapsulation is useful when you have an attribute that is affected by several different methods,
        // each of which needs that attribute to be in a known state. To prevent programmers from changing the attribute
        // in the 4GL code, you can make the attribute private so that programmers can only access it from the object's methods.
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //add new method
            String className = r.getLastClassName();
            assert r.getRightSideLocations().size() == 2;
            assert mappings.containsKey(className);
            HashMap<String, String> info = r.getRightSideLocations().get(1).parseMethodDeclaration();
            CodeBlock classBlock = mappings.get(className);
            CodeBlock methodBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Method);
            MethodTime methodTime = new MethodTime(info.get("MN"), commitTime, Operator.Add_Method, methodBlock, classBlock, info.get("PA"));
            mappings.put(className + ":" + info.get("MN"), methodBlock);
            codeBlocks.add(methodBlock);
            ClassTime classTime = (ClassTime) classBlock.getLastHistory().clone();
            classTime.setTime(commitTime);
            commitTime.addCodeChange(classTime);
            classTime.setRefactorType(Operator.Encapsulate_Attribute);
            classTime.getMethods().add(methodBlock);
            classBlock.addHistory(classTime);
//            System.out.println(r.getType());
        }
    },
    Inline_Attribute {//remove_attribute, 去掉属性，直接使用属性的值,从旧的类中移除
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //remove from oldClass
            String className = r.getLastClassName();
            String attriName = r.getLeftSideLocations().get(0).parseAttributeOrParameter();
            assert mappings.containsKey(className);
            assert mappings.containsKey(attriName);
            CodeBlock classBlock = mappings.get(className);
            CodeBlock attriBlock = mappings.get(attriName);
            //remove from class
            ClassTime classTime = (ClassTime) classBlock.getLastHistory().clone();
            classTime.setTime(commitTime);
            classTime.setRefactorType(Operator.Inline_Attribute);
            classTime.getAttributes().remove(attriBlock);
            classBlock.addHistory(classTime);
            commitTime.addCodeChange(classTime);
            // add new attributeTime
            AttributeTime attributeTime = (AttributeTime) attriBlock.getLastHistory().clone();
            attributeTime.setTime(commitTime);
            attributeTime.setRefactorType(Operator.Inline_Attribute);
            attriBlock.addHistory(attributeTime);
            commitTime.addCodeChange(attributeTime);

            System.out.println(r.getType());
        }
    },
    Move_And_Rename_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String oldClass = r.getFirstClassName();
            String newClass = r.getLastClassName();
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert oldClass.contains(".");
            assert newClass.contains(".");
            assert left.size() == right.size();
            assert left.size() == 1;
            String oldAttri = left.get(0).parseAttributeOrParameter();//parse the method name
            String newAttri = right.get(0).parseAttributeOrParameter();
            String oldSig = oldClass + ":" + oldAttri;
            String newSig = newClass + ":" + newAttri;
            assert mappings.containsKey(oldSig);
            assert mappings.containsKey(oldClass);
            assert mappings.containsKey(newClass);
            CodeBlock attriBlock = mappings.get(oldSig);
            CodeBlock classBlockOld = mappings.get(oldClass);//original class
            CodeBlock classBlockNew = mappings.get(newClass);
            if (attriBlock.getLastHistory().getParentCodeBlock().equals(classBlockNew)) {
                return;
            }// 如果已经在前边移动过了，就结束本次；如果还没有移动过，就进行迁移
            assert classBlockOld.equals(attriBlock.getLastHistory().getParentCodeBlock());

            // create new attributeTime, update parentBlock; create two classTime for oldClassBlock and newClassBlock, move from oldTime to newTime
            AttributeTime attriTime = (AttributeTime) attriBlock.getLastHistory().clone();
            attriTime.setName(newAttri);
            attriTime.setTime(commitTime);
            attriTime.setRefactorType(Operator.Move_Attribute);
            attriTime.setParentCodeBlock(classBlockNew);
            commitTime.addCodeChange(attriTime);
            attriBlock.addHistory(attriTime);
            mappings.put(newSig, attriBlock);

            //remove from oldClass
            ClassTime oldClassTimeNew = (ClassTime) classBlockOld.getLastHistory().clone();
            oldClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(oldClassTimeNew);
            oldClassTimeNew.setRefactorType(Operator.Move_Attribute);
            assert oldClassTimeNew.getAttributes().contains(attriBlock);
            oldClassTimeNew.getAttributes().remove(attriBlock);
            classBlockOld.addHistory(oldClassTimeNew);
            //add to newClass
            ClassTime newClassTimeNew = (ClassTime) classBlockNew.getLastHistory().clone();
            newClassTimeNew.setTime(commitTime);
            commitTime.addCodeChange(newClassTimeNew);
            newClassTimeNew.setRefactorType(Operator.Move_Attribute);
            newClassTimeNew.getAttributes().add(attriBlock);
            classBlockNew.addHistory(newClassTimeNew);
            System.out.println(r.getType());
        }
    },
    //    Replace_Attribute_(With_Attribute) {
//        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime) {
//            System.out.println(r.getType());
//        }
//    },//TODO 暂时没找到例子
    Replace_Attribute_With_Variable {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //不太确定到底是啥 应该是涉及attribute的弃用 （但是有时候不删除attribute） 影响不大
            System.out.println(r.getType());
        }
    },
    Replace_Anonymous_With_Lambda {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },//TODO 暂时没找到
    ;

    public abstract void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name);

}
