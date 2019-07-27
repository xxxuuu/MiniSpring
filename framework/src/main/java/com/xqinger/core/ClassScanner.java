package com.xqinger.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 * @author XUQING
 * @date 2019-07-24
 */
public class ClassScanner {
    /**
     * 扫描包下的所有类
     * @param packageName 包名
     * @return 扫描出的Class
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        // 包名转成路径
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            System.out.println(resource);
            // jar包
            if(resource.getProtocol().contains("jar")) {
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarFilePath, path));
            } else if(resource.getProtocol().contains("file")) {
                // 目录
                classList.addAll(getClassesFromDirectory(resource.getFile()));
            }
        }

        return classList;
    }

    /**
     * 从Jar包中获取所有类
     * @param jarFilePath Jar包路径
     * @param packagePath 包路径 用于判断是否是该包下的类
     * @return 扫描出的类
     * @throws IOException
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String packagePath) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            // 在传入的路径下并且是class文件
            if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                // 获取类的全名 把路径转为包名 去掉后缀
                String classFullName = entryName.replace("/", ".")
                        .substring(0, entryName.length() - ".class".length());
                classList.add(Class.forName(classFullName));
            }
        }
        return classList;
    }

    /**
     * 从文件目录中获取所有类
     * @param dir 路径
     * @return 扫描出的类
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassesFromDirectory(String dir) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        // 包的根目录所在目录
        String basePath = ClassScanner.class.getResource("/").getPath();
        // TODO [BUG] ClassScanner.class.getResource("/").getPath();获取的是包在当前模块的目录，传入路径是另一个模块时就会有问题

        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // .class结尾 就丢进classList
                if(file.toString().endsWith(".class")) {
                    // 把包前面的那部分路径和.class去掉
                    System.out.println(basePath);
                    System.out.println(file.toString());
                    String classFullName = file.toString().replace(basePath, "")
                            .replace("/", ".");
                    classFullName = classFullName.substring(0, classFullName.length() - ".class".length());
                    System.out.println(classFullName);

                    try {
                        classList.add(Class.forName(classFullName));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        System.err.println("无法找到类 可能是由于多模块引起的 尝试构建Jar包运行");
                    }
                }

                return FileVisitResult.CONTINUE;
            }
        });

        return classList;
    }
}
