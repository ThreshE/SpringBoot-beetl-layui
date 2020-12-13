package com.ibeetl.admin.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.ibeetl.admin.core.util.PlatformException;
/**
 * 在地文件係統
 * @author xiandafu
 *
 */
class LocalFileItem extends PersistFileItem{
    String root = null;
    public LocalFileItem(String root) {
        this.root = root;
    }
    public OutputStream openOutpuStream() {
        File file = new File(root + File.separator + path);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            return fos;
        } catch (IOException e) {
            throw new PlatformException("Open stream error "+path);
        }
        
    }

    @Override
    public void copy(OutputStream os) {
        File file = new File(root + File.separator + path);
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
          
        }catch(Exception ex) {
            throw new PlatformException("下載文件失敗"+ex);
        }
        finally {
            try {
                input.close();
                os.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }

        if(path.startsWith("temp")) {
            this.delete();
        }
        
    }

    @Override
    public boolean delete() {
        File file = new File(root + File.separator + path);
        return file.delete();
        
    }
   
    
}

