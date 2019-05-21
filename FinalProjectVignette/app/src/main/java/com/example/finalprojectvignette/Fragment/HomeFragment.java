package com.example.finalprojectvignette.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvignette.Adapter.PostAdapter;
import com.example.finalprojectvignette.Model.Post;
import com.example.finalprojectvignette.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String currentClass = "";


    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    private static final int Pick_Video_Request = 3;

    private OnFragmentInteractionListener mListener;
    Dialog popAddPost;
    ImageView popupUserImage,popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription;
    ProgressBar popupClickProgress;
    private VideoView videoView;
    private MediaController mc;

    private static String uid;

    String user_image;

    private Uri pickedImgUri = null;
    private Uri videouri = null;
    private  String imageDownlaodLink;
    private  String videolink;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    RecyclerView postRecyclerView ;
    PostAdapter postAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Post> postList;

    FloatingActionButton floataddpost;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        postRecyclerView  = fragmentView.findViewById(R.id.postRV);
        floataddpost = fragmentView.findViewById(R.id.fab);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        uid = currentUser.getUid();
        currentClass = ClassFragment.classid;

        databaseReference = firebaseDatabase.getReference();
        return fragmentView ;
    }



    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allpost = dataSnapshot.child("classes").child(currentClass).child("posts").getValue().toString();
                System.out.println("()())()))())()" + dataSnapshot.child("classes").child(currentClass).getValue().toString());
                postList = new ArrayList<>();
                String[] myList = allpost.split(",");
                if(!allpost.equals("0")){
                    for (DataSnapshot postsnap: dataSnapshot.child("Posts").getChildren()) {
                        String postt = postsnap.getKey().toString();
                        for(final String childInClass : myList) {
                            if(childInClass.equals(postt)){
                                Post post = postsnap.getValue(Post.class);
                                System.out.println("!!!!!?????????!??!?!??!?!?!?!?!?!??!?!? " + postt);
                                postList.add(post);
                            }
                        }
                    }
                }
                else {
                    //Toast.makeText(getContext(),"No Post",Toast.LENGTH_SHORT).show();
                }
                postAdapter = new PostAdapter(getActivity(),postList,true);
                postRecyclerView.setAdapter(postAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        floataddpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniPopup();
                setupPopupImageClick();
                setupPopupVideoClick();
            }
        });
    }


    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void setupPopupVideoClick() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideo();
            }
        });
    }


    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void openVideo(){
        Intent video = new Intent(Intent.ACTION_GET_CONTENT);
        video.setType("video/*");
        startActivityForResult(video,Pick_Video_Request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data!=null){
            if(requestCode == REQUESCODE){
                pickedImgUri = data.getData();
                popupPostImage.setImageURI(pickedImgUri);
            }
            if(requestCode == Pick_Video_Request){
                videouri = data.getData();
                videoView.setVideoURI(videouri);
            }
        }
    }





    private void iniPopup() {
        popAddPost = new Dialog(getContext());
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        setCurrentData();
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);
        videoView = popAddPost.findViewById(R.id.videoview);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc = new MediaController(getContext());
                        videoView.setMediaController(mc);
                        mc.setAnchorView(videoView);
                    }
                });
            }
        });

        videoView.start();
//        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserImage);



        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image

                if (!popupTitle.getText().toString().isEmpty() && !popupDescription.getText().toString().isEmpty() && pickedImgUri != null ) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageDownlaodLink = uri.toString();
                                    System.out.println("((((())))(()))()()()())((((())))" + imageDownlaodLink);
                                    showMessage("Image has done");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                    });

                    final StorageReference videofile = storageReference.child(videouri.getLastPathSegment());
                    videofile.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            videofile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    videolink = uri.toString();
                                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + videolink);
                                    Post post = new Post(popupTitle.getText().toString(),
                                            popupDescription.getText().toString(),
                                            imageDownlaodLink,
                                            currentUser.getUid(),
                                            user_image,
                                            videolink);
                                    addPost(post);
                                    showMessage("Video has done");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
                else {
                    showMessage("Please verify all input fields and choose Post Image") ;
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
        popAddPost.show();
    }


    private void setCurrentData(){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String pic_name = (dataSnapshot.child("admin").child(uid).child("image").getValue() != null)? dataSnapshot.child("admin").child(uid).child("image").getValue().toString() : "";
                if(!pic_name.isEmpty()||pic_name.equalsIgnoreCase("0")) {
                    user_image = dataSnapshot.child("admin").child(uid).child("image").getValue().toString();
                    Glide.with(getContext()).load(pic_name).into(popupUserImage);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and update post key
        String key = myRef.getKey();
        post.setPostKey(key);
        AddFragment(key);

        System.out.println("?????????????!??!??!?!?! " + key);
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });

        Fragment frag = Fragment_ActivitiesGrid.newInstance(key);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.teacher_content_holder,frag).commit();
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }


    private void AddFragment(final String key){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String uidk = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currClasses = dataSnapshot.child("classes").child(currentClass).child("posts").getValue().toString();
                if(currClasses.equals("0")) {
                    databaseReference.child("classes").child(currentClass).child("posts").setValue(key + ",");
                }
                else if(!isClassAssigned(currClasses, key)) {
                    currClasses += key + ",";
                    databaseReference.child("classes").child(currentClass).child("posts").setValue(currClasses);
                    Toast.makeText(getContext(), "Your new class '" + key + "' has been added", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "You have been assigned to this class already!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private boolean isClassAssigned(String classList, String NewClass) {
        return classList.contains(NewClass);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
