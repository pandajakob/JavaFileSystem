import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {



        try {
            File root = new File("./");

            if (root.exists()) {
                FileTree fileTree = new FileTree(args, root);;
                fileTree.makeTree();
            } else {
                System.out.println("error getting root file");
            }
        } catch(RuntimeException e){
            System.out.println("error: "+e.getMessage());
        }
    }

}

class FileTree {
    private final File root;
    private final List<FileNode> fileNodes = new ArrayList<>();
    private final List<String> args = new ArrayList<>();
    private final int maxDepth;

    private int getPermittedDepth() {
        if (args.contains("-d")) {
            // finds the int after -d
            String intString = args.get(args.indexOf("-d")+1);
            args.remove("-d");
            args.remove(intString);

            return Integer.parseInt(intString);
        } else {
            return 1000;
        }
    }

    FileTree(String[] args, File root) {
        this.root = root;
        this.args.addAll(List.of(args));
        this.maxDepth = getPermittedDepth();
    }

    public void makeTree() {
        getChildren(root, 0);

        for (FileNode fileNode : fileNodes) {
                fileNode.printNodeString();
        }

    }

    private void getChildren(File file, int depth) {
        File[] children = file.listFiles();
        if (children == null || depth == maxDepth)
            return;

        for (File child : children) {
            if (args.contains(child.getName()))
                continue;

            fileNodes.add(new FileNode(child, depth));
            getChildren(child, depth+1);
        }
    }
}

class FileNode {
    private final int depth;
    private final File file;


    public FileNode(File file, int depth) {
        this.file = file;
        this.depth = depth;
    }

    private File getParentFile() {
        File parentFile = this.file.getParentFile();

        if (parentFile != null)
            return parentFile;
        else
            throw new NoSuchElementException("No parent element for file or is root" + file.getName());
    }

    private boolean isLastNode() {
        if (file.getName().equals(".")) {
            return true;
        }
        return List.of(getParentFile().listFiles()).getLast().equals(file);
    }

    /* │ ├ ─ */
    public void printNodeString() {
        StringBuilder printString = new StringBuilder();

        if (depth==0)
            if (isLastNode())
                printString.append("└── ");
            else
                printString.append("├── ");

        for (int i = 0; i < depth; i++)
            printString.append("  ");

        if (depth>0)
            if (isLastNode())
                printString.append("└── ");
            else
                printString.append("├── ");


        printString.append(file.getName());

        if (file.isDirectory()) {
            printString.append("/");
        }
        System.out.println(printString);
    }

    public File getFile() {
        return file;
    }

    public int getDepth() {
        return depth;
    }
}