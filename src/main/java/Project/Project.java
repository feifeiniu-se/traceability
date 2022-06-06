package Project;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import Constructor.Enums.FileType;
import Project.RefactoringMiner.Commits;
import Project.RefactoringMiner.Refactorings;
import Project.Utils.CommitHashCode;
import Project.Utils.DiffFile;
import com.google.gson.Gson;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.refactoringminer.api.GitService;
import org.refactoringminer.util.GitServiceImpl;

import static Project.Utils.ReadJsonFiles.readFile;

@Data
public class Project {
    String name;
    String startHash;
    String endHash;
    String projectAddress;
    String refactoringMinerAddress;
    HashMap<String, Refactorings> refactorings;
    List<CommitHashCode> commitList;
    private static GitService gitService = new GitServiceImpl();


    public Project(String[] info) {
        name = info[0];
        startHash = info[1];
        endHash = info[2];
//        startHash = "43114f176bc646483a1d75a4b0b07a18aec229d5";
//        endHash = "9ed57d1e21e9bb3795e02aaed6bc40696fa51de5";//TODO for test f65d3a05dfec17d851aed9f1b262ee64710b99a7
        projectAddress = "C:\\Users\\Feifei\\dataset\\projects\\" + name;
        refactoringMinerAddress = "C:\\Users\\Feifei\\dataset\\projects\\allRefactorings\\" + name + ".json";
        commitList = getList();
        refactorings = readRefactoring();
    }

    //return the list of commit hashcode between startHash and endHash
    public List<CommitHashCode> getList() {
        //done
        List<CommitHashCode> commits = new ArrayList<>();
        commits.add(new CommitHashCode(null, startHash));
        String last = startHash;
        try (Repository repo = gitService.openRepository(projectAddress); Git git = Git.open(new File(projectAddress))) {
            Iterable<RevCommit> walk = gitService.createRevsWalkBetweenCommits(repo, startHash, endHash);
            Iterator<RevCommit> iterator = walk.iterator();
            while (iterator.hasNext()) {
                RevCommit currentCommit = iterator.next();
                commits.add(new CommitHashCode(last, currentCommit.getId().getName()));
                last = currentCommit.getId().getName();
            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commits;
    }

    // return the source code of project after one commit: hashCode
    public Map<String, String> getSourceCode(String hashCode) {
        //done return all the java file, notice: return list is null
        Map<String, String> res = new HashMap<>();

        try (Repository repository = gitService.openRepository(projectAddress); Git git = Git.open(new File(projectAddress))) {
            RevWalk walk = new RevWalk(repository);
            RevCommit commit = walk.parseCommit(repository.resolve(hashCode));
            res = fileIter(repository, commit);
//            System.out.println(fileContents.size());
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> fileIter(Repository repository, RevCommit commit) {

        Set<String> repositoryDirectories = new LinkedHashSet<>();  // file path
        Map<String, String> fileContents = new LinkedHashMap<>();  // 所有的文件内容，{ key: filePath, value: fileContent }

        // A reference to a tree of subtrees/files.
        RevTree parentTree = commit.getTree();
        // Walks one or more AbstractTreeIterators in parallel.
        try (TreeWalk treeWalk = new TreeWalk(repository)) {
            // Add an already created tree iterator for walking.
            treeWalk.addTree(parentTree);
            // Set the walker to enter (or not enter) subtrees automatically.
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {

                String pathString = treeWalk.getPathString();

                if (pathString.endsWith(".java")) {
                    //获得当前commit下的所有java文件
                    // Obtain the ObjectId for the current entry.
                    ObjectId objectId = treeWalk.getObjectId(0);
                    // Base class for a set of loaders for different representations of Git objects.
                    ObjectLoader loader = repository.open(objectId);
                    StringWriter writer = new StringWriter();
                    // 将 loader 中打开的用于读取 object 数据的 input stream 中的内容拷贝到 writer 中
                    IOUtils.copy(loader.openStream(), writer);
                    if (!writer.toString().contains("<<<<<<< .working")) {//过滤掉文件中包含的非merge冲突文件
                        fileContents.put(pathString, writer.toString());
                    }
                }
                if (pathString.endsWith(".java") && pathString.contains("/")) {
                    // 获取 Java 文件的目录
                    String directory = pathString.substring(0, pathString.lastIndexOf("/"));
                    repositoryDirectories.add(directory);
                    // 包括所有子目录
                    String subDirectory = directory;
                    while (subDirectory.contains("/")) {
                        subDirectory = subDirectory.substring(0, subDirectory.lastIndexOf("/"));
                        repositoryDirectories.add(subDirectory);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContents;
    }

    //return the refactorings
    public HashMap<String, Refactorings> readRefactoring() {
        //done
        String fileContent = readFile(refactoringMinerAddress);

        // 2. 将 JSON 文件中的数据转换为 Java Bean
        Gson gson = new Gson();
        Commits refactors = gson.fromJson(fileContent, Commits.class);
        refactors.filter();//filter out unused refactoring types
        HashMap<String, Refactorings> res = transferRefactorings(refactors.getCommits());// transfer refactorings into hashmap<sha, refactorings> format
        return res;
    }

    private static HashMap<String, Refactorings> transferRefactorings(List<Refactorings> listOfRefactors) {
        HashMap<String, Refactorings> res = new HashMap<>();
        for (Refactorings r : listOfRefactors) {
            if (r.getRefactorings().size() > 0) {
                res.put(r.getSha1(), r);
            }
        }
        return res;
    }

    //return the list of <oldFile, newFile>, which stands for the parent file and current file
    public HashMap<String, DiffFile> getDiffList(CommitHashCode commitHash) {
        //done
        HashMap<String, DiffFile> res = new HashMap<>();
        String parent = commitHash.getParent();
        String hashCode = commitHash.getHashCode();
        if (parent == null) {
            //this is the first commit
            Map<String, String> sourceCode = getSourceCode(hashCode);//all the java code and it's name during this commit
            // set all the files as add
            if (sourceCode.size() < 1) {
                return null;
            }// no new java file
            for (Map.Entry<String, String> entry : sourceCode.entrySet()) {
                res.put(entry.getKey(), new DiffFile(FileType.ADD, entry.getKey(), entry.getValue()));
            }
        } else {
            //get the list of diff between two commits
            Map<String, String> oldCode = getSourceCode(parent);
            Map<String, String> newCode = getSourceCode(hashCode);

            try (Git git = Git.open(new File(projectAddress)); Repository repository = git.getRepository();) {
                final List<DiffEntry> diffs = git.diff()
                        .setOldTree(prepareTreeParser(repository, parent))
                        .setNewTree(prepareTreeParser(repository, hashCode))
                        .setPathFilter(PathSuffixFilter.create(".java"))
                        .call();
                if (diffs.size() < 1) {
                    return null;
                }
//                System.out.println("Found: " + diffs.size() + " differences");
                for (DiffEntry diff : diffs) {
                    //newCode.containsKey()
                    if (diff.getChangeType().name().equals("DELETE")) {
                        res.put(diff.getOldPath(), new DiffFile(FileType.valueOf(diff.getChangeType().name()), diff.getNewPath(), newCode.get(diff.getNewPath()), diff.getOldPath(), oldCode.get(diff.getOldPath())));
                    } else {
                        if(newCode.get(diff.getNewPath())!=null){
                            res.put(diff.getNewPath(), new DiffFile(FileType.valueOf(diff.getChangeType().name()), diff.getNewPath(), newCode.get(diff.getNewPath()), diff.getOldPath(), oldCode.get(diff.getOldPath())));
                        }

                    }
//                    System.out.println("Diff: " + diff.getChangeType() + ": " +
//                            (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
//            System.out.println(diff);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }

        }

        return res;

    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }
}
