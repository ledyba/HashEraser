package hasheraser;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;

public class Match {
    private boolean Recurrent = false;
    private File Folder;
    private Hash Hash;
    private ArrayList<HashItem> HashList;
    public Match(File folder, boolean R) {
        Recurrent = R;
        if (folder.isDirectory()) {
            Folder = folder;
        } else {
            Folder = folder.getParentFile();
        }
        Hash = new Hash();
        HashList = new ArrayList<HashItem>();
    }

    public File[][] match() {
        search(Folder);
        int dupe_cnt = 0;
        int max = tmpHashCnt;
        for(int i=0;i<max;i++){
            if (tmpHashItem[i].isOverlapped()) {
                dupe_cnt++;
            }
        }
        File[][] arr = new File[dupe_cnt][];
        int cnt = 0;
        for(int i=0;i<max;i++){
            if (tmpHashItem[i].isOverlapped()) {
                arr[cnt] = tmpHashItem[i].getArray();
                cnt++;
            }
        }
        return arr;
    }

    private void search(File folder) {
        try {
            System.out.println("\nEntering:" + folder.getCanonicalPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        File[] file = folder.listFiles();
        for (int i = 0; i < file.length; i++) {
            File f = file[i];
            if (f.isDirectory()) {
                if (Recurrent) {
                    search(f);
                }
                continue;
            }
            checkFile(f);
        }
    }
    private HashItem[] tmpHashItem = new HashItem[65536];
    private int tmpHashCnt = 0;
    private void checkFile(File file) {
        final byte[] hash = Hash.getHash(file);
        int max = tmpHashCnt;
        HashItem item;
        for(int i=0;i<max;i++){
            item = tmpHashItem[i];
            if (item.comp(hash)) { //�n�b�V������v
                System.out.println("Overlapped:" + file.getName());
                item.addItem(file);
                return;
            }
        }
        //�Ⴄ
        addItem(hash,file);
    }
    private void addItem(final byte[] hash,File file){
        HashItem item = new HashItem(hash, file);
        HashList.add(item);
        //�������ēx�m��
        if(tmpHashItem.length < tmpHashCnt){
            int size = tmpHashItem.length+4096;
            tmpHashItem = null;
            System.gc();
            tmpHashItem = new HashItem[size];
            tmpHashItem = HashList.toArray(tmpHashItem);
            tmpHashCnt++;
            return;
        }
        tmpHashItem[tmpHashCnt] = item;
        tmpHashCnt++;
    }
}


class HashItem {
    private final byte[] ItemHash;
    private LinkedList<File> List;
    private int Count = 1;
    public HashItem(final byte[] hash, File file) {
        ItemHash = hash;
        List = new LinkedList<File>();
        List.add(file);
    }

    public boolean comp(byte[] hash) {
        /*
        if(Hash.comp(ItemHash, hash)){
            for(int i=0;i<ItemHash.length;i++){
                System.out.print(ItemHash[i]);
            }
            System.out.println("");
            for(int i=0;i<hash.length;i++){
                System.out.print(hash[i]);
            }
            System.out.println("");
        }
        */
        return Hash.comp(ItemHash, hash);
    }

    public void addItem(File file) {
        Count++;
        List.add(file);
    }

    public boolean isOverlapped() {
        return Count > 1;
    }

    public File[] getArray() {
        File[] array = List.toArray(new File[Count]);
        return array;
    }
}
