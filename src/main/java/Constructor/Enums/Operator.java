package Constructor.Enums;


import Constructor.Utils;
import Model.*;
import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.SideLocation;
import Project.Utils.DiffFile;

import java.util.HashMap;
import java.util.List;

import static Constructor.Utils.codeElement2Method;

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
    Delete_Method {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },
    Add_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },
    Delete_Attribute {
        @Override
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

        }
    },

    //TODO 为每一种refactoring添加commit的codeblocktime信息
    Rename_Package {
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

        }
    },
    Move_Package {
        //TODO create new packageTime,
        //文件重命名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //use mappings to find codeblock, create packagetime, update mapping package, its classes, etc.
            String[] des = r.getDescription().split(" ");
            String leftP = des[2];
            String rightP = des[4];
            //package signature 变更
            assert (mappings.containsKey(leftP));
            CodeBlock packageBlock = mappings.get(leftP);
            PackageTime pkgTime = new PackageTime(rightP, commitTime, Operator.Move_Package, packageBlock);
            if (pkgTime.getOwner() != null) {
                //create packagetimes for owner
            }
            mappings.put(rightP, packageBlock);
            //
            for (CodeBlock classBlock : pkgTime.getClasses()) {
                classBlock.updateMapping(leftP, rightP);
            }

        }
    },
    Split_Package {
        //文件重命名
        //TODO create new package, create new packageTime, classTime, methodTime, attributeTime to update the filePath, go through all the file for class level update
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String[] newPkgNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String oldPkgName = r.getDescription().split(" ")[2];
            assert mappings.containsKey(oldPkgName);
            CodeBlock oldPkgBlock = mappings.get(oldPkgName);
            PackageTime newPkgTime4Old = new PackageTime(oldPkgName, commitTime, Operator.Split_Package, oldPkgBlock);

            //TODO 先假设两个新包名字不一样，直接新增两个新的包
            assert !newPkgNames[0].equals(newPkgNames[1]);
            for (String pkgName : newPkgNames) {
                CodeBlock pkgBlockNew = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
                PackageTime pkgTime = new PackageTime(pkgName, commitTime, Operator.Split_Package, newPkgTime4Old, pkgBlockNew);
                mappings.put(pkgName, pkgBlockNew);
            }
        }
    },
    Merge_Package {
        //文件重命名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String[] oldPkgNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String[] desc = r.getDescription().split(" ");
            String newPkgName = desc[desc.length - 1];
            CodeBlock newPkgBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
            PackageTime newPkgTime = new PackageTime(newPkgName, commitTime, Operator.Merge_Package, newPkgBlock);
            mappings.put(newPkgName, newPkgBlock);

            for (String pkgName : oldPkgNames) {
                assert mappings.containsKey(pkgName);
                CodeBlock oldBlock = mappings.get(pkgName);
                PackageTime newPkgTime4Old = new PackageTime(pkgName, commitTime, Operator.Merge_Package, newPkgTime, oldBlock);
            }


        }
    },
    Change_Type_Declaration_Kind {//interface class等更改 一般不会影响codeblock

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            assert r.getDescription().split(" ").length == 10;
            assert r.getLeftSideLocations().size() == r.getRightSideLocations().size();
            assert r.getLeftSideLocations().size() == 1;
            assert r.getLeftSideLocations().get(0).getFilePath().equals(r.getRightSideLocations().get(0).getFilePath());
//            System.out.println(r.getType());
        }
    },
    Collapse_Hierarchy {//较为复杂的 将一个具体的类的内容移到接口类中进行实现

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String oldName = r.getDescription().split(" ")[2];
            String newName = r.getDescription().split(" ")[4];
            assert mappings.containsKey(oldName);
            assert mappings.containsKey(newName);
            ClassTime oldClassNew = new ClassTime((ClassTime) mappings.get(oldName).getLastHistory(), commitTime, Operator.Collapse_Hierarchy);
            ClassTime newClassNew = new ClassTime((ClassTime) mappings.get(newName).getLastHistory(), commitTime, Operator.Collapse_Hierarchy);
            oldClassNew.getDerivee().add(newClassNew);
            newClassNew.getDeriver().add(oldClassNew);
            assert r.getLeftSideLocations().size() == r.getRightSideLocations().size();
            assert r.getLeftSideLocations().size() == 1;
        }
    },
    Extract_Superclass {
        //add a new class, the last filepath on the rightfilepath is the new superclass
        //derive
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String[] oldNames = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String newName = r.getDescription().split(" ")[2];
            CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
            ClassTime classTime = new ClassTime(newName, commitTime, Operator.Extract_Superclass, codeBlock);
            mappings.put(newName, codeBlock);

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size() + 1;
            assert left.size() == oldNames.length;

        }
    },
    Extract_Interface {
        //TODO ***
        //文件重命名 这个有点特殊
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String newName = r.getDescription().split(" ")[2];
            String[] oldName = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            assert !mappings.containsKey(newName);
            CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
            ClassTime classTime = new ClassTime(newName, commitTime, Operator.Extract_Interface, codeBlock);
            mappings.put(newName, codeBlock);

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            String rightFileName = right.get(right.size() - 1).getFilePath();
            assert fileList.containsKey(rightFileName);
            DiffFile rightFile = fileList.get(rightFileName);
            assert left.size() == right.size() - 1;
            assert left.size() == oldName.length;
            if (right.get(right.size() - 2).getFilePath().contains(newName.substring(newName.lastIndexOf(".") + 1))) {
                //不是内部类
                for (int i = 0; i < left.size(); i++) {
                    assert mappings.containsKey(oldName[i]);
                    ClassTime originalClassTimeOld = (ClassTime) mappings.get(oldName[i]).getLastHistory();
                    ClassTime originalClassTimeNew = new ClassTime(originalClassTimeOld, commitTime, Operator.Extract_Interface);
                    originalClassTimeNew.getDerivee().add(classTime);
                    classTime.getDeriver().add(originalClassTimeNew);//更新deriver和derivee信息
                    //更新文件继承信息

                }
            } else {
                System.out.println("Inner class:");
            }
        }
    },
    Extract_Class {
        //将原来代码中的一些方法抽出来，放到新建的类中。新建一个类，将一些方法从旧的类中移到新的类中
        //derive modify
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            assert r.getDescription().split(" ").length == 6;
            String newClassName = r.getDescription().split(" ")[2];
            String oldClassName = r.getDescription().split(" ")[5];

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == 1;
            if (left.get(0).getFilePath().contains(oldClassName.substring(oldClassName.lastIndexOf(".") + 1)) && right.get(1).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".") + 1))) {
                //如果不是内部类
                CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                ClassTime classTime = new ClassTime(newClassName, commitTime, Operator.Extract_Class, codeBlock);
                mappings.put(newClassName, codeBlock);

                assert mappings.containsKey(oldClassName);
                ClassTime oldClassTime = (ClassTime) mappings.get(oldClassName).getLastHistory();
                ClassTime oldClassTimeNew = new ClassTime(oldClassTime, commitTime, Operator.Extract_Class);
                oldClassTimeNew.getDerivee().add(classTime);
                classTime.getDeriver().add(oldClassTimeNew);//添加deriver关系
                //todo 把right中剩下的所有的方法移到newClass中

                for (int i = 2; i < right.size(); i++) {
//                    assert mappings.containsKey(oldClassName.substring(oldClassName.lastIndexOf(".")+1)+":")

                }
            } else {
                System.out.println("Inner class: extract class");
            }
//            //TODO 左边第一个是原始类，右边第一个是原始类，第二个是新的类，剩下的是移动的方法
////            TODO move methods from A to B
        }
    },
    Extract_Subclass {
        //derive class 跟extract class几乎差不多
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            assert r.getDescription().split(" ").length == 6;
            String newClassName = r.getDescription().split(" ")[2];
            String oldClassName = r.getDescription().split(" ")[5];

            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == 1;
            if (left.get(0).getFilePath().contains(oldClassName.substring(oldClassName.lastIndexOf(".") + 1)) && right.get(1).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".") + 1))) {
                //如果不是内部类
                CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                ClassTime classTime = new ClassTime(newClassName, commitTime, Operator.Extract_Class, codeBlock);
                mappings.put(newClassName, codeBlock);

                assert mappings.containsKey(oldClassName);
                ClassTime oldClassTime = (ClassTime) mappings.get(oldClassName).getLastHistory();
                ClassTime oldClassTimeNew = new ClassTime(oldClassTime, commitTime, Operator.Extract_Class);
                oldClassTimeNew.getDerivee().add(classTime);
                classTime.getDeriver().add(oldClassTimeNew);//添加deriver关系
                //todo 把right中剩下的所有的方法移到newClass中

                for (int i = 2; i < right.size(); i++) {
//                    assert mappings.containsKey(oldClassName.substring(oldClassName.lastIndexOf(".")+1)+":")

                }
            } else {
                System.out.println("Inner class: extract subclass");
            }
        }
    },
    Merge_Class {
        //derive
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String[] oldClasses = Utils.cutString(r.getDescription(), "[", "]").replace(" ", "").split(",");
            String[] desc = r.getDescription().split(" ");
            String newClassName = desc[desc.length - 1];
            CodeBlock newClassBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
            ClassTime newClassTime = new ClassTime(newClassName, commitTime, Operator.Merge_Class, newClassBlock);
            mappings.put(newClassName, newClassBlock);

            assert r.getRightSideLocations().size() == 1;
            if (!r.getRightSideLocations().get(0).getFilePath().contains(newClassName.substring(newClassName.lastIndexOf(".") + 1))) {
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

            assert oldClasses.length == left.size();
            for (int i = 0; i < left.size(); i++) {
                String oldName = oldClasses[i];
                String oldFilePath = left.get(i).getFilePath();
                if (!oldFilePath.contains(oldName.substring(oldName.lastIndexOf(".") + 1))) {
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


            }

        }
    },
    Move_Class {
        //TODO 注意内部类的移动情况 63f3a7ebe4915e51acb8b2a8b0b234e5618239ba 根据left right文件名确定文件的对应关系，然后添加新的classBlockTime 进行修改，以及更新内部方法和属性的文件位置，以及签名
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //todo 需要注意如果是内部类的重命名和移动 7c65d01596100a0962d7e9e0b4a55dce461ff54e
            //todo 内部类的内部类 28ef8c9fc78433add3ee0a08f9f38569cd130958
            String[] desc = r.getDescription().split(" ");
            assert desc.length == 6;
            String oldClass = desc[2];
            String newClass = desc[5];
            assert mappings.containsKey(oldClass);
            CodeBlock oldClassBlock = mappings.get(oldClass);


            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;

            if (left.get(0).getFilePath().contains(oldClass.substring(oldClass.lastIndexOf(".") + 1)) && right.get(0).getFilePath().contains(newClass.substring(newClass.lastIndexOf(".") + 1))) {
                //如果类名在文件路径中出现，就表明不是内部类
                assert !(left.get(0).getFilePath().equals(right.get(0).getFilePath()));
                //todo 需要将旧的time中的parents关系复制过来，或者在遍历的时候获得
//                PackageTime pkgTimeOld =(PackageTime) oldClassBlock.getLastHistory().getParentCodeBlock().getLastHistory();//原父亲的codeBlock
//                PackageTime pkgTimeNew = new PackageTime(pkgTimeOld, commitTime, Operator.Move_Class);
//                pkgTimeNew.getClasses().remove(oldClassBlock);
//                pkgTimeNew.getFilePath().remove(left.get(0).getFilePath());//从旧的父节点中删除

                String newParent = newClass.substring(0, newClass.lastIndexOf("."));//新的父节点
                if (!mappings.containsKey(newParent)) {//如果不存在包 就新建
                    CodeBlock pkgBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
                    mappings.put(newParent, pkgBlock);
                    PackageTime pkgTime = new PackageTime(newParent, commitTime, Operator.Add_Package, pkgBlock);
                }
                assert mappings.containsKey(newParent);
                PackageTime newPkgTime = new PackageTime((PackageTime) mappings.get(newParent).getLastHistory(), commitTime, Operator.Move_Class);
                newPkgTime.getClasses().add(oldClassBlock);

                ClassTime classTime = new ClassTime(newClass, commitTime, Operator.Move_Class, oldClassBlock, newPkgTime.getOwner());
                mappings.put(newClass, mappings.get(oldClass));

                //todo 暂时没改变method的签名 留到最后遍历文件时处理
            } else {
                //todo 内部类的处理，不是文件的重命名，但是要对比两个新旧文件的内容 filePairs.put(leftFile, rightFile)
                System.out.println("Inner class remains to be processed...");
            }
        }
    },
    Rename_Class {
        // 创建文件对应关系，然后对应修改classBlockTime，packageBlockTime的内容，然后在最后统一更新method和attribute的内容。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //todo 需要注意如果是内部类的重命名和移动 7c65d01596100a0962d7e9e0b4a55dce461ff54e
            String[] desc = r.getDescription().split(" ");
            assert desc.length == 6;
            String oldClass = desc[2];
            String newClass = desc[5];

            if (r.getLeftSideLocations().get(0).getFilePath().contains(oldClass.substring(oldClass.lastIndexOf(".") + 1)) && r.getRightSideLocations().get(0).getFilePath().contains(newClass.substring(newClass.lastIndexOf(".") + 1))) {
                assert mappings.containsKey(oldClass);
                CodeBlock oldClassBlosk = mappings.get(oldClass);
                ClassTime classTime = new ClassTime(newClass, commitTime, Operator.Rename_Class, oldClassBlosk, oldClassBlosk.getLastHistory().getParentCodeBlock());
                mappings.put(newClass, mappings.get(oldClass));

                List<SideLocation> left = r.getLeftSideLocations();
                List<SideLocation> right = r.getRightSideLocations();
                assert left.size() == right.size();
                assert left.size() == 1;
//                Collections.replaceAll(classTime.getParentCodeBlock().getLastHistory().getFilePath(), left.get(0).getFilePath(), right.get(0).getFilePath());//直接替换pkgBlock的文件list的内容


                //如果类名在文件路径中出现，就表明不是内部类
                assert !(left.get(0).getFilePath().equals(right.get(0).getFilePath()));
                //todo 暂时没改变method的签名 留到最后遍历文件时处理
            } else {
                //todo 内部类的处理，不是文件的重命名，但是要对比两个新旧文件的内容 filePairs.put(leftFile, rightFile)
                System.out.println("Inner class remains to be processed...");
            }


        }
    },
    Move_And_Rename_Class {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            //todo 需要注意如果是内部类的重命名和移动 13a0d1228e15b2386635f4a7df1d814e7a3fe145
            String[] desc = r.getDescription().split(" ");
            assert desc.length == 10;
            String oldClass = desc[4];
            String newClass = desc[9];
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;

            assert mappings.containsKey(oldClass);
            CodeBlock oldClassBlock = mappings.get(oldClass);

            if (left.get(0).getFilePath().contains(oldClass.substring(oldClass.lastIndexOf(".") + 1)) && right.get(0).getFilePath().contains(newClass.substring(newClass.lastIndexOf(".") + 1))) {
                //如果类名在文件路径中出现，就表明不是内部类
                assert !(left.get(0).getFilePath().equals(right.get(0).getFilePath()));
                PackageTime pkgTimeOld = (PackageTime) oldClassBlock.getLastHistory().getParentCodeBlock().getLastHistory();//原父亲的codeBlock
                PackageTime pkgTimeNew = new PackageTime(pkgTimeOld, commitTime, Operator.Move_And_Rename_Class);
                pkgTimeNew.getClasses().remove(oldClassBlock);

                String newParent = newClass.substring(0, newClass.lastIndexOf("."));//新的父节点
                if (!mappings.containsKey(newParent)) {//如果不存在包 就新建
                    CodeBlock pkgBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Package);
                    mappings.put(newParent, pkgBlock);
                    PackageTime pkgTime = new PackageTime(newParent, commitTime, Operator.Add_Package, pkgBlock);
                }
                assert mappings.containsKey(newParent);
                PackageTime newPkgTime = new PackageTime((PackageTime) mappings.get(newParent).getLastHistory(), commitTime, Operator.Move_And_Rename_Class);
                newPkgTime.getClasses().add(oldClassBlock);//更新 新的父节点

                ClassTime classTime = new ClassTime(newClass, commitTime, Operator.Move_And_Rename_Class, oldClassBlock, newPkgTime.getOwner());
                mappings.put(newClass, mappings.get(oldClass));

                //todo 暂时没改变method的签名 留到最后遍历文件时处理
            } else {
                //todo 内部类的处理，不是文件的重命名，但是要对比两个新旧文件的内容 filePairs.put(leftFile, rightFile)
                System.out.println("Inner class remains to be processed...");
            }
        }
    },
    Extract_Method {
        //add method 从现有方法的代码中抽取部分生成新的方法，methodB derived from methodA， 需要在遍历阶段更新methodBlockTime的信息
        //有可能是从很多方法中提取出的新的方法，所以在存储新的代码块之前需要判断是否已经存在，如果已经存在，就在derived from中添加新的方法
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Inline_Method {
        //与extract method相反，delete 方法，将方法的内容合并到已有的方法中
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Pull_Up_Method {
        //move method from one class to super class,将几个子类中的方法移到超类中，跨文件，涉及方法的移动，还可能修改名字，但是不影响文件数目
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Push_Down_Method {
        //将父类中的方法 移到子类中去，一般会在不同的文件之间进行移动，甚至还有rename
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Extract_And_Move_Method {
        //可能从多个方法中提取出一个新的方法， 涉及新建一个methodBlock，有多个derived from，并且移到了一个新的类中，跨文件
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Move_And_Inline_Method {
        //跨文件，移动，移动旧的方法，并拆散方法到已有的方法中。
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Move_And_Rename_Method {//跨文件

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Move_Method {//跨文件

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Change_Return_Type { //trival 只需要修改返回类型 一般不跨文件

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Rename_Method {//trival 一般不跨文件 只需要修改方法名字

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String[] des = r.getDescription().split(" ");
            String classSig = des[des.length - 1];
            String className = classSig.substring(classSig.lastIndexOf(".") + 1);
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            HashMap<String, String> oldMethod = codeElement2Method(left.get(0).getCodeElement());
            HashMap<String, String> newMethod = codeElement2Method(right.get(0).getCodeElement());
            String oldName = className + ":" + oldMethod.get("MN");
            String newName = className + ":" + newMethod.get("MN");
            assert mappings.containsKey(oldName);
            CodeBlock codeBlock = mappings.get(oldName);
            MethodTime methodTimeOld = (MethodTime) codeBlock.getLastHistory();
            MethodTime methodTimeNew = new MethodTime(methodTimeOld, commitTime, Operator.Rename_Method);
//            methodTimeNew.setSignature(newName);
            methodTimeNew.setParameters(newMethod.get("PA"));
            codeBlock.addHistory(methodTimeNew);
            mappings.put(newName, codeBlock);
//            methodTimeNew.setParameterType(newMethod.get("PT")); //parameterType needs to be updated
        }
    },
    Parameterize_Variable {//方法名级别

        //把方法中的一个变量 变为方法的参数 不跨文件，仅需要修改方法的名字 trivial
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Merge_Parameter {//把一个方法的参数进行合并，但是可能会有移动

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Split_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Change_Parameter_Type {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Add_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {

//            AttributeTime attributeTime = new AttributeTime();
//            attributeTime.setTime(currentTime);

            System.out.println(r.getType());
        }
    },
    Remove_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Reorder_Parameter {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Parameterize_Attribute {//把一个attribute变成一个参数，同时修改属性和方法的参数 一般不跨项目

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Pull_Up_Attribute {//把子类中的属性 移到父类中 跨文件

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Push_Down_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Rename_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            String[] des = r.getDescription().split(" ");
            String classSig = des[des.length - 1];
            String className = classSig.substring(classSig.lastIndexOf(".") + 1);
            List<SideLocation> left = r.getLeftSideLocations();
            List<SideLocation> right = r.getRightSideLocations();
            assert left.size() == right.size();
            assert left.size() == 1;
            assert left.get(0).getFilePath().equals(right.get(0).getFilePath());
            String[] leftCodeElement = left.get(0).getCodeElement().split(" : ");
            String[] rightCodeElement = right.get(0).getCodeElement().split(" : ");
            String oldName = className + ":" + leftCodeElement[1] + "_" + leftCodeElement[0];
            String newName = className + ":" + rightCodeElement[1] + "_" + rightCodeElement[0];
            String returnType = rightCodeElement[1];//todo 返回值类型
            assert mappings.containsKey(oldName);
            CodeBlock codeBlock = mappings.get(oldName);
            MethodTime methodTimeOld = (MethodTime) codeBlock.getLastHistory();
            MethodTime methodTimeNew = new MethodTime(methodTimeOld, commitTime, Operator.Rename_Method);
//            methodTimeNew.setSignature(newName);
//            methodTimeNew.getFilePath().add(right.get(0).getFilePath());
//            methodTimeNew.setParameters(newMethod.get("PA"));
            codeBlock.addHistory(methodTimeNew);
            mappings.put(newName, codeBlock);
        }
    },
    Merge_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Split_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Change_Attribute_Type {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Extract_Attribute {//涉及增加新的attribute

        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Encapsulate_Attribute {
        //Attribute encapsulation is useful when you have an attribute that is affected by several different methods,
        // each of which needs that attribute to be in a known state. To prevent programmers from changing the attribute
        // in the 4GL code, you can make the attribute private so that programmers can only access it from the object's methods.
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },//TODO not_sure_if_neccessarry
    Inline_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Move_And_Rename_Attribute {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    //    Replace_Attribute_(With_Attribute) {
//        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime) {
//            System.out.println(r.getType());
//        }
//    },//TODO 暂时没找到例子
    Replace_Attribute_With_Variable {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },
    Replace_Anonymous_With_Lambda {
        public void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name) {
            System.out.println(r.getType());
        }
    },//TODO 暂时没找到
    ;

    public abstract void apply(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, String name);

}
