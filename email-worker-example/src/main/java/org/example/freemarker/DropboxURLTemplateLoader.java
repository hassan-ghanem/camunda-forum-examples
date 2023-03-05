package org.example.freemarker;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.URLTemplateLoader;

public class DropboxURLTemplateLoader extends URLTemplateLoader {

	protected static final Logger LOG = LoggerFactory.getLogger(DropboxURLTemplateLoader.class);

	private final String DROPBOX_DOWNLOAD_FILE_URL = "https://content.dropboxapi.com/2/files/download";
	private final String TEMPLATES_PATH = "/Email Templates/";
	private final String TOKEN = "Bearer sl.BaCXXXXXXX";

	@Override
	protected URL getURL(String name) {

		URL url = null;

		try {

			url = new URL(null, DROPBOX_DOWNLOAD_FILE_URL, new URLStreamHandler() {

				@Override
				protected URLConnection openConnection(URL u) throws IOException {

					URL target;
					URLConnection connection;

					target = new URL(u.toString());
					connection = target.openConnection();

					connection.setRequestProperty("Authorization", TOKEN);
					connection.setRequestProperty("Dropbox-API-Arg", "{\"path\": \"" + TEMPLATES_PATH + name + "\"}");

					return connection;
				}
			});

		} catch (Exception e) {

			LOG.error("Error: " + e.getMessage());
		}

		return url;
	}

}
