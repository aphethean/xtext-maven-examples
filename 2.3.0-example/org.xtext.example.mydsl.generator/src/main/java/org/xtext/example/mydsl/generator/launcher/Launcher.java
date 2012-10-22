package org.xtext.example.mydsl.generator.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A simple self extracting executable jar.
 * @author aphethean
 *
 */
public class Launcher {
	
    static String MANIFEST = "META-INF/MANIFEST.MF";

	public static void main(String[] args) {
		Launcher launcher = new Launcher();

		// extract the jar
		URL jarFile = launcher.getJarFile();
		List<URL> jarUrls = null;
		if (jarFile != null) {
			try {
				jarUrls = launcher.extract(jarFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		} else {
			throw new IllegalArgumentException("Launcher can only be used in a jar file.");
		}
		
		if (jarUrls != null) {
	        try {
				Class<?> main = launcher.getClass().getClassLoader().loadClass(launcher.getClass().getName());
				Method mainMethod = main.getMethod("launch", new Class[]{String[].class});
				mainMethod.invoke(null, new Object[]{args});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Launcher could not find any jars to load prior to launch.");
		}
	}

	public static void launch(String[] args) {
		Main.main(args);
	}

	private URL getJarFile() {
		URL jarUrl = this.getClass().getProtectionDomain().getCodeSource().getLocation();
		return jarUrl;
	}
	
    /**
     * Extract the Jetty Jars from the war
     *
     * @throws IOException
     */
    private List<URL> extract(URL jarPath) throws IOException {
//        String javaTmpDir = System.getProperty("java.io.tmpdir");
//        File tmpDir = new File(javaTmpDir, ".launcher");
    	// extract to temp directory
/*
    	File tmpDir = File.createTempFile("launcher", null);
        if(!(tmpDir.delete()))
            throw new IOException("Could not delete temp file: " + tmpDir.getAbsolutePath());
        if(!(tmpDir.mkdir()))
            throw new IOException("Could not create temp directory: " + tmpDir.getAbsolutePath());
        tmpDir.deleteOnExit();
*/
    	// extract to the current directory of the jar so the Class-Path from manifest will resolve correctly
    	String filePath = jarPath.getPath();
       String path = filePath.substring(0, filePath.lastIndexOf("/"));
    	File tmpDir = new File(path);
        System.out.println("tmpDir " + tmpDir);
        
        JarFile jarFile = new JarFile(jarPath.getPath());
        List<URL> jarUrls = new ArrayList<URL>();
        InputStream inStream = null;
        try {

        	Enumeration<JarEntry> entries = jarFile.entries();
        	while (entries.hasMoreElements()) {
        		JarEntry jarEntry = entries.nextElement();
        		String jarEntryName = jarEntry.getName();
                File tmpFile = new File(tmpDir, jarEntryName);
        		// only extract the jars for the MANIFEST.MF/Class-Path
				if (jarEntry.isDirectory()) {
					tmpFile.mkdir();
			        continue;
				} else if (jarEntryName == null || !jarEntryName.endsWith(".jar")) {
        			continue;
				}

                inStream = jarFile.getInputStream(jarEntry);
                OutputStream outStream = new FileOutputStream(tmpFile);
                try {
                    byte[] buffer = new byte[8192];
                    int readLength;
                    while ((readLength = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, readLength);
                    }
                } catch (IOException e) {
                    throw new IOException("Failed to extract " + jarEntry.getName() + " to " + tmpDir, e);
                } finally {
                    outStream.close();
                    tmpFile.deleteOnExit();
                }

                System.out.println("Extracted " + jarEntry.getName() + " to " + tmpFile);
                jarUrls.add(tmpFile.toURI().toURL());
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }

        return jarUrls;
    }
}