package com.example.q.imageslider;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ListView listView;
        Button submit;
        private final int LOADING=100;
        private boolean isNum(char a) {
            if (a >= 48 && a <= 57)
                return true;
            return false;
        }

        private boolean isDate(String a) {
            if (a.length() != 6)
                return false;
            for (int i = 0; i < 6; i++) {
                if (!isNum(a.charAt(i)))
                    return false;
            }
            return true;

        }


        private class ContactAdapter extends ArrayAdapter<ContactInfo> {

            private ArrayList<ContactInfo> items;

            public ContactAdapter(Context context, int textViewResourceId, ArrayList<ContactInfo> items) {
                super(context, textViewResourceId, items);
                this.items = items;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.contact_item, null);
                }
                ContactInfo p = items.get(position);
                if (p != null) {
                    TextView tt = (TextView) v.findViewById(R.id.toptext);
                    TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                    if (tt != null){
                        tt.setText(p.name);
                    }
                    if(bt != null){
                        bt.setText("number: "+ p.mobileNumber);
                    }
                }
                return v;
            }
        }
        public class ContactInfo{
            public String mobileNumber="";
            public String name="";
            public String id;
            public String homeNumber="";
            public String workNumber="";
            public ArrayList<String> otherNumber;

            public ContactInfo(String name, String number, String id){
                this.name=name;
                mobileNumber=number;
                this.id=id;
                otherNumber=new ArrayList<String>();
            }

            public ContactInfo(String name, String id){
                this.name=name;
                this.id=id;
                otherNumber= new ArrayList<String>();
            }

            @Override
            public String toString(){
                String tostr="";
                if(mobileNumber.length()>1)
                    tostr=tostr+"\nmobile:"+mobileNumber;
                if(homeNumber.length()>1)
                    tostr=tostr+"\nhome:"+homeNumber;
                if(workNumber.length()>1)
                    tostr=tostr+"\nwork:"+workNumber;
                if(otherNumber.size()>1)
                    for(String num:otherNumber)
                        tostr=tostr+"\nother:"+num;
                return tostr;
            }
        }
        JSONObject json=new JSONObject();
        ArrayList<ContactInfo> contact;
        private void readContact() throws JSONException {
            try {
                // Android version is lesser than 6.0 or the permission is already granted.
                contact = new ArrayList<ContactInfo>();
                ContentResolver cr = getActivity().getContentResolver();
                Uri uri = ContactsContract.Contacts.CONTENT_URI;

                Cursor cur = cr.query(uri, null, null, null, null);


                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        JSONObject j = new JSONObject();
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        j.put("NAME", name);
                        ContactInfo c=new ContactInfo(name, id);
                        String phone_num = "";
                        if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = getActivity().getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                int phoneType = pCur.getInt(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.TYPE));
                                String phoneNumber = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                switch (phoneType) {
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                        j.put("Mobile number", phoneNumber);
                                        c.mobileNumber=phoneNumber;
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                        j.put("Home number", phoneNumber);
                                        c.homeNumber=phoneNumber;
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                        j.put("Work number", phoneNumber);
                                        c.workNumber=phoneNumber;
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                        j.put("Other number", phoneNumber);
                                        c.otherNumber.add(phoneNumber);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            pCur.close();
                            json.put(name, j);
                        }
                        else
                            json.put(name, (new JSONObject()).put("NAME", name));
                        contact.add(c);
                    }
                }
                ContactAdapter m_adapter = new ContactAdapter(getActivity(), R.layout.contact_item, contact);
                listView.setAdapter(m_adapter);
            }catch(JSONException e){
                throw new JSONException(e.getMessage());
            }
        }


        private void queryContact() throws JSONException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            } else {
                readContact();
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
            if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    try {
                        readContact();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        private class ListViewItemClickListener implements AdapterView.OnItemClickListener{

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());

                alertDlg.setPositiveButton( "확인", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        dialog.dismiss();  // AlertDialog를 닫는다.
                    }
                });


                alertDlg.setTitle(contact.get(position).name);
                String message=contact.get(position).toString();

                alertDlg.setMessage( message );
                alertDlg.show();
            }
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
                View rootView=inflater.inflate(R.layout.contact, container, false);
                listView=(ListView)rootView.findViewById(R.id.listView);

                try {
                    queryContact();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listView.setOnItemClickListener(new ListViewItemClickListener());


                return rootView;
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                View view1 = inflater.inflate(R.layout.ctab, container, false);
                submit = (Button) view1.findViewById(R.id.button);

                submit.setOnClickListener(this);

                return view1;
            }
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
        int hashValue;

        @Override
        public void onClick(View v) {

            TextView name = (TextView) getActivity().findViewById(R.id.name);
            TextView birthday = (TextView) getActivity().findViewById(R.id.date);
            if (!isDate(birthday.getText().toString())) {
                Toast.makeText(getContext(), "Invalid Birthday", Toast.LENGTH_SHORT).show();
                return;
            }

            CharSequence nameString = name.getText();
            CharSequence birthdayString = birthday.getText();

            hashValue = 0;

            for (int i = 0; i < nameString.length(); i++) {
                hashValue += nameString.charAt(i);
            }
            for (int i = 0; i < birthdayString.length(); i++) {
                hashValue += birthdayString.charAt(i);
            }
            int nDay;
            Calendar calendar = new GregorianCalendar(Locale.KOREA);
            nDay = calendar.get(Calendar.DAY_OF_MONTH);

            hashValue += nDay;
            Intent i = new Intent(getActivity(), showGif.class);
            i.putExtra("fortune", hashValue);
            getActivity().startActivity(i);

        }



        }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    photoFrag tab2 = new photoFrag();
                    return tab2;
                case 2:
                    return PlaceholderFragment.newInstance(position + 1);
            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Contact";
                case 1:
                    return "Gallery";
                case 2:
                    return "My Own";
            }
            return null;
        }
    }

}
