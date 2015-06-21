package phibao37.support.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Build the HTTP request and get the response content with costom output
 *  */
public class HTTPBuilder {
	
	protected static final String S_DEFAULT_CHARSET = "utf-8";
	protected static final int DEFAULT_BUFFER_SIZE = 4096;
	
	protected String mUrl;
	protected String mCharset;
	protected Charset charset;
	protected List<NameValuePair> listGet;
	protected HttpParams mHttpParams;
	protected HttpUriRequest mHttpRequest;
	
	protected List<NameValuePair> listPost;
	protected List<Multipart> listPart;
	
	/**
	 * Create a HTTP Builder with default charset (utf-8)<br/>
	 * By default, this will produce a http GET request to the server,
	 * if you want to make a POST request, then call {@link HTTPBuilder#setPostType(int)}
	 */
	public HTTPBuilder(String url){
		this(url, S_DEFAULT_CHARSET);
	}
	
	/**
	 * Create a HTTP Builder with costom charset<br/>
	 * By default, this will produce a http GET request to the server,
	 * if you want to make a POST request, then call {@link HTTPBuilder#setPostType(int)}
	 */
	public HTTPBuilder(String url, String charset){
		mUrl = url;
		mCharset = charset;
		this.charset = Charset.forName(charset);
		
		listGet = new ArrayList<NameValuePair>();
		mHttpParams = new BasicHttpParams();
	}

	/** Set the timeout for the HTTP connection<br/>
	 * By default, these timeout are set to infinite
	 * @param connect the timeout until a connection is etablished
	 * @param socket the timeout for waiting for data
	 * @before {@link HTTPBuilder#execute(Class, Object...)}
	 * */
	public HTTPBuilder setTimeout(int connect, int socket){
		HttpConnectionParams.setConnectionTimeout(mHttpParams, connect);
		HttpConnectionParams.setSoTimeout(mHttpParams, socket);
		return this;
	}
	
	/**
	 * Add the parameter to the query URL
	 * @param name parameter key
	 * @param value parameter value
	 * @notes
	 * This method won't work if the URL from the constructor
	 *  already contains any parameter before: <br/>
	 *  <pre>
	 *  new HTTPBuilder("http://abc.com/xyz/").addGet("index", 1) //work
	 *  new HTTPBuilder("http://abc.com/xyz/?a=1").addGet("index", 1) //not work
	 *  </pre>
	 * @before {@link HTTPBuilder#execute(Class, Object...)}
	 */
	public HTTPBuilder addGet(String name, Object value){
		listGet.add(new BasicNameValuePair(name, String.valueOf(value)));
		return this;
	}
	
	/**
	 * Add the POST content to the http request
	 * @param name post parameter key
	 * @param value post parameter value
	 * @after {@link HTTPBuilder#setPostType(int)}
	 * @before {@link HTTPBuilder#execute(Class, Object...)}
	 */
	public HTTPBuilder addPost(String name, Object value){
		if (listPost != null)
			listPost.add(new BasicNameValuePair(name, String.valueOf(value)));
		else
			try {
				listPart.add(new Multipart(name, 
						new StringBody(String.valueOf(value), charset)));
			} catch (Exception e) {}
		return this;
	}
	
	/**
	 * Add the FILE to the post content to the http request 
	 * @param name post file key, you may use both "file" or "file[]"
	 * @param file the File object link to the file that want to upload
	 * @after {@link HTTPBuilder#setPostType(int)}
	 * - you must choose {@link HTTPBuilder#POST_MULTIPART} in order to add file
	 * @before {@link HTTPBuilder#execute(Class, Object...)}
	 */
	public HTTPBuilder addFile(String name, File file){
		listPart.add(new Multipart(name, new FileBody(file)));
		return this;
	}
	
	/**
	 * Execute the request and get the response content
	 * @param responseType the class of the type you want from the HTTP response,
	 *  supported:
	 * <ul>
	 * 	<li>{@link InputStream}</li>
	 * 	<li>{@link File}: get and download response content to the file, <br/>
	 * 	you must specify the first optional argument to File object
	 *  or a String object refer to the absolute path to the file you want to download to
	 *  </li>
	 *  <li>{@link JSONObject}</li>
	 *  <li>{@link JSONArray}</li>
	 *  <li>{@link String}</li>
	 *  <li>Other types are same as {@link String}</li>
	 * </ul>
	 * @param args optional parameter to make the response content
	 * @return the object from <i>responseType</i>, contain the response content
	 * @throws ExecuteException there is a connection error or the connection was aborted
	 * <br/>To determize the case, use {@link ExecuteException#isCancelByUser()}
	 * @throws IOException there are some common case that raise this exception
	 * <ul>
	 * 	<li>The encoding is not supported</li>
	 * 	<li>Can't get the server response stream</li>
	 * 	<li>An error when parsing result to specific type (File, JSON, ...)</li>
	 * </ul>
	 */
	@SuppressWarnings("unchecked")
	public <T> T execute(Class<T> responseType, Object... args)
			throws ExecuteException, IOException{
		if (!listGet.isEmpty())
			mUrl += "?" + URLEncodedUtils.format(listGet, mCharset);
		
		HttpClient httpClient = getClient();
		
		if (listPost != null){
			HttpPost httpPost = new HttpPost(mUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(listPost, mCharset));
            mHttpRequest = httpPost;
            
            System.out.println("Post URL Form");
		}
		else if (listPart != null){
			HttpPost httpPost = new HttpPost(mUrl);
			MultipartEntity entries = new MultipartEntity();
			
			for (Multipart part: listPart)
				entries.addPart(part.name, part.body);
			
            httpPost.setEntity(entries);
            mHttpRequest = httpPost;
            
            System.out.println("Post Multi-part");
		}
		else {
			mHttpRequest = new HttpGet(mUrl);
			System.out.println("Get URL");
		}
		
		HttpResponse httpResponse;
		try{
			httpResponse = httpClient.execute(mHttpRequest);
		} catch (IOException e){
			throw new ExecuteException(e, mHttpRequest.isAborted());
		}
		
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inStream = httpEntity.getContent();
        
        if (responseType == InputStream.class){
        	return (T) inStream;
        }
        else if (responseType == File.class){
        	File mDownload = args[0] instanceof File ?
        			(File)args[0] : new File(args[0].toString());
        	FileOutputStream outStream = new FileOutputStream(mDownload);
        	byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            
            while (-1 != (n = inStream.read(buffer))) {
            	outStream.write(buffer, 0, n);
            }
            outStream.close();
        	
        	return (T) mDownload;
        }
        else {
        	BufferedReader reader = new BufferedReader(
        			new InputStreamReader(inStream, mCharset));
            StringBuilder sb = new StringBuilder();
            String line = null, result = null;
            
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            inStream.close();
            result = sb.toString();
            
            System.out.println(result);
            
			try {
				if (responseType == JSONObject.class)
					return (T) new JSONObject(result);
				if (responseType == JSONArray.class)
					return (T) new JSONArray(result);
			} catch (JSONException e) {
				throw new IOException(e);
			}
            	
            return (T) result;
        }
	}
	
	/***
	 * Abort the current request, do this will cause the 
	 * {@link HTTPBuilder#execute(Class, Object...)} method throw an {@link IOException}
	 * when the connection is waitting for server response.<br/>
	 * This method do nothing when the response has been received and later parsing.
	 */
	public void shutdown(){
		if (mHttpRequest == null)
			return;
		mHttpRequest.abort();
	}
	
	/** Get the HTTP client to execute web service */
	protected HttpClient getClient(){
		return new DefaultHttpClient(mHttpParams);
	}
	
	/** Set the POST type to Url Encoded Form*/
	public static final int POST_URL_ENCODED = 1;
	
	/** Set the POST type to Url Multpart Form Data*/
	public static final int POST_MULTIPART = 2;
	
	/**
	 * Indicate that this HTTP request is a POST request,
	 *  also specify the content type to post <br/>
	 *  Without call this method, the HTTP request is treated as GET request
	 * @param postType the type of the post content:
	 * <ul>
	 * 	<li>{@link HTTPBuilder#POST_URL_ENCODED}: make a Form URL-encoded post request</li>
	 * 	<li>{@link HTTPBuilder#POST_MULTIPART}: make a Mulipart Form-data post request</li>
	 * </ul>
	 */
	public HTTPBuilder setPostType(int postType){
		if (postType == POST_URL_ENCODED){
			listPost = new ArrayList<NameValuePair>();
			listPart = null;
		} else {
			listPost = null;
			listPart = new ArrayList<Multipart>();
		}
		
		return this;
	}
	
	/***
	 * An exception when making a request to the server
	 */
	public static class ExecuteException extends IOException{
		private static final long serialVersionUID = 6071651884841360409L;
		private boolean mCancelByUser;
		
		private ExecuteException(IOException e, boolean byUser){
			super(e);
			mCancelByUser = byUser;
		}
		
		/** Determize the connect was abortted by calling 
		 * {@link WebBuilder#shutdown()} or not*/
		public boolean isCancelByUser(){
			return mCancelByUser;
		}
		
		/** Check if a {@link Exception} is a {@link ExecuteException} 
		 * and is an abortted exception
		 * @param e an exception throw by {@link HTTPBuilder#execute(Class, Object...)}
		 * */
		public static boolean isCancelByUser(Exception e){
			if (e instanceof ExecuteException){
				return ((ExecuteException) e).isCancelByUser();
			}
			return false;
		}
	}
	
	protected class Multipart{
		private String name;
		private ContentBody body;
		
		public Multipart(String name, ContentBody body){
			this.name = name;
			this.body = body;
		}
	}
}
