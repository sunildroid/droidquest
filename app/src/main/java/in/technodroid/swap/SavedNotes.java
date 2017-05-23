package in.technodroid.swap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.technodroid.db.DBHelper;
import in.technodroid.model.AsyncResult;
import in.technodroid.model.QAModel;
import in.technodroid.util.AppConstants;
import in.technodroid.util.DownloadWebpageTask;
import in.technodroid.util.ImageLoadTask;
import in.technodroid.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Updates.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Updates#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedNotes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tvNoFav;

    public static final String TAG = "QNA";
    private DBHelper dbHelper;

    // TODO: Rename and change types of parameters
    //  private JSONObject jsonQA;
    private ArrayList<QAModel> arQA;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Updates.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedNotes newInstance(JSONObject param1, String param2) {
        SavedNotes fragment = new SavedNotes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1.toString());
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SavedNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the drawer_item for this fragment
      View v0 =  inflater.inflate(R.layout.fragment_saved_notes, container, false);
        final android.support.v7.widget.RecyclerView v = (RecyclerView) v0.findViewById(R.id.lvQA);
        TextView tvNoFav =(TextView)v0.findViewById(R.id.tvNoFav);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        v.setLayoutManager(llm);

        if(DBHelper.getInstance(getActivity()).getFavQA(true) !=null &&DBHelper.getInstance(getActivity()).getFavQA(true).size()>0){ //Database is already downloaded
            //Read file
            try {
                v.setAdapter(new SavedNotesAdapter(DBHelper.getInstance(getActivity()).getFavQA(true)));

            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Error" + e.getMessage());
            }
        }
        else { //IF file is not downloaded download file
            tvNoFav.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);

        }
        return v0;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
            //return v;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class SavedNotesAdapter extends RecyclerView.Adapter<SavedNotesAdapter.ViewHolder>{

        public  class ViewHolder extends RecyclerView.ViewHolder {
            CardView v;
            TextView tvQ;
            TextView tvA;
            TextView tvRating;
            TextView tvBy;
            ImageView imgShare;
            ImageView imgLike;
            ImageView imgAnswerDesc;
            Button btnAnswer;
            View viewAnswer;

            ViewHolder(View itemView) {
                super(itemView);
                v = (CardView)itemView.findViewById(R.id.llMain);
                btnAnswer= (Button) itemView.findViewById(R.id.btnAnswer);
                viewAnswer=itemView.findViewById(R.id.AnswerView);
                tvA = (TextView) v.findViewById(R.id.tvDesc);
                tvQ= (TextView) v.findViewById(R.id.tvTitle);
                imgAnswerDesc= (ImageView) v.findViewById(R.id.imgDetail);
                imgLike=(ImageView)v.findViewById(R.id.imgLike);
                imgShare=(ImageView)v.findViewById(R.id.imgShare);
                tvRating= (TextView) v.findViewById(R.id.tvRating);
                tvBy = (TextView) v.findViewById(R.id.tvBy);
            }
        }

        private List<?> arQA;
        public SavedNotesAdapter(List<?> objects) {
            this.arQA = objects;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.qnaitem, viewGroup, false);
            ViewHolder pvh = new ViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(final SavedNotesAdapter.ViewHolder viewHolder,final int i) {
            final QAModel qaModel=(QAModel) arQA.get(arQA.size()-i-1);
            viewHolder.tvQ.setText(qaModel.get_Question());
            viewHolder.tvA.setText(qaModel.get_Answer());
            viewHolder.tvBy.setText("By: " + qaModel.get_By());

            if(qaModel.get_imgURL()!=null){
                viewHolder.imgAnswerDesc.setVisibility(View.VISIBLE);
                new ImageLoadTask(qaModel.get_imgURL(),viewHolder.imgAnswerDesc).execute();
                // viewHolder.imgAnswerDesc.setImageResource(R.drawable.tip);//TODO
            }else{
                viewHolder.imgAnswerDesc.setVisibility(View.GONE);
            }
            if(qaModel.isFav()) {
                viewHolder.imgLike.setImageResource(R.drawable.ic_selected);
            }
            else {
                viewHolder.imgLike.setImageResource(R.drawable.ic_like);

            }
            viewHolder.viewAnswer.setVisibility(View.GONE);
            viewHolder.btnAnswer.setText("View Answer");
            viewHolder.imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Update database
                    if(qaModel.isFav()) {
                        qaModel.setIsFav(false);
                        DBHelper.getInstance(v.getContext()).updateQAItem(qaModel);
                        viewHolder.imgLike.setImageResource(R.drawable.ic_like);
                        arQA.remove(i);
                        //notifyDataSetChanged();
                        notifyItemRemoved(i);
                        notifyDataSetChanged();

                    }
                }
            });

            viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.putExtra(Intent.EXTRA_SUBJECT, qaModel.get_Question());
                    intent.putExtra(Intent.EXTRA_TEXT, qaModel.get_Answer() + "\n ##Sent via Andro Quest##");
                    startActivity(Intent.createChooser(intent, "How do you want to share?"));
                }
            });
            viewHolder.btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.viewAnswer.getVisibility() == View.GONE) {
                        viewHolder.viewAnswer.setVisibility(View.VISIBLE);
                        viewHolder.viewAnswer.setAlpha(1.0f);
                        // Start the animation
                        viewHolder.viewAnswer.animate()
                                .translationY(20)
                                .setDuration((int) (viewHolder.viewAnswer.getMeasuredHeight() / v.getContext().getResources().getDisplayMetrics().density))
                                .alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                // viewHolder.viewAnswer.setVisibility(View.VISIBLE);
                                viewHolder.btnAnswer.setText("Hide Answer");
                            }
                        });
                        ;

                    } else {
                        viewHolder.viewAnswer.animate()
                                .translationY(0)
                                .alpha(0.0f)
                                .setDuration((int) (viewHolder.viewAnswer.getMeasuredHeight() / v.getContext().getResources().getDisplayMetrics().density))
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        viewHolder.viewAnswer.setVisibility(View.GONE);
                                        viewHolder.btnAnswer.setText("View Answer");
                                    }
                                });
                    }
                }
            });

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return this.arQA.size();
        }
    }



}
