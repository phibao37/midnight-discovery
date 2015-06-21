package phibao37.ent.midnightdiscovery;

import org.json.JSONObject;

import phibao37.ent.app.App;
import phibao37.ent.app.ID;
import phibao37.ent.models.User;
import phibao37.ent.utils.SessionManager;
import phibao37.support.models.Response;
import phibao37.support.utils.HTTPBuilder;
import phibao37.support.utils.HTTPBuilder.ExecuteException;
import phibao37.support.utils.WebBuilder;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A Login screen that offers login into an account
 */
public class LoginActivity extends AppCompatActivity{

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private AuthTask mAuthTask = null;
	
	public static final int MIN_USERNAME = 4;
	public static final int MIN_PASSWORD = 6;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mRegFormView;
	private TextView mMessageView;
	
	private WebBuilder mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set up the login form.
		mUsernameView = (EditText) findViewById(R.id.txt_username);
		mPasswordView = (EditText) findViewById(R.id.txt_password);
		
		Button btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		
		findViewById(R.id.btn_lost_password).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLostPasswordDialog();
			}
		});
		
		findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				
				startActivity(intent);
				finish();
			}
		});

		mRegFormView = findViewById(R.id.form_login);
		mProgressView = findViewById(R.id.progress_login);
		mMessageView = (TextView) findViewById(R.id.txt_message);
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClient.shutdown();
			}
		});

		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.ime_login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		 
		mClient = App.getInstance().getWebBuilder();
	}
	
	private void showLostPasswordDialog(){
		final EditText txt = new EditText(this);
		txt.setBackgroundColor(Color.TRANSPARENT);
		txt.setSingleLine();
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.dialog_title_email)
			.setView(txt, 20, 5, 20, 5)
			.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new GetPassword().execute(txt.getText().toString());
				}
			})
			.setNegativeButton(R.string.dialog_cancel, null)
			.create().show();
	}
	
	private void setMessage(int msgResId){
		mMessageView.setText(msgResId);
	}

	/**
	 * Attempts to login the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Store values at the time of the register attempt.
		String username = mUsernameView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		TextView[] requiredView = new TextView[]{mUsernameView, mPasswordView};
		String[] requiredStr = new String[]{username, password};
		
		// Reset errors.
		for (TextView view: requiredView)
			view.setError(null);
		
		do{
			boolean required = true;
			String error = null;
			
			for (int i = 0; i < requiredStr.length; i++)
				if (TextUtils.isEmpty(requiredStr[i])){
					required = false;
					requiredView[i].setError(getString(R.string.error_field_required));
					focusView = requiredView[i];
					break;
				}
			if (!required){
				cancel = true;
				break;
			}
			
			if ((error = isUsernameValid(username)) != null){
				mUsernameView.setError(error);
				focusView = mUsernameView;
				cancel = true;
				break;
			}
			
			if ((error = isPasswordValid(password)) != null){
				mPasswordView.setError(error);
				focusView = mPasswordView;
				cancel = true;
				break;
			}
			
		} while (false);

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			setMessage(R.string.t_logging_in);
			showProgress(true);
			mAuthTask = new AuthTask();
			mAuthTask.execute(username, password);
		}
	}
	
	private String isUsernameValid(String username){
		if (username.length() < MIN_USERNAME)
			return getString(R.string.error_username_short);
		
		return null;
	}
	
	private String isPasswordValid(String password){
		if (password.length() < MIN_PASSWORD)
			return getString(R.string.error_password_short);
		
		return null;
	}
	
	private boolean mShowingProcess;
	
	@Override
	public void onBackPressed() {
		if (mShowingProcess){
			mClient.shutdown();
		}
		else
			super.onBackPressed();
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	public void showProgress(final boolean show) {
		mShowingProcess = show;
		int shortAnimTime = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		mRegFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mRegFormView.setVisibility(show ? View.GONE
								: View.VISIBLE);
					}
				});

		mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
		mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mProgressView.setVisibility(show ? View.VISIBLE
								: View.GONE);
					}
				});
	}

	/**
	 * Represents an asynchronous login task
	 */
	public class AuthTask extends AsyncTask<String, Void, Response> {
		
		@Override
		protected Response doInBackground(String... args) {

			try {
				return Response.parse(
						mClient.setURL(ID.URL_LOGIN)
						.setPostType(HTTPBuilder.POST_URL_ENCODED)
						.addPost(ID.PARAM_USERNAME, args[0])
						.addPost(ID.PARAM_PASSWORD, args[1])
						.execute(JSONObject.class));
			} catch (Exception e) {
				if (ExecuteException.isCancelByUser(e))
					this.cancel(false);
				return Response.make(e.getMessage());
			}

		}

		@Override
		protected void onPostExecute(Response r) {
			mAuthTask = null;
			showProgress(false);
			
			if (r.code == ID.CODE_SUCCESS){
				SessionManager.setUser(User.parseUser(r.getData(JSONObject.class)));
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				return;
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
				.setTitle(R.string.dialog_title)
				.setMessage(r.message)
				.setPositiveButton(R.string.dialog_accept, null);
			
			if (r.code == ID.CODE_ACTIVE)
				builder.setNegativeButton(R.string.dialog_active, 
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new ReActive().execute(mUsernameView.getText().toString());
					}
				});
			
			builder.create().show();
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	public class GetPassword extends AsyncTask<String, Void, Response> {
		
		@Override
		protected void onPreExecute() {
			setMessage(R.string.t_sending_request);
			showProgress(true);
		}

		@Override
		protected Response doInBackground(String... args) {

			try {
				return Response.parse(
						mClient.setURL(ID.URL_GET_PASSWORD)
						.addGet(ID.PARAM_EMAIL, args[0])
						.execute(JSONObject.class));
			} catch (Exception e) {
				if (ExecuteException.isCancelByUser(e))
					this.cancel(false);
				return Response.make(e.getMessage());
			}

		}
		
		@Override
		protected void onCancelled() {
			showProgress(false);
		}

		@Override
		protected void onPostExecute(Response r) {
			showProgress(false);
			
			new AlertDialog.Builder(LoginActivity.this)
				.setTitle(R.string.dialog_title)
				.setMessage(r.message)
				.setPositiveButton(R.string.dialog_accept, null)
				.create().show();
		}
	}
	
	public class ReActive extends AsyncTask<String, Void, Response> {
		
		@Override
		protected void onPreExecute() {
			setMessage(R.string.t_sending_request);
			showProgress(true);
		}

		@Override
		protected Response doInBackground(String... args) {

			try {
				return Response.parse(
						mClient.setURL(ID.URL_REACTIVE)
						.addGet(ID.PARAM_USERNAME, args[0])
						.execute(JSONObject.class));
			} catch (Exception e) {
				if (ExecuteException.isCancelByUser(e))
					this.cancel(false);
				return Response.make(e.getMessage());
			}

		}
		
		@Override
		protected void onCancelled() {
			showProgress(false);
		}

		@Override
		protected void onPostExecute(Response r) {
			showProgress(false);
			
			new AlertDialog.Builder(LoginActivity.this)
				.setTitle(R.string.dialog_title)
				.setMessage(r.message)
				.setPositiveButton(R.string.dialog_accept, null)
				.create().show();
		}
	}
}
