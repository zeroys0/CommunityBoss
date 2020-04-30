package net.leelink.communityboss.city;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class FileUtil {

    /**
     * 鍒犻櫎鏂囦欢
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚嶏紝瑕佸湪绯荤粺鍐呬繚鎸佸敮涓�
     * @return boolean 瀛樺偍鎴愬姛鐨勬爣蹇�?     */
    public static boolean deleteFile(Context context, String fileName){

        return context.deleteFile(fileName);

    }

    /**
     * 鏂囦欢鏄惁瀛樺�?
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static boolean exists(Context context, String fileName) {

        return new File(context.getFilesDir(), fileName).exists();

    }

    /**
     * 瀛樺偍鏂囨湰鏁版�?
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚嶏紝瑕佸湪绯荤粺鍐呬繚鎸佸敮涓�
     * @param content
     *            鏂囨湰鍐呭
     * @return boolean 瀛樺偍鎴愬姛鐨勬爣蹇�?     */
    public static boolean writeFile(Context context, String fileName, String content){

        boolean success = false;
        FileOutputStream fos = null;
        try{

            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        }
        catch (FileNotFoundException e){

            e.printStackTrace();

        }
        catch (IOException e){

            e.printStackTrace();

        }
        finally{

            try{

                if (fos != null) fos.close();

            }
            catch (IOException ioe){

                ioe.printStackTrace();

            }
        }

        return success;
    }

    public static boolean writeFile(String filePath, String content){

        boolean success = false;
        FileOutputStream fos = null;
        try{

            fos = new FileOutputStream(filePath);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        }
        catch (FileNotFoundException e){

            e.printStackTrace();
        }
        catch (IOException e){

            e.printStackTrace();
        }
        finally{

            try{

                if (fos != null) fos.close();
            }
            catch (IOException ioe){

                ioe.printStackTrace();
            }
        }

        return success;
    }

    /**
     * 璇诲彇鏂囨湰鏁版�?
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚�?     * @return String, 璇诲彇鍒扮殑鏂囨湰鍐呭锛屽け璐ヨ繑鍥�?ull
     */
    public static String readFile(Context context, String fileName){

        if (!exists(context, fileName)) { return null; }
        FileInputStream fis = null;
        String content = null;
        try{

            fis = context.openFileInput(fileName);
            if (fis != null){


                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true){

                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e){

            e.printStackTrace();
        }
        catch (IOException e){

            e.printStackTrace();
            content = null;
        }
        finally{

            try{

                if (fis != null) fis.close();
            }
            catch (IOException ioe){

                ioe.printStackTrace();
            }
        }
        return content;
    }
    public static String readFile(String filePath){

        if (filePath == null || !new File(filePath).exists()) { return null; }
        FileInputStream fis = null;
        String content = null;
        try{

            fis = new FileInputStream(filePath);
            if (fis != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (fis != null) fis.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 璇诲彇鏂囨湰鏁版�?
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚�?     * @return String, 璇诲彇鍒扮殑鏂囨湰鍐呭锛屽け璐ヨ繑鍥�?ull
     */
    public static String readAssets(Context context, String fileName)
    {
        InputStream is = null;
        String content = null;
        try
        {
            is = context.getAssets().open(fileName);
            if (is != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (is != null) is.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 瀛樺偍鍗曚釜Parcelable瀵硅�?
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚嶏紝瑕佸湪绯荤粺鍐呬繚鎸佸敮涓�
     * @param parcelObject
     *            瀵硅薄蹇呴�?瀹炵幇Parcelable
     * @return boolean 瀛樺偍鎴愬姛鐨勬爣蹇�?     */
    public static boolean writeParcelable(Context context, String fileName, Parcelable parcelObject)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Parcel parcel = Parcel.obtain();
            parcel.writeParcelable(parcelObject, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            byte[] data = parcel.marshall();
            fos.write(data);

            success = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * 瀛樺偍List瀵硅�?
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚嶏紝瑕佸湪绯荤粺鍐呬繚鎸佸敮涓�
     * @param list
     *            瀵硅薄鏁扮粍闆嗗悎锛屽璞�?�?��诲疄鐜癙arcelable
     * @return boolean 瀛樺偍鎴愬姛鐨勬爣蹇�?     */
    public static boolean writeParcelableList(Context context, String fileName, List<Parcelable> list)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            if (list instanceof List)
            {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                Parcel parcel = Parcel.obtain();
                parcel.writeList(list);
                byte[] data = parcel.marshall();
                fos.write(data);

                success = true;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * 璇诲彇鍗曚釜鏁版嵁�?硅薄
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚�?     * @return Parcelable, 璇诲彇鍒扮殑Parcelable瀵硅薄锛屽け璐ヨ繑鍥瀗ull
     */
    @SuppressWarnings("unchecked")
    public static Parcelable readParcelable(Context context, String fileName, ClassLoader classLoader)
    {
        Parcelable parcelable = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                parcelable = parcel.readParcelable(classLoader);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            parcelable = null;
        }
        finally
        {
            if (fis != null) try
            {
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (bos != null) try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return parcelable;
    }

    /**
     * 璇诲彇鏁版嵁瀵硅薄鍒楄�?
     * 
     * @param context
     *            绋嬪簭涓婁笅鏂�
     * @param fileName
     *            鏂囦欢鍚�?     * @return List, 璇诲彇鍒扮殑瀵硅薄鏁扮粍锛屽け璐ヨ繑鍥�?ull
     */
    @SuppressWarnings("unchecked")
    public static List<Parcelable> readParcelableList(Context context, String fileName, ClassLoader classLoader)
    {
        List<Parcelable> results = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                results = parcel.readArrayList(classLoader);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            results = null;
        }
        finally
        {
            if (fis != null) try
            {
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (bos != null) try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }

    public static boolean saveSerializable(Context context, String fileName, Serializable data)
    {
        boolean success = false;
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            oos.writeObject(data);
            success = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    public static Serializable readSerialLizable(Context context, String fileName)
    {
        Serializable data = null;
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(context.openFileInput(fileName));
            data = (Serializable) ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ois != null)
            {
                try
                {
                    ois.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    /**
     * 浠巃ssets閲岃竟璇诲彇瀛楃涓�?     * 
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 澶嶅埗鏂囦欢
     * 
     * @param srcFile
     * @param dstFile
     * @return
     */
    public static boolean copy(String srcFile, String dstFile)
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {

            File dst = new File(dstFile);
            if (!dst.getParentFile().exists())
            {
                dst.getParentFile().mkdirs();
            }

            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(dstFile);

            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = fis.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

}