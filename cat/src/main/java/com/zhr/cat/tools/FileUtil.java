package com.zhr.cat.tools;

import java.io.File;

public class FileUtil {
    public static void main(String[] args) {
        String filePath = "E:\\VIDEO\\电影";
        String regex = "阳光电影www.ygdy8.com.";
        String replacement = "";
        int failedNumbers = changeFilesName(filePath, regex, replacement);
        System.out.println("failedNumbers = " + failedNumbers);
    }

    /**
     * 修改文件名称
     *
     * @param filePath    文件路径
     * @param regex       正则
     * @param replacement 替换的内容
     * @return 修改成功 返回true 否则 返回false
     */

    public static boolean changeFileName(String filePath, String regex, String replacement) {
        File f = new File(filePath);
        File mm = new File(f.getParent() + File.separator + f.getName().replaceAll(regex, replacement));
        return f.renameTo(mm);
    }

    /**
     * @param directory   目录可为文件
     * @param regex       正则
     * @param replacement 替换的内容
     * @return 返回修改失败的文件数量
     */
    ///返回失败的文件数
    public static int changeFilesName(String directory, String regex, String replacement) {
        int number = 0;//文件数量
        File file = new File(directory);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            number = files.length;
            for (int i = 0; i < files.length; i++) {
                if (changeFileName(files[i].getAbsolutePath(), regex, replacement)) {
                    number--;
                }
            }
        } else {
            number = 1;
            if (changeFileName(file.getAbsolutePath(), regex, replacement)) {
                number--;
            }
        }
        return number;
    }
}
