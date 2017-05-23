package in.technodroid.swap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import in.technodroid.mail.Mail;
import in.technodroid.util.AppConstants;
import in.technodroid.util.Util;


public class JoinCommunity extends Fragment {

    EditText edtName;
    EditText edtMail;
    EditText edtPhoneNo;
    EditText edtOrgName;
    Switch swAgree;
    Button btnSubmit;

    TextInputLayout tilOrgName;
    TextInputLayout tilEmail;
    TextInputLayout tilPhone;
    TextInputLayout tilName;

    private static final String TAG = "JoinCommunity";
    private static final int MIN_TEXT_LENGTH = 4;
    private static final String EMPTY_STRING = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tempView =inflater.inflate(R.layout.fragment_join_community,null);

        //Inflate Components
        edtName =(EditText)tempView.findViewById(R.id.edName);
        edtMail =(EditText)tempView.findViewById(R.id.edMail);
        edtPhoneNo =(EditText)tempView.findViewById(R.id.edPhoneNumber);
        edtOrgName =(EditText)tempView.findViewById(R.id.edOrgName);
        swAgree =(Switch)tempView.findViewById(R.id.swAgree);
        btnSubmit=(Button)tempView.findViewById(R.id.btnSubmit);

        edtOrgName =(EditText)tempView.findViewById(R.id.edOrgName);
        tilOrgName = (TextInputLayout) tempView.findViewById(R.id.tilOrgName);
        tilOrgName.setHint(getString(R.string.company));

        tilEmail = (TextInputLayout) tempView.findViewById(R.id.tilEmail);
        tilEmail.setHint(getString(R.string.email));

        tilOrgName = (TextInputLayout) tempView.findViewById(R.id.tilOrgName);
        tilOrgName.setHint(getString(R.string.company));

        tilPhone = (TextInputLayout) tempView.findViewById(R.id.tilPhone);
        tilPhone.setHint(getString(R.string.phoneNumber));

        tilName = (TextInputLayout) tempView.findViewById(R.id.tilName);
        tilName.setHint(getString(R.string.name));

        edtOrgName.setOnEditorActionListener(ActionListener.newInstance((HomeScreen) getActivity()));
        edtMail.setOnEditorActionListener(ActionListener.newInstance((HomeScreen)getActivity()));
        edtPhoneNo.setOnEditorActionListener(ActionListener.newInstance((HomeScreen)getActivity()));
        edtName.setOnEditorActionListener(ActionListener.newInstance((HomeScreen)getActivity()));
        handleAnimation(swAgree, tilPhone);
        submitData();
        return tempView;

    }

    private static final class ActionListener implements TextView.OnEditorActionListener {
        private final WeakReference<HomeScreen> mainActivityWeakReference;

        public static ActionListener newInstance(HomeScreen mainActivity) {
            WeakReference<HomeScreen> mainActivityWeakReference = new WeakReference<>(mainActivity);
            return new ActionListener(mainActivityWeakReference);
        }

        private ActionListener(WeakReference<HomeScreen> mainActivityWeakReference) {
            this.mainActivityWeakReference = mainActivityWeakReference;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            HomeScreen mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
              /*  if (actionId == EditorInfo.IME_ACTION_GO && JoinCommunity.this.shouldShowError(null)) {
                    showError(null);
                } else {
                    hideError(null);
                }*/
            }
            return true;
        }
    }

    public boolean shouldShowError(EditText ed) {
        int textLength = ed.getText().length();
        return textLength > 0 && textLength < MIN_TEXT_LENGTH;
    }

    public void showError(TextInputLayout textInputLayout) {
        textInputLayout.setError(getString(R.string.error));
    }

    private void hideError(TextInputLayout textInputLayout) {
        textInputLayout.setError(EMPTY_STRING);
    }

    private void submitData(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData())
                    new SendMailTask().execute();
            }
        });
    }

    private class SendMailTask extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog dialog= new ProgressDialog(getActivity(),R.style.MyTheme);
        private boolean isSent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            dialog.show();
        }

        protected Boolean doInBackground(String... urls) {

            Mail mail = new Mail(AppConstants.SENDER_EMAIL_ID, AppConstants.SENDER_PWD);
            String[] toArr = {AppConstants.TO_MAIL_1,AppConstants.TO_MAIL_2,AppConstants.TO_MAIL_3};
            mail.set_to(toArr);
            mail.set_from(AppConstants.SENDER_EMAIL_ID);
            mail.set_subject(AppConstants.SUBJECT);
            mail.setBody(composeBody());
            try {
              isSent=  mail.send();
            } catch(Exception e) {

                Log.e(TAG, "Could not send email", e);
            }
            return isSent;
        }


        protected void onPostExecute(Boolean result) {
            dialog.hide();
           if(result){
               Toast.makeText(getActivity(), "Your request has been successfully sent.", Toast.LENGTH_LONG).show();
               resetForm();
           }else{
               Toast.makeText(getActivity(), "There was a problem in sending your request. Try again.", Toast.LENGTH_LONG).show();
           }
        }
    }

    private boolean validateData(){
        if((edtName.getText()==null) || edtName.getText().length()<=0){
            Toast.makeText(getActivity(),"User name is required field.",Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtMail.getText()==null || edtMail.getText().length()<=0){
            Toast.makeText(getActivity(),"Email is required field.",Toast.LENGTH_LONG).show();
            return false;
        } else {
            if( !android.util.Patterns.EMAIL_ADDRESS.matcher(edtMail.getText()).matches()){
                Toast.makeText(getActivity(),"Entered email id is not valid.",Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (edtOrgName.getText()==null || edtOrgName.getText().length()<=0){
            Toast.makeText(getActivity(),"Organization name is required field.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void resetForm(){
        edtMail.setText("");
        edtOrgName.setText("");
        edtName.setText("");
        edtPhoneNo.setText("");
    }
    private String composeBody(){
        StringBuilder body = new StringBuilder();
        body.append("Sender ::"+edtName.getText()+"\n");
        body.append("Phone Number ::"+edtPhoneNo.getText()+"\n");
        body.append("EmailID ::"+edtMail.getText()+"\n");
        body.append("OrgName ::"+edtOrgName.getText()+"\n");
        return  body.toString();
    }

    private void handleAnimation(Switch tvAgree, final TextInputLayout edtPhoneNo){
        tvAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (isChecked) {
                                                       edtPhoneNo.setVisibility(View.VISIBLE);
                                                       edtPhoneNo.setAlpha(1.0f);
                                                       // Start the animation
                                                       edtPhoneNo.animate()
                                                               .translationY(10)
                                                               .setDuration((int) (edtPhoneNo.getMeasuredHeight() / buttonView.getContext().getResources().getDisplayMetrics().density))
                                                               .alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                                                           @Override
                                                           public void onAnimationEnd(Animator animation) {
                                                               super.onAnimationEnd(animation);

                                                           }
                                                       });
                                                   } else {
                                                       edtPhoneNo.animate()
                                                               .translationY(0)
                                                               .alpha(0.0f)
                                                               .setDuration((int) (edtPhoneNo.getMeasuredHeight() / buttonView.getContext().getResources().getDisplayMetrics().density))
                                                               .setListener(new AnimatorListenerAdapter() {
                                                                   @Override
                                                                   public void onAnimationEnd(Animator animation) {
                                                                       super.onAnimationEnd(animation);
                                                                       edtPhoneNo.setVisibility(View.GONE);

                                                                   }
                                                               });

                                                   }
                                               }
                                           }
            );



    }
}
