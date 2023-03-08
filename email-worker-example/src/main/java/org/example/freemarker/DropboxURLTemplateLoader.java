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

	private String downloadFilesUrl;
	private String templatesPath;
	private String dropboxToken;

	public DropboxURLTemplateLoader(String downloadFilesUrl, String templatesPath, String dropboxToken) {

		this.downloadFilesUrl = downloadFilesUrl;
		this.templatesPath = templatesPath;
		this.dropboxToken = dropboxToken;
	}

	@Override
	protected URL getURL(String name) {

		URL url = null;

		try {

			url = new URL(null, downloadFilesUrl, new URLStreamHandler() {

				@Override
				protected URLConnection openConnection(URL u) throws IOException {

					URL target;
					URLConnection connection;

					target = new URL(u.toString());
					connection = target.openConnection();

					connection.setRequestProperty("Authorization", dropboxToken);
					connection.setRequestProperty("Dropbox-API-Arg", "{\"path\": \"" + templatesPath + name + "\"}");

					return connection;
				}
			});

		} catch (Exception e) {

			LOG.error("Error: " + e.getMessage());
		}

		return url;
	}

}
