package com.example.mymark;

import android.media.Image;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PromptFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PromptFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_name, tv_bio;
    private Button bt1, bt2, bt3, bt4, bt5;
    private CircleImageView imageView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PromptFragment newInstance(String param1, String param2) {
        PromptFragment fragment = new PromptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PromptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_prompt, container, false);
        HomeActivity ha = new HomeActivity();
        ImageButton ib_dismiss = (ImageButton) v.findViewById(R.id.ib_dismiss);

        tv_name = (TextView) v.findViewById(R.id.tv_profile_name);
        tv_bio = (TextView) v.findViewById(R.id.tv_bio);
        bt1 = (Button) v.findViewById(R.id.interest_1);
        bt2 = (Button) v.findViewById(R.id.interest_2);
        bt3 = (Button) v.findViewById(R.id.interest_3);
        bt4 = (Button) v.findViewById(R.id.interest_4);
        bt5 = (Button) v.findViewById(R.id.interest_5);
        imageView = (CircleImageView) v.findViewById(R.id.circle_image_profile_picture);

        tv_name.setText("Match Found");
        tv_bio.setText("Lakshh Khatri");
        imageView.setImageResource(R.drawable.lakshh);
        bt1.setText("Soccer");
        bt2.setText("Harry Potter");
        bt3.setText("80s kid");
        bt4.setText("Chelsea");
        bt5.setText("Tennis");

        ib_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeView(v);
                //ha.disappearButton();
                Toast.makeText(getContext(), "Dismiss Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}