import Constructor.Constructor;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Persistence.CodeBlockSaver;
import Persistence.CommitCodeChangeSaver;
import Persistence.MappingSaver;
import Project.Project;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class start {

    public static void main(String[] args){

        String info[] = new String[]{
                "axis-axis2-java-core 5e257a10f3d0cff2ff2572a00787d1582a706c87 82e251bd1af51b5106357772b13000cda151b97e",
                "derby f68f62fd81539c77a456631fbb47970ef33bc4fa 0199c2ecf5abe872f229831db89b425932421cd9", //done
                "drools ",//暂时没有refactoring结果
//                "hadoop-common",
                "hadoop-hdfs 6a3ac690e493c7da45bbf2ae2054768c427fd0e1 b2d2a3262c587638db04c2991d48656b3d06275c",
                "hadoop-mapreduce 546d96754ffee3142bcbbf4563c624c053d0ed0d 307cb5b316e10defdbbc228d8cdcdb627191ea15",
                "hornetq 33bc296f4a30336a2d7cf62c34102c959057f73e fdc19ebf7e456571860ec229a504bf73a2b4cb8d ", // done
                "infinispan f43d52c9ae76cd4b15be0dd4eb48f9ad45029be4 dace20e9f6c4366f51f89c96c4fac0ed7d2e05ca",
                "izpack 2129654742f1ce3da1f4852bef44831803becaad dc346dbd228d628c242780e936465b49f31a9446",//done
                "keycloak a0f0e3034890b7548396cc4cdd2572d3266c52be 8e53ccf5abb4d7cc3ab8d5abc9d078a7f8725e8a",//done
                "logging-log4j2 3e6bb87f728a9da48d33cecf9dd02dd09bc1a330 211326b32450cd71282c4328378d51a85d20c6c2",
                "pig afb2ec1dc3d30a761756cf4840528b40a0b2e52d 600c19be4b0e0a4e868f55a939a662fc1248ef4a",
                "railo ce51a97c9054cfeb15bf9200ed638f02f0e2b9ae ca2389a7abbfeeddbcdba6d4e78a194287f60a04",
                "jboss-seam ba413e17a9916976bff4f17055685ba96ddcbc8a 965d4f3ea4dd527a41402f4758878de02d5ede7d",
                "teiid",
                "weld",
                "wildfly 033fc94f0829b291e7a458e3b776217924d1b445 5303f8d68e1eee0a44e03febf697f31fb880eae7",

                "groovy-core", // 可以跑了
                "narayana 41b827234a532a37f4c5c424319acc43e8b4c49f de330d21a54b3d0bcda32e82aeff93e34c1d2271",
                "Resteasy 1bc32b87dcd3a8694eabf518cf1632a8f11fe747 90e70c2b1d938f1ebccd1ef40cec1c0b86d8b9bf",
                "spark df29d0ea4c8b7137fdd1844219c7d489e3b0d9c9 c13b60e0194c90156e74d10b19f94c70675d21ae",
                "switchyard 281bf4cefc504fe896ac436f87b4a4ec1f214da6 4976a88187b807569438e3beba7337ec0f748bf5",
                "zookeeper 047d9258a4730791b85cc81b0e1435465a32acbf f299303add79250ec2181f6c03b15e3754825284",
                "errai ed8b7efdc66380f3da37c547f358e8972aafc8ba 295e90f7d665e6bc98c8d182578462d039e8e2d1",
                "kafka 642da2f28c9bc6e373603d6d9119ce33684090f5 83b8cf96f92242d61db9e87aafce1e8dc457c1a1",
                "maven d5a0360d8d418cbc12be605a0676a0e9ba8902db f5f76c70e1828a7e6c6267fc4bc53abc35c19ce7"
        };

        int i = 1;
        System.out.println(info[i]);
        Project p = new Project(info[i].split(" "));
        String database = "C:\\Users\\Feifei\\dataset\\tracescore\\seam2.sqlite3";
        Constructor constructor = new Constructor(p);
        constructor.start();// start code analysis
        List<CodeBlock> codeBlocks = constructor.getCodeBlocks();  //
        List<CommitCodeChange> commits = constructor.getCodeChange();  // commitId对应hash值，代表在当前commit hash中，纵向
        HashMap<String, CodeBlock> mappings = constructor.getMappings();
        // codeBlockId、commitId可以唯一确定一个codeblocktime，但也有可能是没有东西的
//        save(codeBlocks, commits);

////        log.info("Constructor finished.");
////        log.info("Start to save CommitCodeChange");
//        CommitCodeChangeSaver commitCodeChangeSaver = new CommitCodeChangeSaver(database);
//        commitCodeChangeSaver.save(commits);
//        commitCodeChangeSaver.close();
//
//        MappingSaver mappingSaver = new MappingSaver(database);
//        mappingSaver.save(mappings);
//        mappingSaver.close();
//
////        log.info("Start to save CodeBlock");
//        CodeBlockSaver codeBlockSaver = new CodeBlockSaver(database);
//        codeBlockSaver.save(codeBlocks);
//        codeBlockSaver.close();
////        log.info("CodeBlockSaver finished.");

        int x = 0;
        for(CodeBlock cb: codeBlocks){
            x = x + cb.getHistory().size();
        }

        System.out.println(x);
        System.out.println(codeBlocks.size());
        System.out.println(commits.size());
        System.out.println(mappings.size());
    }
}
