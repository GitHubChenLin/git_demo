package com.chenjf.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.ObjectDirectory;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitective.core.BlobUtils;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Queue;
import java.util.zip.InflaterInputStream;

public class GitUtils {
    public static final String LOCALPATH = "D:/Code/git-space/getContent";

    public static final String LOCALGITFILE = LOCALPATH + "/.git";

    public static String id = "";

    public static void getTypeAndContent() throws Exception {
        FileRepository repository = new FileRepository(LOCALGITFILE);
        ObjectDirectory objectDatabase = repository.getObjectDatabase();

        Git git = new Git(repository);
//        git.branchDelete().setForce(false).setBranchNames("test_branch").call();
//
//
//        git.branchRename().call();
//        git.branchCreate().call();
//        List<Ref> listbranch = git.branchList().call();

        RevWalk revWalk = new RevWalk(repository);
        RevCommit revCommit = revWalk.parseCommit(repository.resolve(Constants.HEAD));
        RevCommit[] parents = revCommit.getParents();
        RevCommit parent = revCommit.getParent(0);
        ObjectId id1 = parent.getId();
        RevCommit revCommit1 = revWalk.parseCommit(id1);
        RevTree tree = revCommit.getTree();
        TreeWalk treeWalk = TreeWalk.forPath(repository, ".", tree);
        while (treeWalk.next()) {
            System.out.println(treeWalk.getNameString());
            System.out.println(treeWalk.getPathString());
        }

//
//        while (treeWalk.next()){
//            System.out.println(treeWalk.getNameString());
//            System.out.println(treeWalk.getPathString());
//        }

        File directory = objectDatabase.getDirectory();

        byte[] output = null;

        File[] files = directory.listFiles();
        if(files.length >0 ){
            File[] files1 = files[0].listFiles();
            if(files1.length>0){
                String id = files1[0].getParentFile().getName()+files1[0].getName();
                ObjectId objectId = ObjectId.fromString(id);
//                FileInputStream fis = new FileInputStream(files1[0]);
//                output = decompress(fis);
//                String s1 = Base64.getEncoder().encodeToString(output);
                ObjectLoader open = objectDatabase.open(objectId);
                output = open.getBytes();
                int type = open.getType();
                String s = new String(output);
                System.out.println(s);
                System.out.println("size: "+ open.getSize());
                System.out.println("type: "+Constants.typeString(type));
            }
        }
    }



    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
        try {
            int i = 1024;
            byte[] buf = new byte[i];

            while ((i = iis.read(buf, 0, i)) > 0) {
                o.write(buf, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }


}
