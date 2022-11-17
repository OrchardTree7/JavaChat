import java.net.URL;

public class URLGetter {
	
	private URLGetter() {}
	
	public static URL getResource(String filename) {
		 // jar안에서 읽게한다.
		URL url = ClassLoader.getSystemResource(filename);

		//		 jar파일에서 발견되지 않으면 disk로부터 읽는다.
		if (url == null) { 
			try {
				url = new URL("file", "localhost", filename);
			} catch (Exception urlException) {} // ignore
		}
		return url;
	}
}