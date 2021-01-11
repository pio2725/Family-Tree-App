package pio2725.familymap.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.net.MalformedURLException;
import java.net.URL;

import Reqeust.LoginRequest;
import Reqeust.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.PersonResultSingle;
import Result.RegisterResult;

public class LoginFragment extends Fragment {

    private EditText mServerHostText;
    private EditText mServerPortText;
    private EditText mUsernameText;
    private EditText mPasswordText;
    private EditText mFirstNameText;
    private EditText mLastNameText;
    private EditText mEmailText;
    private RadioGroup mGenderRadioGroup;
    private Button mSignInButton;
    private Button mRegisterButton;

    private String serverHost;
    private String serverPort;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    private boolean isChecked;
    private String personId;
    private String auth;

    private LoginRequest mLoginRequest;
    private RegisterRequest mRegisterRequest;
    private LoginResult mLoginResult;
    private RegisterResult mRegisterResult;
    private PersonResultSingle thisPersonResult;

    private ServerProxy mServerProxy;

    public LoginFragment() {
        isChecked = false;
        mServerProxy = new ServerProxy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mServerHostText = (EditText) view.findViewById(R.id.serverHostEditText);
        mServerPortText = (EditText) view.findViewById(R.id.serverPortEditText);
        mUsernameText = (EditText) view.findViewById(R.id.userNameEditText);
        mPasswordText = (EditText) view.findViewById(R.id.passwordEditText);
        mFirstNameText = (EditText) view.findViewById(R.id.firstNameEditText);
        mLastNameText = (EditText) view.findViewById(R.id.lastNameEditText);
        mEmailText = (EditText) view.findViewById(R.id.emailEditText);

        mServerHostText.addTextChangedListener(RegisterWatcher);
        mServerPortText.addTextChangedListener(RegisterWatcher);
        mUsernameText.addTextChangedListener(RegisterWatcher);
        mPasswordText.addTextChangedListener(RegisterWatcher);
        mFirstNameText.addTextChangedListener(RegisterWatcher);
        mLastNameText.addTextChangedListener(RegisterWatcher);
        mEmailText.addTextChangedListener(RegisterWatcher);

        mGenderRadioGroup = (RadioGroup) view.findViewById(R.id.genderRadioGroup);
        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMale) {
                    gender = "m";
                }
                else if (checkedId == R.id.radioButtonFemale) {
                    gender = "f";
                }
                isChecked = true;
                mRegisterButton.setEnabled(!serverHost.isEmpty() && !serverPort.isEmpty() && !userName.isEmpty()
                        && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty());
            }
        });

        mSignInButton = (Button) view.findViewById(R.id.singInButton);
        mSignInButton.setEnabled(false);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButtonClicked();
            }
        });

        mRegisterButton = (Button) view.findViewById(R.id.registerButton);
        mRegisterButton.setEnabled(false);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterButtonClicked();
            }
        });

        return view;
    }

    private TextWatcher RegisterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            serverHost = mServerHostText.getText().toString();
            serverPort = mServerPortText.getText().toString();
            userName = mUsernameText.getText().toString();
            password = mPasswordText.getText().toString();
            firstName = mFirstNameText.getText().toString();
            lastName = mLastNameText.getText().toString();
            email = mEmailText.getText().toString();

            mSignInButton.setEnabled(!userName.isEmpty() && !password.isEmpty());
            isChecked = getIsFilled();
            mRegisterButton.setEnabled(!serverHost.isEmpty() && !serverPort.isEmpty() && !userName.isEmpty()
                    && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && isChecked);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean getIsFilled() {
        return isChecked;
    }

    public void signInButtonClicked() {
        mLoginRequest = new LoginRequest(userName, password);
        LogInTask logInTask = new LogInTask();
        logInTask.execute(mLoginRequest);
    }

    private void RegisterButtonClicked() {
        mRegisterRequest = new RegisterRequest(userName, password, email, firstName, lastName, gender);
        RegisterTask registerTask = new RegisterTask();
        registerTask.execute(mRegisterRequest);
    }

    private class LogInTask extends AsyncTask<LoginRequest, Void, LoginResult> {

        @Override
        protected LoginResult doInBackground(LoginRequest... loginRequests) {

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
                mLoginResult = mServerProxy.getLoginResult(url, loginRequests[0]);
                return mLoginResult;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                mLoginResult.setMessage("Malformed url when Signing in");
                return mLoginResult;
            }
        }

        @Override
        protected void onPostExecute(LoginResult loginResult) {
            super.onPostExecute(loginResult);

            if (!mServerProxy.isSuccess()) {
                Toast.makeText(getActivity(), "Fail to Sign in", Toast.LENGTH_SHORT).show();
            }
            else {
                mServerProxy.setSuccess(false);
                personId = loginResult.getPersonID();
                auth = loginResult.getAuthToken();
                GetDataTask getDataTask = new GetDataTask();
                getDataTask.execute();
            }
        }
    }

    private class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResult> {

        @Override
        protected RegisterResult doInBackground(RegisterRequest... registerRequests) {

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
                mRegisterResult = mServerProxy.getRegisterResult(url, registerRequests[0]);
                return mRegisterResult;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                mRegisterResult.setMessage("Malformed url when registering");
                return mRegisterResult;
            }
        }

        @Override
        protected void onPostExecute(RegisterResult registerResult) {
            super.onPostExecute(registerResult);

            if (!mServerProxy.isSuccess()) {
                Toast.makeText(getActivity(), "Fail to Register", Toast.LENGTH_SHORT).show();
            }
            else {
                mServerProxy.setSuccess(false);
                personId = registerResult.getPersonID();
                auth = registerResult.getAuthToken();
                GetDataTask getDataTask = new GetDataTask();
                getDataTask.execute();
            }
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        FamilyData familyData = FamilyData.get();

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" + personId);
                thisPersonResult = mServerProxy.getPersonResult(url, auth);
                if (thisPersonResult != null) {
                    familyData.setThisPerson(thisPersonResult);

                    url = new URL("http://" + serverHost + ":" + serverPort + "/event");
                    EventResult eventResult = mServerProxy.getAllEventResult(url, auth);
                    url = new URL("http://" + serverHost + ":" + serverPort + "/person");
                    PersonResult personResult = mServerProxy.getAllPeopleResult(url, auth);
                    familyData.setAllEvents(eventResult.getData());
                    familyData.setAllPeople(personResult.getData());
                    familyData.setPersonIDToPersonMap(personResult.getData());
                    familyData.setMotherSideFamily(familyData.findPersonbyId(thisPersonResult.getMotherID()));
                    familyData.setFatherSideFamily(familyData.findPersonbyId(thisPersonResult.getFatherID()));
                    familyData.setEventTypeToColor();


                    return true;
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean && thisPersonResult != null) {
                Toast.makeText(getActivity(), "Log In Success for " + familyData.getFirstName() + " " + familyData.getLastName()
                , Toast.LENGTH_SHORT).show();
                switchFragment();
            }
            else {
                Toast.makeText(getActivity(), "Fail to Log In", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void switchFragment() {
        MapFragment mapFragment = new MapFragment();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.login_container, mapFragment)
                .addToBackStack(null)
                .commit();

    }
}
