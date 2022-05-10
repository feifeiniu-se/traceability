import Constructor.Constructor;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Project.Project;
import Project.RefactoringMiner.Commits;
import Project.Utils.CommitHashCode;

import java.util.List;

public class start {

    public static void main(String[] args){

        String info[] = new String[]{
                "derby 20d9cee957c322cb5382eab53abe95d4ab37521a 7f3984246d625b811bf2557d75d26835229f747a",
                "groovy-core 449ebe9d9514ec278682ca73d432257c435a27f5 01309f9d4be34ddf93c4a9943b5a97843bff6181",
                "hornetq 1a9aa377d578e1ca8bb7aa4cf99ab53d9183801e fdc19ebf7e456571860ec229a504bf73a2b4cb8d",
                "izpack 55e619ea6e9dcc9c6c0bad01e1bc3430a726381b dc346dbd228d628c242780e936465b49f31a9446",
                "narayana 41b827234a532a37f4c5c424319acc43e8b4c49f de330d21a54b3d0bcda32e82aeff93e34c1d2271",
                "railo ce51a97c9054cfeb15bf9200ed638f02f0e2b9ae ca2389a7abbfeeddbcdba6d4e78a194287f60a04",
                "Resteasy 1bc32b87dcd3a8694eabf518cf1632a8f11fe747 90e70c2b1d938f1ebccd1ef40cec1c0b86d8b9bf",
                "spark df29d0ea4c8b7137fdd1844219c7d489e3b0d9c9 c13b60e0194c90156e74d10b19f94c70675d21ae",
                "switchyard 281bf4cefc504fe896ac436f87b4a4ec1f214da6 4976a88187b807569438e3beba7337ec0f748bf5",
                "wildfly 033fc94f0829b291e7a458e3b776217924d1b445 5303f8d68e1eee0a44e03febf697f31fb880eae7",
                "zookeeper 047d9258a4730791b85cc81b0e1435465a32acbf f299303add79250ec2181f6c03b15e3754825284",
                "jboss-seam ba413e17a9916976bff4f17055685ba96ddcbc8a 965d4f3ea4dd527a41402f4758878de02d5ede7d",
                "infinispan d58d881a60929eddbc944cfade618eed563266fc dace20e9f6c4366f51f89c96c4fac0ed7d2e05ca",
                "errai ed8b7efdc66380f3da37c547f358e8972aafc8ba 295e90f7d665e6bc98c8d182578462d039e8e2d1",
                "kafka 642da2f28c9bc6e373603d6d9119ce33684090f5 83b8cf96f92242d61db9e87aafce1e8dc457c1a1",
                "keycloak 503cede15eb8a5877a2e91a639286dbfcfb86fbd 8e53ccf5abb4d7cc3ab8d5abc9d078a7f8725e8a",
                "maven d5a0360d8d418cbc12be605a0676a0e9ba8902db f5f76c70e1828a7e6c6267fc4bc53abc35c19ce7"
        };

        Project p = new Project(info[2].split(" "));
        Constructor constructor = new Constructor(p);
        constructor.start();// start code analysis
        List<CodeBlock> codeBlocks = constructor.getCodeBlocks();
        List<CommitCodeChange> commits = constructor.getCodeChange();
        //todo save to file/database
//        save(codeBlocks, commits);
        System.out.println(codeBlocks.size());
        System.out.println(commits.size());


    }
}
