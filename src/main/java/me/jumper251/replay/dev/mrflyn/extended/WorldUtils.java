package me.jumper251.replay.dev.mrflyn.extended;



import me.jumper251.replay.com.twmacinta.util.MD5;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class WorldUtils {

    public static String hashDirectory(File fileDir){
        try {
            MD5 md5 = new MD5();
            for (File file : FileUtils.listFiles(fileDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE)) {
                System.out.println(file.getName());
                String hashFile = MD5.asHex(MD5.getHash(file));
                md5.Update(hashFile, null);
            }
            return md5.asHex();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String hash(byte[] data) {
        try {
            MD5 md5 = new MD5();
                String hashFile = MD5.asHex(data);
                md5.Update(hashFile, null);
            return md5.asHex();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean doesFolderExists(String name, String directory){
        File file = new File(directory);
        if (!file.exists())return false;
//        if(!file.isDirectory())return false;
        String[] files = file.list();
        if (files==null)return false;
        for (String s : files){
            if (s.equals(name))return true;
        }
        return false;
    }

    public static ByteArrayOutputStream createTarGzip(File source) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
             GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(bufferedOutputStream);
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(gzipOutputStream)) {

            tarArchiveOutputStream.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
            tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

            List<File> files = new ArrayList<>(FileUtils.listFiles(
                    source,
                    new RegexFileFilter("^(.*?)"),
                    DirectoryFileFilter.DIRECTORY
            ));

            for (int i = 0; i < files.size(); i++) {
                File currentFile = files.get(i);

                String relativeFilePath = source.toURI().relativize(
                        new File(currentFile.getAbsolutePath()).toURI()).getPath();

                TarArchiveEntry tarEntry = new TarArchiveEntry(currentFile, relativeFilePath);
                tarEntry.setSize(currentFile.length());

                tarArchiveOutputStream.putArchiveEntry(tarEntry);
                tarArchiveOutputStream.write(IOUtils.toByteArray(new FileInputStream(currentFile)));
                tarArchiveOutputStream.closeArchiveEntry();
            }
            tarArchiveOutputStream.close();
            return byteArrayOutputStream;
        }
    }

    public static void extractTarGZ(InputStream in, String dest) {
        TarArchiveInputStream tis = null;
        try {
            // .gz
            GZIPInputStream gzipInputStream = new GZIPInputStream(in);
            //.tar.gz
            tis = new TarArchiveInputStream(gzipInputStream);
            TarArchiveEntry tarEntry = null;
            while ((tarEntry = tis.getNextTarEntry()) != null) {
                System.out.println(" tar entry- " + tarEntry.getName());
                if (tarEntry.isDirectory()) {
                    continue;
                } else {
                    // In case entry is for file ensure parent directory is in place
                    // and write file content to Output Stream
                    File outputFile = new File(dest + File.separator + tarEntry.getName());
                    outputFile.getParentFile().mkdirs();
                    IOUtils.copy(tis, new FileOutputStream(outputFile));
                }
            }
        } catch (IOException ex) {
            System.out.println("Error while untarring a file- " + ex.getMessage());
        } finally {
            if (tis != null) {
                try {
                    tis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
