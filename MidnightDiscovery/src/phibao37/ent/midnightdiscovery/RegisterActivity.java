package phibao37.ent.midnightdiscovery;

import org.json.JSONObject;

import phibao37.ent.app.App;
import phibao37.ent.app.ID;
import phibao37.support.models.Response;
import phibao37.support.utils.HTTPBuilder;
import phibao37.support.utils.WebBuilder;
import phibao37.support.utils.HTTPBuilder.ExecuteException;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
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
 * A Register screen that offers register new account
 */
public class RegisterActivity extends AppCompatActivity{

	/**
	 * Keep track of the register task to ensure we can cancel it if requested.
	 */
	private RegisterTask mRegTask = null;
	
	public static final int MIN_USERNAME = 4;
	public static final int MIN_PASSWORD = 6;
	public static final int MIN_FULLNAME = 5;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private EditText mPassword2View;
	private EditText mEmailView;
	private EditText mFullNameView;
	private View mProgressView;
	private View mRegFormView;
	private TextView mMessageView;
	
	private WebBuilder mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// Set up the register form.
		mUsernameView = (EditText) findViewById(R.id.txt_username);
		mPasswordView = (EditText) findViewById(R.id.txt_password);
		mPassword2View = (EditText) findViewById(R.id.txt_password_2);
		mEmailView = (EditText) findViewById(R.id.txt_email);
		mFullNameView = (EditText) findViewById(R.id.txt_fullname);
		
		Button mRegButton = (Button) findViewById(R.id.btn_register);
		mRegButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptRegister();
			}
		});
		
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goLoginPage();
			}
		});

		mRegFormView = findViewById(R.id.form_register);
		mProgressView = findViewById(R.id.progress_register);
		mMessageView = (TextView) findViewById(R.id.txt_message);
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClient.shutdown();
			}
		});

		mFullNameView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.ime_register || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});
		 
		mClient = App.getInstance().getWebBuilder();
	}
	
	private void goLoginPage(){
		Intent intent = new Intent(this, LoginActivity.class);
		
		startActivity(intent);
		finish();
	}
	
	private void setMessage(int msgResId){
		mMessageView.setText(msgResId);
	}

	/**
	 * Attempts to register the account specified by the register form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual register attempt is made.
	 */
	public void attemptRegister() {
		if (mRegTask != null) {
			return;
		}

		// Store values at the time of the register attempt.
		String username = mUsernameView.getText().toString();
		String password = mPasswordView.getText().toString();
		String password2 = mPassword2View.getText().toString();
		String email = mEmailView.getText().toString();
		String fullname = mFullNameView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		TextView[] requiredView = new TextView[]{mUsernameView, mPasswordView, 
				mPassword2View, mEmailView, mFullNameView};
		String[] requiredStr = new String[]{username, password, password2, email, fullname};
		
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
			
			if (!password.equals(password2)){
				mPassword2View.setError(getString(R.string.error_password_mismatch));
				focusView = mPassword2View;
				cancel = true;
				break;
			}
			
			if ((error = isEmailValid(email)) != null){
				mEmailView.setError(error);
				focusView = mEmailView;
				cancel = true;
				break;
			}
			
			if ((error = isFullNameValid(fullname)) != null){
				mFullNameView.setError(error);
				focusView = mFullNameView;
				cancel = true;
				break;
			}
			
		} while (false);

		if (cancel) {
			// There was an error; don't attempt register and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user register attempt.
			setMessage(R.string.t_creating_account);
			showProgress(true);
			mRegTask = new RegisterTask();
			mRegTask.execute(username, password, password2, email, fullname);
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
	
	private String isEmailValid(String email){
		if (!email.contains("@"))
			return getString(R.string.error_invalid_email);
		
		return null;
	}
	
	private String isFullNameValid(String name){
		if (name.length() < MIN_FULLNAME)
			return getString(R.string.error_fullname_short);
		
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
	 * Shows the progress UI and hides the register form.
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
	 * Represents an asynchronous registration task
	 */
	public class RegisterTask extends AsyncTask<String, Void, Response> {

		@Override
		protected Response doInBackground(String... args) {

			try {
				return Response.parse(mClient.setURL(ID.URL_REGISTER)
						.setPostType(HTTPBuilder.POST_MULTIPART)
						.addPost(ID.PARAM_USERNAME, args[0])
						.addPost(ID.PARAM_PASSWORD, args[1])
						.addPost(ID.PARAM_PASSWORD_2, args[2])
						.addPost(ID.PARAM_EMAIL, args[3])
						.addPost(ID.PARAM_FULLNAME, args[4])
						.execute(JSONObject.class));
			} catch (Exception e) {
				if (ExecuteException.isCancelByUser(e))
					this.cancel(false);
				return Response.make(e.getMessage());
			}

		}

		@Override
		protected void onPostExecute(Response r) {
			mRegTask = null;
			showProgress(false);

			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
				.setTitle(R.string.dialog_title)
				.setMessage(r.message)
				.setPositiveButton(R.string.dialog_accept, null);
			
			if (r.code == ID.CODE_SUCCESS)
				builder.setNegativeButton(R.string.action_login, 
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						goLoginPage();
					}
				});
			
			builder.create().show();
		}

		@Override
		protected void onCancelled() {
			mRegTask = null;
			showProgress(false);
		}
	}
}
