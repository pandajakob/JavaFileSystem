import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        File root = new File("./");
        if (root.exists()) {
            FileTree fileTree = new FileTree(args, root);
            fileTree.makeTree();
        } else {
            System.out.println("error getting root file");
        }
    }

}

class FileTree {
    private final File root;

    private final List<String> args;

    FileTree(String[] args, File root) {
        this.root = root;
        this.args = List.of(args);
    }

    public void makeTree() {
        printChildren(root, 0);

    }

    private void printChildren(File file, int depth) {
        File[] children = file.listFiles();
        if (children == null)
            return;

        for (File child : children) {
            if (args.contains(child.getName()))
                continue;

            print(child, depth);
            printChildren(child, depth+1);
        }
    }

    /* │ ├ ─ */
    private void print(File file, int depth) {
        StringBuilder printString = new StringBuilder();

        if (depth==0) {
            if (isLast(file))
                printString.append("└── ");
            else
                printString.append("├── ");

        }

        for (int i = 0; i < depth; i++) {
            printString.append("  ");
        }
        if (depth>0) {
            if (isLast(file))
                printString.append("└── ");
            else
                printString.append("├── ");
        }

        printString.append(file.getName());

        if (file.isDirectory()) {
            printString.append("/");
        }
        System.out.println(printString);
    }
    private boolean isLast(File file) {
        return List.of(Objects.requireNonNull(file.getParentFile().listFiles())).getLast().equals(file);
    }

}