package com.zuccessful.a2;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Detail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Contact contact;
    private ImageView contactImage;
    private TextView contactName;
    FrameLayout layout;

    private OnFragmentInteractionListener mListener;

    public Fragment_Detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Detail.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Detail newInstance(String param1, String param2) {
        Fragment_Detail fragment = new Fragment_Detail();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_layout, container, false);

        FloatingActionButton editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(Intent.ACTION_EDIT);
                editIntent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                        Integer.parseInt(contact.getId())));
                startActivityForResult(editIntent, 1);
            }
        });

        contactImage = view.findViewById(R.id.contact_image);
        contactName = view.findViewById(R.id.contactName);
        layout = view.findViewById(R.id.fragment_detail_layout);


        // Inflate the layout for this fragment
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        detailIntent(contact.getId());
    }

    class CustomAdapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        CustomAdapter(Context context, ArrayList<String> data) {
            super(context, R.layout.list_view, R.id.content, data);
            this.data = data;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItemView = layoutInflater.inflate(R.layout.list_view, parent, false);

            ImageView contentIcon = listItemView.findViewById(R.id.content_icon);
            TextView content = listItemView.findViewById(R.id.content);
            content.setText(data.get(position));

            if (data.get(position).contains("@")) {
                contentIcon.setImageResource(R.drawable.email_icon);

                //intent call for email
                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:" + data.get(position)));
                        startActivity(Intent.createChooser(emailIntent, "Send Email to"));
                    }
                });
            } else {
                contentIcon.setImageResource(R.drawable.call_icon);

                //intent call for dialer
                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + data.get(position)));
                        startActivity(intent);
                    }
                });
            }
            return listItemView;
        }
    }

    public void detailIntent(String contactId) {
        layout.setVisibility(View.VISIBLE);
        // Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.Data.CONTACT_ID + " = " + Integer.parseInt(contactId),
                null, null);

        String name = "";
        String imageUri = "";
        if (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            imageUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            this.contact = new Contact(contactId, name, imageUri);

            Cursor mcursor = getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{contactId}, null);

            while (mcursor.moveToNext()) {
                this.contact.setNumber(mcursor.getString(mcursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            mcursor.close();

            Cursor emailCursor = getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{contactId}, null);

            while (emailCursor.moveToNext()) {
                this.contact.setEmail(emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
            }
            emailCursor.close();
        }
        cursor.close();

        contactName.setText(contact.getTitle().toUpperCase());

        if (contact.getImage() != null) {
            contactImage.setImageURI(Uri.parse(contact.getImage()));
        } else {
            contactImage.setImageResource(R.drawable.empty_image);
        }

        ArrayList<String> data = new ArrayList<>();
        data.addAll(contact.getNumber());
        data.addAll(contact.getEmail());
        CustomAdapter customAdapter = new CustomAdapter(getContext(), data);
        ListView listViewNumber = getActivity().findViewById(R.id.listViewNumbers);
        listViewNumber.setAdapter(customAdapter);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
