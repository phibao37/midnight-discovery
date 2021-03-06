package phibao37.support.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * The extended of {@link HTTPBuilder} that support manager cookie
 */
public class WebBuilder extends HTTPBuilder {
	
	private DefaultHttpClient mClient;
	private CookieStore mCookies;
	private URL mURL;
	
	/**
	 * Create a blank Web Builder with default charset (utf-8)<br/>
	 * To set the URL, call {@link WebBuilder#setURL(String)}<br/>
	 * By default, this will produce a http GET request to the server,
	 * if you want to make a POST request, then call {@link WebBuilder#setPostType(int)}
	 */
	public WebBuilder(){
		this(null, S_DEFAULT_CHARSET);
	}
	
	/**
	 * Create a Web Builder with default charset (utf-8)<br/>
	 * By default, this will produce a http GET request to the server,
	 * if you want to make a POST request, then call {@link WebBuilder#setPostType(int)}
	 */
	public WebBuilder(String url) {
		this(url, S_DEFAULT_CHARSET);
	}
	
	/**
	 * Create a Web Builder with costom charset<br/>
	 * By default, this will produce a http GET request to the server,
	 * if you want to make a POST request, then call {@link WebBuilder#setPostType(int)}
	 */
	public WebBuilder(String url, String charset) {
		super(url, charset);
		
		mClient = new DefaultHttpClient();
		mCookies = mClient.getCookieStore();
		if (url != null)
			setURL(url);
	}
	
	/**
	 * Set the URL for this builder
	 */
	public WebBuilder setURL(String url){
		try{
			mUrl = url;
			mURL = new URL(url);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		return this;
	}
	
	/**
	 * Add a cookie with expire date set to null
	 * @param name cookie name
	 * @param value cookie value
	 * @after {@link WebBuilder#setURL(String)}
	 * @before {@link WebBuilder#execute(Class, Object...)}
	 */
	public WebBuilder addCookie(String name, String value){
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		
		cookie.setDomain(mURL.getHost());
		mCookies.addCookie(cookie);
		return this;
	}
	
	/**
	 * Add a cookie with expire date set to spicific time after the time call this method
	 * @param name cookie name
	 * @param value cookie value
	 * @param expire_type the type of expire time will be "plus" to remain valid, such as:
	 * {@link Calendar#HOUR_OF_DAY}, {@link Calendar#DATE}, ...
	 * @param expire_value the amount of expire_type that this cookie still remain valid 
	 * @after {@link WebBuilder#setURL(String)}
	 * @before {@link WebBuilder#execute(Class, Object...)}
	 */
	public WebBuilder addCookie(String name, String value,
			int expire_type, int expire_value){
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		Calendar calendar = Calendar.getInstance();
		
		calendar.add(expire_type, expire_value);
		cookie.setExpiryDate(calendar.getTime());
		cookie.setDomain(mURL.getHost());
		mCookies.addCookie(cookie);
		return this;
	}
	
	
	/** Retrieve all cookies from this builder*/
	public List<Cookie> getCookies(){
		return mCookies.getCookies();
	}
	
	/** Clear all cookie stored in this client*/
	public WebBuilder clearCookie(){
		mCookies.clear();
		return this;
	}
	
	/** Clear all cookie stored in this client that will be expire
	 *  after a specific amount of expire type
	 *  @param expire_type the type of expire time will be "plus" to clear, such as:
	 * {@link Calendar#HOUR_OF_DAY}, {@link Calendar#DATE}, ...
	 * @param expire_value the amount of expire_type that these cookies will be clear 
	 *   */
	public WebBuilder clearCookie(int expire_type, int expire_value){
		Calendar calendar = Calendar.getInstance();
		
		calendar.add(expire_type, expire_value);
		mCookies.clearExpired(calendar.getTime());
		return this;
	}

	@Override
	protected HttpClient getClient() {
		mClient.setParams(mHttpParams);
		return mClient;
	}

	@Override
	public WebBuilder setTimeout(int connect, int socket) {
		return (WebBuilder) super.setTimeout(connect, socket);
	}

	@Override
	public WebBuilder addGet(String name, Object value) {
		return (WebBuilder) super.addGet(name, value);
	}

	@Override
	public WebBuilder addPost(String name, Object value) {
		return (WebBuilder) super.addPost(name, value);
	}

	@Override
	public WebBuilder addFile(String name, File file) {
		return (WebBuilder) super.addFile(name, file);
	}

	/**
	 * Extended version of {@link HTTPBuilder#execute(Class, Object...)}<br/>
	 * In this version, after the response has been parsed; all GET and POST parameter,
	 * the URL, are erased, you must re-specify it when make the following request<br/>
	 * The timeout limit and cookie are still remained
	 * @after {@link WebBuilder#setURL(String)}
	 */
	@Override
	public <T> T execute(Class<T> responseType, Object... args)
			throws ExecuteException, IOException {
		try{
			return super.execute(responseType, args);
		} finally{
			listGet.clear();
			listPost = null;
			listPart = null;
			mUrl = null;
		}
	}

	@Override
	public WebBuilder setPostType(int postType) {
		return (WebBuilder) super.setPostType(postType);
	}
	
	

}
