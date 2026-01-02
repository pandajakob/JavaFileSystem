import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            File root = new File("./");

            if (root.exists()) {
                FileTree fileTree = new FileTree(args, root);;
                fileTree.makeTree();

                // search mode
                List<String> argsList = List.of(args);
                if (argsList.contains("search")) {
                    String searchString = argsList.get(argsList.indexOf("search")+1);
                    File searchFile = fileTree.searchFor(searchString);
                    System.out.println(searchFile.getAbsolutePath());
                } else {
                    fileTree.printTree();
                }
            } else {
                System.out.println("error getting root file");
            }
        } catch(RuntimeException | FileNotFoundException e){
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

            try {
                return Integer.parseInt(intString);
            } catch (NumberFormatException e) {
                throw new InputMismatchException("depth is not defined or not an int. Remember to add an int after \"-d\"");
            }
        } else {
            return 1000; // maxDepth
        }
    }

    FileTree(String[] args, File root) {
        this.root = root;
        this.args.addAll(List.of(args));
        this.maxDepth = getPermittedDepth();
        if (this.args.contains("search")) {
            int idx = this.args.indexOf("search");
            this.args.remove(idx);
            this.args.remove(idx);
        }
    }

    public void makeTree() {
        getChildren(root, 0);
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
    public void printTree() {
        for (FileNode fileNode : fileNodes) {
            fileNode.printNodeString();
        }
    }

    public File searchFor(String searchString) throws FileNotFoundException {
        for (FileNode fileNode : getFileNodes()) {
            if (fileNode.getFileName().equalsIgnoreCase(searchString)) {
                return fileNode.getFile();
            }
        }
        throw new FileNotFoundException("Could not find file with name " + searchString);

    }

    public List<FileNode> getFileNodes() {
        return fileNodes;
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

    public String getFileName() {
        return getFile().getName();
    }
}