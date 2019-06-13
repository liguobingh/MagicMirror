package viomi.com.mojingface.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;

public class FileUtil {

    private static final String TAG = "FileUtil";

    /**
     * 创建文件f,如果已存在，不操作
     *
     * @param path 文件路径
     * @throws IOException
     */
    public static void creatFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            file.createNewFile();
        }
    }

    /***
     * 建立文件路径
     * @param path
     */
    public static void creatDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }


    /**
     * 打印f路径下所有的文件和文件夹
     *
     * @param f 文件对象
     */
    public static void printAllFile(File f) {
        //打印当前文件名
        System.out.println(f.getName());
        //是否是文件夹
        if (f.isDirectory()) {
            //获得该文件夹下所有子文件和子文件夹
            File[] f1 = f.listFiles();
            //循环处理每个对象
            int len = f1.length;
            for (int i = 0; i < len; i++) {
                //递归调用，处理每个文件对象
                printAllFile(f1[i]);
            }
        }
    }

    /**
     * 删除对象f下的所有文件和文件夹（含自身）
     *
     * @param f 文件路径
     */
    public static void deleteAll(File f) {
        if (!f.exists()) {
            LogUtils.e(TAG, " the file is not exists");
            return;
        }
        //文件
        if (f.isFile()) {
            if (f.delete())
                LogUtils.e(TAG, "delete success");
        } else { //文件夹
            //获得当前文件夹下的所有子文件和子文件夹
            File f1[] = f.listFiles();
            //循环处理每个对象
            int len = f1.length;
            for (int i = 0; i < len; i++) {
                //递归调用，处理每个文件对象
                deleteAll(f1[i]);
            }
            //删除当前文件夹
            if (f.delete())
                LogUtils.e(TAG, "delete success");
        }
    }

    /**
     * f为文件则删除自身，f为目录则删除目录下的子文件
     *
     * @param f 文件路径
     */
    public static void deleteChildFile(File f) {
        if (!f.exists()) {
            return;
        }
        //文件
        if (f.isFile()) {
            f.delete();
        } else { //文件夹
            //获得当前文件夹下的所有子文件和子文件夹
            File f1[] = f.listFiles();
            //循环处理每个对象
            int len = f1.length;
            for (int i = 0; i < len; i++) {
                //递归调用，处理每个文件对象
                deleteAll(f1[i]);
            }
        }
    }

    /**
     * 读取asset目录下文件。
     *
     * @param context
     * @param file    文件
     * @param code    字符编码 如"utf-8"
     * @return content
     */
    public static String readAssetsFile(Context context, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            InputStream in = context.getAssets().open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取asset目录下文件。
     *
     * @param context
     * @param filename 文件
     * @return 二进制文件
     */
    public static byte[] readAssetsFile(Context context, String filename) {
        try {
            InputStream ins = context.getAssets().open(filename);
            byte[] data = new byte[ins.available()];

            ins.read(data);
            ins.close();

            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static byte[] readFileByBytes(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFile(String fileName) {
        String result = "";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                line++;
                result += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }

    /**
     * 随机读取文件内容
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * A方法追加文件：使用RandomAccessFile
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void appendMethodB(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            LogUtils.e(TAG, "appendMethodB fail !msg=" + e.getMessage());
            e.printStackTrace();
        }
    }

    //写文件  
    public static void writeFile(String fileName, String write_str) throws IOException {
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = write_str.getBytes();
        fos.write(bytes);
        fos.close();
    }

    /***
     * 将一个对象保存到本地(手机内部存储)
     * @param context
     * @param filename 文件名
     * @param object 对象
     */
    public static void saveObject(Context context, String filename, Object object) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /***
     * 从本地(手机内部存储)读取保存的对象
     * @param filename 文件名称
     * @return
     */
    public static Object getObject(Context context, String filename) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(filename);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            LogUtils.e(TAG, "getObject error,msg=" + e.getMessage());
            //e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static File uri2File(Context context, Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        File file = new File(res);
        return file;
    }

    /**
     * 移动指定文件或文件夹(包括所有文件和子文件夹)
     *
     * @param fromDir
     *            要移动的文件或文件夹
     * @param toDir
     *            目标文件夹
     * @throws Exception
     */
    public static void moveFolderAndFileWithSelf(String fromDir, String toDir) throws Exception {
        try {
            File dir = new File(fromDir);
            // 目标
            toDir +=  File.separator + dir.getName();
            File moveDir = new File(toDir);
            if(dir.isDirectory()){
                if (!moveDir.exists()) {
                    moveDir.mkdirs();
                }
            }else{
                File tofile = new File(toDir);
                dir.renameTo(tofile);
                return;
            }

            //System.out.println("dir.isDirectory()"+dir.isDirectory());
            //System.out.println("dir.isFile():"+dir.isFile());

            // 文件一览
            File[] files = dir.listFiles();
            if (files == null)
                return;

            // 文件移动
            for (int i = 0; i < files.length; i++) {
                LogUtils.i("XXX", "文件名："+files[i].getName());
                if (files[i].isDirectory()) {
                    moveFolderAndFileWithSelf(files[i].getPath(), toDir);
                    // 成功，删除原文件
                    files[i].delete();
                }
                File moveFile = new File(moveDir.getPath() + File.separator + files[i].getName());
                // 目标文件夹下存在的话，删除
                if (moveFile.exists()) {
                    moveFile.delete();
                }
                files[i].renameTo(moveFile);
            }
            dir.delete();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 将文件迁移到指定目录
     *
     * @param originPath
     * @param originFileName
     * @param targetPath
     * @param targetFileName
     */
    public static void moveTotherFolders(String originPath, String originFileName,
                                         String targetPath, String targetFileName) {
        String startPath = originPath + File.separator + originFileName;
        String endPath = targetPath + File.separator + targetFileName;

        try {
            File startFile = new File(startPath);
            File tmpFile = new File(endPath);//获取文件夹路径
            if(!tmpFile.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
                tmpFile.mkdirs();
            }
            LogUtils.i("XXX" , "startPath：" + startPath);
            LogUtils.i("XXX" , "endPath：" + endPath);
            if (startFile.renameTo(new File(endPath))) {
                LogUtils.i("XXX" , "File is moved successful!");
            } else {
                LogUtils.i("XXX" ,"File is failed to move!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("XXX" ,e.getMessage());
        }
    }

}
