package com.example.siddharthgupta.healthcare;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsManager;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Emergency_SMS extends AppCompatActivity {


    RecyclerView mRecyclerView;
    Button pick_contact_btn,send_sms_btn;
    TextView chargesApplied;
    private static final int RESULT_PICK_CONTACT = 855;
    int max_person=3;
    int no_contact_added=0;

    public int starthua=1;
    String phoneNo = null ;
    String name = null;
    String message = "EMERGENCY MEDICAL CHECK UP NEEDED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency__sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Slide slide=new Slide(Gravity.RIGHT);
        getWindow().setReturnTransition(slide);
        /*
        if (!(getIntent().getStringExtra("activityFROM").equalsIgnoreCase("Login"))){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        */
//        getSupportActionBar().setTitle("Select Contacts");
        //getSupportActionBar().setSubtitle("");

        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Select Contacts");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setUpRecyclerView();
        pick_contact_btn=(Button)findViewById(R.id.pick_contact_btn);
        pick_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });

        send_sms_btn=(Button)findViewById(R.id.send_sms_btn);
        chargesApplied= (TextView) findViewById(R.id.chargesApplied);
        if (getIntent().getStringExtra("activityFROM").equalsIgnoreCase("Login")){
            send_sms_btn.setText("Next");
            chargesApplied.setVisibility(View.INVISIBLE);
        }
        else{
            send_sms_btn.setText("Send SMS*");
            chargesApplied.setVisibility(View.VISIBLE);
        }
        send_sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_sms_btn.getText().toString().equalsIgnoreCase("Send SMS*")) {
                    ((TestAdapter) mRecyclerView.getAdapter()).sendSMS();
                }
                else{
                    startActivity(new Intent(Emergency_SMS.this, Main2Activity.class)
                            .putExtra("name", getIntent().getStringExtra("name"))
                            .putExtra("email", getIntent().getStringExtra("email"))
                            .putExtra("imgurl", getIntent().getStringExtra("imgurl"))
                    );
                    finish();
                }
            }
        });
        SharedPreferences sharedPreferences=Emergency_SMS.this.getSharedPreferences("ContactDetails",MODE_PRIVATE);
        no_contact_added=sharedPreferences.getInt("NoContacts",0);
        Log.e("NOContacts : ",""+no_contact_added);
        for(int i=0;i<no_contact_added;i++){
            String namei="name"+i;
            String numberi="number"+i;
            phoneNo=sharedPreferences.getString(numberi,"");
            name=sharedPreferences.getString(namei,"");
            Log.e(namei,name);
            Log.e(numberi,phoneNo);
            ((TestAdapter)mRecyclerView.getAdapter()).addItems(1);
        }
        Log.e("hello","its me");
        starthua=0;
    }

    @Override
    public void onBackPressed() {
        if (no_contact_added>0){
            finish();
        }else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {

            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
//            contact_name.setText(name);
//            contact_number.setText(phoneNo);
//            buttonEnable();
            ((TestAdapter)mRecyclerView.getAdapter()).addItems(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
 */
    /**
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.menu_item_undo_checkbox) {
                item.setChecked(!item.isChecked());
                ((TestAdapter)mRecyclerView.getAdapter()).setUndoOn(item.isChecked());
            }
            if (item.getItemId() == R.id.menu_item_add_5_items) {
                ((TestAdapter)mRecyclerView.getAdapter()).addItems(5);
            }
            return super.onOptionsItemSelected(item);
        }
*/
    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new TestAdapter(this));
        mRecyclerView.setHasFixedSize(true);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(Emergency_SMS.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) Emergency_SMS.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                TestAdapter testAdapter = (TestAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                TestAdapter adapter = (TestAdapter)mRecyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    /**
     * RecyclerView adapter enabling undo on a swiped away item.
     */
    class TestAdapter extends RecyclerView.Adapter {

        private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

        List<String> items,ivalues;
        List<String> itemsPendingRemoval,valuePendingRemoval;
        int lastInsertedIndex; // so we can add some more items for testing purposes
        boolean undoOn; // is undo on, you can turn it on from the toolbar menu

        private Handler handler = new Handler(); // hanlder for running delayed runnables
        HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

        Context context;

        NotificationCompat.Builder mBuilder;
        NotificationCompat.InboxStyle inboxStyle;
        int sendto=0;
        public TestAdapter(Context context) {
            this.context=context;
            items = new ArrayList<>();
            ivalues = new ArrayList<>();
            itemsPendingRemoval = new ArrayList<>();
            valuePendingRemoval=new ArrayList<>();
            // let's generate some items
            lastInsertedIndex = 15;
            // this should give us a couple of screens worth
            /*for (int i=1; i<= lastInsertedIndex; i++) {
                items.add("Item " + i);
                ivalues.add("value "+i);
            }*/

            mBuilder= new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.drawable.healthcarelogo);
            mBuilder.setContentTitle("Emergency message send");
            mBuilder.setContentText("to");
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TestViewHolder viewHolder = (TestViewHolder)holder;
            final String item = items.get(position);
            final String ival=ivalues.get(position);

            if (itemsPendingRemoval.contains(item)) {
                // we need to show the "undo" state of the row
                viewHolder.itemView.setBackgroundColor(Color.RED);
                viewHolder.titleTextView.setVisibility(View.GONE);
                viewHolder.phonenoTV.setVisibility(View.GONE);
                viewHolder.undoButton.setVisibility(View.VISIBLE);
                viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // user wants to undo the removal, let's cancel the pending task
                        Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                        pendingRunnables.remove(item);
                        if (pendingRemovalRunnable != null)
                            handler.removeCallbacks(pendingRemovalRunnable);
                        itemsPendingRemoval.remove(item);
                        valuePendingRemoval.remove(ival);
                        // this will rebind the row in "normal" state
                        notifyItemChanged(items.indexOf(item));
                    }
                });
            } else {
                // we need to show the "normal" state
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
                viewHolder.titleTextView.setVisibility(View.VISIBLE);
                viewHolder.titleTextView.setText(item);
                viewHolder.phonenoTV.setVisibility(View.VISIBLE);
                viewHolder.phonenoTV.setText(ival);
                viewHolder.undoButton.setVisibility(View.GONE);
                viewHolder.undoButton.setOnClickListener(null);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        /**
         *  Utility method to add some rows for testing purposes. You can add rows from the toolbar menu.
         */
        public void addItems(int howMany){
            if (howMany > 0) {
                /*for (int i = lastInsertedIndex + 1; i <= lastInsertedIndex + howMany; i++) {
                    items.add("Item " + i);
                    ivalues.add("values "+i);
                    notifyItemInserted(items.size() - 1);
                }
                lastInsertedIndex = lastInsertedIndex + howMany;*/
                if(ivalues.contains(phoneNo) || phoneNo.equalsIgnoreCase(""))
                {
                    //         Toast.makeText(getApplicationContext(),"contact already exists",Toast.LENGTH_LONG);
                }
                else {
                    items.add(name);
                    ivalues.add(phoneNo);
                    no_contact_added++;
                    notifyItemInserted(items.size() - 1);
                    checkButtonCondition();
                    if(starthua==0)
                        saveContacts();
                }
            }
        }

        public void setUndoOn(boolean undoOn) {
            this.undoOn = undoOn;
        }

        public boolean isUndoOn() {
            return undoOn;
        }

        public void pendingRemoval(int position) {
            final String item = items.get(position);
            final String ival = ivalues.get(position);
            if (!itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.add(item);
                itemsPendingRemoval.add(item);
                valuePendingRemoval.add(ival);
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                // let's create, store and post a runnable to remove the item
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        remove(items.indexOf(item));
                        remove(ivalues.indexOf(ival));
                    }
                };
                handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
                pendingRunnables.put(item, pendingRemovalRunnable);
            }
        }

        public void remove(int position) {
            String item = items.get(position);
            if (itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.remove(item);
            }
            if (items.contains(item)) {
                items.remove(position);
                ivalues.remove(position);
                no_contact_added--;
                notifyItemRemoved(position);
                checkButtonCondition();
                if(starthua==0)
                    saveContacts();
            }
        }

        public boolean isPendingRemoval(int position) {
            String item = items.get(position);
            return itemsPendingRemoval.contains(item);
        }

        public void checkButtonCondition(){
            if(getItemCount()<=3){
                pick_contact_btn.setEnabled(true);
            }else{
                pick_contact_btn.setEnabled(false);
            }
            if (getItemCount()>0){
                send_sms_btn.setEnabled(true);
            }else{
                send_sms_btn.setEnabled(false);
            }
        }

        public void sendSMS()
        {
            sendto=0;

            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            inboxStyle= new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Emergency message send to:");

            PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                            inboxStyle.addLine(items.get(sendto)+":"+ivalues.get(sendto));
                            sendto++;
                            mBuilder.setStyle(inboxStyle);
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                            sendto++;
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                            sendto++;
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                            sendto++;
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                            sendto++;
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            SmsManager sms = SmsManager.getDefault();

            for(int ij=0;ij<getItemCount();ij++) {
                sms.sendTextMessage(ivalues.get(ij), null, message, sentPI, deliveredPI);
            }

            if (sendto==getItemCount())addNotification();

            finish();
        }

        public void addNotification() {
            if (sendto > 0) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }

        public void saveContacts(){
            SharedPreferences sharedPreferences=Emergency_SMS.this.getSharedPreferences("ContactDetails",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.putInt("NoContacts",getItemCount());

            String namei,numberi;
            for(int i=0;i<getItemCount();i++){
                namei="name"+i;
                numberi="number"+i;
                editor.putString(namei,items.get(i));
                editor.putString(numberi,ivalues.get(i));
            }
            editor.apply();

        }
    }

    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView,phonenoTV;
        Button undoButton;

        public TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false));
            titleTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            phonenoTV=(TextView)itemView.findViewById(R.id.phoneno_text_view);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);
        }

    }


}
/*
    Button pick_contact_btn,send_sms_btn;
    private static final int RESULT_PICK_CONTACT = 855;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1;
    private TextView contact_name;
    private TextView contact_number,messege_tv;
    String message,phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency__sms);
        message = "needs EMERGENCY medical CHECK UP";
        messege_tv=(TextView)findViewById(R.id.messege_tv);
        messege_tv.setText(message);

        send_sms_btn=(Button)findViewById(R.id.send_sms_btn);
        contact_name=(TextView)findViewById(R.id.contact_name_tv);
        contact_number=(TextView)findViewById(R.id.contact_number_tv);

        pick_contact_btn=(Button)findViewById(R.id.pick_contact_btn);
        pick_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });

        send_sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            contact_name.setText(name);

            contact_number.setText(phoneNo);
            buttonEnable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonEnable() {
        if(!(contact_number.getText().toString().equalsIgnoreCase("")))
        {
            send_sms_btn.setEnabled(true);
            phoneno = contact_number.getText().toString();

        }
        else{
            send_sms_btn.setEnabled(false);
        }
    }
//not used
    protected void sendSMSMessage() {
        phoneno = contact_number.getText().toString();
//        phoneno.replaceAll(" ","");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }
//not used
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    Log.e("phone no. :",phoneno);
                    smsManager.sendTextMessage(phoneno, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    private void sendSMS()
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneno, null, message, sentPI, deliveredPI);
        finish();
    }
*/
//}
