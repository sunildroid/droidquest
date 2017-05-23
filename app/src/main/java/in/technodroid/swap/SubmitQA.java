package in.technodroid.swap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import in.technodroid.mail.Mail;
import in.technodroid.util.AppConstants;

public class SubmitQA extends Fragment {
    EditText edtQuestion;
    EditText edtAnswer;
    EditText edtName;
    EditText edtOrgName;
    Button btnSubmit;
    Switch swAnswer;
    TextInputLayout tilQuestion;
    TextInputLayout tilAnswer;
    TextInputLayout tilName;
    TextInputLayout tilOrg;
    private static final String TAG = "SubmitQA";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tempView =inflater.inflate(R.layout.fragment_submit_q,null);

        //Inflate Components
        edtName =(EditText)tempView.findViewById(R.id.edtName);
        edtAnswer =(EditText)tempView.findViewById(R.id.edtAnswer);
        edtQuestion =(EditText)tempView.findViewById(R.id.edtQuestion);
        edtOrgName =(EditText)tempView.findViewById(R.id.edtOrg);
        swAnswer =(Switch)tempView.findViewById(R.id.swAgree);
        btnSubmit=(Button)tempView.findViewById(R.id.btnSubmit);

        tilAnswer=(TextInputLayout)tempView.findViewById(R.id.tilAnswer);
        tilName=(TextInputLayout)tempView.findViewById(R.id.tilName);
        tilOrg=(TextInputLayout)tempView.findViewById(R.id.tilOrg);
        tilQuestion=(TextInputLayout)tempView.findViewById(R.id.tilQuestion);

        edtOrgName.setOnEditorActionListener(ActionListener.newInstance((HomeScreen) getActivity()));
        tilOrg.setHint(getString(R.string.company));
        edtName.setOnEditorActionListener(ActionListener.newInstance((HomeScreen) getActivity()));
        tilName.setHint(getString(R.string.name));
        edtQuestion.setOnEditorActionListener(ActionListener.newInstance((HomeScreen) getActivity()));
        tilQuestion.setHint(getString(R.string.question));
        edtAnswer.setOnEditorActionListener(ActionListener.newInstance((HomeScreen) getActivity()));
        tilAnswer.setHint(getString(R.string.answer));

        handleAnimation(swAnswer, edtAnswer);
        submitData();
        return tempView;
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
            mail.set_subject(AppConstants.SUBJECT_QA);
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
                Toast.makeText(getActivity(), "Thanks!! Submit more questions.", Toast.LENGTH_LONG).show();
                resetForm();
            }else{
                Toast.makeText(getActivity(), "There was a problem in submitting your question.Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateData(){
        if((edtQuestion.getText()==null) || edtQuestion.getText().length()<=0){
            Toast.makeText(getActivity(),"Question is required field.",Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtName.getText()==null || edtName.getText().length()<=0){
            Toast.makeText(getActivity(),"Name is required field.",Toast.LENGTH_LONG).show();
            return false;
        }

        if (edtOrgName.getText()==null || edtOrgName.getText().length()<=0){
            Toast.makeText(getActivity(),"Organization name is required field.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void resetForm(){
        edtAnswer.setText("");
        edtOrgName.setText("");
        edtName.setText("");
        edtQuestion.setText("");
    }
    private String composeBody(){
        StringBuilder body = new StringBuilder();
        body.append("Question ::"+edtQuestion.getText()+"\n");
        body.append("Answer ::"+edtAnswer.getText()+"\n");
        body.append("Name ::"+edtName.getText()+"\n");
        body.append("OrgName ::"+edtOrgName.getText()+"\n");
        return  body.toString();
    }

    private void handleAnimation(Switch tvAgree, final EditText edtAnswer){

        tvAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtAnswer.setVisibility(View.VISIBLE);
                    edtAnswer.setAlpha(1.0f);
                    // Start the animation
                    edtAnswer.animate()
                            .translationY(10)
                            .setDuration((int) (edtAnswer.getMeasuredHeight() / buttonView.getContext().getResources().getDisplayMetrics().density))
                            .alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
                } else {
                    edtAnswer.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setDuration((int) (edtAnswer.getMeasuredHeight() / buttonView.getContext().getResources().getDisplayMetrics().density))
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    edtAnswer.setVisibility(View.GONE);

                                }
                            });
                }
            }
        });


    }
}

